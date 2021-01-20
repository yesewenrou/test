package net.cdsunrise.hy.lyyt;

import net.cdsunrise.hy.lyyt.config.HyConfiguration;
import net.cdsunrise.hy.lyyt.config.PropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties({PropertiesConfig.class, HyConfiguration.class})
public class LyytServerApplication {

    private static final String ES_SET_NETTY_RUNTIME_AVAILABLE_PROCESSORS = "es.set.netty.runtime.available.processors";

    public static void main(String[] args) {
        // 防止es的client不能启动
        System.setProperty(ES_SET_NETTY_RUNTIME_AVAILABLE_PROCESSORS, Boolean.FALSE.toString());
        SpringApplication.run(LyytServerApplication.class, args);
    }

}
