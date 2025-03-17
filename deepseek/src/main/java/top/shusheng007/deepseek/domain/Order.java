package top.shusheng007.deepseek.domain;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.V;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Copyright (C) 2023 ShuSheng007
 * <p>
 * Author ShuSheng007
 * Time 2025/3/16 16:27
 * Description
 */

@Description("商家生成的外卖订单")
@Data
public class Order {
    @Description("商家的订单号")
    private String orderId;

    @Description("订单包含的项目，例如一份外卖订单里包含：一份牛肉饭，一瓶可乐")
    private List<OrderItem> items;

    @Description("订单的送货地址")
    private String address;
    @Description("商品详情，包含名称，价格，份数")
    @Data
    public static class OrderItem{
        @Description("商品名称")
        private String name;
        @Description("商品价格")
        private BigDecimal price;
        @Description("商品份数")
        private Integer count;
    }

}
