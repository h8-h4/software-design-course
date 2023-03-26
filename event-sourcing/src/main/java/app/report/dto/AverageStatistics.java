package app.report.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AverageStatistics {
    double averageVisitFrequency;
    double averageVisitHours;
}
