package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/13 16:23
 */
@Data
public class HotelCondition {

    /**
     * 酒店唯一标识
     * */
    private String stationId;

    /**
     * 酒店名称
     * */
    private String name;

    /**
     * 所属区域
     * */
    private String area;

    /**
     * 所属商圈
     * */
    private String businessCircle;

    /**
     * 酒店地址
     * */
    private String address;

    /**
     * 省份名称
     */
    private String provName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 统计时间
     */
    private String startTime;

    private String endTime;

}
