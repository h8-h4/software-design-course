package frequency;


import lister.SubmissionLister;
import model.Submission;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequencyCalculator {
    private final SubmissionLister lister;
    private final Clock clock;

    public FrequencyCalculator(SubmissionLister lister, Clock clock) {
        this.lister = lister;
        this.clock = clock;
    }

    public Map<Long, Long> calculateFrequency(String subreddit, String keyword, int hours) {
        OffsetDateTime now = OffsetDateTime.ofInstant(clock.instant(), clock.getZone());

        Stream<Submission> submissions = lister.listSubmissionsWithKeyword(subreddit, keyword, now.minusHours(hours));

        return submissions.collect(
                Collectors.groupingBy(
                        s -> extractRelativeHour(s, now),
                        TreeMap::new,
                        Collectors.counting()
                )
        );
    }

    private Long extractRelativeHour(Submission submission, OffsetDateTime now) {
        return ChronoUnit.HOURS.between(
                Instant.ofEpochSecond(submission.getCreatedAt()).atOffset(ZoneOffset.UTC)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0),
                now
        );
    }
}
