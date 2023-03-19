import actor.MasterActor;
import actor.MasterConfig;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import model.SearchLink;
import model.SearchService;
import model.message.SearchReply;
import model.message.SearchRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort = 9867)
public class StubServerTest {
    private static final int PORT = 9867;
    private static final String URL = "http://localhost:".concat(String.valueOf(PORT));

    private final static MasterConfig CONFIG = new MasterConfig(
            List.of(
                    new SearchService("Google", URL.concat("/google")),
                    new SearchService("Yandex", URL.concat("/yandex")),
                    new SearchService("Yahoo", URL.concat("/yahoo"))
            ),
            Duration.ofSeconds(1)
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAllServicesAreUp(WireMockRuntimeInfo wmRuntimeInfo) {
        final List<SearchLink> expected = List.of(
                new SearchLink("a", "url", "google"),
                new SearchLink("b", "url", "google"),
                new SearchLink("aa", "url", "yandex"),
                new SearchLink("aaa", "url", "yahoo")
        );

        stubFor(get("/google").willReturn(ok(links(
                expected.get(0), expected.get(1)
        ))));

        stubFor(get("/yandex").willReturn(ok(links(
                expected.get(2)
        ))));

        stubFor(get("/yahoo").willReturn(ok(links(
                expected.get(3)
        ))));

        final SearchReply reply = request(CONFIG);

        Assertions.assertEquals(4, reply.getLinks().size());
        Assertions.assertEquals(
                new HashSet<>(expected),
                new HashSet<>(reply.getLinks())
        );
    }

    @Test
    public void testSomeServicesAreUnavailable(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get("/google").willReturn(ok(links(
                new SearchLink("a", "url", "google"),
                new SearchLink("b", "url", "google"),
                new SearchLink("c", "url", "google")
        ))));

        final SearchReply reply = request(CONFIG);

        Assertions.assertEquals(3, reply.getLinks().size());
    }


    @Test
    public void testSomeServicesAreSlow(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get("/google").willReturn(
                ok(links(
                        new SearchLink("a", "url", "google"),
                        new SearchLink("b", "url", "google")
                )).withFixedDelay(1000)
        ));

        stubFor(get("/yahoo").willReturn(
                ok(links(
                        new SearchLink("1", "url", "yahoo"),
                        new SearchLink("2", "url", "yahoo"),
                        new SearchLink("3", "url", "yahoo")
                )).withFixedDelay(2000)
        ));

        stubFor(get("/yandex").willReturn(
                ok(links(
                        new SearchLink("aa", "url", "yandex")
                )).withFixedDelay(3000)
        ));

        Assertions.assertEquals(
                0,
                request(CONFIG.withTimeout(Duration.ofMillis(500))).getLinks().size()
        );

        Assertions.assertEquals(
                6,
                request(CONFIG.withTimeout(Duration.ofMillis(7000))).getLinks().size()
        );
    }

    private String links(SearchLink... searchLinks) {
        try {
            return objectMapper.writeValueAsString(Arrays.stream(searchLinks).toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private SearchReply request(MasterConfig config) {
        final CompletableFuture<SearchReply> future = new CompletableFuture<>();

        final ActorSystem actorSystem = ActorSystem.create("search-actors");

        final Consumer<SearchReply> callback = future::complete;
        final Props props = Props.create(
                MasterActor.class,
                config,
                callback
        );

        actorSystem.actorOf(props, "master").tell(new SearchRequest(""), ActorRef.noSender());

        final Duration testTimeout = config.getTimeout().plus(Duration.of(10, ChronoUnit.SECONDS));

        final SearchReply result = future.get(testTimeout.getSeconds(), TimeUnit.SECONDS);

        actorSystem.terminate();

        return result;
    }
}
