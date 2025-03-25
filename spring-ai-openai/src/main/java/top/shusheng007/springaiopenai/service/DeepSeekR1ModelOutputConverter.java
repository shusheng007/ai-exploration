package top.shusheng007.springaiopenai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.util.StringUtils;

@Slf4j
public class DeepSeekR1ModelOutputConverter implements StructuredOutputConverter<CotResponseValue> {
    private static final String OPENING_THINK_TAG = "<think>";
    private static final String CLOSING_THINK_TAG = "</think>";

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public CotResponseValue convert(String source) {
        if (!StringUtils.hasText(source)) {
            throw new IllegalArgumentException("Text cannot be blank");
        }
        int openingThinkTagIndex = source.indexOf(OPENING_THINK_TAG);
        int closingThinkTagIndex = source.indexOf(CLOSING_THINK_TAG);

        if (openingThinkTagIndex != -1 && closingThinkTagIndex != -1 && closingThinkTagIndex > openingThinkTagIndex) {
            String chainOfThought = source.substring(openingThinkTagIndex + OPENING_THINK_TAG.length(), closingThinkTagIndex);
            String answer = source.substring(closingThinkTagIndex + CLOSING_THINK_TAG.length());
            return new CotResponseValue(chainOfThought, answer);
        } else {
            log.info("No <think> tags found in the response. Treating entire text as answer.");
            return new CotResponseValue(null, source);
        }
    }

}
