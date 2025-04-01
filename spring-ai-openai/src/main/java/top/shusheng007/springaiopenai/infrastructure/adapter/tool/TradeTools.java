package top.shusheng007.springaiopenai.infrastructure.adapter.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import top.shusheng007.springaiopenai.domain.aggregate.Order;
import top.shusheng007.springaiopenai.domain.entity.Product;
import top.shusheng007.springaiopenai.domain.valueobj.ProductFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2023 ShuSheng007
 * <p>
 * Author ShuSheng007
 * Time 2025/3/31 23:04
 * Description
 */

@Component
@Slf4j
public class TradeTools {

    @Tool(name = "selectProducts", description = "Search for products through third-party e-commerce platforms")
    public List<Product> selectProducts(@ToolParam(description = "Filters for product search") List<ProductFilter> productFilters) {
        log.info("start search products:{}", productFilters);

        Product product1 = new Product("p-101", "name", BigDecimal.valueOf(40), "des");
        Product product2 = new Product("p-102", "name", BigDecimal.valueOf(10), "des");
        Product product3 = new Product("p-103", "name", BigDecimal.valueOf(100), "des");
        List<Product> allProducts = List.of(product1, product2, product3);

        List<Product> resultList = new ArrayList<>();
        for (ProductFilter filter : productFilters) {
            for (Product product : allProducts) {
                if (isMatch(filter, product)) {
                    //get the first
                    resultList.add(product);
                    break;
                }
            }
        }

        log.info("stop search products:{}", resultList);

        return resultList;
    }

    private boolean isMatch(ProductFilter filter, Product product) {
        if (!filter.getDescription().contains(product.getName())) {
            return false;
        }
        BigDecimal priceLowLimit = filter.getPriceLowLimit();
        if (priceLowLimit != null && priceLowLimit.compareTo(product.getPrice()) > 0) {
            return false;
        }

        BigDecimal priceTopLimit = filter.getPriceTopLimit();
        if (priceTopLimit != null && priceTopLimit.compareTo(product.getPrice()) < 0) {
            return false;
        }
        return true;
    }

    @Tool(name = "makeOrder", description = "request third party purchase platform to make an order")
    public Order makeOrder(@ToolParam(description = "The order is constructed using the products queried in the previous step") Order order, ToolContext toolContext) {
        log.info("start make order");

        //call third party service make order
        order.setOrderId(String.valueOf(toolContext.getContext().get("myOrderId")));

        log.info("stop make order");

        return order;
    }


}
