package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lijiafeng
 * @date 2019/9/26 12:35
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parse(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("json解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(in, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json解析失败", e);
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("json序列化失败", e);
            throw new RuntimeException(e);
        }
    }
}
