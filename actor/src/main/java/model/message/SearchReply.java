package model.message;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import model.SearchLink;

import java.util.List;

@Value
@RequiredArgsConstructor
@Builder
@Jacksonized
@With
public class SearchReply implements Message {
    List<SearchLink> links;
}
