package exchange.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeConfig {
    String exchangeServiceUri;
}
