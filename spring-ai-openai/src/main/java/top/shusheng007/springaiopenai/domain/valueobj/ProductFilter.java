package top.shusheng007.springaiopenai.domain.valueobj;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class ProductFilter {
    private String description;
    @ToolParam(required = false)
    private BigDecimal priceLowLimit;
    @ToolParam(required = false)
    private BigDecimal priceTopLimit;
}
