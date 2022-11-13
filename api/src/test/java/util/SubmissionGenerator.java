package util;

import lombok.experimental.UtilityClass;
import model.Submission;

import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class SubmissionGenerator {
    public static Submission generateSubmission(String title, String text) {
        return Submission.builder()
                .text(text)
                .title(title)
                .subreddit(UUID.randomUUID().toString())
                .build();
    }

    public static Submission generateSubmission() {
        return generateSubmission("", "");
    }

    public static Submission generateSubmission(OffsetDateTime createdAt) {
        return generateSubmission()
                .toBuilder()
                .createdAt(createdAt.toEpochSecond())
                .build();
    }

    public static Submission generateSubmissionMinusHours(int hours) {
        return generateSubmission(OffsetDateTime.now().minusHours(hours));
    }
}
