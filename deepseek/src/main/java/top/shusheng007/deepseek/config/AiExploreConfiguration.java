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
import java.util.Optional;

@Slf4j
@Configuration
public class AiExploreConfiguration {

//    @Bean
//    OpenAiChatModel openAiChatModel() {
//        String openai_api_key = System.getenv("OPENAI_API_KEY");
//        OpenAiChatModel model = OpenAiChatModel.builder()
//                .baseUrl("https://langchain4j.dev/demo/openai/v1")
////                .apiKey(openai_api_key)
//                .apiKey("demo")
////                .baseUrl("https://api.deepseek.com")
////                .modelName("deepseek-chat") //DeepSeek-V3
//                .modelName("gpt-4o-mini")
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
                log.info("=====================onRequest===========================");

                List<ChatMessage> messages = chatRequest.messages();
                log.info("request chatMessages: {}", messages);

                ChatRequestParameters parameters = chatRequest.parameters();
                log.info("modelName:{}", parameters.modelName());
                log.info("temperature:{}", parameters.temperature());
                log.info("topP:{}", parameters.topP());
                log.info("topK:{}", parameters.topK());
                log.info("frequencyPenalty:{}", parameters.frequencyPenalty());
                log.info("presencePenalty:{}", parameters.presencePenalty());
                log.info("maxOutputTokens:{}", parameters.maxOutputTokens());
                log.info("stopSequences:{}", parameters.stopSequences());
                log.info("toolSpecifications:{}", parameters.toolSpecifications());
                log.info("toolChoice:{}", parameters.toolChoice());
                log.info("responseFormat:{}", parameters.responseFormat());

                if (parameters instanceof OpenAiChatRequestParameters openAiParameters) {
                    log.info("maxCompletionTokens:{}", openAiParameters.maxCompletionTokens());
                    log.info("logitBias:{}", openAiParameters.logitBias());
                    log.info("parallelToolCalls:{}", openAiParameters.parallelToolCalls());
                    log.info("seed:{}", openAiParameters.seed());
                    log.info("user:{}", openAiParameters.user());
                    log.info("store:{}", openAiParameters.store());
                    log.info("metadata:{}", openAiParameters.metadata());
                    log.info("serviceTier:{}", openAiParameters.serviceTier());
                    log.info("reasoningEffort:{}", openAiParameters.reasoningEffort());
                }

                Map<Object, Object> attributes = requestContext.attributes();
                attributes.put("my-attribute", "my-value");
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                ChatResponse chatResponse = responseContext.chatResponse();
                log.info("=====================onResponse===========================");

                AiMessage aiMessage = chatResponse.aiMessage();
                log.info("response aiMessage:{}", aiMessage);

                ChatResponseMetadata metadata = chatResponse.metadata();
                log.info("id:{}", metadata.id());
                log.info("modelName:{}", metadata.modelName());
                log.info("finishReason:{}", metadata.finishReason());

                if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
                    log.info("created:{}", openAiMetadata.created());
                    log.info("serviceTier:{}", openAiMetadata.serviceTier());
                    log.info("systemFingerprint:{}", openAiMetadata.systemFingerprint());
                }

                TokenUsage tokenUsage = metadata.tokenUsage();
                log.info("inputTokenCount:{}", tokenUsage.inputTokenCount());
                log.info("outputTokenCount:{}", tokenUsage.outputTokenCount());
                log.info("totalTokenCount:{}", tokenUsage.totalTokenCount());
                if (tokenUsage instanceof OpenAiTokenUsage openAiTokenUsage) {
                    log.info("cachedTokens:{}", Optional.ofNullable(openAiTokenUsage.inputTokensDetails())
                            .map(OpenAiTokenUsage.InputTokensDetails::cachedTokens).orElse(null));
                    log.info("reasoningTokens:{}", Optional.ofNullable(openAiTokenUsage.outputTokensDetails())
                            .map(OpenAiTokenUsage.OutputTokensDetails::reasoningTokens).orElse(null));
                }

                ChatRequest chatRequest = responseContext.chatRequest();
                log.info("chatRequest:{}", chatRequest);

                Map<Object, Object> attributes = responseContext.attributes();
                log.info("my-attribute:{}", attributes.get("my-attribute"));
            }

            @Override
            public void onError(ChatModelErrorContext errorContext) {
                log.info("=====================onError===========================");
                log.error("error", errorContext.error());

                ChatRequest chatRequest = errorContext.chatRequest();
                log.info("chatRequest:{}", chatRequest);

                Map<Object, Object> attributes = errorContext.attributes();
                log.info("my-attribute:{}", attributes.get("my-attribute"));
            }
        };

        return listener;
    }


    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .maxMessages(15)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .id(memoryId).build();
    }

    @Bean
    public EmbeddingStore embeddingStore() {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("./data");
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.from(embeddingStore());
    }


}
