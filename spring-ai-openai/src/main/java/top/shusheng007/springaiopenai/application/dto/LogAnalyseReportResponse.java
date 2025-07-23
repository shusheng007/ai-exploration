package top.shusheng007.springaiopenai.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shusheng007.springaiopenai.domain.entity.ReportDetail;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAnalyseReportResponse {
    private String chatId;
    private ReportDetail reportDetail;
}
