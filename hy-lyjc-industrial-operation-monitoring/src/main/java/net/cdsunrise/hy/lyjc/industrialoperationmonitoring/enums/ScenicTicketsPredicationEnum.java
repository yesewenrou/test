package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author FangYunLong
 * @date in 2020/5/27
 */
public enum ScenicTicketsPredicationEnum {
    /** 昨日门票数 权重: 0.1 **/
    YESTERDAY_TICKETS(0.1),

    /** 上周同期门票数 权重: 0.3 **/
    LAST_WEEK_SAME_DAY_TICKETS(0.3),

    /** 上上周同期门票数据 权重: 0.2 **/
    LAST_TWO_WEEK_SAME_DAY_TICKETS(0.2),

    /** 上月同期门票数 权重: 0.1  **/
    LAST_MONTH_SAME_DAY_TICKETS(0.1),

    /** 上周平均门票数据 权重: 0.1 **/
    LAST_WEEK_AVG_TICKETS(0.1),

    /** 上月平局门票数据 权重: 0.1 **/
    LAST_MONTH_AVG_TICKETS(0.1),

    /** 本月平均门票数据 权重 0.1 **/
    CURRENT_MONTH_AVG_TICKETS(0.1);

    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    ScenicTicketsPredicationEnum(Double weight) {
        this.weight = weight;
    }

    private static final Map<ScenicTicketsPredicationEnum, Double> DEFAULT_WEIGHTS;


    static {
        DEFAULT_WEIGHTS = Arrays.stream(values()).collect(Collectors.toMap(Function.identity(), ScenicTicketsPredicationEnum::getWeight));
    }

    public static Map<ScenicTicketsPredicationEnum, Double> getDefaultWeights() {
        return DEFAULT_WEIGHTS;
    }

}
