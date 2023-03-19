package model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class SearchService {
    String name;
    String searchUrl;
}
