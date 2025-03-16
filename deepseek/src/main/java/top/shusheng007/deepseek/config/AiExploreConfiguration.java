package top.shusheng007.deepseek.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class AiExploreConfiguration {

//    @Bean
//    OpenAiChatModel openAiChatModel() {
//        OpenAiChatModel model = OpenAiChatModel.builder()
//                .baseUrl("http://langchain4j.dev/demo/openai/v1")
////                .apiKey(System.getenv("DS_API_KEY"))
//                .apiKey("demo")
////                .baseUrl("https://api.deepseek.com")
////                .modelName("deepseek-chat") //DeepSeek-V3
//                .modelName("gpt-4o-mini") //DeepSeek-V3
//                .temperature(1.3)
//                .timeout(Duration.ofSeconds(60))
//                .topP(0.8)
//                .maxTokens(1000)
//                .responseFormat("json_schema")
//                .strictJsonSchema(true)
//                .logRequests(true)
//                .logResponses(true)
////                .listeners(List.of(listener))
//                .build();
//
//        return model;
//    }


    @Bean
    public ChatModelListener chatModelListener() {
        ChatModelListener listener = new ChatModelListener() {

            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                ChatRequest chatRequest = requestContext.chatRequest();

                List<ChatMessage> messages = chatRequest.messages();
                log.info("{}", messages);

                ChatRequestParameters parameters = chatRequest.parameters();
                log.info("{}", parameters.modelName());
                log.info("{}", parameters.temperature());
                log.info("{}", parameters.topP());
                log.info("{}", parameters.topK());
                log.info("{}", parameters.frequencyPenalty());
                log.info("{}", parameters.presencePenalty());
                log.info("{}", parameters.maxOutputTokens());
                log.info("{}", parameters.stopSequences());
                log.info("{}", parameters.toolSpecifications());
                log.info("{}", parameters.toolChoice());
                log.info("{}", parameters.responseFormat());

                if (parameters instanceof OpenAiChatRequestParameters openAiParameters) {
                    log.info("{}", openAiParameters.maxCompletionTokens());
                    log.info("{}", openAiParameters.logitBias());
                    log.info("{}", openAiParameters.parallelToolCalls());
                    log.info("{}", openAiParameters.seed());
                    log.info("{}", openAiParameters.user());
                    log.info("{}", openAiParameters.store());
                    log.info("{}", openAiParameters.metadata());
                    log.info("{}", openAiParameters.serviceTier());
                    log.info("{}", openAiParameters.reasoningEffort());
                }

                Map<Object, Object> attributes = requestContext.attributes();
                attributes.put("my-attribute", "my-value");
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                ChatResponse chatResponse = responseContext.chatResponse();

                AiMessage aiMessage = chatResponse.aiMessage();
                log.info("{}", aiMessage);

                ChatResponseMetadata metadata = chatResponse.metadata();
                log.info("{}", metadata.id());
                log.info("{}", metadata.modelName());
                log.info("{}", metadata.finishReason());

                if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
                    log.info("{}", openAiMetadata.created());
                    log.info("{}", openAiMetadata.serviceTier());
                    log.info("{}", openAiMetadata.systemFingerprint());
                }

                TokenUsage tokenUsage = metadata.tokenUsage();
                log.info("{}", tokenUsage.inputTokenCount());
                log.info("{}", tokenUsage.outputTokenCount());
                log.info("{}", tokenUsage.totalTokenCount());
//                if (tokenUsage instanceof OpenAiTokenUsage openAiTokenUsage) {
//                    log.info("{}", openAiTokenUsage.inputTokensDetails().cachedTokens());
//                    log.info("{}", openAiTokenUsage.outputTokensDetails().reasoningTokens());
//                }

                ChatRequest chatRequest = responseContext.chatRequest();
                log.info("{}", chatRequest);

                Map<Object, Object> attributes = responseContext.attributes();
                log.info("{}", attributes.get("my-attribute"));
            }

            @Override
            public void onError(ChatModelErrorContext errorContext) {
                Throwable error = errorContext.error();
                error.printStackTrace();

                ChatRequest chatRequest = errorContext.chatRequest();
                log.info("{}", chatRequest);

                Map<Object, Object> attributes = errorContext.attributes();
                log.info("{}", attributes.get("my-attribute"));
            }
        };

        return listener;
    }


    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .id(memoryId).build();
    }

//    @Bean
//    public EmbeddingStore embeddingStore() {
//        List<Document> documents = FileSystemDocumentLoader.loadDocuments("./data");
//        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
//
//        return embeddingStore;
//    }
//
//    @Bean
//    public ContentRetriever contentRetriever() {
//        return EmbeddingStoreContentRetriever.from(embeddingStore());
//    }


}
