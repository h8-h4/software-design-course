package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class Submission {
    @JsonProperty("subreddit")
    String subreddit;
    @JsonProperty("title")
    String title;
    @JsonProperty("selftext")
    String text;
    @JsonProperty("created_utc")
    long createdAt;
}
