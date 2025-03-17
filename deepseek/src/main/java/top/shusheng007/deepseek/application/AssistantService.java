package top.shusheng007.deepseek.application;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shusheng007.deepseek.service.AssistantAiService;

import java.util.List;

/**
 * Copyright (C) 2023 ShuSheng007
 * <p>
 * Author ShuSheng007
 * Time 2025/3/16 11:16
 * Description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AssistantService {
    private final AssistantAiService assistantAiService;


    public String chat(String userId, String userMessage) {
        Result<AiMessage> result = assistantAiService.chat(userId, userMessage);

        AiMessage content = result.content();
        FinishReason finishReason = result.finishReason();
        List<Content> sources = result.sources();
        TokenUsage tokenUsage = result.tokenUsage();
        List<ToolExecution> toolExecutions = result.toolExecutions();

        log.info("==chat-result==");
        log.info("content:{}", content.text());
        log.info("finishReason:{}", finishReason.toString());
        log.info("toolExecutions:{}", toolExecutions.toString());
        log.info("tokenUsage:{}", tokenUsage);
        log.info("sources:{}", sources);

        return result.content().text();
    }


//    public void callTools() {
//        // STEP 1: User specify tools and query
//        // Tools
//        PurchaseTool weatherTools = new PurchaseTool();
//        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(weatherTools);
//        // User query
//        List<ChatMessage> chatMessages = new ArrayList<>();
//        UserMessage userMessage = UserMessage.from("""
//
//                """);
//        chatMessages.add(userMessage);
//        // Chat request
//        ChatRequest chatRequest = ChatRequest.builder()
//                .messages(chatMessages)
//                .parameters(ChatRequestParameters.builder()
//                        .toolSpecifications(toolSpecifications)
//                        .build())
//                .build();
//
//
//        // STEP 2: Model generates tool execution request
//        ChatResponse chatResponse = openAiModel.chat(chatRequest);
//        AiMessage aiMessage = chatResponse.aiMessage();
//        List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
//        System.out.println("Out of the " + toolSpecifications.size() + " tools declared in WeatherTools, " + toolExecutionRequests.size() + " will be invoked:");
//        toolExecutionRequests.forEach(toolExecutionRequest -> {
//            System.out.println("Tool name: " + toolExecutionRequest.name());
//            System.out.println("Tool args:" + toolExecutionRequest.arguments());
//        });
//        chatMessages.add(aiMessage);
//
//
//        // STEP 3: User executes tool(s) to obtain tool results
//        toolExecutionRequests.forEach(toolExecutionRequest -> {
//            ToolExecutor toolExecutor = new DefaultToolExecutor(weatherTools, toolExecutionRequest);
//            System.out.println("Now let's execute the tool " + toolExecutionRequest.name());
//            String result = toolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
//            ToolExecutionResultMessage toolExecutionResultMessages = ToolExecutionResultMessage.from(toolExecutionRequest, result);
//            chatMessages.add(toolExecutionResultMessages);
//        });
//
//
//        // STEP 4: Model generates final response
//        ChatRequest chatRequest2 = ChatRequest.builder()
//                .messages(chatMessages)
//                .parameters(ChatRequestParameters.builder()
//                        .toolSpecifications(toolSpecifications)
//                        .build())
//                .build();
//        ChatResponse finalChatResponse = openAiModel.chat(chatRequest2);
//    }


}
