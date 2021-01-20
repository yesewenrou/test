package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author suzhouhe  @date 2018/12/19 17:57
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object obj) {
        try {
            return mapper.writer().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("method:{},error:{}", "toString", e);
        }
        return null;
    }

    public static <T> T toObject(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
