package app.manager.appliers;

import app.common.event.applier.ApplierContainer;
import app.common.event.spring.annotation.Applier;
import app.manager.dto.Subscription;
import app.manager.events.RegisterClientEvent;
import app.manager.events.UpdateSubscriptionEvent;

@Applier
public class SubscriptionApplier extends ApplierContainer<Subscription> {

    @Applier
    public Subscription apply(RegisterClientEvent event, Subscription subscription) {
        subscription.setClientId(event.getClientId());
        subscription.setClientName(event.getClientName());
        return subscription;
    }

    @Applier
    public Subscription apply(UpdateSubscriptionEvent event, Subscription subscription) {
        if (subscription.getEndTime() == null) {
            subscription.setEndTime(event.getCreatedAt().plusMonths(event.getMonths()));
        } else {
            subscription.setEndTime(subscription.getEndTime().plusMonths(event.getMonths()));
        }

        return subscription;
    }
}
