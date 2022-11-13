package client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PushshiftApiClient implements ApiClient {
    private static final String REQUIRED_FIELDS_PARAM = "fields=selftext,title,created_utc,subreddit";
    private final HttpClient client;
    private final String apiEndpoint;

    public PushshiftApiClient(HttpClient client, String apiHost) {
        this.client = client;
        this.apiEndpoint = apiHost + "/reddit/search/submission/";
    }

    public String requestSubmissions(String subreddit, long fromTimestamp) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(buildUri(subreddit, fromTimestamp))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new InvalidApiResponseException("Api didn't return any data");
            }

            return response.body();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private URI buildUri(String subreddit, long fromTimestamp) {
        String params = "?%s&subreddit=%s&after=%d&size=500"
                .formatted(REQUIRED_FIELDS_PARAM, subreddit, fromTimestamp);

        return URI.create(
                apiEndpoint.concat(params)
        );
    }
}
