package app.manager.events;

import app.common.storage.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = UpdateSubscriptionEvent.AGGREGATE_ID)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubscriptionEvent extends Event {
    public static final String AGGREGATE_ID = "updateSubscription";

    private String clientId;

    private int months;
}
