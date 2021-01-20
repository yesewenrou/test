package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/10/18 15:50
 */
@Data
public class TouristSourceCityData {

    /**
     * 区域名称
     */
    private String scenicName;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 省份名称
     */
    private String provName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 人数
     */
    private Integer peopleNum;

    /**
     * 时间
     */
    private String time;

    /**
     * 数据来源
     */
    private String datasource;

    /**
     * 标记：day、month
     */
    private String flag;
}
