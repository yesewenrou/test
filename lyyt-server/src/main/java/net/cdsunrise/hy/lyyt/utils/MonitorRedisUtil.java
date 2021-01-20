package net.cdsunrise.hy.lyyt.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.domain.dto.TrafficFlowConfigDTO;
import net.cdsunrise.hy.lyyt.entity.vo.MonitorSiteVO;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @ClassName MonitorRedisUtil
 * @Description 监控点Redis工具类
 * @Author LiuYin
 * @Date 2020/1/13 19:47
 */
public class MonitorRedisUtil {

    private static final String JOINER = "_";

    /**
     * redis中的key前缀
     */
    private static final String KEY_PREFIXE = "LYJTGL_";

    /**
     * 每天按照小时统计进城车辆流量key(hash)
     */
    private static final String DAY_IN_CITY_STATISTICS_KEY_PREFIX = KEY_PREFIXE + "MONITOR_DAY_IN_CITY";
    /**
     * 每天按照小时统计每个卡口的流量（hash)
     */
    private static final String DAY_MONITOR_STATISTICS_KEY_PREFIX = KEY_PREFIXE + "MONITOR_DAY";

    /**
     * 每年按照月统计进城车辆流量key（hash）
     */
    private static final String YEAR_IN_CITY_STATISTICS_KEY_PREFIX = KEY_PREFIXE + "MONITOR_YEAR_IN_CITY";

    /**
     * 每月按照卡口统计流量key（hash）
     */
    private static final String MONTH_MONITOR_STATISTICS_KEY_PREFIX = KEY_PREFIXE + "MONITOR_MONTH";

    /**
     * 当前流量key（hash）
     */
    private static final String CURRENT_FLOW_KEY = KEY_PREFIXE + "MONITOR_CURRENT_FLOW";

    /**
     * 日按照省的统计key（hash）
     */
    private static final String DAY_SOURCE_IN_CITY_STATISTICS_PROVINCE_KEY_PREFIX = KEY_PREFIXE + "SOURCE_DAY_IN_CITY";

    /**
     * 日按照省的统计key（hash）
     */
    private static final String DAY_SOURCE_IN_CITY_STATISTICS_SI_CHIUAN_CITY = KEY_PREFIXE + "SOURCE_DAY_IN_CITY_SICHIUAN";


    /**
     * 流量大小配置key（hash）
     */
    private static final String KEY_HASH_MONITOR_FLOW_CONFIG = "LYJTGL_TRAFFIC_FLOW_PARAM";

    /**
     * 基础信息key（hash）
     */
    private static final String LYJTGL_MONITOR_BASE_INFO = "LYJTGL_RESOURCE_MONITOR";


    /**
     * 每日进城统计key（hash)
     *
     * @param localDate 日期
     * @return key
     */
    public static String getDayInCityStatisticsKey(LocalDate localDate) {
        return DAY_IN_CITY_STATISTICS_KEY_PREFIX + JOINER + DateUtil.dateToNumberFormat(localDate);
    }


    /**
     * 是否有每日进城统计key
     *
     * @param localDate 日期
     * @return 是否
     */
    public static boolean hashDayInCityStatisticsKey(LocalDate localDate) {
        return hasKey(getDayInCityStatisticsKey(localDate));
    }


    /**
     * 更新每日进城统计
     *
     * @param map
     */
    public static void updateDayInCityStatistics(LocalDate localDate, Map<Integer, Long> map) {
        assertNotNull(localDate, "local date");
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        final Map<String, String> redisMap = ConvertUtil.convertMap(map, String::valueOf, String::valueOf);
        String key = getDayInCityStatisticsKey(localDate);
        RedisUtil.hashOperations().putAll(key, redisMap);
    }

    /**
     * 按照卡口更新日统计（按照小时）
     *
     * @param localDate   日期
     * @param monitorCode 监控点code（卡口code）
     * @param flowMap     每小时流量统计
     */
    public static void updateDayFlowWithHourMap(LocalDate localDate, String monitorCode, Map<Integer, Long> flowMap) {
        if (CollectionUtils.isEmpty(flowMap)) {
            return;
        }
        final String key = getDayMonitorStatisticsKey(localDate, monitorCode);
        RedisUtil.hashOperations().putAll(key, ConvertUtil.convertMap(flowMap, String::valueOf, String::valueOf));

    }

