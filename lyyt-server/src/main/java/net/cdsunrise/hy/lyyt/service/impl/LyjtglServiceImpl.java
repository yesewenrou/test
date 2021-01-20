package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.StringUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.domain.dto.MonitorSiteDTO;
import net.cdsunrise.hy.lyyt.domain.dto.TrafficBaiduFiveMinuteDTO;
import net.cdsunrise.hy.lyyt.domain.dto.TrafficFlowConfigDTO;
import net.cdsunrise.hy.lyyt.domain.vo.*;
import net.cdsunrise.hy.lyyt.entity.dto.RoadSectionTimeValueForecastDTO;
import net.cdsunrise.hy.lyyt.entity.resp.ForecastValueResponse;
import net.cdsunrise.hy.lyyt.entity.vo.*;
import net.cdsunrise.hy.lyyt.entity.vo.reids.RoadSectionJamStatisticsDo;
import net.cdsunrise.hy.lyyt.entity.vo.reids.TimeValueForecastDTO;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.enums.ImportantRoadSection;
import net.cdsunrise.hy.lyyt.enums.JamLevelEnum;
import net.cdsunrise.hy.lyyt.enums.MonitorColor;
import net.cdsunrise.hy.lyyt.service.LyjtglService;
import net.cdsunrise.hy.lyyt.utils.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YQ on 2019/11/5.
 */
@Service
@Slf4j
public class LyjtglServiceImpl implements LyjtglService {
    private static final String FIVE_MINUTE_INDEX = "hy_mock_data_five_minute";
    private static final String HY_TRAFFIC_BAIDU_FIVE_MINUTE = "hy_traffic_baidu_five_minute";

    private static final String UNKNOW_PLATE_NO = "未识别";
    private static final String SICHUAN_PLATE_NO = "四川";
    private static final String HOLIDAY_KEY_PREFIX = "CONFIG_HOLIDAY";

    private static final String KEY_HASH_FORECAST_FLOW = "FORECAST_LYJTGL_MONITOR_FLOW";


    private static final String MEI_SHAN = "眉山";

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public LyjtglServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public TrafficWholeSituationVo trafficWholeSituation() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Map<String, String> yesterdayHistogram = trafficWholeSituationHistogram(yesterday, null);
        Map<String, String> todayHistogram = trafficWholeSituationHistogram(today, null);

        final TrafficWholeSituationVo vo = TrafficWholeSituationVo.build(yesterdayHistogram, todayHistogram);

        final Integer todayInCityMeiShanCarFlow = getInCityMeiShanCarFlow(today);
        final Integer yesterdayInCityMeiShanCarFlow = getInCityMeiShanCarFlow(today.minusDays(1));

