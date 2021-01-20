package net.cdsunrise.hy.lyyt.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.vo.RoadSectionExtDO;
import net.cdsunrise.hy.lyyt.entity.vo.reids.TimeValueForecastDTO;
import net.cdsunrise.hy.lyyt.enums.ImportantEnum;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName RoadSectionUtil
 * @Description
 * @Author LiuYin
 * @Date 2019/12/13 16:54
 */
public class RoadSectionUtil {


    /**
     * redis 模板
     */
    private static StringRedisTemplate stringRedisTemplate;

    private static final String IMPORT_ROAD_SECTION_KEY = "LYJTGL_RESOURCE_SECTION";

    private static final String KEY_ROAD_SECTION = "LYJTGL_RESOURCE_SECTION";

    private static final String KEY_HASH_FORECAST_LYJTGL_SECTION_TPI = "FORECAST_LYJTGL_SECTION_TPI";


    /**
     * 获取所有的路段map
     * @return map[sectionId， RoadSectionExtDO]
     */
    public static Map<String, RoadSectionExtDO> getAllRoadSectionMap(){
        final Map<String, String> entries = RedisUtil.hashOperations().entries(KEY_ROAD_SECTION);
        if(entries.isEmpty()){
            return new HashMap<>(0);
        }
        return entries.values().stream().map(v -> JsonUtils.toObject(v,new TypeReference<RoadSectionExtDO>() {
        })).collect(Collectors.toMap(RoadSectionExtDO::getSectionId, Function.identity()));

    }

    /**
     * 获取所有路段预测结果
     * @return map
     */
    public static Map<String, List<TimeValueForecastDTO>> getAllRoadForecast(){
        final Map<String, String> entries = RedisUtil.hashOperations().entries(KEY_HASH_FORECAST_LYJTGL_SECTION_TPI);
        if(entries.isEmpty()){
            return new HashMap<>(0);
        }
        final Map<String, List<TimeValueForecastDTO>> map = entries.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> JsonUtils.toObject(entry.getValue(), new TypeReference<List<TimeValueForecastDTO>>() {
                        })
                ));
        map.forEach((k,v) -> v.forEach(f -> f.setLocalDateTime(DateUtil.stringToLocalDateTime(f.getTime()))));
        return map;

    }



    /**
     * 获取重点路段
     *
     * @return 重点路段id集合
     */
    public static Set<String> getImportRoadSectionIds() {
        Map<String, String> entries = RedisUtil.hashOperations().entries(IMPORT_ROAD_SECTION_KEY);
        if (CollectionUtils.isEmpty(entries)) {
            return new HashSet<>();
        }
        return entries.entrySet().stream()
                .map(e -> JsonUtils.toObject(e.getValue(), new TypeReference<RoadSectionExtDO>() {
                }))
                .filter(e -> ImportantEnum.YES.getType().equals(e.getImportant()))
                .map(e -> String.valueOf(e.getSectionId()))
                .collect(Collectors.toSet());
    }

    private static Map<String, String> getSectionNameMap() {
        Map<String, String> entries = RedisUtil.hashOperations().entries(IMPORT_ROAD_SECTION_KEY);
        return entries.values().stream()
                .map(s -> JsonUtils.toObject(s, new TypeReference<RoadSectionExtDO>() {
                })).collect(Collectors.toMap(RoadSectionExtDO::getSectionId, RoadSectionExtDO::getRoadSectionName));
    }


    /**
     * 根据路段id，获取路段名称
     *
     * @param roadSectionId 路段id
     * @return 路段名称
     */
    public static String getNameBySectionId(String roadSectionId) {
        return getSectionNameMap().get(roadSectionId);
    }


    public static synchronized void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        if (Objects.isNull(RoadSectionUtil.stringRedisTemplate)) {
            RoadSectionUtil.stringRedisTemplate = stringRedisTemplate;
        }
    }

    /**
     * 删除所有的重点路段
     */
    public static void deleteAllImportRoadSection() {
//        stringRedisTemplate.delete(IMPORT_ROAD_SECTION_KEY);
    }

    /**
     * 更新重点路段
     *
     * @param sectionIds 重点路段id数组
     */
    public static void updateImportRoadSection(String... sectionIds) {
//        if(Objects.isNull(sectionIds) || sectionIds.length == 0){
//            return;
//        }
//        deleteAllImportRoadSection();
//        stringRedisTemplate.opsForSet().add(IMPORT_ROAD_SECTION_KEY, sectionIds);
    }

    /**
     * 获取所有路段id
     */
    public static List<String> getAllSectionIds() {
        return getSectionNameMap().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
