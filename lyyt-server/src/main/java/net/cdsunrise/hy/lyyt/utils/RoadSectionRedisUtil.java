package net.cdsunrise.hy.lyyt.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.vo.reids.RoadSectionJamStatisticsDo;
import net.cdsunrise.hy.lyyt.entity.vo.reids.RoadSectionTpiDto;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName RoadSectionRedisUtil
 * @Description 路段Redis工具类
 * @Author LiuYin
 * @Date 2019/12/13 16:54
 */
public class RoadSectionRedisUtil {


    /**
     * 实时数据过期时间，默认20分钟
     * 当一条路段数据已经20分钟没有更新，则默认是不堵的
     */
    private static final int DATA_EXPIRE_MINUTE = 20;


    private static final String KEY_PREFIXE = "LYJTGL_";

    /**
     * 重点路段key(hash)
     */
    private static final String IMPORT_ROAD_SECTION_KEY = KEY_PREFIXE + "IMPORTANT_ROAD_SECTION";
    /**
     * 路段实时信息数据key(hash)
     */
    private static final String ROAD_SECTION_CURRENT_INFO_KEY = KEY_PREFIXE + "ROAD_SECTION_CURRENT_INFO";
    /**
     * 路段每日拥堵统计key（hash）
     */
    private static final String DAY_JAM_STATISTICS_KEY = KEY_PREFIXE + "ROAD_SECTION_DAY_JAM_STATISTICS";
    /**
     * 路段每月拥堵统计key(zset)
     */
    private static final String MONTH_JAM_STATISTICS_KEY = KEY_PREFIXE + "ROAD_SECTION_MONTH_JAM_STATISTICS";
    /**
     * 近期拥堵排行key(zset)
     */
    private static final String NEARLY_JAM_RANKING_KEY = KEY_PREFIXE + "ROAD_SECTION_NEARLY_RANGING";



    private static String getNearlyDaysRankingKey(Integer days){
        return getNearlyJamRankingKey() + "_" + days;
    }



    private static String getMonthJameKey(LocalDate localDate){
        final String dateToNumberFormat = DateUtil.dateToNumberFormat(localDate);
        final char[] chars = dateToNumberFormat.toCharArray();
        final String prefix = getMonthJamStatisticsKey();
        return prefix + "_" + chars[0] + chars[1] + chars[2] + chars[3] + chars[4] + chars[5];
    }



    public static String getDayJameKey(LocalDate localDate) {
        final String dateToNumberFormat = DateUtil.dateToNumberFormat(localDate);
        return getDayJamStatisticsKey() + "_" + dateToNumberFormat;
    }



    public static String getImportRoadSectionKey() {
        return IMPORT_ROAD_SECTION_KEY;
    }

    public static String getRoadSectionCurrentInfoKey() {
        return ROAD_SECTION_CURRENT_INFO_KEY;
    }


    public static String getDayJamStatisticsKey() {
        return DAY_JAM_STATISTICS_KEY;
    }

    public static String getMonthJamStatisticsKey() {
        return MONTH_JAM_STATISTICS_KEY;
    }

    public static String getNearlyJamRankingKey() {
        return NEARLY_JAM_RANKING_KEY;
    }



    /**
     * 获取一天所有sectionId的拥堵数据
     * @param localDate 日期
     */
    public static Map<String,RoadSectionJamStatisticsDo> batchFetchDayJamStatistics(LocalDate localDate){
        AssertUtil.notNull(localDate, () -> new RuntimeException("local date is null"));

        Map<String,RoadSectionJamStatisticsDo> serializationMap  = new HashMap<>();
        Map<String, String> redisData = RedisUtil.hashOperations().entries(getDayJameKey(localDate));
        redisData.forEach((sectionId, value) -> {
            RoadSectionJamStatisticsDo vo = JsonUtils.toObject(value, new TypeReference<RoadSectionJamStatisticsDo>() {
            });
            serializationMap.put(sectionId, vo);
        });
        return serializationMap;
    }

    /**
     * 获取实时路段拥堵数据
     * @param sectionIds 过滤路段id
     */
    public static List<RoadSectionTpiDto> fetchCurrentDayJamStatistics(Set<String> sectionIds) {
        List<String> redisData;
        if (CollectionUtils.isEmpty(sectionIds)) {
            redisData = new ArrayList<>(RedisUtil.hashOperations().entries(getRoadSectionCurrentInfoKey()).values());
        } else {
            redisData = RedisUtil.hashOperations().multiGet(getRoadSectionCurrentInfoKey(), sectionIds);
        }
        return redisData.stream()
                .map(value -> JsonUtils.toObject(value, new TypeReference<RoadSectionTpiDto>() {
                }))
                .sorted(Comparator.comparingDouble(RoadSectionTpiDto::getTpi).reversed())
                .collect(Collectors.toList());
    }

}
