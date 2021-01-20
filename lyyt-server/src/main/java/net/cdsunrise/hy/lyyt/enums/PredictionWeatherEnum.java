package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 预测天气状况枚举
 *
 * @author lijiafeng
 * @date 2020/04/22 14:49
 */
public enum PredictionWeatherEnum {

    /**
     * 好天气
     */
    GOOD(1),

    /**
     * 坏天气
     */
    BAD(0.6);

    /**
     * 默认系数
     */
    private static final Map<PredictionWeatherEnum, Double> DEFAULT_COEFFICIENTS;

    static {
        DEFAULT_COEFFICIENTS = Arrays.stream(values()).collect(Collectors.toMap(Function.identity(), PredictionWeatherEnum::getDefaultCoefficient));
    }

    /**
     * 默认系数
     */
    private final double defaultCoefficient;

    PredictionWeatherEnum(double defaultCoefficient) {
        this.defaultCoefficient = defaultCoefficient;
    }

    public double getDefaultCoefficient() {
        return defaultCoefficient;
    }

    public static Map<PredictionWeatherEnum, Double> getDefaultCoefficients() {
        return DEFAULT_COEFFICIENTS;
    }
}
