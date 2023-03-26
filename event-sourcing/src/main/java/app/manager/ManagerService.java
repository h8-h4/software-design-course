package app.manager;

import app.common.event.publisher.EventPublisher;
import app.manager.dto.Client;
import app.manager.dto.SubscriptionUpdate;
import app.manager.events.RegisterClientEvent;
import app.manager.events.UpdateSubscriptionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final EventPublisher eventPublisher;

    public String register(Client client) {
        final String clientId = UUID.randomUUID().toString();
        eventPublisher.publish(
                RegisterClientEvent.builder()
                        .clientId(clientId)
                        .clientName(client.getName())
                        .build()
        );

        return clientId;
    }

    public void updateSubscription(SubscriptionUpdate subscriptionUpdate) {
        eventPublisher.publish(
                UpdateSubscriptionEvent.builder()
                        .clientId(subscriptionUpdate.getClientId())
                        .months(subscriptionUpdate.getMonths())
                        .build()
        );

    }
}
