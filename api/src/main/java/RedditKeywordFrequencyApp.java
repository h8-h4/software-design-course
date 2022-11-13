import client.ApiClient;
import client.PushshiftApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import frequency.FrequencyCalculator;
import lister.SubmissionLister;
import parser.PushshiftApiSubmissionsParser;
import parser.SubmissionsParser;

import java.net.http.HttpClient;
import java.time.Clock;
import java.util.Map;

public class RedditKeywordFrequencyApp {
    private static final String PUSHSHIFT_HOST = "https://api.pushshift.io/";
    private static final String USAGE = """
            Unknown arguments.
            Required arguments structure:
            <subreddit> <keyword> <hours>
            ------------------------------
            <subreddit> -- Subreddit to search.
            <hashtag> -- Keyword to search.
            <hours> -- Number of last hours to count the
            frequency diagram of keyword occurrences. Should be positive Integer.
            """;

    public static void main(String[] args) {
        if (args.length != 3 || args[0] == null || args[1] == null || args[2] == null) {
            printUsageAndQuit();
        }

        String subreddit = args[0];
        String keyword = args[1];
        int hours = validateHours(args[2]);

        ApiClient apiClient = new PushshiftApiClient(HttpClient.newBuilder().build(), PUSHSHIFT_HOST);
        SubmissionsParser parser = new PushshiftApiSubmissionsParser(new ObjectMapper());

        SubmissionLister submissionLister = new SubmissionLister(apiClient, parser);

        FrequencyCalculator frequencyCalculator = new FrequencyCalculator(submissionLister, Clock.systemUTC());

        Map<Long, Long> freq = frequencyCalculator.calculateFrequency(subreddit, keyword, hours);

        System.out.printf("[%s] subreddit submission frequency for keyword=[%s]: %n", subreddit, keyword);
        for (Map.Entry<Long, Long> hourToFreq : freq.entrySet()) {
            System.out.printf("%2d hours ago: %10d%n", hourToFreq.getKey(), hourToFreq.getValue());
        }
    }

    private static void printUsageAndQuit() {
        System.out.println(USAGE);
        System.exit(1);
    }

    private static int validateHours(String hoursArg) {
        int hours;
        try {
            hours = Integer.parseInt(hoursArg);


            return hours;
        } catch (NumberFormatException e) {
            printUsageAndQuit();
        }

        throw new IllegalStateException();
    }
}
