package top.shusheng007.deepseek.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import top.shusheng007.deepseek.domain.Order;

import java.io.IOException;

@Slf4j
@Component
public class Tools {

    @Tool(name = "webParser", value = {"基于给定的web url获取其内容"})
    public String getWebPageContent(@P("url of the page") String url) {
        Document jsoupDocument = null;
        try {
            jsoupDocument = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsoupDocument.body().text();
    }

    @Tool(name = "createOrder", value = {"向美团等外卖服务平台下单并返回结果"})
    public Order makeOrder(@P("the order detail that be send to purchase platform") Order order) {

        order.setOrderId("sng001");

        log.info("下单成功：{}", order);
        
        return order;
    }


}
