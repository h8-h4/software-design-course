package client;

public interface ApiClient {
    String requestSubmissions(String subreddit, long fromTimestamp);
}
