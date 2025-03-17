package top.shusheng007.deepseek.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@UtilityClass
@Slf4j
public class JsonUtil {

    private static final String PARSE_OBJECT_TO_JSON_STRING_EXCEPTION = "Parse object to Json String exception";
    private static final String PARSE_OBJECT_TO_JSON_NODE_EXCEPTION = "Parse object to JsonNode exception";
    private static final String PARSE_STRING_TO_JSON_NODE_EXCEPTION = "Parse string to JsonNode exception";
    private static final String PARSE_JSON_STRING_TO_OBJECT_EXCEPTION = "Parse json string to Object exception";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static JsonNode parseBytes(byte[] rawData) {
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(rawData);
        } catch (IOException e) {
            log.info(PARSE_STRING_TO_JSON_NODE_EXCEPTION, e);
        }
        return jsonNode;
    }

    public static JsonNode parseString(String rawData) {
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(rawData);
        } catch (JsonProcessingException e) {
            log.info(PARSE_STRING_TO_JSON_NODE_EXCEPTION, e);
        }
        return jsonNode;
    }

    @Nullable
    public static <T> T parseStringToObj(String rawData, TypeReference<T> typeReference) {
        T result = null;
        try {
            result = objectMapper.readValue(rawData, typeReference);
        } catch (JsonProcessingException e) {
            log.info(PARSE_JSON_STRING_TO_OBJECT_EXCEPTION, e);
        }
        return result;
    }

    public static <T> T parseStringToObj(String rawData, Class<T> cls) {
        return parseStringToObj(objectMapper, rawData, cls);
    }

    public static <T> T parseStringToObj(ObjectMapper objectMapper, String rawData, Class<T> cls) {
        T result = null;

        try {
            result = objectMapper.readValue(rawData, cls);
        } catch (JsonProcessingException e) {
            log.info(PARSE_JSON_STRING_TO_OBJECT_EXCEPTION, e);
        }
        return result;
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info(PARSE_OBJECT_TO_JSON_STRING_EXCEPTION, e);
        }
        return "";
    }

    public static JsonNode toJson(Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            return parseString(json);
        } catch (JsonProcessingException e) {
            log.info(PARSE_OBJECT_TO_JSON_NODE_EXCEPTION, e);
        }
        return null;
    }

    public static <T> T jsonNodeToType(JsonNode jsonNode, Class<T> cls) {
        try {
            return objectMapper.convertValue(jsonNode, cls);
        } catch (IllegalArgumentException e) {
            log.info("JsonNode to Object error ", e);
        }
        return null;
    }

    public static String maskAndToJsonString(Object obj, List<String> excludeFields) {
        JsonNode rootNode = objectMapper.valueToTree(obj);
        maskFields(rootNode, excludeFields);
        try {
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.error(PARSE_OBJECT_TO_JSON_STRING_EXCEPTION, e);
        }
        return "";
    }

    public static String maskJsonString(String json, List<String> excludeFields) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            maskFields(rootNode, excludeFields);
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.error(PARSE_STRING_TO_JSON_NODE_EXCEPTION, e);
        }
        return "";
    }

    public static void maskFields(JsonNode rootNode, List<String> maskFields) {
        if (rootNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) rootNode;
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (isMaskField(fieldName, maskFields)) {
                    objectNode.put(fieldName, "****");
                } else {
                    maskFields(objectNode.get(fieldName), maskFields);
                }
            }
        } else if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                maskFields(node, maskFields);
            }
        }
    }

    private static boolean isMaskField(String fieldName, List<String> maskFields) {
        for (String maskField : maskFields) {
            if (maskField.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJsonString(String raw) {
        return raw.startsWith("{") || raw.startsWith("[");
    }


    public static String toJsonStringByFilter(Object obj, String[] excludeFields) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(excludeFields);
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("exceptFilter", filter);
        objectMapper.setFilterProvider(filterProvider);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(PARSE_OBJECT_TO_JSON_STRING_EXCEPTION, e.getMessage());
        }
        return "";
    }

    public static String maskAndToJsonString(Object obj, String[] excludeFields) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.valueToTree(obj);
        maskFields(rootNode, excludeFields);
        try {
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.error(PARSE_OBJECT_TO_JSON_STRING_EXCEPTION, e.getMessage());
        }
        return "";
    }

    public static String maskJsonString(String json, String[] excludeFields) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            maskFields(rootNode, excludeFields);
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.error(PARSE_OBJECT_TO_JSON_STRING_EXCEPTION, e.getMessage());
        }
        return "";
    }

    public static void maskFields(JsonNode rootNode, String[] maskFields) {
        if (rootNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) rootNode;
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (isMaskField(fieldName, maskFields)) {
                    objectNode.put(fieldName, "****");
                } else {
                    maskFields(objectNode.get(fieldName), maskFields);
                }
            }
        } else if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                maskFields(node, maskFields);
            }
        }
    }

    private static boolean isMaskField(String fieldName, String[] maskFields) {
        for (String maskField : maskFields) {
            if (maskField.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }
}
