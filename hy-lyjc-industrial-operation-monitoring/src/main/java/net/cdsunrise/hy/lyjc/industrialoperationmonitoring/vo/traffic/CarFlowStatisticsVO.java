package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 * @date 2019/12/10 11:03
 */
@Data
public class CarFlowStatisticsVO {

    /**
     * 车流量折线图
     */
    private List<CarFlowTime> carFlowTimeList;
    /**
     * 车流量详情表格
     */
    private List<CarFlowInfo> carFlowInfoList;
    /**
     * 车流总量
     */
    private Integer flowCount;

    @Data
    public static class CarFlowTime {
        private Long time;

        private Integer flow;
    }

    @Data
    public static class CarFlowInfo {
        private Long beginTime;

        private Long endTime;

        private Integer flow;

        private Integer flowStatus;
    }
}