        vo.setTodayOutLandFlowCount(vo.getTodayTotalFlow() - todayInCityMeiShanCarFlow);
        vo.setYesterdayOutLandFlowCount(vo.getYesterdayTotalFlow() - yesterdayInCityMeiShanCarFlow);
        return vo;
    }

    /**
     * 得到入县域的眉山车流量
     * @param localDate 日期
     * @return not null flow count
     */
    private Integer getInCityMeiShanCarFlow(LocalDate localDate){
        final String key = MonitorRedisUtil.getDaySourceInCityStatisticsWithSiChuanCityKey(localDate);
        final String value = RedisUtil.hashOperations().get(key, MEI_SHAN);
        return StringUtil.isEmpty(value) ? 0 : Integer.parseInt(value);

    }


    @Override
    public CarSourceTopTenVo carSourceTopTen() {
        LocalDate today = LocalDate.now();
        return carSourceTopTen(today);
    }

    @Override
    public CrossCarCountVo crossCarCount() {
        LocalDate today = LocalDate.now();
        Integer todayCrossCarCount = crossCarCountDay(Collections.singletonList(today));
        Long thisMonth = DateUtil.getCurrentMonthStartTime(0);
        Integer monthCrossCarCount = crossCarCountMonth(DateUtil.convert(thisMonth).toLocalDate());
        return CrossCarCountVo.build(todayCrossCarCount, monthCrossCarCount);
    }

    @Override
    public List<ImportantRoadSection.ImportantRoadSectionClazz> importantRoad() {
        return ImportantRoadSection.list();
    }

    @Override
    public ImportantHistogramVo importantHistogram(String monitorCode) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        Map<String, String> yesterdayHistogram = trafficWholeSituationHistogram(yesterday, monitorCode);
        Map<String, String> todayHistogram = trafficWholeSituationHistogram(today, monitorCode);
        return ImportantHistogramVo.build(yesterdayHistogram, todayHistogram);
    }

    @Override
    public CarSourceRatioVo carSourceRatio() {
        LocalDate thisWeek = DateUtil.convert(DateUtil.getCurrentWeekStartTime()).toLocalDate();
        LocalDate next = DateUtil.convert(DateUtil.getCurrentStartTime(1)).toLocalDate();
        Map<String, String> provinceHistogram = carSourceRatio(thisWeek, next, true);
        Map<String, String> outProvinceHistogram = carSourceRatio(thisWeek, next, false);
        return CarSourceRatioVo.build(provinceHistogram, outProvinceHistogram);
    }

    @Override
    public RoadDetailMonitorVo oftenJamRoad() {
        int size = 10;
        RoadDetailMonitorVo roadDetailMonitorVo = new RoadDetailMonitorVo();
        //获取排名前10的经常拥堵路段
        Long last30Day = DateUtil.getCurrentStartTime(-30);
        List<RoadDetailVo> oftenJamList = jamRank(JamLevelEnum.SLOW.getValue(), DateUtil.convert(last30Day).toLocalDate(), LocalDate.now());
        if (oftenJamList != null && oftenJamList.size() > size) {
            oftenJamList = oftenJamList.subList(0, size);
        }

        Map<String, RoadDetailVo> lastRoadDetailMap = lastRoadDetailMap(null);

        //给经常拥堵路段填充实时信息
        if (oftenJamList != null) {
            fillRoadDetail(oftenJamList, lastRoadDetailMap);
            roadDetailMonitorVo.setOftenJamList(oftenJamList);
            //找到现在拥堵 但是又不在常发拥堵里面的
            Set<String> offJamSet = oftenJamList.stream().map(RoadDetailVo::getSectionId).collect(Collectors.toSet());
            List<RoadDetailVo> unOftenJamList = lastRoadDetailMap.entrySet().stream()
                    .filter(e -> {
                        RoadDetailVo vo = e.getValue();
                        JamLevelEnum jamLevel = vo.getJamLevel();
                        String sectionId = vo.getSectionId();
                        return (jamLevel != JamLevelEnum.UNBLOCKED && jamLevel != JamLevelEnum.SLOW) && !offJamSet.contains(sectionId);
                    })
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            if (unOftenJamList != null && unOftenJamList.size() > size) {
                unOftenJamList = unOftenJamList.subList(0, size);
            }
            roadDetailMonitorVo.setUnOftenJamList(unOftenJamList);
        }
        return roadDetailMonitorVo;
    }

    @Override
    public List<RoadSectionMonitorSiteVo> roadMonitorImportant() {
        Set<String> importRoadSectionIds = RoadSectionUtil.getImportRoadSectionIds();
        List<RoadDetailVo> roadDetailVoList = lastRoadDetail(importRoadSectionIds);
        if (roadDetailVoList == null) {
            return null;
        }
        List<RoadDetailVo> collect = roadDetailVoList.stream()
                .sorted(Comparator.comparingDouble(RoadDetailVo::getTpi).reversed())
                .collect(Collectors.toList());

        return collect.stream().map(roadDetailVo -> {
            RoadSectionMonitorSiteVo roadSectionMonitorSite = new RoadSectionMonitorSiteVo();
            roadSectionMonitorSite.setSectionId(roadDetailVo.getSectionId());
            roadSectionMonitorSite.setAvgSpeed(roadDetailVo.getAvgSpeed());
            roadSectionMonitorSite.setJamCount(roadDetailVo.getJamCount());
            roadSectionMonitorSite.setRangeTime(roadDetailVo.getRangeTime());
            roadSectionMonitorSite.setTpi(roadDetailVo.getTpi());
            roadSectionMonitorSite.setEndTime(roadDetailVo.getEndTime());
            roadSectionMonitorSite.setJamLength(roadDetailVo.getJamLength());
            roadSectionMonitorSite.setJamLevel(roadDetailVo.getJamLevel());
            roadSectionMonitorSite.setJamTimeLength(roadDetailVo.getJamTimeLength());
            roadSectionMonitorSite.setRoadName(roadDetailVo.getRoadName());
            roadSectionMonitorSite.setStartTime(roadDetailVo.getStartTime());

            String deviceCode = RoadSectionMonitorUtil.ROAD_SECTION_MONITOR_SITE.get(roadDetailVo.getSectionId());
            roadSectionMonitorSite.setDeviceCode(deviceCode);
            return roadSectionMonitorSite;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoadDetailVo> roadMonitor() {
        return lastRoadDetail(null);
    }

    @Override
    public List<OftenJamTimeVo> oftenJamTimeDay() {
        Long currentMonthStartTime = DateUtil.getCurrentStartTime(0);
        return oftenJamTime(JamLevelEnum.SLOW.getValue(), new Date(currentMonthStartTime), new Date());
    }

    @Override
    public List<MonitorRealTimeData> getMonitorRealTimeData() {
        return getMonitorRealTimeDataEs();
    }

    @Override
    public List<RoadJamTimeLengthVo> roadSegJamRank() {

        //获取排前10的经常拥堵路段
        Long monthStartTime = DateUtil.getCurrentMonthStartTime(0);
        List<RoadDetailVo> oftenJamList = jamRank(JamLevelEnum.SLOW.getValue(), DateUtil.convert(monthStartTime).toLocalDate(), LocalDate.now());
        if (oftenJamList == null) {
            return null;
        }
        Map<String, RoadDetailVo> lastRoadDetailMap = lastRoadDetailMap(null);
        fillRoadDetail(oftenJamList, lastRoadDetailMap);


        List<RoadJamTimeLengthVo> collect = oftenJamList
                .stream()
                .map(RoadJamTimeLengthVo::build)
                .filter(e -> e.getJamTimeLength() != null && e.getJamTimeLength() > 0)
                .collect(Collectors.toList());
        int size = 10;
        if (collect != null && collect.size() > size) {
            collect = collect.subList(0, size);
        }
        return collect;
    }

    @Override
    public List<TrafficBaiduFiveMinuteDTO> query(Set<String> sectionIds, Long startTime, Long endTime) {
        return queryEs(sectionIds, startTime, endTime);
    }

    @Override
    public Integer jamCountToday() {
        String redisKey = RoadSectionRedisUtil.getDayJameKey(LocalDate.now());
        Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
        return entries.entrySet().stream()
                .map(e -> JsonUtils.toObject(e.getValue(), new TypeReference<RoadSectionJamStatisticsDo>() {
                }))
                .map(RoadSectionJamStatisticsDo::getJamCount).reduce(0, (e1, e2) -> e1 + e2);
    }

    public Integer jamCountToday2() {
        Long currentStartTime = DateUtil.getCurrentStartTime(0);
        Long now = System.currentTimeMillis();
        Map<String, Integer> map = getJamCount(currentStartTime, now);
        return map.entrySet().stream().map(Map.Entry::getValue).reduce(0, (e1, e2) -> e1 + e2);
    }

    @Override
    public JamRoadCountVo jamRoadCountToday() {
        Long currentStartTime = DateUtil.getCurrentStartTime(0);
        List<RoadDetailVo> roadDetailList = jamRank(JamLevelEnum.SLOW.getValue(), DateUtil.convert(currentStartTime).toLocalDate(), LocalDate.now());
        JamRoadCountVo vo = new JamRoadCountVo();
        if (roadDetailList == null) {
            vo.setRoadJamCount(0);
        } else {
            vo.setRoadJamCount(roadDetailList.size());
        }
        return vo;
    }

    @Override
    public List<MonitorDetailVo> monitorList() {
        Map<String, MonitorSiteVO> monitorMap = MonitorUtil.getMonitorMap();
        return monitorMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .map(e -> MonitorDetailVo.buildDefault(e.getMonitorSiteId(), e.getMonitorSiteName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoadDetailVo> roadJamTimeRankMonth() {
        int size = 10;
        //获取排名前10的经常拥堵路段
        Long last30Day = DateUtil.getCurrentMonthStartTime(0);
        List<RoadDetailVo> oftenJamList = jamRank(JamLevelEnum.SLOW.getValue(), DateUtil.convert(last30Day).toLocalDate(), LocalDate.now());
        if (oftenJamList != null && oftenJamList.size() > size) {
            oftenJamList = oftenJamList.subList(0, size);
        }
        //给经常拥堵路段填充实时信息
        Map<String, RoadDetailVo> lastRoadDetailMap = lastRoadDetailMap(null);
        if (oftenJamList != null) {
            fillRoadDetail(oftenJamList, lastRoadDetailMap);
        }
        return oftenJamList;
    }

    @Override
    public MonitorCarCountVo monitorCarCount() {
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;

        Map<String, String> thisYearData = monitorCarCountEs(thisYear);
        Map<String, String> lastYearData = monitorCarCountEs(lastYear);

        return MonitorCarCountVo.build(thisYearData, lastYearData);
    }

    @Override
    public TrafficAnalyzeVo trafficAnalyze(HolidayTypeEnum holiday) {
        if (holiday == null) {
            return TrafficAnalyzeVo.buildDefault();
        }
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;


        HolidayVO thisYearHoliday = fetchHolidayData(holiday, thisYear);
        HolidayVO lastYearHoliday = fetchHolidayData(holiday, lastYear);

        List<KeyValueVo> thisYearCarCount = thisYearHoliday.fetchDateList().stream()
                .map(e -> {
                    String key = DateUtil.localDateToString(e);
                    Integer count = crossCarCountDay(Collections.singletonList(e));
                    return new KeyValueVo(key, String.valueOf(count));
                }).collect(Collectors.toList());

        List<KeyValueVo> lastYearCarCount = lastYearHoliday.fetchDateList().stream()
                .map(e -> {
                    String key = DateUtil.localDateToString(e);
                    Integer count = crossCarCountDay(Collections.singletonList(e));
                    return new KeyValueVo(key, String.valueOf(count));
                }).collect(Collectors.toList());

        List<RoadDetailVo> roadDetailVoList = jamRank(JamLevelEnum.SLOW.getValue(), thisYearHoliday.fetchStartDate(), thisYearHoliday.fetchEndDate());
        List<KeyValueVo> jamTimeLengthTopFive = roadDetailVoList.stream().limit(5)
                .map(e -> {
                    String key = e.getRoadName();
                    String value = String.valueOf(e.getJamTimeLength());
                    return new KeyValueVo(key, value);
                }).collect(Collectors.toList());

        return TrafficAnalyzeVo.build(thisYearCarCount, lastYearCarCount, jamTimeLengthTopFive);
    }

    /**
     * 当前进城流量预测
     *
     * @return resp
     */
    @Override
    public List<ForecastValueResponse<Integer>> currentInCityFlowForecast() {

        // 如果redis里面没有值，直接返回默认值
        final Map<String, String> entries = RedisUtil.hashOperations().entries(KEY_HASH_FORECAST_FLOW);
        if(CollectionUtils.isEmpty(entries)){
            return createDefaultInCityFlowForecast();
        }

        final List<ForecastValueResponse<Integer>> list = new ArrayList<>(2);
        List<TimeValueForecastDTO> dtoList =new ArrayList<>(32);
        entries.forEach((k,v) ->{
            // 只需要进城的
            final boolean isInCityKey = MonitorUtil.getInCityMonitorCodeSet().contains(k);
            if(isInCityKey){
                final List<TimeValueForecastDTO> timeValueForecastDTOS = JsonUtils.toObject(v, new TypeReference<List<TimeValueForecastDTO>>() {
                });
                dtoList.addAll(timeValueForecastDTOS);
            }
        });
        // 时间规整，只保留小时
        dtoList.forEach(dto -> {
            final LocalDateTime dateTime = DateUtil.stringToLocalDateTime(dto.getTime());
            dto.setLocalDateTime(LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.getHour(), 0, 0)));
        });
        // 对时间分组
        final Map<LocalDateTime, List<TimeValueForecastDTO>> map = dtoList.stream().collect(Collectors.groupingBy(TimeValueForecastDTO::getLocalDateTime));
        // 每一组，做成一个预测响应对象
        map.forEach((k,v) ->{
            final double value = v.stream().mapToDouble(TimeValueForecastDTO::getValue).sum();
            ForecastValueResponse<Integer> resp = ForecastValueResponse.create(k.getHour(),k.plusHours(1).getHour(), (int) value);
            resp.setTime(DateUtil.localDateTimeToLong(k));
            list.add(resp);
        });
        // 按照时间排序
        return list.stream().sorted(Comparator.comparing(ForecastValueResponse::getTime)).collect(Collectors.toList());
    }


    private  List<ForecastValueResponse<Integer>> createDefaultInCityFlowForecast(){
        final LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(8);
        final int beginHour = localDateTime.getHour();

        final List<ForecastValueResponse<Integer>> list = new ArrayList<>(2);
        list.add(ForecastValueResponse.create(beginHour + 1, beginHour +2, 0));
        list.add(ForecastValueResponse.create(beginHour + 2, beginHour +3, 0));
        return list;
    }




    /**
     * 当前拥堵路段预测
     * @param tpi 至少要达到多少tpi，才展示
     * @return resp
     */
    @Override
    public List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> currentJamRoleSectionForecast(Double tpi) {
        Double minTpi = Objects.isNull(tpi) ? JamLevelEnum.SLOW.getValue(): tpi;

        // 获取所有预测结果
        final Map<String, List<TimeValueForecastDTO>> map = RoadSectionUtil.getAllRoadForecast();
        if(CollectionUtils.isEmpty(map)){
            return createDefaultJamRoleSectionForecast();
        }
        List<RoadSectionTimeValueForecastDTO> roadSectionTimeValueForecastDTOList = new ArrayList<>(map.size() * 2);
        map.forEach((k,v) -> v.forEach(dto -> {
            final Double value = dto.getValue();
            // 这里，只有拥堵的才展示
            if(Objects.nonNull(value) && value.compareTo(minTpi) >= 0){
                final RoadSectionTimeValueForecastDTO rsDto = RoadSectionTimeValueForecastDTO.from(dto, k);
                // 这里要把时间规整到小时级别
                rsDto.setLocalDateTime(LocalDateTime.of(dto.getLocalDateTime().toLocalDate(), LocalTime.of(dto.getLocalDateTime().getHour(),0,0)));
                roadSectionTimeValueForecastDTOList.add(rsDto);
            }
        }));

        if(CollectionUtils.isEmpty(roadSectionTimeValueForecastDTOList)){
            return createDefaultJamRoleSectionForecast();
        }

        final Map<String, RoadSectionExtDO> allRoadSectionMap = RoadSectionUtil.getAllRoadSectionMap();

        // 按照时间分组
        final Map<LocalDateTime, List<RoadSectionTimeValueForecastDTO>> timeListMap = roadSectionTimeValueForecastDTOList.stream().collect(Collectors.groupingBy(RoadSectionTimeValueForecastDTO::getLocalDateTime));
        List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> resultList = new ArrayList<>(timeListMap.size());
        timeListMap.forEach((k,v) ->{
            final List<RoadSectionTpiForecastVO> voList = v.stream().map(dto -> {
                final RoadSectionExtDO roadSectionExtDO = allRoadSectionMap.get(dto.getSectionId());
                if (Objects.nonNull(roadSectionExtDO)) {
                    final RoadSectionTpiForecastVO vo = RoadSectionTpiForecastVO.from(roadSectionExtDO);
                    vo.setTpi(dto.getValue());
                    return vo;
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).sorted((o1, o2) -> {
                final int compare = Double.compare(o2.getTpi(), o1.getTpi());
                return compare != 0 ? compare : o1.getSectionId().compareTo(o2.getSectionId());
            }).collect(Collectors.toList());

            final ForecastValueResponse<List<RoadSectionTpiForecastVO>> resp = ForecastValueResponse.create(k.getHour(), k.plusHours(1).getHour(), voList);
            resp.setTime(DateUtil.localDateTimeToLong(k));
            resultList.add(resp);
        });

        // 按照时间排序
        return resultList.stream().sorted(Comparator.comparing(ForecastValueResponse::getTime)).collect(Collectors.toList());
    }

    private List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> createDefaultJamRoleSectionForecast() {
        // 因为路段数据要在15分钟以后才能请求到上一个小时的完整数据，所有这里默认减去15分钟
        final LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(15);
        // 下一小时
        final int nextOneHour = localDateTime.plusHours(1).getHour();
        // 下两小时
        final int nextTwoHour = localDateTime.plusHours(2).getHour();
        // 下三小时
        final int nextThreeHour = localDateTime.plusHours(3).getHour();

        final List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> list = new ArrayList<>(2);
        list.add(ForecastValueResponse.create(nextOneHour, nextTwoHour, new ArrayList<>(0)));
        list.add(ForecastValueResponse.create(nextTwoHour, nextThreeHour, new ArrayList<>(0)));
        return list;
    }



    private HolidayVO fetchHolidayData(HolidayTypeEnum holiday, int thisYear) {
        String redisData = RedisUtil.hashOperations().get(getHolidayKey(thisYear), holiday.getShortening());
        return JsonUtils.toObject(redisData, new TypeReference<HolidayVO>() {
        });
    }

    /**
     * GET hy_mock_data_five_minute/_search
     * {
     * "size":0,
     * "query": {
     * "terms": {
     * "monitorCode.keyword": [
     * "KK-03",
     * "KK-05"
     * ]
     * }
     * },
     * "aggs": {
     * "date_his": {
     * "date_histogram": {
     * "field": "rangeBegin",
     * "interval": "month",
     * "format": "MM"
     * },
     * "aggs": {
     * "sum": {
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     * }
     * }
     */
    private Map<String, String> monitorCarCountEs2() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("count_sum")
                .field("count");
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("time_histogram")
                .field("rangeEnd")
                .dateHistogramInterval(DateHistogramInterval.MONTH)
                .format("MM")
                .order(BucketOrder.key(true))
                .subAggregation(sumAggregation);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .withQuery(queryBuilder)
                .addAggregation(dateHistogram)
                .build();
        Aggregations query = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        return convertDateHistogram(query);
    }

    private Map<String, String> monitorCarCountEs(int year) {
        String redisKey = MonitorRedisUtil.getYearInCityStatisticsKey(year);
        return RedisUtil.hashOperations().entries(redisKey);
    }

    private Map<String, Integer> getJamCount(Long startTime, Long endTime) {
        List<TrafficBaiduFiveMinuteDTO> fiveMinuteData = queryAll(startTime, endTime);
        Map<String, List<TrafficBaiduFiveMinuteDTO>> fiveMinuteDataMap = fiveMinuteData.stream()
                .collect(Collectors.groupingBy(TrafficBaiduFiveMinuteDTO::getSectionId));
        Map<String, Integer> map = new HashMap<>();
        for (String sectionId : fiveMinuteDataMap.keySet()) {
            List<TrafficBaiduFiveMinuteDTO> trafficBaiduFiveMinuteList = fiveMinuteDataMap.get(sectionId);
            List<TrafficBaiduFiveMinuteDTO> tempList = doFor(trafficBaiduFiveMinuteList, startTime, endTime, sectionId);
            List<Double> tpiList = tempList.stream().map(TrafficBaiduFiveMinuteDTO::getTpi).collect(Collectors.toList());
            Integer jamCount = getCongestionTimes(tpiList);
            map.put(sectionId, jamCount);
        }
        return map;
    }

    /**
     * 分批查询所有路段的,减少es压力
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 所有路段数据
     */
    public List<TrafficBaiduFiveMinuteDTO> queryAll(Long startTime, Long endTime) {
        int offset = 10;
        List<String> sectionIds = RoadSectionUtil.getAllSectionIds();
        int size = sectionIds.size();
        List<TrafficBaiduFiveMinuteDTO> list = new ArrayList<>();
        for (int i = 0; i < size; i += offset) {
            int endIndex = offset + i;
            if (endIndex > size) {
                endIndex = size;
            }
            Set<String> tempSectionIds = new HashSet<>(sectionIds.subList(i, endIndex));
            List<TrafficBaiduFiveMinuteDTO> tempData = queryEs(tempSectionIds, startTime, endTime);
            list.addAll(tempData);
        }
        return list;
    }

    private void fillRoadDetail(List<RoadDetailVo> oftenJamList, Map<String, RoadDetailVo> lastRoadDetailMap) {
        //给经常拥堵路段填充实时信息
        oftenJamList.forEach(e -> {
            String sectionId = e.getSectionId();
            RoadDetailVo roadDetailVo = lastRoadDetailMap.get(sectionId);
            if (roadDetailVo != null) {
                MyBeanUtils.copyNonNullProperties(roadDetailVo, e);
            }
        });
    }

    /**
     * GET /hy_mock_data_five_minute/_search
     * {
     * "size": 0,
     * "aggs": {
     * "id_terms": {
     * "terms": {
     * "field": "cityName.keyword",
     * "size": 100
     * },
     * "aggs": {
     * "top_hits": {
     * "top_hits": {
     * "size": 1,
     * "sort": {
     * "rangeEnd": {
     * "order": "desc"
     * }
     * }
     * }
     * },
     * "sum_count":{
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     * }
     * }
     *
     * @return data
     */
    private List<MonitorRealTimeData> getMonitorRealTimeDataEs2() {
        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("top_hits")
                .size(1)
                .sort("rangeEnd", SortOrder.DESC);

        TermsAggregationBuilder idTerms = AggregationBuilders.terms("id_terms")
                .field("cityName.keyword")
                .size(1000)
                .subAggregation(topHits);


        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .addAggregation(idTerms)
                .build();

        Aggregations aggs = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        StringTerms termAggs = aggs.get("id_terms");
        if (termAggs != null) {
            List<MonitorRealTimeData> topHitsList = termAggs.getBuckets().stream()
                    .map(e -> {
                        InternalTopHits topHitsAggs = e.getAggregations().get("top_hits");
                        SearchHit firstHit = topHitsAggs.getHits().getAt(0);
                        return JsonUtils.toObject(firstHit.getSourceAsString(), new TypeReference<MonitorRealTimeData>() {
                        });
                    }).collect(Collectors.toList());
            Map<String, Integer> tempMap = new HashMap<>();
            topHitsList.forEach(e -> {
                String monitorCode = e.getMonitorCode();
                Integer count = tempMap.get(monitorCode);
                if (count == null) {
                    count = 0;
                }
                tempMap.merge(monitorCode, count, (a, b) -> b + e.getCount());
            });

            // 获取流量状态配置
            final Map<String, TrafficFlowConfigDTO> monitorFlowCfg = MonitorRedisUtil.getMonitorFlowCfg();

            return tempMap.entrySet().stream().map(e -> {
                String monitorCode = e.getKey();
                Integer count = e.getValue();
                final TrafficFlowConfigDTO cfg = monitorFlowCfg.get(monitorCode);
                MonitorColor monitorColor = Objects.isNull(cfg) ? MonitorColor.fetchMonitorColor(count) : MonitorColor.fetchMonitorColor(count, cfg.getMinNum(), cfg.getMaxNum());
                return new MonitorRealTimeData(monitorCode, monitorColor, count);
            }).collect(Collectors.toList());
        }
        return null;
    }

    private List<MonitorRealTimeData> getMonitorRealTimeDataEs() {
        Map<String, String> entries = RedisUtil.hashOperations().entries(MonitorRedisUtil.getCurrentFlowKey());
        final Map<String, TrafficFlowConfigDTO> monitorFlowCfg = MonitorRedisUtil.getMonitorFlowCfg();

        return entries.entrySet().stream()
                .map(e -> {
                    Integer count = Integer.parseInt(e.getValue());
                    final String monitorId = e.getKey();
                    final TrafficFlowConfigDTO trafficFlowConfigDTO = monitorFlowCfg.get(monitorId);
                    MonitorColor monitorColor = Objects.isNull(trafficFlowConfigDTO) ? MonitorColor.fetchMonitorColor(count) : MonitorColor.fetchMonitorColor(count, trafficFlowConfigDTO.getMinNum(), trafficFlowConfigDTO.getMaxNum());
                    return new MonitorRealTimeData(monitorId, monitorColor, count);
                }).collect(Collectors.toList());

    }






    @Override
    public List<OftenJamTimeVo> oftenJamTime() {
        Long currentMonthStartTime = DateUtil.getCurrentMonthStartTime(0);
        return oftenJamTime(JamLevelEnum.SLOW.getValue(), new Date(currentMonthStartTime), new Date());
    }

    /**
     * 时间内的车来源数量柱状图
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param location  是否是川内
     * @return 柱状图
     * <p>
     * GET /hy_mock_data_five_minute/_search
     * {
     * "size": "0",
     * "query": {
     * "bool": {
     * "filter": {
     * "range": {
     * "rangeEnd": {
     * "gte": "2019-11-01 00:00:00",
     * "lt": "2019-11-06 00:00:00"
     * }
     * }
     * },
     * "must": {
     * "term": {
     * "provinceName.keyword": {
     * "value": "四川"
     * }
     * }
     * }
     * }
     * },
     * "aggs": {
     * "time_histogram": {
     * "date_histogram": {
     * "field": "rangeEnd",
     * "interval": "1d",
     * "format": "MM.dd",
     * "order": {
     * "_key": "asc"
     * }
     * },
     * "aggs": {
     * "count_sum": {
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     * }
     * }
     */
    private Map<String, String> carSourceRatio2(Date startTime, Date endTime, boolean location) {
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] carSourceRatio startTime={},endTime={}",
                startTimeStr, endTimeStr);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilder = boolQuery
                .filter(QueryBuilders.rangeQuery("rangeEnd").gte(startTimeStr).lt(endTimeStr));
        if (location) {
            boolQuery.must(QueryBuilders.termsQuery("provinceName.keyword", "四川"));
        } else {
            boolQuery.mustNot(QueryBuilders.termsQuery("provinceName.keyword", "四川"));
        }
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("count_sum")
                .field("count");
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("time_histogram")
                .field("rangeEnd")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .format("MM.dd")
                .order(BucketOrder.key(true))
                .subAggregation(sumAggregation);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .withQuery(queryBuilder)
                .addAggregation(dateHistogram)
                .build();
        Aggregations query = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);

        return convertDateHistogram(query);
    }


    private Map<String, String> carSourceRatio(LocalDate startTime, LocalDate endTime, boolean location) {
        String startTimeStr = DateUtil.localDateToString(startTime);
        String endTimeStr = DateUtil.localDateToString(endTime);
        log.info("[CarSourceStatisticsServiceImpl] carSourceRatio startTime={},endTime={}",
                startTimeStr, endTimeStr);
        Map<String, String> map = new LinkedHashMap<>();
        List<LocalDate> localDates = DateUtil.rangeLocalDateList(startTime, endTime);
        //四川数据
        localDates.forEach(localDate -> {
            String redisKey = MonitorRedisUtil.getDaySourceInCityStatisticsWithSiChuanCityKey(localDate);
            Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
            String key = localDate.format(DateTimeFormatter.ofPattern("MM.dd"));

            Integer count;

            if (!location) {
                //外省数据  需要减去省内数据
                String redisKey2 = MonitorRedisUtil.getDaySourceInCityStatisticsWithProvinceKey(localDate);
                Map<String, String> entries2 = RedisUtil.hashOperations().entries(redisKey2);
                count = entries2.entrySet().stream()
                        .filter(e -> !UNKNOW_PLATE_NO.equals(e.getKey()) && !SICHUAN_PLATE_NO.equals(e.getKey()))
                        .map(e -> Integer.parseInt(e.getValue()))
                        .reduce(0, (e1, e2) -> e1 + e2);
            } else {
                count = entries.values().stream().map(Integer::parseInt).reduce(0, (e1, e2) -> e1 + e2);
            }
            map.put(key, String.valueOf(count));
        });

        return map;
    }

    private Map<String, String> convertDateHistogram(Aggregations query) {
        Map<String, String> map = new LinkedHashMap<>();
        query.asList().stream().flatMap(e -> ((InternalDateHistogram) e).getBuckets().stream())
                .forEach(e -> {
                    String keyAsString = e.getKeyAsString();
                    InternalSum sumCount = (InternalSum) e.getAggregations().asMap().get("count_sum");
                    Integer value = (int) sumCount.getValue();
                    map.put(keyAsString, value.toString());
                });
        return map;
    }

    /**
     * 范围时间经过车的数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 经过车的数量
     * <p>
     * GET /hy_mock_data_five_minute/_search
     * {
     * "size": "0",
     * "query": {
     * "bool": {
     * "filter": {
     * "range": {
     * "rangeEnd": {
     * "gte": "2019-11-05 00:00:00",
     * "lt": "2019-11-06 00:00:00"
     * }
     * }
     * }
     * }
     * },
     * "aggs": {
     * "count_sum": {
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     */
    private Integer crossCarCount2(Date startTime, Date endTime) {
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] crossCarCount startTime={},endTime={}",
                startTimeStr, endTimeStr);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        Set<String> inCityMonitorCode = new HashSet<>(MonitorUtil.getInCityMonitorCodeSet());
        boolQuery.must(QueryBuilders.termsQuery("monitorCode.keyword", inCityMonitorCode));
        BoolQueryBuilder queryBuilder = boolQuery
                .filter(QueryBuilders.rangeQuery("rangeEnd").gte(startTimeStr).lt(endTimeStr));
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("count_sum")
                .field("count");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .withQuery(queryBuilder)
                .addAggregation(sumAggregation)
                .build();
        Aggregations query = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        InternalSum sumCount = (InternalSum) query.getAsMap().get("count_sum");
        return (int) sumCount.getValue();
    }

    private Integer crossCarCountDay(List<LocalDate> dates) {
        if (CollectionUtils.isEmpty(dates)) {
            return 0;
        }
        return dates.stream()
                .map(date -> {
                    String redisKey = MonitorRedisUtil.getDayInCityStatisticsKey(date);
                    Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
                    return entries.values().stream().map(Integer::parseInt).reduce(0, (e1, e2) -> e1 + e2);
                }).reduce(0, (e1, e2) -> e1 + e2);

    }

    private Integer crossCarCountMonth(LocalDate date) {
        String redisKey = MonitorRedisUtil.getMonthMonitorDayFlowKey(date);
        Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
        Set<String> inCity = MonitorUtil.getInCityMonitorCodeSet();
        return entries.entrySet().stream()
                .filter(e -> inCity.contains(e.getKey()))
                .map(e -> Integer.parseInt(e.getValue())).reduce(0, (e1, e2) -> e1 + e2);
    }

    /**
     * 车流量top10
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return vo
     * GET /hy_mock_data_five_minute/_search
     * {
     * "size": "0",
     * "query": {
     * "bool": {
     * "filter": {
     * "range": {
     * "rangeEnd": {
     * "gte": "2019-11-05 00:00:00",
     * "lt": "2019-11-06 00:00:00"
     * }
     * }
     * },
     * "must": [
     * {"term": {
     * "provinceName.keyword": {
     * "value": "四川"
     * }
     * }}
     * ]
     * }
     * },
     * "aggs": {
     * "terms_city":{
     * "terms": {
     * "field": "cityName.keyword",
     * "size": 999
     * },
     * "aggs": {
     * "count_sum": {
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     * }
     * }
     */
    private CarSourceTopTenVo carSourceTopTen2(Date startTime, Date endTime) {
        int top = 10;
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] carSourceTopTen startTime={},endTime={}",
                startTimeStr, endTimeStr);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilder = boolQuery
                .filter(QueryBuilders.rangeQuery("rangeEnd").gte(startTimeStr).lt(endTimeStr));
        boolQuery.must(QueryBuilders.termsQuery("provinceName.keyword", "四川"));
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("count_sum")
                .field("count");
        TermsAggregationBuilder termsCity = AggregationBuilders.terms("terms_city")
                .field("cityName.keyword")
                .subAggregation(sumAggregation)
                .size(999);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .withQuery(queryBuilder)
                .addAggregation(termsCity)
                .build();
        Aggregations query = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        StringTerms cityTerms = (StringTerms) query.asMap().get("terms_city");
        Map<String, String> map = new LinkedHashMap<>();
        AtomicReference<Integer> index = new AtomicReference<>(0);
        AtomicReference<Integer> count = new AtomicReference<>(0);
        cityTerms.getBuckets().forEach(e -> {
            String keyAsString = e.getKeyAsString();
            InternalSum sumCount = (InternalSum) e.getAggregations().asMap().get("count_sum");
            Integer value = (int) sumCount.getValue();
            if (!keyAsString.equals(UNKNOW_PLATE_NO) && index.get() < top) {
                map.put(keyAsString, value.toString());
                index.getAndSet(index.get() + 1);
            }
            count.updateAndGet(v -> v + value);
        });
        return CarSourceTopTenVo.build(count.get(), map);
    }

    private CarSourceTopTenVo carSourceTopTen(LocalDate today) {
        int size = 10;
        CarSourceTopTenVo vo = new CarSourceTopTenVo();
        String redisKey = MonitorRedisUtil.getDaySourceInCityStatisticsWithSiChuanCityKey(today);
        Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
        if (!CollectionUtils.isEmpty(entries)) {
            entries = entries.entrySet().stream()
                    .sorted(Comparator.comparingInt((e -> -Integer.parseInt(e.getValue()))))
                    .limit(size)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            vo.setCarSourceHistogram(entries);
            int totalCount = entries.values().stream().map(Integer::parseInt).reduce(0, (e1, e2) -> e1 + e2);
            vo.setTotalCount(totalCount);
        }
        return vo;
    }


    /**
     * 进城车流量柱状图
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 柱状图表
     * <p>
     * GET /hy_mock_data_five_minute/_search
     * {
     * "size": "0",
     * "query": {
     * "bool": {
     * "filter": {
     * "range": {
     * "rangeEnd": {
     * "gte": "2019-11-05 00:00:00",
     * "lt": "2019-11-06 00:00:00"
     * }
     * }
     * },
     * "must": {
     * "term": {
     * "monitorCode.keyword": {
     * "value": "KK-02"
     * }
     * }
     * }
     * }
     * },
     * "aggs": {
     * "time_histogram": {
     * "date_histogram": {
     * "field": "rangeEnd",
     * "interval": "1h",
     * "format": "HH:mm:ss",
     * "order": {
     * "_key": "asc"
     * }
     * },
     * "aggs": {
     * "count_sum": {
     * "sum": {
     * "field": "count"
     * }
     * }
     * }
     * }
     * }
     * }
     */
    private Map<String, String> trafficWholeSituationHistogram2(Date startTime, Date endTime, String monitorCode) {
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] trafficWholeSituationHistogram startTime={},endTime={}",
                startTimeStr, endTimeStr);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilder = boolQuery
                .filter(QueryBuilders.rangeQuery("rangeEnd").gte(startTimeStr).lt(endTimeStr));
        if (!StringUtils.isEmpty(monitorCode)) {
            boolQuery.must(QueryBuilders.termsQuery("monitorCode.keyword", monitorCode));
        }
        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("count_sum")
                .field("count");
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("time_histogram")
                .field("rangeEnd")
                .dateHistogramInterval(DateHistogramInterval.HOUR)
                .format("HH:mm:ss")
                .order(BucketOrder.key(true))
                .subAggregation(sumAggregation);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(FIVE_MINUTE_INDEX)
                .withQuery(queryBuilder)
                .addAggregation(dateHistogram)
                .build();
        Aggregations query = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        return convertDateHistogram(query);
    }


    private Map<String, String> trafficWholeSituationHistogram(LocalDate date, String monitorCode) {
        if (StringUtils.isEmpty(monitorCode)) {
            return trafficWholeSituationHistogramInCity(date);
        }
        String redisKey = MonitorRedisUtil.getDayMonitorStatisticsKey(date, monitorCode);
        Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
        return convertHistogramMap(entries);
    }

    private Map<String, String> trafficWholeSituationHistogramInCity(LocalDate date) {
        String redisKey = MonitorRedisUtil.getDayInCityStatisticsKey(date);
        Map<String, String> entries = RedisUtil.hashOperations().entries(redisKey);
        return convertHistogramMap(entries);
    }

    private Map<String, String> convertHistogramMap(Map<String, String> entries) {
        return entries.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .collect(Collectors.toMap(k -> getDateKey(k.getKey()), Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private String getDateKey(String k) {
        int limit = 10;
        Integer key = Integer.parseInt(k);
        String newKey = "";
        if (key < limit) {
            newKey += "0";
        }
        newKey = newKey + key + ":00:00";
        return newKey;
    }

    /**
     * 一段时间拥堵排名
     *
     * @param tpi       tpi
     * @param startTime 开始时间
     * @param endTime   结束时间
     *                  GET /hy_traffic_baidu_five_minute/_search
     *                  {
     *                  "size": 0,
     *                  "query": {
     *                  "bool": {
     *                  "must": [
     *                  {
     *                  "range": {
     *                  "rangeTime": {
     *                  "gte": "2019-11-14 00:00:00",
     *                  "lte": "2019-12-14 11:21:00"
     *                  }
     *                  }
     *                  },
     *                  {
     *                  "range": {
     *                  "tpi": {
     *                  "gte": 2
     *                  }
     *                  }
     *                  }
     *                  ]
     *                  }
     *                  },
     *                  "aggs": {
     *                  "id_terms": {
     *                  "terms": {
     *                  "field": "sectionId.keyword",
     *                  "size": 100
     *                  }
     *                  }
     *                  }
     *                  }
     */
    private List<RoadDetailVo> jamRank2(Double tpi, Date startTime, Date endTime) {
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] jamRank startTime={},endTime={}",
                startTimeStr, endTimeStr);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("rangeTime").gte(startTimeStr).lte(endTimeStr));
        boolQuery.filter(QueryBuilders.rangeQuery("tpi").gte(tpi));

        TermsAggregationBuilder idTerms = AggregationBuilders.terms("id_terms")
                .field("sectionId.keyword")
                .size(100);


        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(HY_TRAFFIC_BAIDU_FIVE_MINUTE)
                .withQuery(boolQuery)
                .addAggregation(idTerms)
                .build();

        Aggregations aggs = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        StringTerms termAggs = aggs.get("id_terms");
        if (termAggs != null) {
            return termAggs.getBuckets().stream()
                    .map(e -> {
                        String sectionId = e.getKeyAsString();
                        Long jamCount = e.getDocCount();
                        RoadDetailVo vo = new RoadDetailVo();
                        vo.setSectionId(sectionId);
                        vo.setJamCount(jamCount);
                        vo.setStartTime(startTime);
                        vo.setEndTime(endTime);
                        return vo;
                    }).collect(Collectors.toList());
        }
        return null;
    }


    private List<RoadDetailVo> jamRank(Double tpi, LocalDate startTime, LocalDate endTime) {
        String startTimeStr = DateUtil.localDateToString(startTime);
        String endTimeStr = DateUtil.localDateToString(endTime);
        log.info("[CarSourceStatisticsServiceImpl] jamRank startTime={},endTime={},tpi={}",
                startTimeStr, endTimeStr, tpi);

        List<LocalDate> localDates = DateUtil.rangeLocalDateList(startTime, endTime);
        Map<String, RoadSectionJamStatisticsDo> initData = new HashMap<>();
        localDates.forEach(date -> {
            Map<String, RoadSectionJamStatisticsDo> data = RoadSectionRedisUtil.batchFetchDayJamStatistics(date);
            mergeData(initData, data);
        });
        return initData.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(RoadDetailVo::build)
                .sorted(Comparator.comparingLong(RoadDetailVo::getJamCount).reversed())
                .collect(Collectors.toList());

    }

    /**
     * 合并两个数据
     *
     * @param initData 初始数据
     * @param data     添加的数据
     */
    private void mergeData(Map<String, RoadSectionJamStatisticsDo> initData, Map<String, RoadSectionJamStatisticsDo> data) {
        if (CollectionUtils.isEmpty(initData)) {
            initData.putAll(data);
        } else {
            initData.forEach((key, oldData) -> {
                RoadSectionJamStatisticsDo newData = data.get(key);
                if (newData != null) {
                    oldData.setContinuousJamCount(oldData.getContinuousJamCount() + newData.getContinuousJamCount());
                    oldData.setJamCount(oldData.getJamCount() + newData.getJamCount());
                }
            });
        }
    }

    /**
     * 最新的一天数据
     *
     * @param sectionIds ids
     * @return data
     * GET /hy_traffic_baidu_five_minute/_search
     * {
     * "size": 0,
     * "query": {
     * "bool": {
     * "must": [
     * {
     * "terms": {
     * "sectionId.keyword": [
     * "15-1",
     * "20-2"
     * ]
     * }
     * }
     * ]
     * }
     * },
     * "aggs": {
     * "id_terms": {
     * "terms": {
     * "field": "sectionId.keyword",
     * "size": 100
     * },
     * "aggs": {
     * "top_hits": {
     * "top_hits": {
     * "size": 1,
     * "sort": {
     * "rangeTime": {
     * "order": "desc"
     * }
     * }
     * }
     * }
     * }
     * }
     * }
     * }
     */
    private List<RoadDetailVo> lastRoadDetail2(Set<String> sectionIds) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(sectionIds)) {
            boolQuery.must(QueryBuilders.termsQuery("sectionId.keyword", sectionIds));
        }

        TopHitsAggregationBuilder topHits = AggregationBuilders.topHits("top_hits")
                .size(1)
                .sort("rangeTime", SortOrder.DESC);

        TermsAggregationBuilder idTerms = AggregationBuilders.terms("id_terms")
                .field("sectionId.keyword")
                .size(100)
                .subAggregation(topHits);


        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(HY_TRAFFIC_BAIDU_FIVE_MINUTE)
                .withQuery(boolQuery)
                .addAggregation(idTerms)
                .build();

        Aggregations aggs = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        StringTerms termAggs = aggs.get("id_terms");
        if (termAggs != null) {
            return termAggs.getBuckets().stream()
                    .map(e -> {
                        InternalTopHits topHitsAggs = e.getAggregations().get("top_hits");
                        SearchHit firstHit = topHitsAggs.getHits().getAt(0);
                        RoadDetailVo roadDetailVo = JsonUtils.toObject(firstHit.getSourceAsString(), new TypeReference<RoadDetailVo>() {
                        });
                        roadDetailVo.update();
                        return roadDetailVo;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    private List<RoadDetailVo> lastRoadDetail(Set<String> sectionIds) {
        return RoadSectionRedisUtil
                .fetchCurrentDayJamStatistics(sectionIds)
                .stream()
                .map(RoadDetailVo::build)
                .collect(Collectors.toList());

    }

    private Map<String, RoadDetailVo> lastRoadDetailMap(Set<String> sectionIds) {
        Map<String, RoadDetailVo> map = new HashMap<>();
        List<RoadDetailVo> list = lastRoadDetail(sectionIds);
        if (list != null) {
            list.forEach(e -> map.put(e.getSectionId(), e));
        }
        return map;
    }

    /**
     * GET /hy_traffic_baidu_five_minute/_search
     * {
     * "size": 0,
     * "query": {
     * "bool": {
     * "must": [
     * {
     * "range": {
     * "rangeTime": {
     * "gte": "2019-11-14 00:00:00",
     * "lte": "2019-12-14 11:21:00"
     * }
     * }
     * },
     * {
     * "range": {
     * "tpi": {
     * "gte": 2
     * }
     * }
     * }
     * ]
     * }
     * },
     * "aggs": {
     * "histogram": {
     * "date_histogram": {
     * "field": "rangeTime",
     * "interval": "1h",
     * "format": "HH:mm:ss"
     * }
     * }
     * }
     * }
     */
    private List<OftenJamTimeVo> oftenJamTime(Double tpi, Date startTime, Date endTime) {
        int size = 10;
        String startTimeStr = DateUtil.convertLong2String(startTime.getTime());
        String endTimeStr = DateUtil.convertLong2String(endTime.getTime());
        log.info("[CarSourceStatisticsServiceImpl] oftenJamTime startTime={},endTime={}",
                startTimeStr, endTimeStr);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("rangeTime").gte(startTimeStr).lte(endTimeStr));
        boolQuery.filter(QueryBuilders.rangeQuery("tpi").gte(tpi));

        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("date_histogram")
                .field("rangeTime")
                .dateHistogramInterval(new DateHistogramInterval("1h"))
                .format("HH:mm:ss");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(HY_TRAFFIC_BAIDU_FIVE_MINUTE)
                .withQuery(boolQuery)
                .addAggregation(dateHistogram)
                .build();
        Aggregations aggs = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);
        InternalDateHistogram dateHistogramAggs = aggs.get("date_histogram");
        Map<String, Long> map = new HashMap<>();
        if (dateHistogramAggs != null) {
            dateHistogramAggs.getBuckets()
                    .forEach(e -> {
                        String key = e.getKeyAsString();
                        Long count = e.getDocCount();
                        map.merge(key, count, (a, b) -> b + a);
                    });
        }
        List<OftenJamTimeVo> list = map.entrySet().stream()
                .map(e -> new OftenJamTimeVo(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        list = list.stream()
                .sorted(Comparator.comparingLong(OftenJamTimeVo::getCount).reversed())
                .collect(Collectors.toList());
        if (list != null && list.size() > size) {
            list = list.subList(0, size);
        }
        return list;
    }

    /**
     * GET /hy_traffic_baidu_five_minute/_search
     * {"size": 1000,"query": {"bool": {"filter": {"range": {"rangeTime": {
     * "gte": "2019-12-12 11:45:00","lte": "2019-12-12 11:46:00"}}},"must": [
     * {"terms": {"sectionId.keyword": ["23-2","8-1"]}}]}}}
     *
     * @param sectionIds 路段id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 百度拥堵数据
     */
    private List<TrafficBaiduFiveMinuteDTO> queryEs(Set<String> sectionIds, Long startTime, Long endTime) {
        String startTimeStr = DateUtil.convertLong2String(startTime);
        String endTimeStr = DateUtil.convertLong2String(endTime);
        log.info("[TrafficBaiduFiveMinuteServiceImpl] query startTime={},endTime={}", startTimeStr, endTimeStr);

        if (CollectionUtils.isEmpty(sectionIds)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.rangeQuery("rangeTime").gte(startTimeStr).lte(endTimeStr));
            NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                    .withIndices(HY_TRAFFIC_BAIDU_FIVE_MINUTE)
                    .withQuery(queryBuilder)
                    .withPageable(PageRequest.of(0, 10000))
                    .build();
            return elasticsearchTemplate.queryForList(nativeSearchQuery, TrafficBaiduFiveMinuteDTO.class);
        } else {
            List<TrafficBaiduFiveMinuteDTO> trafficBaiduFiveMinutes = new ArrayList<>();
            sectionIds.forEach(sectionId -> {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                boolQuery.must(QueryBuilders.termQuery("sectionId.keyword", sectionId));
                BoolQueryBuilder queryBuilder = boolQuery
                        .filter(QueryBuilders.rangeQuery("rangeTime").gte(startTimeStr).lte(endTimeStr));
                NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                        .withIndices(HY_TRAFFIC_BAIDU_FIVE_MINUTE)
                        .withPageable(PageRequest.of(0, 10000))
                        .withQuery(queryBuilder)
                        .build();
                trafficBaiduFiveMinutes.addAll(elasticsearchTemplate.queryForList(nativeSearchQuery, TrafficBaiduFiveMinuteDTO.class));
            });
            return trafficBaiduFiveMinutes;
        }
    }


    /**
     * 对百度查询出来的拥堵数据做0值填充，因为es查询可能会存在没有发送数据的情况
     *
     * @param trafficBaiduFiveMinutes es查询的百度数据
     * @param reportBeginTime         开始时间
     * @param reportEndTime           结束时间
     * @param sectionId               路段id
     * @return 填充后百度拥堵数据
     */
    private List<TrafficBaiduFiveMinuteDTO> doFor(List<TrafficBaiduFiveMinuteDTO> trafficBaiduFiveMinutes,
                                                  Long reportBeginTime, Long reportEndTime, String sectionId) {
        int offset = 5;
        long beginTime = DateUtil.fiveMinutes(reportBeginTime);
        long endTime = DateUtil.fiveMinutes(reportEndTime);
        LocalDateTime beginDateTime = DateUtil.convert(beginTime);
        int between = (int) Duration.between(Objects.requireNonNull(beginDateTime), DateUtil.convert(endTime)).toMinutes();
        int numbers = (Math.abs(between) / offset) + 1;
        LocalDateTime realBegin = DateUtil.convert(DateUtil.fiveMinutes(beginTime));

        List<TrafficBaiduFiveMinuteDTO> trafficBaiduFiveMinuteList = new ArrayList<>(numbers);
        for (int i = 0; i < numbers; i++) {
            LocalDateTime localDateTime = Objects.requireNonNull(realBegin).plusMinutes(i * offset);
            long time = DateUtil.DateTimeToLong(localDateTime);
            TrafficBaiduFiveMinuteDTO trafficBaiduFiveMinute;
            if (Objects.isNull(trafficBaiduFiveMinutes)) {
                trafficBaiduFiveMinute = TrafficBaiduFiveMinuteDTO.createZero(sectionId, time);
            } else {
                trafficBaiduFiveMinute = trafficBaiduFiveMinutes.stream()
                        .filter(baidu -> baidu.getRangeTime().getTime() == time).findAny()
                        .orElseGet(() -> TrafficBaiduFiveMinuteDTO.createZero(sectionId, time));
            }
            trafficBaiduFiveMinuteList.add(trafficBaiduFiveMinute);
        }
        return trafficBaiduFiveMinuteList;
    }

    /**
     * 获取拥堵次数
     *
     * @param tpis 元数据
     * @return 拥堵次数
     */
    private int getCongestionTimes(List<Double> tpis) {
        int congestionTimes = 0;
        boolean lastCongestion = false;
        for (Double tpi : tpis) {
            if (!lastCongestion && tpi > JamLevelEnum.SLOW.getValue()) {
                congestionTimes++;
                lastCongestion = true;
            } else if (tpi <= JamLevelEnum.SLOW.getValue()) {
                lastCongestion = false;
            }
        }
        return congestionTimes;
    }

    /**
     * 获取节假日key
     *
     * @param year
     * @return
     */
    private static String getHolidayKey(Integer year) {
        Objects.requireNonNull(year);
        return HOLIDAY_KEY_PREFIX + "_" + year;
    }
}
