package top.shusheng007.deepseek.application.facade.web;

import dev.langchain4j.data.message.AiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.shusheng007.deepseek.domain.GovReport;
import top.shusheng007.deepseek.domain.Student;
import top.shusheng007.deepseek.service.AssistantAiService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("ai")
public class AiController {

    private final AssistantAiService assistantAiService;


//    @GetMapping("/chat")
//    public String test(@RequestParam String question){
//        return String.valueOf(assistantAiService.isPositive(question));
//    }
//
//
//    @GetMapping("/extract")
//    public Student extract(){
//        final String info = """
//                今年是2024年，在2023年的秋天2017年出生的王小二与家人一起从中国天津市津南区搬迁到到河西区生活，
//                他于当年进入天津市河西第二中心小学学习。
//                """;
//        AiMessage aiMessage = assistantAiService.extractStudentFrom(info);
//
//        log.info("****:{}", aiMessage.toString());
//
//        return new Student();
//    }
//
//
//    @GetMapping("/query-web")
//    public String parseWebPage(@RequestParam String url){
//        return assistantAiService.summaryPageContent(url);
//    }
//
//
//    @GetMapping("/summary-report")
//    public GovReport summaryReport(){
////        return augurAiService.summaryReport();
//
//        AiMessage aiMessage = assistantAiService.summaryReport2();
//
//
//
//        return new GovReport();
//    }

}
