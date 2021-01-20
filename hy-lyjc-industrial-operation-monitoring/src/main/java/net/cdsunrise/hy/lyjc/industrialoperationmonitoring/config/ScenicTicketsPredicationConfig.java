package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ScenicTicketsPredicationEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author FangYunLong
 * @date in 2020/5/27
 */
@Data
@Component
@ConfigurationPropertiesBinding
@ConfigurationProperties("hy.lyjc.ticket-predicate")
public class ScenicTicketsPredicationConfig {

    private Map<ScenicTicketsPredicationEnum, Double> weightConfig;

}
