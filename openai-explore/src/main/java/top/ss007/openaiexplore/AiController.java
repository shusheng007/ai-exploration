package top.ss007.openaiexplore;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.ss007.openaiexplore.domain.Student;
import top.ss007.openaiexplore.service.AugurAiService;

@RequiredArgsConstructor
@RestController
@RequestMapping("ai")
public class AiController {

    private final AugurAiService augurAiService;


    @GetMapping("/chat")
    public String test(@RequestParam String question){
        return String.valueOf(augurAiService.isPositive(question));
    }


    @GetMapping("/extract")
    public Student extract(){
        final String info = """
                今年是2024年，在2023年的秋天2017年出生的王永翰与家人一起从中国天津市津南区搬迁到到河西区生活，
                他于当年进入天津市河西第二中心小学学习。
                """;
        return augurAiService.extractStudentFrom(info);
    }

}
