package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.dto.DataBaseTypeDTO;
import net.cdsunrise.hy.lyyt.entity.dto.DataSourceDTO;
import net.cdsunrise.hy.lyyt.entity.dto.DataTypeDTO;
import net.cdsunrise.hy.lyyt.enums.DataBaseTypeEnum;
import net.cdsunrise.hy.lyyt.enums.DataSourceUnitEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName RedisService
 * @Description
 * @Author LiuYin
 * @Date 2019/11/5 17:57
 */
@Component
public class BigDataCenterRedisService {

    /** 键的根前缀*/
    private static final String LYDSJ_ = "LYDSJ_";
    /** 总量键后缀*/
    private static final String TOTAL = "TOTAL";

    /** 数据类型*/
    private static final String KEY_DATA_TYPE = LYDSJ_ + "DATA_TYPE";
    /** 数据来源*/
    private static final String KEY_DATA_SOURCE = LYDSJ_ + "DATA_SOURCE";
    /** 数据库（资源库）类型*/
    private static final String KEY_DATA_BASE_TYPE = LYDSJ_ + "DATA_BASE_TYPE";

    /** 采集服务状态*/
    private static final String KEY_GATHER_LOG_STATE = LYDSJ_ + "GATHER_LOG_STATE";
    /** 采集数据所在地*/
    private static final String KEY_GATHER_LOG_LOCATION = LYDSJ_ + "GATHER_LOG_LOCATION";

    /** 日采集条数*/
    private static final String KEY_PREFIX_GATHER_LOG_COUNT = LYDSJ_ + "GATHER_LOG_COUNT_";
    /** 日调用异常数*/
    private static final String KEY_PREFIX_GATHER_LOG_ERROR = LYDSJ_ + "GATHER_LOG_ERROR_";
    /** 日存储数据量*/
    private static final String KEY_PREFIX_GATHER_LOG_STORE = LYDSJ_ + "GATHER_LOG_STORE_";

    /** 总采集条数*/
    private static final String KEY_GATHER_LOG_COUNT_TOTAL = LYDSJ_ + "GATHER_LOG_COUNT_" + TOTAL;
    /** 总调用异常数*/
    private static final String KEY_GATHER_LOG_ERROR_TOTAL = LYDSJ_ + "GATHER_LOG_ERROR_" + TOTAL;
    /** 总储数据量*/
    private static final String KEY_GATHER_LOG_STORE_TOTAL = LYDSJ_ + "GATHER_LOG_STORE_" + TOTAL;

    /** 日调用次数*/
    private static final String KEY_PREFIX_GATHER_CALL_COUNT = LYDSJ_ + "GATHER_CALL_COUNT_";
    /** 日调用异常数*/
    private static final String KEY_PREFIX_GATHER_ERROR = LYDSJ_ + "GATHER_CALL_ERROR_";

    /** 总调用次数*/
    private static final String KEY_GATHER_CALL_COUNT_TOTAL = LYDSJ_ + "GATHER_CALL_COUNT_" + TOTAL;
    /** 总调用异常数*/
    private static final String KEY_GATHER_CALL_ERROR_TOTAL = LYDSJ_ + "GATHER_CALL_ERROR_" + TOTAL;

    /** 存儲位置对应ip地址*/
    private static final String KEY_GATHER_HOST_IP = LYDSJ_ + "GATHER_HOST_IP";


    /** topic（topic第一个前缀）到数据类型的映射*/
    private static final String KEY_GATHER_DATA_TYPE_MAP = LYDSJ_ + "GATHER_DATA_TYPE_MAP";


    /** 元数据*/
    private static final String KEY_META_MAP = LYDSJ_ + "_" + "META_MAP";


    private static final String KEY_GATHER_DATA_SOURCE_MAP = LYDSJ_ + "GATHER_DATA_SOURCE_MAP";


    /**
     * 共享-》当日接入数
     */
    private static final String KEY_SHARE_RECEIVE_DAY_COUNT = LYDSJ_ + "SHARE_RECEIVE_DAY_COUNT";

