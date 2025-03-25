package top.shusheng007.springaiopenai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.web.dto.MyChatResponse;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssistantAppService {
    private final ChatClient chatClient;


    public MyChatResponse chat(MyChatRequest myChatRequest) {
        String chatId = Optional
                .ofNullable(myChatRequest.getChatId())
                .orElse(UUID.randomUUID().toString());
        CotResponseValue response = chatClient
                .prompt()
                .user(myChatRequest.getQuestion())
                .advisors(advisorSpec ->
                        advisorSpec
                                .param("chat_memory_conversation_id", chatId))
                .call()
                .entity(new DeepSeekR1ModelOutputConverter());

        return new MyChatResponse(chatId, response.chainOfThought(), response.answer());
    }

}
