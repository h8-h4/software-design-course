package model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@RequiredArgsConstructor
@Builder
@Jacksonized
public class SearchLink {
    String name;
    String url;
    String source;
}
