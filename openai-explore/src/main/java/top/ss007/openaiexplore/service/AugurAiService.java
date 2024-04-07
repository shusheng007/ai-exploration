package top.ss007.openaiexplore.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import top.ss007.openaiexplore.domain.Student;

@AiService
public interface AugurAiService {

    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);

    @UserMessage("Does {{input}} has a positive sentiment?")
    boolean isPositive(@V("input") String text);


    @UserMessage("Extract information about a student from {{it}}")
    Student extractStudentFrom(String text);
}
