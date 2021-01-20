package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author LHY
 * 用于分页搜索条件
 */
@Data
public class DataResourceVO {
    /**游客接待数**/
    private Integer count;
    private String scenicName;
    private String countryName;
    private String provName;
    private String cityName;
    /**统计时间**/
    private String statisticsTime;

    public DataResourceVO() {
    }

    public DataResourceVO(String scenicName, Integer count, String statisticsTime) {
        this.count = count;
        this.scenicName = scenicName;
        this.statisticsTime = statisticsTime;
    }
}
