package top.shusheng007.springaiopenai.infrastructure.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatResponse;
import top.shusheng007.springaiopenai.infrastructure.adapter.tool.DateTimeTools;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RequiredArgsConstructor
@Service
public class AssistantAppService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final SyncMcpToolCallbackProvider toolCallbackProvider;

    public MyChatResponse chat(MyChatRequest myChatRequest) {

        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.60)
                        .topK(3)
                        .vectorStore(vectorStore)
                        .build())
                .build();


        String chatId = Optional
                .ofNullable(myChatRequest.getChatId())
                .orElse(UUID.randomUUID().toString());
        Message userMsg = new UserMessage(myChatRequest.getQuestion());
        String systemMessage = """
                  You are a helpful assistant who answers questions about Weather. 
                  Use your training data to provide answers about the questions. 
                  If the requested information is not available in your training data, use the provided Tools to get the information.
                  If the requested information is not available from any sources, then respond by explaining the reason that the information is not available. 
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
//        Prompt prompt = new Prompt(List.of(userMsg), ChatOptions
//                .builder()
//                .temperature(0.5D)
//                .build());

        String response = chatClient
                .prompt(systemPromptTemplate.create())
//                .prompt()
                .user(myChatRequest.getQuestion())
                .advisors(advisorSpec ->
                        advisorSpec.
                                param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(retrievalAugmentationAdvisor)
                .tools(new DateTimeTools())
                .toolCallbacks(toolCallbackProvider)
//                .toolContext(Map.of("myOrderId","sng-001"))
                .call()
                .content();

        return new MyChatResponse(chatId, response);
    }


}
