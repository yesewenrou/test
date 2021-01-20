package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 13:44
 */
@Data
public class TouristLocalData {

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
     * 人员类型 0为游客 1 为居住 2为上班
     */
    private Integer memberType;

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
