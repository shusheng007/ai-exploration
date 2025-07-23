package top.shusheng007.springaiopenai.infrastructure.adapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    public static final String DEFAULT_SYSTEM_MESSAGE_PROMPT ="classpath:prompts/default-system-message-prompt.st";
    public static final String LOG_DIAGNOSE_SYSTEM_MESSAGE_PROMPT ="classpath:prompts/log-diagnose-system-prompt.st";
    public static final String LOG_DIAGNOSE_USER_MESSAGE_PROMPT ="classpath:prompts/log-diagnose-user-prompt.st";

    public static final String TEST_LOG_01= "classpath:static/test-log.log";

    private final ResourceLoader resourceLoader;

    public Resource loadFile(String fileName) {
        return resourceLoader.getResource(fileName);
    }

    public String loadFileContent(String fileName) {
        try {
            Resource resource = loadFile(fileName);
            return new String(resource.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + fileName, e);
        }
    }
}
