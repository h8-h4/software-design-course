package exchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ExchangeConfig exchangeConfig(
            @Value("exchange.service-endpoint") String endpoint
    ) {
        return new ExchangeConfig(endpoint);
    }
}
