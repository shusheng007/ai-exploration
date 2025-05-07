package top.shusheng007.springaiopenai.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IngestionService {
    private final VectorStore vectorStore;

    @Value("classpath:/static/dog2wang_profile.txt")
    private Resource personInfo;

    public void ingestData() {
        //Extract
        DocumentReader txtReader = new TextReader(personInfo);
        List<Document> documents = txtReader.read();

        //Transformer
        DocumentTransformer textSplitter = new TokenTextSplitter();
        List<Document> transformDocuments = textSplitter.transform(documents);

        //Load
        vectorStore.write(transformDocuments);

        log.info("Vector store loaded with data");
    }
}
