package top.shusheng007.springaiopenai.web.dto;

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
    private String chainOfThought;
    private String answer;
}
