package net.cdsunrise.hy.lyyt.domain.dto;

import lombok.Data;

/**
 * @author wyb
 * @date 2019/8/15 10:15
 * @description 交通流量参数配置
 */
@Data
public class TrafficFlowConfigDTO {
    /**id*/
    private Long id;
    /** 监控点主键id*/
    private Long monitorId;
    /** 监控点名称*/
    private String monitorSiteName;
    /** 车流量阀值设置最小值*/
    private Long minNum;
    /** 车流量阀值设置最大值*/
    private Long maxNum;
    /** 统计时间配置 -多少时间 单位分钟*/
    private Long timeNum;
    /**
     * 删除状态  1：正常  2：删除
     */
    private Integer deleteStatus;
    /**
     * 创建时间 完全的时间格式
     */
    private String createTime;
    /**
     * 修改时间  完全的时间格式
     */
    private String updateTime;
}
