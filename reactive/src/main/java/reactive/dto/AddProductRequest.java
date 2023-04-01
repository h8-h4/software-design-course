package reactive.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import reactive.model.Currency;

@Value
@With
@RequiredArgsConstructor
@Builder
@Jacksonized
public class AddProductRequest {
    String name;
    double price;
    Currency currency;
}
