package top.shusheng007.springaiopenai.facade.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shusheng007.springaiopenai.application.dto.LogAnalyseReportResponse;
import top.shusheng007.springaiopenai.application.service.AssistantAppService;
import top.shusheng007.springaiopenai.application.service.IngestionService;
import top.shusheng007.springaiopenai.application.dto.DefaultChatRequest;
import top.shusheng007.springaiopenai.application.dto.DefaultChatResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class XiaoBenController {

    private final IngestionService ingestionService;
    private final AssistantAppService assistantService;


    @PostMapping("/ingest-data")
    public ResponseEntity<String> ingestData(){
        ingestionService.ingestData();
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/chat")
    public ResponseEntity<DefaultChatResponse> chat(@RequestBody DefaultChatRequest request) {
        DefaultChatResponse response = assistantService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analyse-log")
    public ResponseEntity<LogAnalyseReportResponse> analyseLog() {
        LogAnalyseReportResponse response = assistantService.analyseErrorLog();
        return ResponseEntity.ok(response);
    }

}
