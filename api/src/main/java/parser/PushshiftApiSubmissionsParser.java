package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Submission;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class PushshiftApiSubmissionsParser implements SubmissionsParser {
    private static final String SUBMISSION_ARRAY_NODE = "data";

    private final ObjectMapper objectMapper;

    public PushshiftApiSubmissionsParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Submission> parseSubmissions(String rawSubmissions) {
        try {
            JsonNode dataNode = objectMapper
                    .readTree(rawSubmissions)
                    .get(SUBMISSION_ARRAY_NODE);

            if (dataNode == null) {
                throw new ParseException("Response does not have mandatory data field");
            }

            return objectMapper.readValue(
                    objectMapper.treeAsTokens(dataNode),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Submission.class)
            );
        } catch (JsonProcessingException e) {
            throw new ParseException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
