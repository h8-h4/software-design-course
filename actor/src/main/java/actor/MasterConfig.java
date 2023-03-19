package actor;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import model.SearchService;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Value
@With
public class MasterConfig {
    List<SearchService> services;
    Duration timeout;
}
