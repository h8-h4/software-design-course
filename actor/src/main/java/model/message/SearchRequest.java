package model.message;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@RequiredArgsConstructor
@Builder
@Jacksonized
public class SearchRequest implements Message {
    String request;
}
