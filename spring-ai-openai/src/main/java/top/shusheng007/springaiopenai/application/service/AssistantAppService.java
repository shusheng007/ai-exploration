package top.shusheng007.springaiopenai.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.application.dto.LogAnalyseReportResponse;
import top.shusheng007.springaiopenai.application.port.LLMChatPort;
import top.shusheng007.springaiopenai.application.dto.DefaultChatRequest;
import top.shusheng007.springaiopenai.application.dto.DefaultChatResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantAppService {
    private final LLMChatPort llmChatPort;

    public DefaultChatResponse chat(DefaultChatRequest request) {
        log.info("==AssistantAppService==: request: {}", request);
        log.info("---------------------------------------------------------------------");

        return llmChatPort.chat(request);
    }

    public LogAnalyseReportResponse analyseErrorLog(){
        log.info("==AssistantAppService==: analyseErrorLog");
        log.info("---------------------------------------------------------------------");

        return llmChatPort.analyseErrorLog();
    }

}
