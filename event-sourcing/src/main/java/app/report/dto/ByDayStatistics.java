package app.report.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class ByDayStatistics {
    Map<LocalDate, Integer> visitsByDays;
}
