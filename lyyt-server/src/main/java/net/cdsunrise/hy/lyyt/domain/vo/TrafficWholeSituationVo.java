package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.Map;
import java.util.Objects;

/**
 * 旅游交通整体态势
 * @author YQ on 2019/11/5.
 */
@Data
public class TrafficWholeSituationVo {
    /**
     * 本周拥堵时长
     */
    private Integer jamDurationTimeWeekly;
    /**
     * 本周拥堵次数
     */
    private Integer jamTimesWeekly;
    /**
     * 昨天进城车流量
     */
    private Map<String,String> yesterdayHistogram;
    /**
     * 今天进城车流量
     */
    private Map<String, String> todayHistogram;

    /**
     * 今日车流
     */
    private Integer todayTotalFlow;

    /**
     * 昨日车流
     */
    private Integer yesterdayTotalFlow;

    /**
     * 今日外地车辆数(外地车辆指的是眉山市以外车辆）
     */
    private Integer todayOutLandFlowCount;

    /**
     * 昨日外地车辆数(外地车辆指的是眉山市以外车辆)
     */
    private Integer yesterdayOutLandFlowCount;


    public static TrafficWholeSituationVo build(Map<String, String> yesterdayHistogram, Map<String, String> todayHistogram) {
        TrafficWholeSituationVo vo = new TrafficWholeSituationVo();
        vo.setYesterdayHistogram(yesterdayHistogram);
        vo.setTodayHistogram(todayHistogram);

        vo.setYesterdayTotalFlow(yesterdayHistogram.values().stream().filter(Objects::nonNull).mapToInt(Integer::parseInt).sum());
        vo.setTodayTotalFlow(todayHistogram.values().stream().filter(Objects::nonNull).mapToInt(Integer::parseInt).sum());

        // 这里给的是一个写死的默认值，目前业务上没有用到
        vo.setJamDurationTimeWeekly(105);
        vo.setJamTimesWeekly(8);
        return vo;
    }
}