    /**
     * 获取监控点（卡口）日统计key（按照小时）
     *
     * @param localDate   日期
     * @param monitorCode 监控点code
     * @return key
     */
    public static String getDayMonitorStatisticsKey(LocalDate localDate, String monitorCode) {
        assertNotNull(localDate, "local date");
        assertNotEmpty(monitorCode, "monitor code");
        return DAY_MONITOR_STATISTICS_KEY_PREFIX + JOINER + DateUtil.dateToNumberFormat(localDate) + JOINER + monitorCode;
    }


    private static boolean hasKey(String key) {
        assertNotEmpty(key, "key");
        //noinspection ConstantConditions
        return RedisUtil.getStringRedisTemplate().hasKey(key);
    }


    private static void assertNotNull(Object o, String message) {
        AssertUtil.notNull(o, e(message + " is null"));
    }

    private static void assertNotEmpty(String str, String message) {
        AssertUtil.notEmpty(str, e(message + " is empty"));
    }


    /**
     * 一个产生运行时异常的方法
     *
     * @param message 异常信息
     * @return 方法
     */
    private static Supplier<RuntimeException> e(String message) {
        return () -> new RuntimeException(message);
    }

    /**
     * 更新年进城统计（按照月）
     * @param year 年度
     * @param monthMap 月进城流量统计
     */
    public static void updateYearInCityStatistics(Integer year, Map<Integer, Long> monthMap) {
        assertNotNull(year, "year");
        if(CollectionUtils.isEmpty(monthMap)){
            return ;
        }
        final String key = getYearInCityStatisticsKey(year);
        RedisUtil.hashOperations().putAll(key, ConvertUtil.convertMap(monthMap, String::valueOf, String::valueOf));
    }

    /**
     * 是否有年进城统计（按照月）的键(hash)
     * @param year 年度
     * @return 是否
     */
    public static boolean hasYearInCityStatisticsKey(Integer year){
        return hasKey(getYearInCityStatisticsKey(year));
    }


    /**
     * 得到年进城统计（按照月）的键(hash)
     * @param year 年度
     * @return key
     */
    public static String getYearInCityStatisticsKey(Integer year) {
        assertNotNull(year, "year");
        return YEAR_IN_CITY_STATISTICS_KEY_PREFIX + JOINER + year;
    }

    /**
     * 更新当前卡口车流量
     * @param currentFlowMap 卡口车流量map
     */
    public static void updateCurrentMonitorFlow(Map<String, Long> currentFlowMap) {
        if(CollectionUtils.isEmpty(currentFlowMap)){
            return;
        }
        RedisUtil.hashOperations().putAll(getCurrentFlowKey(), ConvertUtil.convertMap(currentFlowMap, String::valueOf));
    }

    /**
     * 获取当前流量的key（hash）
     * @return key
     */
    public static String getCurrentFlowKey(){
        return CURRENT_FLOW_KEY;
    }


    /**
     * 按照省进行每日入城数量统计（包括未识别的省）
     * @param localDate 日期
     * @param provinceMap 省对应的数量
     */
    public static void updateDaySourceInCityStatisticsWithProvince(LocalDate localDate, Map<String, Long> provinceMap) {
        assertNotNull(localDate, "local date");
        if(CollectionUtils.isEmpty(provinceMap)){
            return;
        }
        String key = getDaySourceInCityStatisticsWithProvinceKey(localDate);
        RedisUtil.hashOperations().putAll(key, ConvertUtil.convertMap(provinceMap, String::valueOf, String::valueOf));
    }

    /**
     * 得到按照车辆来源地，按照省分类的统计的key（hash）
     * @param localDate 日期
     * @return key
     */
    public static String getDaySourceInCityStatisticsWithProvinceKey(LocalDate localDate) {
        assertNotNull(localDate, "local date");
        return DAY_SOURCE_IN_CITY_STATISTICS_PROVINCE_KEY_PREFIX + JOINER + DateUtil.dateToNumberFormat(localDate);
    }


    /**
     * 是否有到按照车辆来源地，按照省分类的统计的key（hash）
     * @param localDate 日期
     * @return 是否
     */
    public static boolean hasDaySourceInCityStatisticsWithProvinceKey(LocalDate localDate) {
        return hasKey(getDaySourceInCityStatisticsWithProvinceKey(localDate));
    }

    public static void updateDaySourceInCityStatisticsWithSiChuanCity(LocalDate localDate, Map<String, Long> map) {
        assertNotNull(localDate, "local date");
        if(CollectionUtils.isEmpty(map)){
            return ;
        }
        String key = getDaySourceInCityStatisticsWithSiChuanCityKey(localDate);
        RedisUtil.hashOperations().putAll(key, ConvertUtil.convertMap(map, String::valueOf));
    }

