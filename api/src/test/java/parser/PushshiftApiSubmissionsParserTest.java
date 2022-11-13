package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PushshiftApiSubmissionsParserTest {
    private static final Submission SUBMISSION = Submission.builder()
            .createdAt(1)
            .subreddit("popular")
            .text("text")
            .title("title")
            .build();
    private ObjectMapper mapper;
    private PushshiftApiSubmissionsParser parser;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        parser = new PushshiftApiSubmissionsParser(mapper);
    }

    @Test
    public void testSerializeSubmission() throws JsonProcessingException {
        String json = mapper.writeValueAsString(SUBMISSION);

        Submission deserializedSubmission = mapper.readValue(json, Submission.class);

        assertEquals(SUBMISSION, deserializedSubmission);
    }

    @Test
    public void testParseSerializedSubmission() throws JsonProcessingException {
        String submissionJson = mapper.writeValueAsString(SUBMISSION);

        String rawSubmissions = """
                {"data" : [%s]}
                """.formatted(submissionJson);

        List<Submission> submissions = parser.parseSubmissions(rawSubmissions);

        assertEquals(1, submissions.size());
        assertEquals(SUBMISSION, submissions.get(0));
    }

    @Test
    public void testMalformedSubmission() {
        assertThrows(ParseException.class, () -> parser.parseSubmissions("malformed"));
    }

    @Test
    public void testParseSubmissions() {
        List<Submission> submissions = parser.parseSubmissions("""
                {
                      "data": [
                          {
                              "created_utc": 1668290640,
                              "selftext": "",
                              "subreddit": "cats",
                              "title": "MidderBoo is curious how you're doin"
                          }
                      ]
                  }
                """
        );

        assertEquals(1, submissions.size());
        assertEquals("cats", submissions.get(0).getSubreddit());
        assertEquals("", submissions.get(0).getText());
    }


    @Test
    public void testParseMultipleSubmissions() {
        List<Submission> submissions = parser.parseSubmissions("""
                {
                        "data": [
                            {
                                "created_utc": 1668291093,
                                "selftext": "",
                                "subreddit": "cats",
                                "title": "Cherokee tuxedo pants"
                            },
                            {
                                "created_utc": 1668291065,
                                "selftext": "[removed]",
                                "subreddit": "cats",
                                "title": "Why does my cat lick my armpit?"
                            },
                            {
                                "created_utc": 1668291037,
                                "selftext": "",
                                "subreddit": "cats",
                                "title": "Every Christmas \\ud83c\\udf84"
                            }
                        ]
                    }
                """
        );

        assertEquals(3, submissions.size());
        assertEquals("cats", submissions.get(0).getSubreddit());
        assertEquals("cats", submissions.get(1).getSubreddit());
        assertEquals("cats", submissions.get(2).getSubreddit());


        assertTrue(submissions.get(0).getTitle().contains("Cherokee"));
        assertTrue(submissions.get(1).getTitle().contains("armpit"));
        assertTrue(submissions.get(2).getTitle().contains("Christmas"));
    }
}