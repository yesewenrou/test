package net.cdsunrise.hy.lyyt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: suzhouhe @Date: 2019/11/5 15:40 @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "lyyt")
public class PropertiesConfig {

    /**
     * 海康对接数据
     */
    private Artemisconfig artemisconfig;

    @Data
    public static class Artemisconfig {
        private String host;
        private String port;
        private String appKey;
        private String appSecret;
    }
}
