package net.cdsunrise.hy.lyyt.service.impl;

import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.entity.resp.GatherResponse;
import net.cdsunrise.hy.lyyt.entity.resp.SourceShareResponse;
import net.cdsunrise.hy.lyyt.entity.resp.VisualResponse;
import net.cdsunrise.hy.lyyt.entity.vo.*;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.DataSourceUnitEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import net.cdsunrise.hy.lyyt.service.GatherViewService;
import net.cdsunrise.hy.lyyt.service.BigDataCenterRedisService;
import net.cdsunrise.hy.lyyt.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.ws.Holder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @ClassName GatherViewServiceImpl
 * @Description 采集呈现服务实现类
 * @Author LiuYin
 * @Date 2019/11/6 11:06
 */
@Service
public class GatherViewServiceImpl implements GatherViewService {


    @Autowired
    private BigDataCenterRedisService bigDataCenterRedisService;

    @Autowired
    private TransferService transferService;

    private static final List<Integer> EXISTS_TYPE_INDEX_LIST = new ArrayList<>();

    private static final Map<String, Integer> IP_INDEX_MAP = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        // 获取所有已有数据类型
        final List<Integer> typeIndexList =
                bigDataCenterRedisService.getTopicPrefixToDateTypeIndexMap().values().stream()
                        .distinct()
                        .sorted(Long::compare)
                        .map(Long::intValue)
                        .collect(Collectors.toList());
        EXISTS_TYPE_INDEX_LIST.addAll(typeIndexList);
    }



    @Override
    public VisualResponse getByTypeIndex(Integer typeIndex) {
        AssertUtil.isTrue(DataTypeEnum.isIndexExists(typeIndex), bizErr(BusinessExceptionEnum.INDEX_ERROR));
        LocalDate now = LocalDate.now();
        final DataTypeEnum dataTypeEnum = DataTypeEnum.getIndexMap().get(typeIndex);

        // 获取typeIndex对应的topic前缀
        Set<String> prefixSet = getPrefixSetByDataType(dataTypeEnum);

        // 获取总的topic采集条数(数据数量）
        final Map<String, Long> totalCountMap = bigDataCenterRedisService.getTotalCountMap();
        // 获取数据数量
        Long dataCount = addCount(totalCountMap, prefixSet);

        // 获取总的topic采集容量（数据总量）
        Map<String, Long> totalStoreMap = bigDataCenterRedisService.getTotalStoreMap();
        // 获取数据总量
        Long dataTotal = addCount(totalStoreMap, prefixSet);

        // 得到topic对应的今日调用数
        final Map<String, Long> dailyCallCountMap = bigDataCenterRedisService.getDailyCallCount(now);
        // 得到typeIndex对应的今日调用数
        Long dailyCallCount = addCount(dailyCallCountMap, prefixSet);


        // 得到topic对应的总调用数
        final Map<String, Long> totalCallCountMap = bigDataCenterRedisService.getTotalCallCount();
        // 得到typeIndex对应的总调用数
        Long totalCallCount = addCount(totalCallCountMap, prefixSet);


        VisualResponse vr = new VisualResponse();
        vr.setIndex(dataTypeEnum.getIndex());
        vr.setTitle(dataTypeEnum.getTitle());

        Long normal = transferService.normal(dataTypeEnum, null);
        Long normalTotal = transferService.normalTotal(dataTypeEnum);

        vr.setDataCount(UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, dataCount));
        vr.setDataTotal(UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, dataTotal));
        vr.setTodayCallTimes(UnitDataVO.create(DataDescriptionEnum.TODAY_CALL_TIMES, normal));
        vr.setTotalCallTimes(UnitDataVO.create(DataDescriptionEnum.TOTAL_CALL_TIMES, normalTotal));

        TotalStatisticsVO totalStatisticsVO = getTotalStatisticsVO();
        vr.setTotalStatistics(totalStatisticsVO);

        return vr;

    }





    /**
     * 获得数据类型对应的topic前缀
     *
     * @param typeEnum 数据类型枚举 {@link DataTypeEnum}
     * @return topic前缀集合
     */
    private Set<String> getPrefixSetByDataType(final DataTypeEnum typeEnum) {
        // <topic前缀， 数据类型索引>
        final Map<String, Long> topicPrefixToDateTypeIndexMap = bigDataCenterRedisService.getTopicPrefixToDateTypeIndexMap();
        // 找到符合typeIndex的对应前缀
        Set<String> prefixSet = new HashSet<>(topicPrefixToDateTypeIndexMap.size());
        topicPrefixToDateTypeIndexMap.forEach((k, v) -> {
            if (typeEnum.getIndex() == v.intValue()) {
                prefixSet.add(k);
            }
        });
        return prefixSet;
    }


    /**
     * 得到采集数据列表及统计
     *
     * @return 采集数据响应对象
     */
    @Override
    public GatherResponse getGatherResponse(Integer typeIndex) {
        List<DataGatherVO> list = new ArrayList<>();
        // 如果是-1，表示有效随机生成

        LocalDate now = LocalDate.now();
        if(typeIndex == -1){
            final int size = EXISTS_TYPE_INDEX_LIST.size();
            // 得到随机生成数量
            int number = ThreadLocalRandom.current().nextInt(0, size);
            if(number == 0){
                number = 1;
            }

            // 随机拿第一个元素
            Holder<Integer> indexHolder = new Holder<>(null);

            for(int i = 0; i < number; i++){
                if(indexHolder.value == null){
                    indexHolder.value = new SplittableRandom().nextInt(0, size);
                }else{
                    indexHolder.value = getNextIndex(indexHolder.value);
                }
                final DataTypeEnum dataTypeEnum = DataTypeEnum.getIndexMap().get(EXISTS_TYPE_INDEX_LIST.get(indexHolder.value));
//                AssertUtil.isTrue(DataTypeEnum.isIndexExists(typeIndex), bizErr(BusinessExceptionEnum.INDEX_ERROR));
                final DataGatherVO gatherVO = getGatherVOByDataType(dataTypeEnum, now);
                list.add(gatherVO);
            }
        }else{
            AssertUtil.isTrue(DataTypeEnum.isIndexExists(typeIndex), bizErr(BusinessExceptionEnum.INDEX_ERROR));
            final DataTypeEnum dataTypeEnum = DataTypeEnum.getIndexMap().get(typeIndex);
            final DataGatherVO gatherVO = getGatherVOByDataType(dataTypeEnum, now);
            list.add(gatherVO);
        }
        // 获取所有的数据统计
        List<DataGatherStatisticsVO> dataGatherStatisticsVOList = getDataGatherStatisticsList();


        final GatherResponse response = new GatherResponse();
        response.setGatherList(list);
        response.setGatherStatistics(dataGatherStatisticsVOList);


        response.setGatherStatistics(response.getGatherStatistics().stream().limit(4).collect(Collectors.toList()));
        return response;
    }

    /**
     * 按照数据类型和日期，获取采集对象
     * @param dataTypeEnum 数据类型
     * @param date 日期
     * @return
     */
    private DataGatherVO getGatherVOByDataType(DataTypeEnum dataTypeEnum, LocalDate date) {

        DataGatherVO vo = DataGatherVO.create(dataTypeEnum);

        // 获取DataTypeEnum对应的topic前缀集合
        final Set<String> prefixSet = getPrefixSetByDataType(dataTypeEnum);

        // 获取所有的完整topic对应的ip地址
        Map<String, String> topicPrefixHostIpMap = bigDataCenterRedisService.getTopicPrefixHostIpMap();

        // 数据类型包含的ip
        final Set<String> dataTypeIpSet = new HashSet<>();
        topicPrefixHostIpMap.forEach((k, v) -> {
            final String[] split = k.split("-");
            if (split.length > 1 && prefixSet.contains(split[0])) {
                dataTypeIpSet.add(v);
            }
        });
        // 得到存储位置，最多3个
        String location = dataTypeIpSet.isEmpty() ? "" : dataTypeIpSet.stream().limit(3).collect(Collectors.joining(","));
        vo.setStoreLocation(UnitDataVO.createStoreLocationType(location));

        // 得到调用耗时
        Long duration = ThreadLocalRandom.current().nextLong(20, 100);
        vo.setGatherDuration(UnitDataVO.create(DataDescriptionEnum.GATHER_DURATION, duration));


        // 获取实时的采集量
        // TODO 这里暂时使用的是当天的采集总量
        final Map<String, Long> dailyCountMap = bigDataCenterRedisService.getDailyCountMap(date);
        final Long dailyCount = addCount(dailyCountMap, prefixSet);
        vo.setRealTimeSize(UnitDataVO.create(DataDescriptionEnum.REAL_TIME_GATHER_SIZE, dailyCount));

        return vo;
    }

    /**
     * 获取所有数据类型的采集统计
     *
     * @return 采集统计列表
     */
    @Override
    public List<DataGatherStatisticsVO> getDataGatherStatisticsList() {
        // 获取所有存在的映射
        final Map<String, Long> topicPrefixToDateTypeIndexMap = bigDataCenterRedisService.getTopicPrefixToDateTypeIndexMap();
        // 去重、排序
        final List<Integer> indexList = topicPrefixToDateTypeIndexMap.values().stream().distinct().map(Long::intValue).sorted(Integer::compare).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(indexList)) {
            return new ArrayList<>();
        }
        List<DataGatherStatisticsVO> voList = new ArrayList<>(indexList.size());

        indexList.forEach(index -> {
            final DataTypeEnum dataTypeEnum = DataTypeEnum.getIndexMap().get(index);
            if (Objects.nonNull(dataTypeEnum)) {
                final Long store = getStoreTotalByDataType(dataTypeEnum);
                final DataGatherStatisticsVO vo = DataGatherStatisticsVO.create(dataTypeEnum);
                // 数据描述
                final UnitDataVO unitDataVO = UnitDataVO.create(DataDescriptionEnum.GATHER_TOTAL, store);
                vo.setGatherTotal(unitDataVO);
                voList.add(vo);
            }
        });
        return voList;
    }

    /**
     * 获取数据共享对象
     *
     * @param typeIndex 数据类型索引
     * @return
     */
    @Override
    public SourceShareResponse getShareResponse(Integer typeIndex) {
        AssertUtil.isTrue(DataSourceUnitEnum.isIndexExist(typeIndex), bizErr(BusinessExceptionEnum.INDEX_ERROR));
        final SourceShareResponse resp = new SourceShareResponse();
        resp.setIndex(typeIndex);
        resp.setName(DataSourceUnitEnum.getIndexMap().get(typeIndex).getName());


        final Map<String, String> dataSourceMap = bigDataCenterRedisService.getDataSourceMap();
        // 得到typeIndex对应的key，把这个key作为totalCountMap的key的前缀进行匹配
        final Set<String> keySet = dataSourceMap.entrySet().stream().filter(e -> typeIndex.equals(Integer.parseInt(e.getValue()))).map(Map.Entry::getKey).collect(Collectors.toSet());

        // 计算条数总量
        final Map<String, Long> totalCountMap = bigDataCenterRedisService.getTotalCountMap();
        final long totalCount = totalCountMap.entrySet().stream().filter(e -> keySet.contains(e.getKey().split("-")[0])).mapToLong(Map.Entry::getValue).sum();
        resp.setDataCount(UnitDataVO.create(DataDescriptionEnum.DATA_COUNT, totalCount));

        // 计算存储量
        final Map<String, Long> totalStoreMap = bigDataCenterRedisService.getTotalStoreMap();
        final long totalStore = totalStoreMap.entrySet().stream().filter(e -> keySet.contains(e.getKey().split("-")[0])).mapToLong(Map.Entry::getValue).sum();
        resp.setDataTotal(UnitDataVO.create(DataDescriptionEnum.DATA_TOTAL, totalStore));

        // 得到当日接入数
        final Map<Integer, Long> dayShareReceiveCount = bigDataCenterRedisService.getDayShareReceiveCount();
        final Long dayCallTimes = dayShareReceiveCount.get(typeIndex);
        resp.setCurrentDayCallTimes(UnitDataVO.create(DataDescriptionEnum.CURRENT_DAY_CALL_TIMES, dayCallTimes));

        resp.setCurrentDayShareError(UnitDataVO.create(DataDescriptionEnum.CURRENT_DAY_SHARE_ERROR, 0L));

        // 得到今日共享条数和今日接入数
        final Map<String, Long> shareDayCount = bigDataCenterRedisService.getShareDayCount();
        // 今日共享条数
        final Long todayShareCount = shareDayCount.get(BigDataCenterRedisService.FIELD_TODAY_SHARE_COUNT);
        // 今日接入数
        final Long todayConnection = shareDayCount.get(BigDataCenterRedisService.FIELD_TODAY_RECEIVE_COUNT);

        final ShareStatisticsVO vo = new ShareStatisticsVO();
        vo.setTotalType(24);
        vo.setTotalUnit(12);
        vo.setTodayShare(Objects.isNull(todayShareCount) ? 0 :todayShareCount.intValue());
        vo.setTodayConnection(Objects.isNull(todayConnection) ? 0 : todayConnection.intValue());
        vo.setError(0);
        vo.setNormal(vo.getTodayShare() - vo.getError());

        resp.setShareStatistics(vo);

        return resp;
    }

    /**
     * 获取某种数据类型的存储总量
     *
     * @param dataTypeEnum 数据类型枚举 {@link DataTypeEnum}
     * @return 数据存储总量
     */
    private Long getStoreTotalByDataType(DataTypeEnum dataTypeEnum) {
        // 得到总计容量<topic全称，容量>
        final Map<String, Long> totalStoreMap = bigDataCenterRedisService.getTotalStoreMap();
        // 得到datatype对应的topic前缀
        final Set<String> prefixSet = getPrefixSetByDataType(dataTypeEnum);
        // 计算存储总量
        return addCount(totalStoreMap, prefixSet);
    }

    private Map<String, String> getTopicPrefixHostIpMap(Map<String, String> topicPrefixLocationMap, Map<String, String> storeHostIpMap) {
        return null;
    }


    /**
     * 获取topic前缀
     *
     * @param topic 完整的topic
     * @return topic前缀
     */
    private static String getTopicPrefix(String topic) {
        if (Objects.isNull(topic)) {
            return null;
        }
        if (topic.isEmpty()) {
            return "";
        }
        final String[] split = topic.split("-", 2);
        return split[0];

    }


    /**
     * 获取数据可视化统计
     * @return 统计对象
     */
    @Override
    public TotalStatisticsVO getTotalStatisticsVO() {
        final TotalStatisticsVO vo = new TotalStatisticsVO();
        LocalDateTime now = LocalDateTime.now();
        Double visitCountDouble = now.getDayOfYear() * 1.23 + now.getHour() * 2.45 + now.getMinute() * 1.23;
        // 总用户数
        vo.setTotalUser(ThreadLocalRandom.current().nextLong(13, 14));
        // 总访问数
        vo.setTotalVisit(visitCountDouble.longValue());
        // 总连接数
        vo.setTotalConnect(vo.getTotalVisit() / 5);

        // 总数据数量（条）
        vo.setTotalDataCount(getAllDataCount());
        // 总数据总量（字节）
        vo.setTotalData(getAllDataTotal());
        // 总异常调用（次）
        vo.setTotalExceptionCount(getAllExceptionCount());

        return vo.complete();

    }


    /**
     * 得到所有的数据总量
     *
     * @return
     */
    private Long getAllDataTotal() {
        final Map<String, Long> totalStoreMap = bigDataCenterRedisService.getTotalStoreMap();
        return getValueSum(totalStoreMap);
    }

    /**
     * 得到所有的数据数量
     *
     * @return
     */
    private Long getAllDataCount() {
        final Map<String, Long> totalCountMap = bigDataCenterRedisService.getTotalCountMap();
        return getValueSum(totalCountMap);
    }


    /**
     * 得到所有的异常调用数
     *
     * @return
     */
    private Long getAllExceptionCount() {
        return transferService.errorTotal();
//        final Map<String, Long> totalExceptionCountMap = redisService.getTotalExceptionMap();
//        return getValueSum(totalExceptionCountMap);
    }


    /**
     * 得到一个map值的累加
     *
     * @param map
     * @return
     */
    private Long getValueSum(Map<String, Long> map) {
        return CollectionUtils.isEmpty(map) ? 0L : map.values().stream().mapToLong(l -> l).sum();
    }


    /**
     * 累计计数
     *
     * @param map       [topic全称，数字]
     * @param prefixSet [topic前缀]
     * @return 在map中符合topic前缀的累加数
     */
    private static Long addCount(Map<String, Long> map, Set<String> prefixSet) {
        if (Objects.isNull(prefixSet) || prefixSet.isEmpty()) {
            return 0L;
        }
        if (Objects.isNull(map) || map.isEmpty()) {
            return 0L;
        }

        Holder<Long> countHolder = new Holder<>(0L);
        map.forEach((k, v) -> {
            final String topicPrefix = getTopicPrefix(k);
            if (Objects.nonNull(topicPrefix) && prefixSet.contains(topicPrefix)) {
                countHolder.value = countHolder.value + v;
            }
        });
        return countHolder.value;
    }


    private static Integer getNextIndex(Integer index){
        final int size = EXISTS_TYPE_INDEX_LIST.size();
        if(index >= (size -1)){
            return 0;
        }else{
            return index + 1;
        }
    }

    private static Integer recordAndReturn(String ip){
        String key = StringUtils.isEmpty(ip) ? "UNKNOWN" : ip;
        final Integer index = IP_INDEX_MAP.get(key);
        if(Objects.isNull(index)){
            IP_INDEX_MAP.putIfAbsent(key, 0);
        }
        IP_INDEX_MAP.put(key, getNextIndex(IP_INDEX_MAP.get(key)));
        return IP_INDEX_MAP.get(key);
    }


    private static Supplier<BusinessException> bizErr(BusinessExceptionEnum exceptionEnum) {
        return BusinessException.fail(exceptionEnum);
    }

}
