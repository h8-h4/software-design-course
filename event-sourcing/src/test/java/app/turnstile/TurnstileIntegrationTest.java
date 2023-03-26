package app.turnstile;

import app.manager.ManagerService;
import app.manager.dto.Client;
import app.manager.dto.SubscriptionUpdate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TurnstileIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerService managerService;


    @Test
    public void testUnknownClient() {
        in("unknown", HttpStatus.FORBIDDEN);
    }

    @Test
    public void testNoSubscription() {
        final String client = managerService.register(new Client("client"));
        in(client, HttpStatus.FORBIDDEN);
    }

    @Test
    public void testOk() {
        final String clientId = managerService.register(new Client("client"));
        managerService.updateSubscription(new SubscriptionUpdate(clientId, 1));
        in(clientId, HttpStatus.OK);
        out(clientId);
    }

    @Test
    public void testSubScriptionEnded() {
        final String clientId = managerService.register(new Client("client"));
        managerService.updateSubscription(new SubscriptionUpdate(clientId, 1));
        in(clientId, HttpStatus.OK);
        out(clientId);

        managerService.updateSubscription(new SubscriptionUpdate(clientId, -2));
        in(clientId, HttpStatus.FORBIDDEN);
    }


    @SneakyThrows
    private void in(String clientId, HttpStatus status) {
        mockMvc.perform(
                get("/api/turnstile/in/{clientId}", clientId)
        ).andExpect(status().is(status.value()));
    }

    @SneakyThrows
    private void out(String clientId) {
        mockMvc.perform(
                get("/api/turnstile/out/{clientId}", clientId)
        ).andExpect(status().isOk());
    }
}
