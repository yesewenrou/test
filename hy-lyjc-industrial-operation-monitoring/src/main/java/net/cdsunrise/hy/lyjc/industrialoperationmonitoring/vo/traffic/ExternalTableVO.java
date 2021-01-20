package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

/**
 * @author : suzhouhe  @date : 2019/8/25 16:26  @description : 外部表数据VO
 */
@Data
public class ExternalTableVO {
    /**
     * 进程车辆数
     */
    private Integer flow;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 统计时间
     */
    private Long statisticsTime;
}
