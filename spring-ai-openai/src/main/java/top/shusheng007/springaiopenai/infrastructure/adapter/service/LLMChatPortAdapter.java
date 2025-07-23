package top.shusheng007.springaiopenai.infrastructure.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.shusheng007.springaiopenai.application.dto.LogAnalyseReportResponse;
import top.shusheng007.springaiopenai.application.port.LLMChatPort;
import top.shusheng007.springaiopenai.application.dto.DefaultChatRequest;
import top.shusheng007.springaiopenai.application.dto.DefaultChatResponse;
import top.shusheng007.springaiopenai.domain.entity.ReportDetail;
import top.shusheng007.springaiopenai.infrastructure.adapter.tool.LifeHelpTools;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LLMChatPortAdapter implements LLMChatPort {
    @Qualifier("deepSeekChatClient")
    private final ChatClient deepSeekChatClient;
//    @Qualifier("openAiChatClient")
//    private final ChatClient openAiChatClient;

    private final VectorStore vectorStore;
    private final SyncMcpToolCallbackProvider toolCallbackProvider;
    private final FileService fileService;

    @Override
    public DefaultChatResponse chat(DefaultChatRequest defaultChatRequest) {

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
                .ofNullable(defaultChatRequest.getChatId())
                .orElse(UUID.randomUUID().toString());

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(fileService.loadFile(FileService.DEFAULT_SYSTEM_MESSAGE_PROMPT));
//        Prompt prompt = new Prompt(List.of(userMsg), ChatOptions
//                .builder()
//                .temperature(0.5D)
//                .build());

        String response = deepSeekChatClient
                .prompt()
                .system(systemPromptTemplate.render())
                .user(defaultChatRequest.getQuestion())
                .advisors(advisorSpec ->
                        advisorSpec.
                                param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(retrievalAugmentationAdvisor)
                .tools(new LifeHelpTools())
                .toolCallbacks(toolCallbackProvider.getToolCallbacks())
//                .toolContext(Map.of("myOrderId","sng-001"))
                .call()
                .content();

        return new DefaultChatResponse(chatId, response);
    }

    @Override
    public LogAnalyseReportResponse analyseErrorLog() {
        final String chatId = "system-log-analyser";

        PromptTemplate systemPromptTemplate = new SystemPromptTemplate(fileService.loadFile(FileService.LOG_DIAGNOSE_SYSTEM_MESSAGE_PROMPT));
        PromptTemplate userPromptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .resource(fileService.loadFile(FileService.LOG_DIAGNOSE_USER_MESSAGE_PROMPT))
                .variables(Map.of("error_log", fileService.loadFile(FileService.TEST_LOG_01)))
                .build();

//        String jsonSchema = new BeanOutputConverter<>(ReportDetail.class).getJsonSchema();
//        Prompt prompt = Prompt.builder()
//                .chatOptions(OpenAiChatOptions.builder()
//                        .responseFormat(ResponseFormat.builder()
//                                .type(ResponseFormat.Type.JSON_OBJECT)
//                                .jsonSchema(jsonSchema)
//                                .build())
//                        .build())
//                .messages(systemPromptTemplate.createMessage(), userPromptTemplate.createMessage())
//                .build();
        ReportDetail report = deepSeekChatClient
                .prompt()
                .system(systemPromptTemplate.render())
                .user(userPromptTemplate.render())
                .advisors(advisorSpec ->
                        advisorSpec.
                                param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(toolCallbackProvider.getToolCallbacks())
//                .toolContext(Map.of("myOrderId","sng-001"))
                .call()
                .entity(ReportDetail.class);

        return new LogAnalyseReportResponse(chatId, report);
    }

}
