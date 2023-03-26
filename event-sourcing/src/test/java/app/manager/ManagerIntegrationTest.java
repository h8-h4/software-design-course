package app.manager;

import app.manager.dto.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ManagerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testRegisterClient() {
        registerClient("client");
    }

    @Test
    public void testCreteSubscription() {
        final String clientId = registerClient("client");
        updateSubscription(clientId, 3);

        final Subscription subscription = getSubscription(clientId);

        assertEquals("client", subscription.getClientName());
        assertNotNull(subscription.getEndTime());
        assertTrue(checkEndTime(subscription.getEndTime(), 3));
    }

    @Test
    public void testUpdateSubscription() {
        final String clientId = registerClient("client");
        updateSubscription(clientId, 3);
        final Subscription subscription = getSubscription(clientId);

        assertNotNull(subscription.getEndTime());
        assertTrue(subscription.getEndTime().isAfter(OffsetDateTime.now().plusMonths(3).minusDays(1)));


        updateSubscription(clientId, 4);
        final Subscription updated = getSubscription(clientId);
        assertNotNull(updated.getEndTime());
        assertTrue(checkEndTime(updated.getEndTime(), 7));
    }

    @SneakyThrows
    private String registerClient(String clientName) {
        final MvcResult result = mockMvc.perform(
                        post("/api/admin/client")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"name": "%s"}
                                        """.formatted(clientName))
                ).andExpect(status().isOk())
                .andReturn();

        final String clientId = result.getResponse().getContentAsString();

        assertFalse(clientId.isBlank());

        return clientId;
    }

    @SneakyThrows
    private void updateSubscription(String clientId, int months) {
        mockMvc.perform(
                post("/api/admin/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientId": "%s",
                                    "months": %d
                                }
                                """.formatted(clientId, months))
        ).andExpect(status().isOk());
    }

    @SneakyThrows
    private Subscription getSubscription(String clientId) {
        final MvcResult mvcResult = mockMvc.perform(
                        get("/api/admin/subscription/{clientId}", clientId)
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        final Subscription subscription =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Subscription.class);

        assertEquals(clientId, subscription.getClientId());

        return subscription;
    }

    private boolean checkEndTime(OffsetDateTime endTime, int month) {
        return endTime.isAfter(OffsetDateTime.now().plusMonths(month).minusDays(1))
                && endTime.isBefore(OffsetDateTime.now().plusMonths(month).plusDays(1));
    }
}
