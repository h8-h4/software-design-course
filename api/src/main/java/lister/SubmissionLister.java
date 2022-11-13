package lister;

import client.ApiClient;
import model.Submission;
import parser.SubmissionsParser;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

public class SubmissionLister {
    private final ApiClient apiClient;
    private final SubmissionsParser submissionsParser;

    public SubmissionLister(ApiClient apiClient, SubmissionsParser submissionsParser) {
        this.apiClient = apiClient;
        this.submissionsParser = submissionsParser;
    }

    public Stream<Submission> listSubmissionsWithKeyword(String subreddit, String keyword, OffsetDateTime fromTimestamp) {
        String rawSubmissions = apiClient.requestSubmissions(
                subreddit, fromTimestamp.toEpochSecond()
        );

        return filterByKeyWord(submissionsParser.parseSubmissions(rawSubmissions), keyword);
    }

    private Stream<Submission> filterByKeyWord(List<Submission> submissions, String keyword) {
        return submissions
                .stream()
                .filter(s -> containsKeyword(s, keyword));
    }

    private boolean containsKeyword(Submission submission, String keyword) {
        return (submission.getTitle() != null && submission.getTitle().toLowerCase().contains(keyword))
                ||
                (submission.getText() != null && submission.getText().toLowerCase().contains(keyword));
    }
}
