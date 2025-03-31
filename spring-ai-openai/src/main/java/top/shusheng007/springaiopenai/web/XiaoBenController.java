package top.shusheng007.springaiopenai.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shusheng007.springaiopenai.application.IngestionService;
import top.shusheng007.springaiopenai.service.AssistantAppService;
import top.shusheng007.springaiopenai.web.dto.MyChatRequest;
import top.shusheng007.springaiopenai.web.dto.MyChatResponse;

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
    public ResponseEntity<MyChatResponse> chat(@RequestBody MyChatRequest request) {
        MyChatResponse response = assistantService.chat(request);
        return ResponseEntity.ok(response);
    }
}
