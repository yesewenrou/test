package net.cdsunrise.hy.lyyt.domain.dto;


import lombok.Data;

/**
 * @author wyb
 * @date 2019/8/5 16:55
 * @description 监控点信息
 */
@Data
public class MonitorSiteDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 监控点id
     */
    private String monitorSiteId;
    /**
     * 监控点名称
     */
    private String monitorSiteName;
    /**
     * 关联设备id
     */
    private String deviceId;
    /**
     * 关联区域code
     */
    private String relationCode;
    /**
     * 关联区域、所属区域
     */
    private String relationArea;
    /**
     * 详细地址
     */
    private String detailedAddress;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 路段id
     */
    private Long roadId;
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
