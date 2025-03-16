package top.shusheng007.deepseek.application.facade.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shusheng007.deepseek.application.ChatRobotService;

/**
 * Copyright (C) 2023 ShuSheng007
 * <p>
 * Author ShuSheng007
 * Time 2025/3/16 16:51
 * Description
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("chat-robot/")
public class ChatRobotController {
    private final ChatRobotService chatRobotService;

    @PostMapping("chat")
    public String chat(@RequestBody AssistantRequest request) {
        return chatRobotService.chat(request.userId, request.getUserMessage());
    }

    @Setter
    @Getter
    public static class AssistantRequest {
        private String userId;
        private String userMessage;
    }

}
