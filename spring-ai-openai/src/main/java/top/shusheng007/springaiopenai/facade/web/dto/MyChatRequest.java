package top.shusheng007.springaiopenai.facade.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyChatRequest {
    private String chatId;
    private String question;
}
