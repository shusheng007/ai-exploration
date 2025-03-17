package top.shusheng007.deepseek.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.service.*;
import dev.langchain4j.service.spring.AiService;
import top.shusheng007.deepseek.domain.GovReport;


@AiService(chatMemoryProvider = "chatMemoryProvider")
public interface AssistantAiService {

    @SystemMessage("""
            you are a smart assistant.
            """)
    Result<AiMessage> chat(@MemoryId String memoryId, @UserMessage String userMessage);

//
//    @UserMessage("中国2024年政府工作报告要点都有什么?")
//    AiMessage summaryReport2();
}
