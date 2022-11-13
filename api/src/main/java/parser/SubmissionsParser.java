package parser;

import model.Submission;

import java.util.List;

public interface SubmissionsParser {
    List<Submission> parseSubmissions(String rawSubmissions);
}
