package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 14:18
 */
@Data
public class TouristSourceCityData {

    private String id;

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
     * 标记：month、day
     */
    private String flag;
}
