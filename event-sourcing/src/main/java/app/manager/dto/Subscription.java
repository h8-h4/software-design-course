package app.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Subscription {
    private String clientId;
    private String clientName;
    @Nullable
    private OffsetDateTime endTime;
}
