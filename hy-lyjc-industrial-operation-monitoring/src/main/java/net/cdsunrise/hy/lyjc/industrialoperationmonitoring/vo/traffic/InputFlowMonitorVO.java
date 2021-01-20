package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 * @date 2019/12/10 11:02
 */
@Data
public class InputFlowMonitorVO {

    /**
     * 今日总流量
     */
    private Integer dayFlowCount;

    /**
     * 实时流量数据
     */
    private Integer realTimeFlowCount;

    /**
     * 车流量图表数据
     */
    private List<CarFlowStatisticsVO.CarFlowTime> carFlowTimeList;
}
