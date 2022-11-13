package util;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assumptions;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class HostReachableAssumptions {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public void assumeHostReachable(String host) {
        Assumptions.assumeTrue(() -> {
            try {
                HttpResponse<Void> response = CLIENT.send(
                        HttpRequest.newBuilder()
                                .uri(URI.create(host))
                                .timeout(Duration.of(30, ChronoUnit.SECONDS))
                                .build(),
                        HttpResponse.BodyHandlers.discarding()
                );

                return response.statusCode() / 100 != 5;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
