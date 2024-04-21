package top.ss007.openaiexplore.domain;

import lombok.Data;

import java.util.List;

@Data
public class GovReport {
    private String title;
    private List<String> keyPoints;
}
