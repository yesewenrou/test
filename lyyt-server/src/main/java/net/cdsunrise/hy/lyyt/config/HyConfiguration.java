package net.cdsunrise.hy.lyyt.config;

import lombok.Data;
import net.cdsunrise.hy.lyyt.entity.co.RoadSectionCo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @ClassName HyConfiguration
 * @Description
 * @Author LiuYin
 * @Date 2019/12/13 16:41
 */
@Configuration
@ConfigurationProperties("hy-config")
@Data
public class HyConfiguration {


    private List<RoadSectionCo> roadSections;

}
