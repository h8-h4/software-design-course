package app.report;

import app.report.dto.AverageStatistics;
import app.report.dto.ByDayStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/day")
    public ByDayStatistics byDayStatistics() {
        return reportService.getByDayStatistics();
    }

    @GetMapping("/average")
    public AverageStatistics averageStatistics() {
        return reportService.getAverageStatistics();
    }
}
