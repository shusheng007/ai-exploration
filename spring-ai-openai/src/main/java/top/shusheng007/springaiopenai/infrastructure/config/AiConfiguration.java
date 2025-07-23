package top.shusheng007.springaiopenai.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.ResponseFormat;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AiConfiguration {
    //private final VectorStore vectorStore;
    //private final ChatMemoryRepository chatMemoryRepository;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient.Builder deepSeekChatClientBuilder(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultOptions(DeepSeekChatOptions.builder()
                        .temperature(1.3)
                        .responseFormat(ResponseFormat.builder()
                                .type(ResponseFormat.Type.JSON_OBJECT)
                                .build())
                        .build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build());
    }

    @Bean
    public ChatClient deepSeekChatClient(@Qualifier("deepSeekChatClientBuilder") ChatClient.Builder deepSeekChatClientBuilder) {
        return deepSeekChatClientBuilder.build();
    }

    @Bean
    public ChatClient.Builder openAiChatClientBuilder(OpenAiChatModel openAichatModel) {
        return ChatClient.builder(openAichatModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.7)
                        .build())
                .defaultAdvisors(new SimpleLoggerAdvisor())//for log
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build());
    }

    @Bean
    public ChatClient openAiChatClient(@Qualifier("openAiChatClientBuilder") ChatClient.Builder openAiChatClientBuilder) {
        return openAiChatClientBuilder.build();
    }


}
