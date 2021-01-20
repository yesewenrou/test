package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author fang yun long
 * on 2021/1/13
 */
@Data
public class TouristCountVO {
    /** 当月客流统计 */
    private BigDecimal currentMonthTourists;
    /** 上月客流统计 */
    private BigDecimal lastMonthTourists;
}
