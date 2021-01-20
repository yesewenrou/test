package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @Author: LHY
 * @Date: 2019/9/17 18:04
 */
@Data
public class TourismIncomeVO {
    /**旅游收入总数**/
    private Double count;
    private String incomeSource;
    private String scenicName;
    /**统计时间**/
    private String statisticsTime;
}
