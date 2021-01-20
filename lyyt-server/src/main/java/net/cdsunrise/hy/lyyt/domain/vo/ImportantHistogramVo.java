package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author YQ on 2019/11/6.
 */
@Data
public class ImportantHistogramVo {
    /**
     * 昨天进城车流量
     */
    private Map<String,String> yesterdayHistogram;
    /**
     * 今天进城车流量
     */
    private Map<String, String> todayHistogram;

    public static ImportantHistogramVo build(Map<String, String> yesterdayHistogram, Map<String, String> todayHistogram) {
        ImportantHistogramVo vo = new ImportantHistogramVo();
        vo.setYesterdayHistogram(yesterdayHistogram);
        vo.setTodayHistogram(todayHistogram);
        return vo;
    }
}
