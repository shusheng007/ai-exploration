package top.shusheng007.springaiopenai.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDetail {
    private Summary summary;
    private Severity severity;
    private List<Pattern> patterns;
    @JsonProperty("context_clues")
    private List<String> contextClues;
    @JsonProperty("similar_cases")
    private List<SimilarCase> similarCases;
    private Recommendation recommendations;
    private String confidence;
    @JsonProperty("verification_notes")
    private List<String> verificationNotes;

    @Getter
    @Setter
    public static class Summary {
        @JsonProperty("error_types")
        private List<String> errorTypes;
        @JsonProperty("time_range")
        private String timeRange;
        private String module;
    }

    @Getter
    @Setter
    public static class Severity {
        private String level;
        private String rationale;
    }

    @Getter
    @Setter
    public static class Pattern {
        private String message;
        private int count;
    }

    @Getter
    @Setter
    public static class SimilarCase {
        @JsonProperty("past_log_snippet")
        private String pastLogSnippet;
        private String reference;
    }

    @Getter
    @Setter
    public static class Recommendation {
        @JsonProperty("short_term")
        private List<String> shortTerm;
        @JsonProperty("long_term")
        private List<String> longTerm;
    }
}
