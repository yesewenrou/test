package net.cdsunrise.hy.lyyt.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 游客预测因子枚举
 *
 * @author lijiafeng
 * @date 2020/04/22 14:39
 */
public enum TouristPredictionFactorEnum {

    /**
     * 昨日游客数
     */
    YESTERDAY(0.1),

    /**
     * 上周同期游客数
     */
    LAST_WEEK_SAME_PERIOD(0.3),

    /**
     * 上上周同期游客数
     */
    TWO_WEEKS_AGO_SAME_PERIOD(0.2),

    /**
     * 上月同期游客数
     */
    LAST_MONTH_SAME_PERIOD(0.1),

    /**
     * 上周平均游客数
     */
    LAST_WEEK_AVG(0.1),

    /**
     * 上月平均游客数
     */
    LAST_MONTH_AVG(0.1),

    /**
     * 本月游客平均数
     */
    THIS_MONTH_AVG(0.1);

    /**
     * 默认因子
     */
    private static final Map<TouristPredictionFactorEnum, Double> DEFAULT_FACTORS;

    static {
        DEFAULT_FACTORS = Arrays.stream(values()).collect(Collectors.toMap(Function.identity(), TouristPredictionFactorEnum::getDefaultWeight));
    }

    /**
     * 默认权重
     */
    private final double defaultWeight;

    TouristPredictionFactorEnum(double defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public double getDefaultWeight() {
        return defaultWeight;
    }

    public static Map<TouristPredictionFactorEnum, Double> getDefaultFactors() {
        return DEFAULT_FACTORS;
    }
}
