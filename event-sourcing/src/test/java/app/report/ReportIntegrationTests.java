package app.report;

import app.manager.ManagerService;
import app.manager.dto.Client;
import app.manager.dto.SubscriptionUpdate;
import app.report.dto.AverageStatistics;
import app.report.dto.ByDayStatistics;
import app.turnstile.TurnstileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReportIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private TurnstileService turnstileService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testNoStats() {
        final ByDayStatistics byDayStatistics = byDayStatistics();
        Assertions.assertTrue(byDayStatistics.getVisitsByDays().isEmpty());

        final AverageStatistics averageStatistics = averageStatistics();
        Assertions.assertEquals(0, averageStatistics.getAverageVisitFrequency());
        Assertions.assertEquals(0, averageStatistics.getAverageVisitHours());
    }


    @Test
    public void testStatsUpdates() {
        final String client1 = registerClient("name", 1);
        inOut(client1, 10);
        final String client2 = registerClient("name2", 2);
        inOut(client2, 5);

        final ByDayStatistics byDayStatistics = byDayStatistics();
        Assertions.assertFalse(byDayStatistics.getVisitsByDays().isEmpty());
        Assertions.assertEquals(
                15,
                byDayStatistics.getVisitsByDays().get(LocalDate.now())
        );
    }

    @SneakyThrows
    private ByDayStatistics byDayStatistics() {
        final MvcResult mvcResult = mockMvc.perform(
                        get("/api/stats/day")
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ByDayStatistics.class);
    }

    @SneakyThrows
    private AverageStatistics averageStatistics() {
        final MvcResult mvcResult = mockMvc.perform(
                        get("/api/stats/average")
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AverageStatistics.class);
    }

    private String registerClient(String name, int months) {
        final String clientId = managerService.register(new Client(name));
        managerService.updateSubscription(new SubscriptionUpdate(clientId, months));

        return clientId;
    }

    private void inOut(String clientId, int times) {
        for (int i = 0; i < times; i++) {
            Assertions.assertTrue(turnstileService.letIn(clientId));
            turnstileService.letOut(clientId);
        }
    }

}
