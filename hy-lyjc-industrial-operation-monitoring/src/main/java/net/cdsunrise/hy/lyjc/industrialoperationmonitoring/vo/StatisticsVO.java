package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author LHY
 * 用于构建带搜索条件的分页数据
 */
@Data
public class StatisticsVO {

    private Integer count;
    /**投诉行业分类**/
    private String industryType;
    /**投诉分类**/
    private String type;
    /**投诉渠道**/
    private String channel;
    /**用于导出Excel显示投诉时间**/
    private String statisticsTime;
}
