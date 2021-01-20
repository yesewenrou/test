package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/19 14:58
 */
@Data
public class HotelTouristSourceVO {
    /**
     * 省份名称
     */
    private String provName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 统计日期
     * */
    private String statisticalDate;

    /**
     * 当日累计接待
     * */
    private Double cumulativeReception;
}
