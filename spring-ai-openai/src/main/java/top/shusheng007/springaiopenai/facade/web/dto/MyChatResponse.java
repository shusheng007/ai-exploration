package top.shusheng007.springaiopenai.facade.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyChatResponse {
    private String chatId;
    private String answer;
}
