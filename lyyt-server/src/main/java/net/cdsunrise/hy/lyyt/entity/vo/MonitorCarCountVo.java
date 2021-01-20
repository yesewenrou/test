package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YQ on 2019/12/22.
 */
@Data
public class MonitorCarCountVo {
    private Map<Integer, Integer> lastYearCount = buildDefault();
    private Map<Integer, Integer> thisYearCount = buildDefault();

    public static MonitorCarCountVo build(Map<String, String> thisYearData, Map<String, String> lastYearData) {
        MonitorCarCountVo vo = new MonitorCarCountVo();
        Map<Integer, Integer> lastYearMap = vo.getLastYearCount();
        convert(lastYearData, lastYearMap);

        Map<Integer, Integer> thisYearMap = vo.getThisYearCount();
        convert(thisYearData, thisYearMap);
        return vo;
    }

    private static void convert(Map<String, String> lastYearData, Map<Integer, Integer> lastYearMap) {
        if (!CollectionUtils.isEmpty(lastYearData)) {
            lastYearData.forEach((k,v)->{
                Integer key = Integer.parseInt(k);
                Integer value = Integer.parseInt(v);
                lastYearMap.put(key, value);
            });
        }
    }

    private static Map<Integer,Integer> buildDefault() {
        int year = 12;
        Map<Integer, Integer> map = new HashMap<>(year);
        for (int i = 1; i <= year; i++) {
            map.put(i, 0);
        }
        return map;
    }
}
