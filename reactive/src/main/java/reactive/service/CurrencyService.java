package reactive.service;

import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactive.model.Currency;

import java.util.Map;
import java.util.Objects;

import static reactive.model.Currency.*;

@Component
@NoArgsConstructor
public class CurrencyService {
    private final static Map<Pair<Currency, Currency>, Double> CONVERT_TABLE = Map.of(
            Pair.of(RUB, RUB), 1d,
            Pair.of(RUB, USD), 0.013d,
            Pair.of(RUB, EUR), 0.012d,
            Pair.of(USD, USD), 1d,
            Pair.of(USD, RUB), 78d,
            Pair.of(USD, EUR), 0.92d,
            Pair.of(EUR, EUR), 1d,
            Pair.of(EUR, USD), 1.09d,
            Pair.of(EUR, RUB), 84.79d
    );


    public double convertPrice(double price, Currency from, Currency to) {
        return price * Objects.requireNonNullElse(CONVERT_TABLE.get(Pair.of(from, to)), 1d);
    }
}
