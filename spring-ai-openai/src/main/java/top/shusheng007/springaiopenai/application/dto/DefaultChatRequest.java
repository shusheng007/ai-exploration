package top.shusheng007.springaiopenai.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultChatRequest {
    private String chatId;
    private String question;
}
