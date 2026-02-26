package com.springerp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springerp.entity.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for JSON operations in audit logging.
 */
@Component
@RequiredArgsConstructor
public class JsonUtil {

    private final ObjectMapper objectMapper;

    /**
     * Convert object to JSON string
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * Convert JSON string to object
     */
    public <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Compare two objects and return differences
     */
    public Map<String, Object> compareObjects(Object oldObject, Object newObject) {
        Map<String, Object> differences = new HashMap<>();
        try {
            Map<String, Object> oldMap = objectMapper.convertValue(oldObject, Map.class);
            Map<String, Object> newMap = objectMapper.convertValue(newObject, Map.class);

            // Find changed fields
            for (String key : newMap.keySet()) {
                Object oldValue = oldMap.get(key);
                Object newValue = newMap.get(key);

                if (oldValue == null && newValue != null) {
                    differences.put(key, new String[]{"", newValue.toString()});
                } else if (oldValue != null && newValue == null) {
                    differences.put(key, new String[]{oldValue.toString(), ""});
                } else if (oldValue != null && !oldValue.equals(newValue)) {
                    differences.put(key, new String[]{oldValue.toString(), newValue.toString()});
                }
            }
        } catch (Exception e) {
            // Silently fail
        }
        return differences;
    }
}

