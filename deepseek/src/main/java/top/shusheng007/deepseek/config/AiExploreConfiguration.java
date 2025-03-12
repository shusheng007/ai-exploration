package top.shusheng007.deepseek.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.segment.TextSegment;
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

    @Bean
    OpenAiChatModel openAiChatModel(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("DS_API_KEY"))
                .baseUrl("https://api.deepseek.com")
                .modelName("deepseek-chat") //DeepSeek-V3
                .temperature(1.3)
                .timeout(Duration.ofSeconds(60))
                .topP(0.8)
                .maxTokens(1000)
                .responseFormat("json_schema")
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
//                .listeners(List.of(listener))
                .build();

        return model;
    }

    ChatModelListener listener = new ChatModelListener() {

        @Override
        public void onRequest(ChatModelRequestContext requestContext) {
            ChatRequest chatRequest = requestContext.chatRequest();

            List<ChatMessage> messages = chatRequest.messages();
            System.out.println(messages);

            ChatRequestParameters parameters = chatRequest.parameters();
            System.out.println(parameters.modelName());
            System.out.println(parameters.temperature());
            System.out.println(parameters.topP());
            System.out.println(parameters.topK());
            System.out.println(parameters.frequencyPenalty());
            System.out.println(parameters.presencePenalty());
            System.out.println(parameters.maxOutputTokens());
            System.out.println(parameters.stopSequences());
            System.out.println(parameters.toolSpecifications());
            System.out.println(parameters.toolChoice());
            System.out.println(parameters.responseFormat());

            if (parameters instanceof OpenAiChatRequestParameters openAiParameters) {
                System.out.println(openAiParameters.maxCompletionTokens());
                System.out.println(openAiParameters.logitBias());
                System.out.println(openAiParameters.parallelToolCalls());
                System.out.println(openAiParameters.seed());
                System.out.println(openAiParameters.user());
                System.out.println(openAiParameters.store());
                System.out.println(openAiParameters.metadata());
                System.out.println(openAiParameters.serviceTier());
                System.out.println(openAiParameters.reasoningEffort());
            }

            Map<Object, Object> attributes = requestContext.attributes();
            attributes.put("my-attribute", "my-value");
        }

        @Override
        public void onResponse(ChatModelResponseContext responseContext) {
            ChatResponse chatResponse = responseContext.chatResponse();

            AiMessage aiMessage = chatResponse.aiMessage();
            System.out.println(aiMessage);

            ChatResponseMetadata metadata = chatResponse.metadata();
            System.out.println(metadata.id());
            System.out.println(metadata.modelName());
            System.out.println(metadata.finishReason());

            if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
                System.out.println(openAiMetadata.created());
                System.out.println(openAiMetadata.serviceTier());
                System.out.println(openAiMetadata.systemFingerprint());
            }

            TokenUsage tokenUsage = metadata.tokenUsage();
            System.out.println(tokenUsage.inputTokenCount());
            System.out.println(tokenUsage.outputTokenCount());
            System.out.println(tokenUsage.totalTokenCount());
            if (tokenUsage instanceof OpenAiTokenUsage openAiTokenUsage) {
                System.out.println(openAiTokenUsage.inputTokensDetails().cachedTokens());
                System.out.println(openAiTokenUsage.outputTokensDetails().reasoningTokens());
            }

            ChatRequest chatRequest = responseContext.chatRequest();
            System.out.println(chatRequest);

            Map<Object, Object> attributes = responseContext.attributes();
            System.out.println(attributes.get("my-attribute"));
        }

        @Override
        public void onError(ChatModelErrorContext errorContext) {
            Throwable error = errorContext.error();
            error.printStackTrace();

            ChatRequest chatRequest = errorContext.chatRequest();
            System.out.println(chatRequest);

            Map<Object, Object> attributes = errorContext.attributes();
            System.out.println(attributes.get("my-attribute"));
        }
    };

    @Bean
    public EmbeddingStore embeddingStore(){
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("./data");
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetriever(){
        return EmbeddingStoreContentRetriever.from(embeddingStore());
    }





}
