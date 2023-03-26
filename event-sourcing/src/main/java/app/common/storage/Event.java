package app.common.storage;

import app.common.event.GenericEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "aggregate_id")
@Table(name = "events")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Event implements GenericEvent {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Column(columnDefinition = "timestamp with time zone")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();
}

