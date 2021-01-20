package net.cdsunrise.hy.lyyt.config;

import net.cdsunrise.hy.lyyt.enums.PredictionWeatherEnum;
import net.cdsunrise.hy.lyyt.enums.TouristPredictionFactorEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author lijiafeng
 * @date 2020/04/22 14:50
 */
@Configuration
@ConfigurationProperties("hy.lyyt.tourist-predicition")
@ConfigurationPropertiesBinding
public class TouristPredictionConfig {

    /**
     * 预测因子
     */
    private Map<TouristPredictionFactorEnum, Double> factorWeights;

    /**
     * 天气系数
     */
    private Map<PredictionWeatherEnum, Double> weatherCoefficients;

    private Boolean useWeatherFactor = true;

    public Map<TouristPredictionFactorEnum, Double> getFactorWeights() {
        return factorWeights;
    }

    public void setFactorWeights(Map<TouristPredictionFactorEnum, Double> factorWeights) {
        this.factorWeights = factorWeights;
    }

    public Map<PredictionWeatherEnum, Double> getWeatherCoefficients() {
        return weatherCoefficients;
    }

    public void setWeatherCoefficients(Map<PredictionWeatherEnum, Double> weatherCoefficients) {
        this.weatherCoefficients = weatherCoefficients;
    }

    public Boolean getUseWeatherFactor() {
        return useWeatherFactor;
    }

    public void setUseWeatherFactor(Boolean useWeatherFactor) {
        this.useWeatherFactor = useWeatherFactor;
    }
}
