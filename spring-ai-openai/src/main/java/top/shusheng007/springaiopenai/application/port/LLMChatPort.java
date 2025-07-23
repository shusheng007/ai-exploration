package top.shusheng007.springaiopenai.application.port;

import top.shusheng007.springaiopenai.application.dto.DefaultChatRequest;
import top.shusheng007.springaiopenai.application.dto.DefaultChatResponse;

public interface LLMChatPort {

    DefaultChatResponse chat(DefaultChatRequest defaultChatRequest);

    DefaultChatResponse analyseErrorLog();
}
