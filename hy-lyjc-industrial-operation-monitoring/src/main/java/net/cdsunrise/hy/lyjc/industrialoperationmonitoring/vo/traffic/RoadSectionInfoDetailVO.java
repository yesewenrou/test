package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.JamLevelEnum;

/**
 * @author LHY
 * @date 2019/12/10 10:53
 */
@Data
public class RoadSectionInfoDetailVO {

    /**
     * 摄像头编码
     */
    private String cameraCode;
    /**
     * 路段id
     */
    private Long roadId;
    /**
     * 路段名称
     */
    private String roadSectionName;
    /**
     * 关联对象
     */
    private String relationObject;
    /**
     * 总里程
     */
    private Double totalMileage;
    /**
     * 路段方向  1：上行  2：下行
     */
    private Integer roadSectionDirection;
    /**
     * 路段描述
     */
    private String roadSectionDescribe;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 监控点id
     */
    private String monitorSiteId;
    /**
     * 拥堵指数
     */
    private Double tpi;
    /**
     * 拥堵枚举
     */
    private JamLevelEnum jamLevelEnum;
    /**
     * 拥堵里程
     */
    private Double jamLength;
    /**
     * 拥堵里程偏差 nowJamLength-lastedJamLength
     */
    private Double offsetJamLength;
    /**
     * 拥堵里程占比= (jamLength*100)/totalMileage
     */
    private Integer jamLengthRatio;
    /**
     * 平均速度
     */
    private Integer avgSpeed;
    /**
     * 实时车流量
     */
    private Integer count;
    /**
     * 累计拥堵时长 单位分钟
     */
    private Integer congestionTimeLength;
    /**
     * 累计拥堵次数
     */
    private Integer congestionTimes;
}
