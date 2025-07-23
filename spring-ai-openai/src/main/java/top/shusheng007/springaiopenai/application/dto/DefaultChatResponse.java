package top.shusheng007.springaiopenai.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultChatResponse {
    private String chatId;
    private String answer;
}
