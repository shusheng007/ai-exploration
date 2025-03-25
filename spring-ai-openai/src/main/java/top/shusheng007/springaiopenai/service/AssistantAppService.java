package top.shusheng007.springaiopenai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.web.dto.MyChatResponse;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssistantAppService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public MyChatResponse chat(MyChatRequest myChatRequest) {

        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.60)
                        .vectorStore(vectorStore)
                        .build())
                .build();


        String chatId = Optional
                .ofNullable(myChatRequest.getChatId())
                .orElse(UUID.randomUUID().toString());
        CotResponseValue response = chatClient
                .prompt()
                .user(myChatRequest.getQuestion())
                .advisors(advisorSpec ->
                        advisorSpec
                                .param("chat_memory_conversation_id", chatId))
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .entity(new DeepSeekR1ModelOutputConverter());

        return new MyChatResponse(chatId, response.chainOfThought(), response.answer());
    }

}
