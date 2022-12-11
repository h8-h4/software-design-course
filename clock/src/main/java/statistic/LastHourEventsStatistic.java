package statistic;

import lombok.Value;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.time.Clock;
import java.util.*;

public class LastHourEventsStatistic implements EventsStatistic {
    private final Map<String, Long> requestsPerEvent;
    private final Queue<TimedEvent> eventQueue;
    private final Clock clock;

    public LastHourEventsStatistic(Clock clock) {
        this.requestsPerEvent = new HashMap<>();
        this.eventQueue = new PriorityQueue<>(Comparator.comparing(TimedEvent::getEpochMinutes));
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        long epochMinutes = getEpochMinutes();

        requestsPerEvent.merge(name, 1L, Long::sum);
        eventQueue.add(new TimedEvent(name, epochMinutes));

        clearOldEvents(epochMinutes);
    }

    @Override
    @Nullable
    public Statistic getEventStatisticByName(String name) {
        clearOldEvents(getEpochMinutes());

        Long requests = requestsPerEvent.get(name);

        return requests != null
                ? new Statistic(name, toRpm(requests))
                : null;
    }

    @Override
    public List<Statistic> getAllEventStatistic() {
        clearOldEvents(getEpochMinutes());

        return requestsPerEvent.entrySet().stream()
                .map(e -> new Statistic(e.getKey(), toRpm(e.getValue())))
                .toList();
    }

    @Override
    public void printStatistic(Writer writer) throws IOException {
        writer.write("Statistics: %n".formatted());

        for (Statistic statistic : getAllEventStatistic()) {
            writer.write("%10s:\t%20f%n".formatted(statistic.getEventName(), statistic.getRequestPerMinute()));
        }
        writer.flush();
    }

    private void clearOldEvents(long currentTimeMinutes) {
        while (!eventQueue.isEmpty() && (currentTimeMinutes - eventQueue.peek().getEpochMinutes()) >= 60) {
            TimedEvent oldEvent = eventQueue.poll();

            requestsPerEvent.computeIfPresent(oldEvent.getEventName(), (__, oldValue) -> {
                if (oldValue == 1) {
                    return null;
                }
                return oldValue - 1;
            });
        }
    }

    private long getEpochMinutes() {
        return clock.instant().getEpochSecond() / 60;
    }

    private static double toRpm(long requests) {
        return requests / 60.0;
    }

    @Value
    private static class TimedEvent {
        String eventName;
        long epochMinutes;
    }
}
