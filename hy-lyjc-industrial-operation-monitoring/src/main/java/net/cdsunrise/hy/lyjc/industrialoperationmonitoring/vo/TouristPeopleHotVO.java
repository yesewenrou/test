package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/11/29 11:03
 */
@Data
public class TouristPeopleHotVO {

    /**
     * ID
     */
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
     * 人数
     */
    private Integer peopleNum;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
}
