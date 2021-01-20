package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 节假日专题 旅游交通分析
 *
 * @author YQ on 2020/1/2.
 */
@Data
public class TrafficAnalyzeVo {
    /**
     * 进洪雅总数量
     */
    private Integer carCount;

    /**
     * 进洪雅总数量 同比上升 百分比
     */
    private Integer carCountRatio;

    /**
     *  去年进城车辆数(节假日有天,数据就有几天的数据)
     */
    private List<KeyValueVo> lastYearCarCount;

    /**
     *  今年年进城车辆数(节假日有天,数据就有几天的数据)
     */
    private List<KeyValueVo> thisYearCarCount;

    /**
     * 路段拥堵时长top5
     */
    private List<KeyValueVo> jamTimeLengthTopFive;


    private static Integer calcRatio(Integer thisYearCount, Integer lastYearCount) {
        if (thisYearCount == null || thisYearCount == 0) {
            return 0;
        }
        if (lastYearCount == null || lastYearCount == 0) {
            return null;
        }
        return (thisYearCount - lastYearCount) * 100 / thisYearCount;
    }

    public static TrafficAnalyzeVo build(List<KeyValueVo> thisYearCarCount, List<KeyValueVo> lastYearCarCount, List<KeyValueVo> jamTimeLengthTopFive) {
        TrafficAnalyzeVo vo = new TrafficAnalyzeVo();
        vo.setThisYearCarCount(thisYearCarCount);
        vo.setLastYearCarCount(lastYearCarCount);
        vo.setJamTimeLengthTopFive(jamTimeLengthTopFive);
        vo.update();
        return vo;
    }

    public static TrafficAnalyzeVo buildDefault() {
        TrafficAnalyzeVo vo = new TrafficAnalyzeVo();
        vo.setCarCount(9063);
        vo.setCarCountRatio(100);
        vo.setLastYearCarCount(Collections.singletonList(new KeyValueVo("2018-01-01", "0")));
        vo.setThisYearCarCount(Collections.singletonList(new KeyValueVo("2019-01-01", "9063")));
        List<KeyValueVo> jamTimeLengthTopFive = Arrays.asList(
                new KeyValueVo("洪瓦路（瓦屋山服务区后-雅东路口）", "100"),
                new KeyValueVo("洪瓦路（花溪口大桥-瓦屋山服务区后）", "50"),
                new KeyValueVo("槽鱼滩至东岳（冯湾-东岳镇政府）", "45"),
                new KeyValueVo("大峨眉国际旅游西环线（郭家山庄-余沟村）", "40"),
                new KeyValueVo(" 大峨眉国际旅游西环线（大峨眉国际旅游西环线-桅杆坪）", "30"));
        vo.setJamTimeLengthTopFive(jamTimeLengthTopFive);
        return vo;
    }

    private void update() {
        carCount = thisYearCarCount.stream()
                .map(e -> Integer.parseInt(e.getValue()))
                .reduce(0, (e1, e2) -> e1 + e2);
        Integer lastCarCount = lastYearCarCount.stream()
                .map(e -> Integer.parseInt(e.getValue()))
                .reduce(0, (e1, e2) -> e1 + e2);

        carCountRatio = calcRatio(carCount, lastCarCount);
    }
}
