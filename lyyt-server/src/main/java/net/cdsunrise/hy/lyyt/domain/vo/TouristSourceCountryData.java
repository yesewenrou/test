package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;
/**
 * @author lijiafeng
 * @date 2019/9/26 14:23
 */
@Data
public class TouristSourceCountryData {

    private String id;

    /**
     * 区域ID
     */
    private String scenicId;

    /**
     * 区域名称
     */
    private String scenicName;

    /**
     * 国家ID
     */
    private String countryId;

    /**
     * 国家名称
     */
    private String countryName;

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
     * 标记：month、day、hour、minute
     */
    private String flag;
}
