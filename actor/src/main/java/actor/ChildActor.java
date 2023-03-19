package actor;

import akka.actor.AbstractLoggingActor;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import model.SearchLink;
import model.SearchService;
import model.message.SearchReply;
import model.message.SearchRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ChildActor extends AbstractLoggingActor {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final SearchService searchService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(SearchRequest.class, this::handleRequest)
                .build();
    }

    public void handleRequest(SearchRequest request) throws IOException, InterruptedException {
        final String searchUri = searchService.getSearchUrl().concat(request.getRequest());


        final HttpResponse<String> response = CLIENT.send(
                HttpRequest.newBuilder().GET().uri(URI.create(searchUri)).build(),
                HttpResponse.BodyHandlers.ofString()
        );

        context().parent().tell(
                new SearchReply(parse(response.body())),
                self()
        );
    }

    private List<SearchLink> parse(String response) {
        try {
            return objectMapper.readValue(
                    response,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SearchLink.class)
            );
        } catch (JsonProcessingException e) {
            log().error("Failed to parse search reply: ", e);
            return Collections.emptyList();
        }
    }
}
