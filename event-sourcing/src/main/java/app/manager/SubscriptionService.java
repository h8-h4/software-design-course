package app.manager;

import app.manager.appliers.SubscriptionApplier;
import app.manager.dto.Subscription;
import app.manager.events.UpdateSubscriptionEvent;
import app.manager.repository.RegisterClientEventRepository;
import app.manager.repository.UpdateSubscriptionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final RegisterClientEventRepository registerClientEventRepository;
    private final UpdateSubscriptionEventRepository updateSubscriptionEventRepository;
    private final SubscriptionApplier subscriptionApplier;

    public Optional<Subscription> getSubscription(String clientId) {
        return registerClientEventRepository
                .findByClientId(clientId)
                .map(clientEvent -> {
                    final List<UpdateSubscriptionEvent> subscriptionUpdates =
                            updateSubscriptionEventRepository.findAllByClientIdOrderByCreatedAt(
                                    clientEvent.getClientId()
                            );

                    Subscription subscription = subscriptionApplier.apply(new Subscription(), clientEvent);
                    return subscriptionApplier.apply(subscription, subscriptionUpdates);
                });
    }
}
