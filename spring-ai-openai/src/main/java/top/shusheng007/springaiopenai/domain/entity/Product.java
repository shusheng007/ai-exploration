package top.shusheng007.springaiopenai.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String description;
}
