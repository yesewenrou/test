package net.cdsunrise.hy.lyyt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * SortUtil
 * 排序工具类
 * @author LiuYin
 * @date 2020/6/8 14:27
 */
public final class SortUtil {
    private static final int MAP_SIZE = 8;

    /** 天气接口，按照景区名称排序*/
    private static final Map<String,Integer> WEATHER_NAME_SORT_MAP = createWeatherNameSortMap();

    /** 产业运行监测，商圈名称排序map*/
    private static final Map<String, Integer> AREA_CHART_NAME_SORT_MAP = createAreaChartNameSortMap();

    /** 停车地点名称摘要排序map*/
    private static final Map<String, Integer> PARKING_SUMMARY_SORT_MAP = createParkingSummarySortMap();


    /**
     * 获取停车摘要排序
     * @param summary1 摘要1
     * @param summary2 摘要2
     * @return 排序结果
     */
    public static int getParkingSummarySortResult(String summary1, String summary2){
        return order(summary1, summary2, PARKING_SUMMARY_SORT_MAP);
    }


    /**
     * 获取商圈排名结果
     * @param areaChartName1 商圈名称1
     * @param areaChartName2 商圈名称2
     * @return 排序结果
     */
    public static int getAreaChartNameSortResult(String areaChartName1, String areaChartName2){
        return order(areaChartName1, areaChartName2, AREA_CHART_NAME_SORT_MAP);
    }


    /**
     * 获得天气名称排序结果
     * @param scenicName1 景区名称1
     * @param scenicName2 景区名称2
     * @return 排序结果
     */
    public static int getWeatherNameSortResult(String scenicName1, String scenicName2){
        return order(scenicName1, scenicName2, WEATHER_NAME_SORT_MAP);
    }

    /**
     * 按照云图调整要求，在产业运行监测->住宿接待中，排序为：
     * <p>县城区域商圈、瓦屋山商圈、柳江商圈、七里坪商圈、槽渔滩商圈、玉屏山商圈</p>
     * <p>对应返回接口字段的scenicName</p>
     * @return map
     */
    private static Map<String, Integer> createAreaChartNameSortMap() {
        final Map<String, Integer> map = createMap();
        map.put("县城区域商圈",10);
        map.put("瓦屋山商圈",20);
        map.put("柳江商圈",30);
        map.put("七里坪商圈",40);
        map.put("槽渔滩商圈",50);
        map.put("玉屏山商圈",60);
        map.put("其他商圈",70);
        return map;
    }

    /**
     * 停车接口，各个景区名称排序map
     * @return map
     */
    private static Map<String, Integer> createParkingSummarySortMap() {
        final Map<String, Integer> map = createWeatherNameSortMap();
        map.put("主城区",9);
        return map;
    }


    /**
     * 按照云图调整要求，在天气环境监测中，排序为：
     * <p>①瓦屋山、②柳江古镇、③七里坪、④槽渔滩、⑤玉屏山</p>
     * <p>对应返回接口字段的scenicName</p>
     * @return map
     */
    private static Map<String,Integer> createWeatherNameSortMap(){
        final Map<String, Integer> map = createMap();
        map.put("瓦屋山",10);
        map.put("柳江古镇",20);
        map.put("七里坪",30);
        map.put("槽渔滩",40);
        map.put("玉屏山",50);
        return map;
    }

    private static Integer order(String s1, String s2, Map<String,Integer> map){
        if(Objects.isNull(map)){
            return 0;
        }
        Integer sort1 = Optional.ofNullable(map.get(s1)).orElse(1000);
        Integer sort2 = Optional.ofNullable(map.get(s2)).orElse(1000);
        return sort1.compareTo(sort2);

    }

    private static Map<String,Integer> createMap(){
        return new HashMap<>(MAP_SIZE);
    }
}
