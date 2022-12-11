package statistic;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LastHourEventsStatisticTest {
    private static final Instant FIXED_TIME = Instant.parse("2022-12-11T18:30:00.00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(
            FIXED_TIME,
            ZoneId.ofOffset("UTC", ZoneOffset.UTC)
    );


    @Test
    public void testNoEvents() {
        EventsStatistic statistics = createStatistics();
        assertNull(statistics.getEventStatisticByName("404"));
        assertEquals(List.of(), statistics.getAllEventStatistic());
    }

    @Test
    public void testWithFixedClock() {
        EventsStatistic statistics = createStatistics();

        statistics.incEvent("a");
        statistics.incEvent("a");
        statistics.incEvent("a");
        statistics.incEvent("b");
        statistics.incEvent("b");
        statistics.incEvent("c");

        Statistic expectedA = toStatistic("a", 3);
        Statistic expectedB = toStatistic("b", 2);
        Statistic expectedC = toStatistic("c", 1);

        assertEquals(expectedA, statistics.getEventStatisticByName("a"));
        assertEquals(expectedB, statistics.getEventStatisticByName("b"));
        assertEquals(expectedC, statistics.getEventStatisticByName("c"));
        assertNull(statistics.getEventStatisticByName("d"));

        assertEquals(List.of(expectedA, expectedB, expectedC), statistics.getAllEventStatistic());
    }

    @Test
    public void testPrintNotEmpty() throws IOException {
        EventsStatistic statistics = createStatistics();

        statistics.incEvent("a");
        statistics.incEvent("b");
        statistics.incEvent("c");

        StringWriter writer = new StringWriter();
        statistics.printStatistic(writer);

        assertFalse(writer.getBuffer().isEmpty());
    }

    @Test
    public void testWithClockSimulation() {
        Clock mockedClock = Mockito.mock(Clock.class);
        EventsStatistic statistics = createStatistics(mockedClock);

        Mockito.when(mockedClock.instant()).thenReturn(FIXED_TIME);
        statistics.incEvent("a");
        statistics.incEvent("a");
        statistics.incEvent("b");

        assertEquals(toStatistic("a", 2), statistics.getEventStatisticByName("a"));
        assertEquals(toStatistic("b", 1), statistics.getEventStatisticByName("b"));
        assertEquals(
                List.of(toStatistic("a", 2), toStatistic("b", 1)),
                statistics.getAllEventStatistic()
        );


        Mockito.when(mockedClock.instant()).thenReturn(FIXED_TIME.plus(30, ChronoUnit.MINUTES));
        statistics.incEvent("a");
        statistics.incEvent("c");

        assertEquals(toStatistic("a", 3), statistics.getEventStatisticByName("a"));
        assertEquals(toStatistic("b", 1), statistics.getEventStatisticByName("b"));
        assertEquals(toStatistic("c", 1), statistics.getEventStatisticByName("c"));
        assertEquals(
                List.of(toStatistic("a", 3), toStatistic("b", 1), toStatistic("c", 1)),
                statistics.getAllEventStatistic()
        );


        Mockito.when(mockedClock.instant()).thenReturn(FIXED_TIME.plus(60, ChronoUnit.MINUTES));
        statistics.incEvent("d");

        assertEquals(toStatistic("a", 1), statistics.getEventStatisticByName("a"));
        assertNull(statistics.getEventStatisticByName("b"));
        assertEquals(toStatistic("c", 1), statistics.getEventStatisticByName("c"));
        assertEquals(toStatistic("d", 1), statistics.getEventStatisticByName("d"));
        assertEquals(
                List.of(toStatistic("a", 1), toStatistic("c", 1), toStatistic("d", 1)),
                statistics.getAllEventStatistic()
        );

        Mockito.when(mockedClock.instant()).thenReturn(FIXED_TIME.plus(120, ChronoUnit.MINUTES));
        assertTrue(statistics.getAllEventStatistic().isEmpty());
    }

    private Statistic toStatistic(String name, long requestsForLastHour) {
        return new Statistic(name, requestsForLastHour / 60.0);
    }


    private static EventsStatistic createStatistics(Clock clock) {
        return new LastHourEventsStatistic(clock);
    }

    private static EventsStatistic createStatistics() {
        return new LastHourEventsStatistic(FIXED_CLOCK);
    }
}