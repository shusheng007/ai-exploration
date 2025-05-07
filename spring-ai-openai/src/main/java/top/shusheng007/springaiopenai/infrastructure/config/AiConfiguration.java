package top.shusheng007.springaiopenai.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AiConfiguration {
//    private final VectorStore vectorStore;

    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder
//                .defaultSystem("You are a helpful assistant")
                .defaultAdvisors(new SimpleLoggerAdvisor())//for log
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory()))
//                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory())
//                .defaultAdvisors(VectorStoreChatMemoryAdvisor.builder(vectorStore).build())
                .build();
    }

}
