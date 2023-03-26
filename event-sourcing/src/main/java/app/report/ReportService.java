package app.report;

import app.common.event.spring.annotation.EventHandler;
import app.report.dto.AverageStatistics;
import app.report.dto.ByDayStatistics;
import app.turnstile.events.TurnstileEvent;
import app.turnstile.repository.TurnstileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final TurnstileRepository turnstileRepository;

    private OffsetDateTime openingDay;
    private OffsetDateTime currentDay;
    private final Map<LocalDate, Integer> visits = new HashMap<>();
    private final Map<LocalDate, Duration> durations = new HashMap<>();
    private final Map<String, OffsetDateTime> clientEnters = new HashMap<>();

    @PostConstruct
    public void initializeStatistics() {
        final List<TurnstileEvent> all = turnstileRepository.findAllByOrderByCreatedAt();

        if (all.isEmpty()) {
            return;
        }

        openingDay = all.get(0).getCreatedAt();
        all.forEach(this::updateStatistics);
    }

    @EventHandler
    public void updateStatistics(TurnstileEvent event) {
        if (openingDay == null) {
            openingDay = event.getCreatedAt();
        }

        currentDay = event.getCreatedAt();

        if (event.getDirection() == TurnstileEvent.Direction.IN) {
            visits.merge(event.getCreatedAt().toLocalDate(), 1, Integer::sum);
            clientEnters.put(event.getClientId(), event.getCreatedAt());
        }

        if (event.getDirection() == TurnstileEvent.Direction.OUT) {
            final OffsetDateTime enterTime = clientEnters.get(event.getClientId());
            if (enterTime != null) {
                durations.merge(
                        event.getCreatedAt().toLocalDate(),
                        Duration.between(enterTime, event.getCreatedAt()),
                        Duration::plus
                );
                clientEnters.remove(event.getClientId());
            }
        }
    }

    public ByDayStatistics getByDayStatistics() {
        return ByDayStatistics.builder().visitsByDays(visits).build();
    }

    public AverageStatistics getAverageStatistics() {
        return AverageStatistics.builder()
                .averageVisitFrequency(averageVisitFrequency())
                .averageVisitHours(averageVisitHours())
                .build();
    }

    private double averageVisitHours() {
        if (openingDay == null || currentDay == null) {
            return 0;
        }

        final long visits = totalVisits();
        return visits == 0
                ? 0
                : totalHours() * 1.0 / visits;
    }

    private double averageVisitFrequency() {
        if (openingDay == null || currentDay == null) {
            return 0;
        }

        final long days = daysCount();
        return days == 0
                ? 0
                : totalVisits() * 1.0 / days;
    }

    private long totalHours() {
        return durations.values().stream().reduce(Duration::plus).orElse(Duration.ZERO).toHours();
    }

    private long totalVisits() {
        return visits.values().stream().mapToInt(it -> it).sum();
    }

    private long daysCount() {
        return Duration.between(currentDay, openingDay).toDays();
    }
}
