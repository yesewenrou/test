package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2020/1/7 16:09
 */
@Data
public class TouristReceptionVO {
    /**游客接待数**/
    private Double count;
    private String scenicName;
    /**统计时间**/
    private String statisticsTime;

    public TouristReceptionVO() {
    }

    public TouristReceptionVO(String scenicName, Double count, String statisticsTime) {
        this.count = count;
        this.scenicName = scenicName;
        this.statisticsTime = statisticsTime;
    }
}
