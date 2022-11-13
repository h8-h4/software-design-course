package frequency;

import lister.SubmissionLister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static util.SubmissionGenerator.generateSubmissionMinusHours;

@ExtendWith(MockitoExtension.class)
class FrequencyCalculatorTest {
    @Mock
    private SubmissionLister submissionLister;

    private FrequencyCalculator calculator;

    @BeforeEach
    public void setup() {
        calculator = new FrequencyCalculator(submissionLister, Clock.systemUTC());
    }

    @Test
    public void testNoSubmission() {
        when(submissionLister.listSubmissionsWithKeyword(anyString(), anyString(), any()))
                .thenReturn(Stream.of(
                        //empty
                ));

        Map<Long, Long> frequency = calculate();

        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testOneSubmission() {
        when(submissionLister.listSubmissionsWithKeyword(anyString(), anyString(), any()))
                .thenReturn(Stream.of(
                        generateSubmissionMinusHours(1)
                ));

        Map<Long, Long> frequency = calculate();

        assertFalse(frequency.isEmpty());
        assertEquals(1, frequency.get(1L));
    }

    @Test
    public void testMultipleSubmissions() {
        when(submissionLister.listSubmissionsWithKeyword(anyString(), anyString(), any()))
                .thenReturn(Stream.of(
                        generateSubmissionMinusHours(1),
                        generateSubmissionMinusHours(1),
                        generateSubmissionMinusHours(1),
                        generateSubmissionMinusHours(2),
                        generateSubmissionMinusHours(2),
                        generateSubmissionMinusHours(10)
                ));

        Map<Long, Long> frequency = calculate();

        assertFalse(frequency.isEmpty());
        assertEquals(3, frequency.get(1L));
        assertEquals(2, frequency.get(2L));
        assertEquals(1, frequency.get(10L));
    }

    private Map<Long, Long> calculate() {
        return calculator.calculateFrequency("s", "k", 1);
    }

}