package net.cdsunrise.hy.lyjc.industrialoperationmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lijiafeng
 * @date 2019/8/19 16:28
 */
@EnableFeignClients
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class IndustrialOperationMonitoringApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(IndustrialOperationMonitoringApplication.class, args);
    }
}
