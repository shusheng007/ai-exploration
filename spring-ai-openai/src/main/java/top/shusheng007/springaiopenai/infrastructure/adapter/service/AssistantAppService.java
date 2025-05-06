package top.shusheng007.springaiopenai.infrastructure.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.facade.web.dto.MyChatResponse;
import top.shusheng007.springaiopenai.infrastructure.adapter.tool.TradeTools;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RequiredArgsConstructor
@Service
public class AssistantAppService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

//    private final TradeTools tradeTools;

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
        Message userMsg = new UserMessage(myChatRequest.getQuestion());
        Prompt prompt = new Prompt(List.of(userMsg), ChatOptions.builder().temperature(0.5D).build());

        String response = chatClient
                .prompt(prompt)
//                .user(myChatRequest.getQuestion())
                .advisors(advisorSpec ->
                        advisorSpec.
                                param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
//                .advisors(retrievalAugmentationAdvisor)
//                .tools(new TradeTools())
//                .toolContext(Map.of("myOrderId","sng-001"))
                .call()
                .content();

        return new MyChatResponse(chatId, "", response);
    }


}
