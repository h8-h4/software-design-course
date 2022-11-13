package client;

import org.junit.jupiter.api.Test;
import util.HostReachableAssumptions;

import java.net.http.HttpClient;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PushshiftApiClientTest {
    private static final String PUSHSHIFT_HOST = "https://api.pushshift.io/";

    private final HttpClient client = HttpClient.newHttpClient();
    private final PushshiftApiClient apiClient = new PushshiftApiClient(client, PUSHSHIFT_HOST);

    @Test
    public void test() {
        HostReachableAssumptions.assumeHostReachable(PUSHSHIFT_HOST);

        String response = apiClient
                .requestSubmissions("cats", OffsetDateTime.now().minusHours(10).toEpochSecond());

        assertFalse(response.isBlank());
    }
}