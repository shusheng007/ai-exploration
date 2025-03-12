package top.shusheng007.deepseek.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Tools {

//    @Tool(name = "webParser",value = {"Returns the content of a web page, given the URL"})
//    public String getWebPageContent(@P("URL of the page") String url) {
//        Document jsoupDocument = null;
//        try {
//            jsoupDocument = Jsoup.connect(url).get();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return jsoupDocument.body().text();
//    }
}
