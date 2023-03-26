package app.manager;

import app.manager.dto.Client;
import app.manager.dto.Subscription;
import app.manager.dto.SubscriptionUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/client")
    public String register(@RequestBody Client client) {
        return managerService.register(client);
    }

    @PostMapping("/subscription")
    public void updateSubscription(@RequestBody SubscriptionUpdate subscriptionUpdate) {
        managerService.updateSubscription(subscriptionUpdate);
    }

    @GetMapping("/subscription/{clientId}")
    public Subscription getSubscription(@PathVariable String clientId) {
        final Optional<Subscription> subscription = subscriptionService.getSubscription(clientId);

        if (subscription.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }

        if (subscription.get().getEndTime() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client has no subscription");
        }

        return subscription.get();
    }
}
