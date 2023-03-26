package app.turnstile.events;

import app.common.storage.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = TurnstileEvent.AGGREGATE_ID)
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TurnstileEvent extends Event {
    public static final String AGGREGATE_ID = "turnstile";

    private String clientId;

    private Direction direction;

    public enum Direction {
        IN, OUT
    }
}
