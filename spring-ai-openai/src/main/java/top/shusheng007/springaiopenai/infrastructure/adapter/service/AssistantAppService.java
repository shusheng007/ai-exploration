package top.shusheng007.springaiopenai.infrastructure.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatResponse;
import top.shusheng007.springaiopenai.infrastructure.adapter.tool.LifeHelpTools;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class AssistantAppService {
    @Qualifier("deepSeekChatClient")
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
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();


        String chatId = Optional
                .ofNullable(myChatRequest.getChatId())
                .orElse(UUID.randomUUID().toString());
//        Message userMsg = new UserMessage(myChatRequest.getQuestion());
//        String systemMessage = """
//                  You are a helpful assistant.
//                  Use your training data to provide answers about the questions.
//                  If the requested information is not available in your training data, use the provided Tools to get the information.
//                  If the requested information is not available from any sources, then respond by explaining the reason that the information is not available.
//                """;
        String systemMessage = """
                  You are a helpful assistant.
                  Use your training data to provide answers about the questions.
                  If the requested information is not available in your training data or user provided context, use the provided Tools to get the information.
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
                                param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(retrievalAugmentationAdvisor)
                .tools(new LifeHelpTools())
                .toolCallbacks(toolCallbackProvider.getToolCallbacks())
//                .toolContext(Map.of("myOrderId","sng-001"))
                .call()
                .content();

        return new MyChatResponse(chatId, response);
    }


}
