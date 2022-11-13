package frequency;

import client.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lister.SubmissionLister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import parser.PushshiftApiSubmissionsParser;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class FrequencyCalculatorIntegrationTest {
    private static final String API_RESPONSE = """
            {
                "data": [
                    {
                        "created_utc": 1668363723,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Usually my cat meets me after work, but today I came back and saw this picture... This smart guy opened a bag of food, ate half a pack and... fell asleep."
                    },
                    {
                        "created_utc": 1668363709,
                        "selftext": "[removed]",
                        "subreddit": "cats",
                        "title": "Spaying and being in Heat."
                    },
                    {
                        "created_utc": 1668363630,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Rescued a kitten, I'm not sure on the breed. Khao Manee?"
                    },
                    {
                        "created_utc": 1668363600,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Throw back to my tiny babies \\ud83e\\udd79"
                    },
                    {
                        "created_utc": 1668363568,
                        "selftext": "I know cats usually don\\u2019t like to go out, but every now and then I have to do a 3 hour drive with my cat but she doesn\\u2019t enjoy it one bit. \\n\\nWe have to stop to clean the poop out of the case as you know, cats are the sweeties thing but the poop definitely aren\\u2019t. \\n\\nThere\\u2019s something I can do to make things better for the cat and I? \\n\\nI have never tried to travel with her in a airplane, wondering if it would be similar as well.",
                        "subreddit": "cats",
                        "title": "Traveling with cats: there\\u2019s something I can do to improve the experience?"
                    },
                    {
                        "created_utc": 1668363464,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Dog mat?"
                    },
                    {
                        "created_utc": 1668363412,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "My two babies"
                    },
                    {
                        "created_utc": 1668363330,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "my sweet girl just turned one &lt;3"
                    },
                    {
                        "created_utc": 1668363291,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Do you want paint a beautiful portrait for your pet . for remembrance"
                    },
                    {
                        "created_utc": 1668363214,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Meet Kelly, my 6 year old calico"
                    },
                    {
                        "created_utc": 1668362981,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "The vet is closed till tomorrow, tried ringing an emergency one and its closed, its where she got spayed months ago and suddenly the area is bald?? She keeps licking it help!"
                    },
                    {
                        "created_utc": 1668362969,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "suggestions on where I can get a cat? social media? or word of mouth? would appreciate any suggestions"
                    },
                    {
                        "created_utc": 1668362840,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "took some pics of my beautiful girl today &lt;3"
                    },
                    {
                        "created_utc": 1668362797,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Progress pics of my cat, Jimmy Dean. He was a stray until I picked him up in March 2022. He\\u2019s around 5-7 years old. Missing a few teeth and has some scarring. He enjoys snuggling, napping, and sun tanning. I love him dearly."
                    },
                    {
                        "created_utc": 1668362633,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Good appetit"
                    },
                    {
                        "created_utc": 1668362609,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Robbery"
                    },
                    {
                        "created_utc": 1668362586,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Is there a sub for petting stray cats?"
                    },
                    {
                        "created_utc": 1668362438,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "My sweet 16 year old boy, Terrance, needs wet food recommendations to support his aging body and mind."
                    },
                    {
                        "created_utc": 1668362330,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Whatcha doin hooman?"
                    },
                    {
                        "created_utc": 1668362314,
                        "selftext": "",
                        "subreddit": "cats",
                        "title": "Our princess was put to rest today after weeks of battle"
                    }
                ]
            }
            """;

    private static final String SUBREDDIT = "cats";
    private static final String EMPTY_SUBREDDIT = "empty";

    private static final Clock TIME = Clock.fixed(
            Instant.parse("2022-11-13T18:30:00.00Z"),
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
    );

    @Mock
    private ApiClient apiClient;

    private FrequencyCalculator calculator;

    @BeforeEach
    public void setup() {
        calculator = new FrequencyCalculator(
                new SubmissionLister(
                        apiClient,
                        new PushshiftApiSubmissionsParser(
                                new ObjectMapper()
                        )
                ),
                TIME
        );

        lenient().when(apiClient.requestSubmissions(same(SUBREDDIT), anyLong()))
                .thenReturn(API_RESPONSE);

        lenient().when(apiClient.requestSubmissions(same(EMPTY_SUBREDDIT), anyLong()))
                .thenReturn("""
                        {"data" : []}
                        """
                );
    }

    @Test
    public void testNoSubmissionInSubreddit() {
        Map<Long, Long> frequency = calculator.calculateFrequency(EMPTY_SUBREDDIT, "cat", 1);

        assertTrue(frequency.isEmpty());
    }


    @Test
    public void testCatsSubreddit() {
        Map<Long, Long> frequency = calculator.calculateFrequency(SUBREDDIT, "cat", 1);

        assertFalse(frequency.isEmpty());
        assertEquals(5, frequency.get(0L));
    }

    @Test
    public void testCatsSubredditForSpace() {
        Map<Long, Long> frequency = calculator.calculateFrequency(SUBREDDIT, " ", 1);

        assertFalse(frequency.isEmpty());
        assertEquals(17, frequency.get(0L));
        assertEquals(2, frequency.get(1L));
    }
}