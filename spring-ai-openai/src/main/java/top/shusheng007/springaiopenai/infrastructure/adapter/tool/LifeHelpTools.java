package top.shusheng007.springaiopenai.infrastructure.adapter.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import top.shusheng007.springaiopenai.infrastructure.adapter.Weather;

import java.time.LocalDateTime;

/**
 * Copyright (C) 2023 ShuSheng007
 * <p>
 * Author ShuSheng007
 * Time 2025/5/7 22:04
 * Description
 */
@Slf4j
public class LifeHelpTools {

    @Tool(name = "getCurrentDateTime", description = "获取当前的日期和时间")
    public String getCurrentDateTime() {
        log.info("==tool==: getCurrentDateTime");
        log.info("--------------------------------------------------------------------");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(name = "checkWeatherForGivenLocationTime", description = "获取给定地点给定时间的天气")
    public Weather checkWeatherForGivenLocationTime(@ToolParam(description = "要查看的地点") String location,
                                                    @ToolParam(description = "要查看的时间") String time){
        log.info("==tool==: checkWeatherForGivenLocationTime");
        log.info("==tool==: check weather in location:{} at time:{}",location, time);
        log.info("---------------------------------------------------------------------");

        if ("天津".equalsIgnoreCase(location)){
            return Weather.RAINY;
        }
        return Weather.SUNNY;
    }

}
