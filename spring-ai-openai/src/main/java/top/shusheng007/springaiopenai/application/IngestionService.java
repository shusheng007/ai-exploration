package top.shusheng007.springaiopenai.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class IngestionService implements CommandLineRunner {
    private final VectorStore vectorStore;

    @Value("classpath:/static/private_data.txt")
    private Resource personInfo;

    @Override
    public void run(String... args) throws Exception {
        var txtReader = new TextReader(personInfo);
        TextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(txtReader.get()));
        log.info("Vector store loaded with data");
    }
}
