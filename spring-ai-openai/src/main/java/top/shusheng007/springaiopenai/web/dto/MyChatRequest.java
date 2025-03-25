package top.shusheng007.springaiopenai.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyChatRequest {
    private String chatId;
    private String question;
}