    public static String getDaySourceInCityStatisticsWithSiChuanCityKey(LocalDate localDate) {
        assertNotNull(localDate, "local date");
        return DAY_SOURCE_IN_CITY_STATISTICS_SI_CHIUAN_CITY + JOINER + DateUtil.dateToNumberFormat(localDate);
    }

    public static boolean hasDaySourceInCityStatisticsWithSiChuanCityKey(LocalDate localDate){
        final String key = getDaySourceInCityStatisticsWithSiChuanCityKey(localDate);
        return hasKey(key);
    }


    /**
     * 更新月卡口流量数据可以
     * @param localDate 日期
     * @param monitorMap 卡口编号对应流量
     */
    public static void updateMonthMonitorDayFlow(LocalDate localDate, Map<String, Long> monitorMap) {
        assertNotNull(localDate, "local date");
        if(CollectionUtils.isEmpty(monitorMap)){
            return ;
        }
        String key = getMonthMonitorDayFlowKey(localDate);
        RedisUtil.hashOperations().putAll(key, ConvertUtil.convertMap(monitorMap, String::valueOf));

        // 获取月流量，更新到年流量的月统计中
        updateYearInCityStatisticsFromMonthData(localDate);
    }

    /**
     * 从月的卡口流量统计中，拿出进城的卡口流量数据，更新到年的月统计中
     * @param localDates 日期数组
     */
    public static void updateYearInCityStatisticsFromMonthData(LocalDate... localDates){
        if(Objects.isNull(localDates) || localDates.length == 0){
            return;
        }
        for (LocalDate localDate : localDates) {
            String key = getMonthMonitorDayFlowKey(localDate);
            // <卡口编号， 流量>
            final Map<String, String> entries = RedisUtil.hashOperations().entries(key);
            final long sum = entries.entrySet().stream().filter(e -> MonitorUtil.getInCityMonitorCodeSet().contains(e.getKey())).map(Map.Entry::getValue).mapToLong(Long::parseLong).sum();
            final String yearKey = getYearInCityStatisticsKey(localDate.getYear());
            RedisUtil.hashOperations().put(yearKey, String.valueOf(localDate.getMonthValue()), String.valueOf(sum));
        }
    }

    /**
     * 获取监控点流量状态配置
     * @return [卡口编号，配置对象] e.p [KK-05, TrafficFlowConfigDTO]
     */
    public static Map<String, TrafficFlowConfigDTO> getMonitorFlowCfg() {

        // 从redis中获取配置信息，该配置信息由旅游交通管理服务写入
        final List<String> configs = RedisUtil.hashOperations().values(KEY_HASH_MONITOR_FLOW_CONFIG);
        // 转换成id对应对象map
        // 这里尤其要注意，TrafficFlowConfigDTO中的monitorId不是KK-01、KK-02，而是数字1 、 2  、3这些，所以接下来还要通过基础信息做一次转换
        final Map<Long, TrafficFlowConfigDTO> cfgMap = configs.stream().map(cfg -> JsonUtils.toObject(cfg, new TypeReference<TrafficFlowConfigDTO>() {
        })).collect(Collectors.toMap(TrafficFlowConfigDTO::getId, Function.identity()));

        // 获取基础信息
        final List<String> infos = RedisUtil.hashOperations().values(LYJTGL_MONITOR_BASE_INFO);
        // 这里得到一个卡口ID对应数字id的map，例如[KK-01,1]
        // MonitorSiteDTO的id与TrafficFlowConfigDTO的id是对应的，
        final Map<String, Long> idMap = infos.stream().map(info -> JsonUtils.toObject(info, new TypeReference<MonitorSiteVO>() {
        })).collect(Collectors.toMap(MonitorSiteVO::getMonitorSiteId, MonitorSiteVO::getId));

        final Map<String, TrafficFlowConfigDTO> map = new HashMap<>(idMap.size());
        idMap.forEach((k,v) -> map.put(k, cfgMap.get(v)));
        return map;
    }


    /**
     * 获取月卡口流量key
     * @param localDate 日期
     * @return key
     */
    public static String getMonthMonitorDayFlowKey(LocalDate localDate) {
        assertNotNull(localDate, "local date");
        return MONTH_MONITOR_STATISTICS_KEY_PREFIX + JOINER + (localDate.getYear() * 100 + localDate.getMonthValue()) ;
    }

    /**
     * 是否有月卡口流量key
     * @param localDate 日期
     * @return 是否
     */
    public static boolean hasMonthMonitorDayFlowKey(LocalDate localDate){
        return hasKey(getMonthMonitorDayFlowKey(localDate));
    }
}
