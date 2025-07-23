package top.shusheng007.springaiopenai.application.port;

import top.shusheng007.springaiopenai.application.dto.DefaultChatRequest;
import top.shusheng007.springaiopenai.application.dto.DefaultChatResponse;
import top.shusheng007.springaiopenai.application.dto.LogAnalyseReportResponse;

public interface LLMChatPort {

    DefaultChatResponse chat(DefaultChatRequest defaultChatRequest);

    LogAnalyseReportResponse analyseErrorLog();
}
