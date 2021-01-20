package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/10/18 14:54
 */
@Data
public class TouristLocalData {
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
     * 标记：day、hour、minute、month
     */
    private String flag;
}
