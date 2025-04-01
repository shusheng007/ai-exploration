package top.shusheng007.springaiopenai.domain.aggregate;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;
import java.util.List;


@Data
public class Order {
    @ToolParam(required = false)
    private String orderId;
    @ToolParam(description = "Order delivery address")
    private String address;
    @ToolParam(description = "Items in the order")
    private List<OrderItem> items;
    @Data
    public static class OrderItem{
        private String name;
        private BigDecimal price;
        @ToolParam(description = "Number of copies of the product")
        private Integer count;
    }

}
