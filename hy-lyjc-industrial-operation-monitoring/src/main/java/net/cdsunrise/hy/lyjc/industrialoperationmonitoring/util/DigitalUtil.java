package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author lijiafeng
 * @date 2020/05/09 15:34
 */
public class DigitalUtil {

    /**
     * 转为万
     *
     * @param src 源
     * @return 结果
     */
    public static double toTenThousand(BigDecimal src) {
        return src.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算占比 百分比
     *
     * @param part  部分
     * @param total 总数
     * @return 结果
     */
    public static double calcPercent(BigDecimal part, BigDecimal total) {
        if (part.compareTo(BigDecimal.ZERO) == 0 || total.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        } else {
            return part.divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }
}
