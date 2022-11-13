package lister;

import client.ApiClient;
import model.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import parser.SubmissionsParser;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static util.SubmissionGenerator.generateSubmission;

@ExtendWith(MockitoExtension.class)
class SubmissionListerTest {
    private static final String API_CLIENT_RESPONSE = "smth";
    private static final String KEYWORD = "keyword";
    private SubmissionLister submissionLister;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SubmissionsParser parser;

    @BeforeEach
    public void setup() {
        submissionLister = new SubmissionLister(apiClient, parser);

        when(apiClient.requestSubmissions(anyString(), anyLong()))
                .thenReturn(API_CLIENT_RESPONSE);
    }

    @Test
    void testEmptyResult() {
        when(parser.parseSubmissions(same(API_CLIENT_RESPONSE)))
                .thenReturn(List.of());

        List<Submission> submissions = list("k");

        assertTrue(submissions.isEmpty());
    }


    @Test
    void testFiltersInTitle() {
        Submission keywordSubmission = generateSubmission(KEYWORD, "text");

        when(parser.parseSubmissions(same(API_CLIENT_RESPONSE)))
                .thenReturn(List.of(
                        keywordSubmission,
                        generateSubmission()
                ));

        List<Submission> submissions = list(KEYWORD);

        assertEquals(1, submissions.size());
        assertEquals(keywordSubmission, submissions.get(0));
    }

    @Test
    void testFiltersInText() {
        Submission keywordSubmission = generateSubmission("title", KEYWORD);

        when(parser.parseSubmissions(same(API_CLIENT_RESPONSE)))
                .thenReturn(List.of(
                        keywordSubmission,
                        generateSubmission()
                ));

        List<Submission> submissions = list(KEYWORD);

        assertEquals(1, submissions.size());
        assertEquals(keywordSubmission, submissions.get(0));
    }

    @Test
    void testFiltersMany() {
        when(parser.parseSubmissions(same(API_CLIENT_RESPONSE)))
                .thenReturn(List.of(
                        generateSubmission(KEYWORD, "1"),
                        generateSubmission(),
                        generateSubmission(KEYWORD, "2"),
                        generateSubmission(KEYWORD, "3"),
                        generateSubmission()
                ));

        List<Submission> submissions = list(KEYWORD);
        assertEquals(3, submissions.size());
    }

    private List<Submission> list(String keyword) {
        return submissionLister
                .listSubmissionsWithKeyword("s", keyword, OffsetDateTime.now())
                .toList();
    }
}