    /**
     * LYDSJ_SHARE_DAY_COUNT
     * keyName: today_share_count  今日共享条数
     * keyName: today_receive_count 今日接入数
     */
    private static final String KEY_SHARE_DAY_COUNT = LYDSJ_+"SHARE_DAY_COUNT";

    public static final String FIELD_TODAY_SHARE_COUNT = "today_share_count";
    public static final String FIELD_TODAY_RECEIVE_COUNT = "today_receive_count";



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String,String,String> hashOperations;

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }


    public HashOperations<String, String, String> getHashOperations() {
        return hashOperations;
    }


    /**
     * 按照日期获取当日统计
     * @param localDate 日期
     * @return <topic全称,当日采集条数>
     */
    public Map<String,Long> getDailyCountMap(LocalDate localDate){

        final String dailyCountKey = getDailyCountKey(localDate);
        return convertMapStringValueToLong(hashOperations.entries(dailyCountKey));
    }

    /**
     * 获取当日接入数
     * @return 【索引，接入数量】
     */
    public Map<Integer, Long> getDayShareReceiveCount(){
        final Map<String, String> entries = hashOperations.entries(KEY_SHARE_RECEIVE_DAY_COUNT);
        if(CollectionUtils.isEmpty(entries)){
            return new HashMap<>(0);
        }
        return entries.entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), e -> Long.parseLong(e.getValue())));
    }

    /**
     * 获取总计：今日共享条数，今日接入数
     * @return
     */
    public Map<String, Long> getShareDayCount(){
        final Map<String, String> entries = hashOperations.entries(KEY_SHARE_DAY_COUNT);
        return entries.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Long.parseLong(e.getValue())));
    }


    /**
     * 获取topic前缀对应的数据类型
     * @return <topic,数据类型索引>
     */
    public Map<String,Long> getTopicPrefixToDateTypeIndexMap() {
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_DATA_TYPE_MAP);
        return convertMapStringValueToLong(entries);
    }


    /**
     * 获取topic对应的总数量map
     * @return
     */
    public Map<String, Long> getTotalCountMap() {
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_LOG_COUNT_TOTAL);
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取topic对应的总容量map
     * @return [topic全称，容量]
     */
    public Map<String, Long> getTotalStoreMap() {
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_LOG_STORE_TOTAL);
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取topic对应的异常调用数
     * @return
     */
    public Map<String, Long> getTotalExceptionMap() {
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_LOG_ERROR_TOTAL);
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取topic对应的日调用次数
     * @param localDate
     * @return
     */
    public Map<String,Long> getDailyCallCount(LocalDate localDate){
        final Map<String, String> entries = hashOperations.entries(getDailyCallKey(localDate));
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取总调用次数
     * @return
     */
    public Map<String, Long> getTotalCallCount(){
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_CALL_COUNT_TOTAL);
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取总异常调用次数
     * @return
     */
    public Map<String, Long> getTotalErrorCallCount(){
        final Map<String, String> entries = hashOperations.entries(KEY_GATHER_CALL_ERROR_TOTAL);
        return convertMapStringValueToLong(entries);
    }

    /**
     * 获取数据源映射数据类型map
     */
    public Map<String, String> getDataSourceMap(){
        return hashOperations.entries(KEY_GATHER_DATA_SOURCE_MAP);
    }




    /**
     * 获取存储位置对应的ip
     * @return &lt topic前缀,IP地址 &gt
     */
    public Map<String, String> getStoreHostIpMap() {
        return hashOperations.entries(KEY_GATHER_HOST_IP);
    }

    /**
     * 获取完整topic对应的存储位置
     * @return &lt topic前缀, 存储位置 &gt
     */
    public Map<String, String> getTopicLocationMap(){
        return hashOperations.entries(KEY_GATHER_LOG_LOCATION);
    }

    /**
     * 获取所有的topic前缀对应的ip地址
     * @return
     */
    public Map<String, String> getTopicPrefixHostIpMap() {
//        final Map<String, String> storeHostIpMap = getStoreHostIpMap();
//        final Map<String, String> topicPrefixLocationMap = getTopicLocationMap();
//        Map<String,String> result = new HashMap<>(topicPrefixLocationMap.size());
//
//        topicPrefixLocationMap.forEach((k,v) -> {
//            final String ip = storeHostIpMap.get(v);
//            result.put(k, Objects.isNull(ip) ? "" : ip);
//        });
//        return result;
        return getTopicLocationMap();
    }


    /**
     * 获取元数据映射key
     * @return
     */
    private static String getMetaMapKey(){
        return KEY_META_MAP;
    }

    /**
     * 获取总采集条数key
     * @return
     */
    private static String getCountTotalKey(){
        return KEY_GATHER_LOG_COUNT_TOTAL;
    }

    /**
     * 获取总调用异常数
     * @return
     */
    private static  String getErrorTotalKey(){
        return KEY_GATHER_LOG_ERROR_TOTAL;
    }

    /**
     * 获取
     * @return
     */
    private static String getStoreTotalKey(){
        return KEY_GATHER_LOG_STORE_TOTAL;
    }

    /**
     * 获取采集数据所在地key
     * @return
     */
    private static String getLocationKeyKey(){
        return KEY_GATHER_LOG_LOCATION;
    }

    /**
     * 获取采集服务状态key
     * @return key
     */
    private static String getStateKey(){
        return KEY_GATHER_LOG_STATE;
    }


    /**
     * 获取日采集条数key
     * @param localDate 日期
     * @return key
     */
    private static String getDailyCountKey(LocalDate localDate){
        return KEY_PREFIX_GATHER_LOG_COUNT + getLocalDateString(localDate);
    }

    /**
     * 获取日采集错误key
     * @param localDate 日期
     * @return key
     */
    private static String getDailyErrorKey(LocalDate localDate){
        return KEY_PREFIX_GATHER_LOG_ERROR + getLocalDateString(localDate);
    }

    /**
     * 获取日存储数据key
     * @param localDate 日期
     * @return key
     */
    private static String getDailyStoreKey(LocalDate localDate){
        return KEY_PREFIX_GATHER_LOG_STORE + getLocalDateString(localDate);
    }

    /**
     * 获取日调用次数key
     * @param localDate 日期
     * @return
     */
    private static String getDailyCallKey(LocalDate localDate){
        return KEY_PREFIX_GATHER_CALL_COUNT + getLocalDateString(localDate);
    }


    private static String getLocalDateString(LocalDate localDate){
        return DateUtil.localDateToString(Objects.isNull(localDate) ? LocalDate.now() : localDate);
    }

    /**
     * 初始化数据类型
     */
    public void initDataType() {
        final Map<Integer, DataTypeEnum> indexMap = DataTypeEnum.getIndexMap();
        indexMap.forEach((k,v) -> hashOperations.put(KEY_DATA_TYPE, String.valueOf(k), JsonUtils.toJsonString(DataTypeDTO.from(v))));
    }

    /**
     * 初始化数据来源
     */
    public void initDataSource(){
        final Map<Integer, DataSourceUnitEnum> indexMap = DataSourceUnitEnum.getIndexMap();
        indexMap.forEach((k, v) -> hashOperations.put(KEY_DATA_SOURCE, String.valueOf(k), JsonUtils.toJsonString(DataSourceDTO.from(v))));
    }

    public void initDataBaseType(){
        final Map<Integer, DataBaseTypeEnum> indexMap = DataBaseTypeEnum.getIndexMap();
        indexMap.forEach((k,v) -> hashOperations.put(KEY_DATA_BASE_TYPE, String.valueOf(k),JsonUtils.toJsonString(DataBaseTypeDTO.from(v))));

    }


    public Long getDataTypeSize() {
        return hashOperations.size(KEY_DATA_TYPE);
    }


    public Long getDataSourceSize() {
        return hashOperations.size(KEY_DATA_SOURCE);
    }

    public Long getDataBaseTypeSize() {
        return hashOperations.size(KEY_DATA_BASE_TYPE);
    }





    private Map<String, Long> convertMapStringValueToLong(Map<String,String> map){
        if(Objects.isNull(map)){
            return null;
        }
        final int size = map.size();
        final HashMap<String, Long> longValueMap = new HashMap<>(size);
        if(size > 0){
            map.forEach((k,v) -> longValueMap.put(k, Long.parseLong(v.split("\\.")[0])));
        }
        return longValueMap;
    }



}
