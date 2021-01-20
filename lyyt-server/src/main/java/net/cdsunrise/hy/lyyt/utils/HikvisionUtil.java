package net.cdsunrise.hy.lyyt.utils;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: suzhouhe @Date: 2019/11/5 9:58 @Description:
 */
@Slf4j
public class HikvisionUtil {
    private static final String ARTEMIS_PATH = "/artemis";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String JSON_CONTENT_TYPE = "application/json";

    private static String createPreviewURL(String url) {
        return ARTEMIS_PATH + url;
    }

    private static Map<String, String> getHttpPath(String previewUrl) {
        return new HashMap<String, String>(2) {
            {
                put(HTTP, previewUrl);
            }
        };
    }

    private static Map<String, String> getHttpsPath(String previewUrl) {
        return new HashMap<String, String>(2) {
            {
                put(HTTPS, previewUrl);
            }
        };
    }

    /**
     * 获取一个POST请求结果
     *
     * @param url        接口地址（可区分部分）
     * @param jsonObject post参数对象
     * @return
     */
    public static String getPostResult(String url, JSONObject jsonObject) {
        final Map<String, String> path = getHttpsPath(createPreviewURL(url));
        String jsonBody = jsonObject.toJSONString();
        log.info("path:[{}],body:[{}]", path, jsonBody);
        try {
            final String s = ArtemisHttpUtil.doPostStringArtemis(path, jsonBody, null, null, JSON_CONTENT_TYPE, null);
            if (StringUtils.isEmpty(s)) {
                log.error("result is null");
                throw new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR);
            }
            log.info("result:[{}]", s);
            return s;

        } catch (Throwable e) {
            log.error("error:", e);
            throw new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR);
        }
    }
}
