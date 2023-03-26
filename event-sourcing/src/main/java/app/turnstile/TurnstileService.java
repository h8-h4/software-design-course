package app.turnstile;

import app.common.event.publisher.EventPublisher;
import app.manager.SubscriptionService;
import app.manager.dto.Subscription;
import app.turnstile.events.TurnstileEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TurnstileService {
    private final EventPublisher eventPublisher;
    private final SubscriptionService subscriptionService;

    public boolean letIn(String clientId) {
        eventPublisher.publish(TurnstileEvent
                .builder()
                .clientId(clientId)
                .direction(TurnstileEvent.Direction.IN)
                .build()
        );

        final Optional<Subscription> subscription = subscriptionService.getSubscription(clientId);

        if (subscription.isEmpty() || subscription.get().getEndTime() == null) {
            return false;
        }

        return subscription.get().getEndTime().isAfter(OffsetDateTime.now());
    }

    public void letOut(String clientId) {
        eventPublisher.publish(
                TurnstileEvent.builder()
                        .clientId(clientId)
                        .direction(TurnstileEvent.Direction.OUT)
                        .build()
        );
    }
}
