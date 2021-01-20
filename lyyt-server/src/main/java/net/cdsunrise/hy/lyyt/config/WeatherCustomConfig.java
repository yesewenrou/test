package net.cdsunrise.hy.lyyt.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * WeatherCustomConfig
 *
 * @author LiuYin
 * @date 2020/10/27 11:16
 */
@Configuration
@ConfigurationProperties("weather.custom")
@Data
@Slf4j
public class WeatherCustomConfig {

    private Integer yupingMountainTemperatureOffset;

    private Double yupingMountainPm25Ratio;

    @PostConstruct
    public void init(){
      log.info("weather custom: yupingMountainTemperatureOffset [{}], yupingMountainPm25Ratio[{}]", yupingMountainTemperatureOffset, yupingMountainPm25Ratio);
    }
}
