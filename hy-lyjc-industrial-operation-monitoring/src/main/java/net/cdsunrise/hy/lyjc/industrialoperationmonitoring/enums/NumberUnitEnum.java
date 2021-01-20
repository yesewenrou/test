package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

import javax.xml.ws.Holder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * NumberUnitEnum
 * 数字单位枚举
 *
 * @author LiuYin
 * @date 2020/5/12 9:59
 */
public enum NumberUnitEnum {

    NO("", 0L, 9999L),
    WAN("万", 10000L, 99999999L),
    YI("亿", 100000000L, Long.MAX_VALUE),
    ;

    /**
     * 单位
     */
    private String unit;

    /**
     * 最小编辑（包含）
     */
    private Long minBoundary;

    /**
     * 最大边界（包含）
     */
    private Long maxBoundary;


    NumberUnitEnum(String unit, Long minBoundary, Long maxBoundary) {
        this.unit = unit;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
    }

    public String getUnit() {
        return unit;
    }

    public Long getMinBoundary() {
        return minBoundary;
    }

    public Long getMaxBoundary() {
        return maxBoundary;
    }

    public static NumberUnitEnum getUnitEnum(Long number) {
        if (Objects.isNull(number)) {
            return NO;
        }
        final long abs = Math.abs(number);
        if (abs <= NO.getMaxBoundary()) {
            return NO;
        } else if (abs <= WAN.getMaxBoundary()) {
            return WAN;
        } else {
            return YI;
        }
    }

    public static String getDescription(Long number) {
        if (Objects.isNull(number)) {
            return "0";
        }

        final NumberUnitEnum unitEnum = getUnitEnum(number);
        switch (unitEnum) {
            case NO:
                return number + "";
            case WAN:
            case YI:
                // 做除法
                final double v = (number + 0D) / (unitEnum.getMinBoundary());
                // 保留两位小数
                final String s = BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).toString();
                return s + unitEnum.getUnit();
            default:
                throw new IllegalArgumentException("unit enum error:" + unitEnum);
        }

    }
}
