package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.AdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.ProvinceVO;
import net.cdsunrise.hy.lyyt.domain.vo.*;
import net.cdsunrise.hy.lyyt.entity.vo.*;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.es.entity.HotelBaseInfo;
import net.cdsunrise.hy.lyyt.es.entity.HotelInventory;
import net.cdsunrise.hy.lyyt.es.entity.TouristMinuteLocalData;
import net.cdsunrise.hy.lyyt.es.entity.TouristPassengerTicket;
import net.cdsunrise.hy.lyyt.es.mapper.HotelBaseInfoMapper;
import net.cdsunrise.hy.lyyt.es.mapper.HotelInventoryMapper;
import net.cdsunrise.hy.lyyt.es.mapper.TouristMinuteLocalMapper;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import net.cdsunrise.hy.lyyt.service.IndustrialMonitorService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import net.cdsunrise.hy.lyyt.utils.SortUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.InternalSimpleValue;
import org.elasticsearch.search.aggregations.pipeline.bucketscript.BucketScriptPipelineAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LHY
 * @date 2019/11/5 14:36
 */
@Service
@Slf4j
public class IndustrialMonitorServiceImpl implements IndustrialMonitorService {

    private static final String HOLIDAY_KEY_PREFIX = "CONFIG_HOLIDAY";
    private static final String COUNTY_BUSINESS_CIRCLE = "县城区域商圈";

    @Value("${hy.today-weight}")
    private Integer todayWeight;

    private ElasticsearchTemplate elasticsearchTemplate;
    private AdministrativeAreaFeignClient feignClient;
    private TouristMinuteLocalMapper touristMinuteLocalMapper;
    private HotelBaseInfoMapper hotelBaseInfoMapper;
    private HotelInventoryMapper hotelInventoryMapper;

    public IndustrialMonitorServiceImpl(ElasticsearchTemplate elasticsearchTemplate, AdministrativeAreaFeignClient feignClient, TouristMinuteLocalMapper touristMinuteLocalMapper, HotelBaseInfoMapper hotelBaseInfoMapper, HotelInventoryMapper hotelInventoryMapper) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.feignClient = feignClient;
        this.touristMinuteLocalMapper = touristMinuteLocalMapper;
        this.hotelBaseInfoMapper = hotelBaseInfoMapper;
        this.hotelInventoryMapper = hotelInventoryMapper;
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

    @Override
    public Map<String, Object> touristAnalyze() {
        String indexName = "tourist_local_data";
        String indexType = "doc";
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        String start = year + "-01-01";
        String end = year + "-12-31";
        Integer lastYear = year - 1;
        String lastStart = lastYear + "-01-01";
        String lastEnd = lastYear + "-12-31";
        Map<String, Object> resultMap = new HashMap<>();
        // 查询县域游客接待走势
        // 今年洪雅县完整游客数据
        List<TouristLocalData> touristDataList = commonQuery(start, end, indexName, indexType);
        // 去年洪雅县完整游客数据
        List<TouristLocalData> lastTouristDataList = commonQuery(lastStart, lastEnd, indexName, indexType);
        resultMap.put("touristDataList", buildYearData(year, touristDataList, null));
        resultMap.put("lastTouristDataList", buildYearData(lastYear, lastTouristDataList, null));
        // 查看景区游客排行
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "month"));
        // 由于1月份时，还未产生当月数据，因此排名就做成”查询去年整年数据，实现复盘效果“
        Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month == 1) {
            resultMap.put("year", lastYear);
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(lastStart).lte(lastEnd));
        } else {
            resultMap.put("year", year);
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        }
        resultMap.put("touristRank", aggregation(indexName, indexType, boolQueryBuilder, "scenicName", "peopleNum"));
        return resultMap;
    }

    private List<TouristLocalData> commonQuery(String start, String end, String indexName, String indexType) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "month"));
        NativeSearchQuery lastMonthQuery = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).build();
        return elasticsearchTemplate.queryForList(lastMonthQuery, TouristLocalData.class);
    }

    private List<ScenicTouristVO> buildYearData(Integer year, List<TouristLocalData> touristList, List<ScenicTouristVO> scenicList) {
        Map<String, Object> paramMap = new HashMap<>();
        String[] monthArr = {year + "-01", year + "-02", year + "-03", year + "-04", year + "-05", year + "-06", year + "-07", year + "-08", year + "-09", year + "-10", year + "-11", year + "-12"};
        List<ScenicTouristVO> dataList = new ArrayList<>();
        if (touristList != null) {
            touristList.forEach(touristLocalData -> {
                paramMap.put(touristLocalData.getTime(), touristLocalData.getPeopleNum());
            });
        }
        if (scenicList != null) {
            scenicList.forEach(scenicTouristVO -> {
                paramMap.put(scenicTouristVO.getName(), scenicTouristVO.getValue());
            });
        }
        for (String month : monthArr) {
            ScenicTouristVO scenicTouristVO;
            if (paramMap.containsKey(month)) {
                Double peopleNum = Double.parseDouble(String.valueOf(paramMap.get(month)));
                BigDecimal bg = new BigDecimal(peopleNum + "");
                Double v = bg.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                scenicTouristVO = new ScenicTouristVO(month, v);
            } else {
                scenicTouristVO = new ScenicTouristVO(month, 0.0);
            }
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    @Override
    public Map<String, Object> touristSourceCity() {
        Map<String, Object> resultMap = new HashMap<>();
        String indexType = "doc";
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        String start = year + "-01-01";
        String end = year + "-12-31";
        resultMap.put("year", year);
        // 由于1月份时，还未产生当月数据，因此排名就做成”查询去年整年数据，实现复盘效果“
        Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month == 1) {
            Integer lastYear = year - 1;
            start = lastYear + "-01-01";
            end = lastYear + "-12-31";
            resultMap.put("year", lastYear);
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 省内
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termsQuery("provName", "四川"))
                .must(QueryBuilders.termQuery("flag", "month"));
        resultMap.put("innerProvince", aggregation("tourist_source_city", indexType, boolQueryBuilder, "cityName", "peopleNum"));
        // 省外
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termsQuery("countryName", "中国"))
                .mustNot(QueryBuilders.termsQuery("provName", "四川"))
                .must(QueryBuilders.termQuery("flag", "month"));
        resultMap.put("outerProvince", aggregation("tourist_source_prov", indexType, boolQueryBuilder, "provName", "peopleNum"));
        // 境外
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .mustNot(QueryBuilders.termsQuery("countryName", "中国"))
                .must(QueryBuilders.termQuery("flag", "month"));
        resultMap.put("overseas", aggregation("tourist_source_country", indexType, boolQueryBuilder, "countryName", "peopleNum"));
        // 今年洪雅县接待游客总数
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "month"));
        resultMap.put("touristCount", aggregation("tourist_local_data", indexType, boolQueryBuilder, "scenicName", "peopleNum"));
        return resultMap;
    }

    @Override
    public Map<String, Object> latestTouristSourceCity() {
        String indexName = "tourist_source_city";
        String indexType = "doc";
        String yesterday = DateUtil.getYesterday();
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 省内
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termsQuery("provName", "四川"))
                .must(QueryBuilders.termsQuery("time", yesterday));
        NativeSearchQuery innerQuery = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).withSort(SortBuilders.fieldSort("peopleNum").order(SortOrder.DESC)).build();
        List<TouristSourceCityData> innerProvince = elasticsearchTemplate.queryForList(innerQuery, TouristSourceCityData.class);
        resultMap.put("innerProvince", innerProvince);
        // 省外
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .mustNot(QueryBuilders.termsQuery("provName", "四川"))
                .must(QueryBuilders.termsQuery("countryName", "中国"))
                .must(QueryBuilders.termsQuery("time", yesterday));
        NativeSearchQuery outerQuery = new NativeSearchQueryBuilder().withIndices("tourist_source_prov").withTypes(indexType)
                .withQuery(boolQueryBuilder).withSort(SortBuilders.fieldSort("peopleNum").order(SortOrder.DESC)).build();
        List<TouristSourceProvData> outerProvince = elasticsearchTemplate.queryForList(outerQuery, TouristSourceProvData.class);
        resultMap.put("outerProvince", outerProvince);
        // 境外
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .mustNot(QueryBuilders.termsQuery("countryName", "中国"))
                .must(QueryBuilders.termsQuery("time", yesterday));
        NativeSearchQuery seasQuery = new NativeSearchQueryBuilder().withIndices("tourist_source_country").withTypes(indexType)
                .withQuery(boolQueryBuilder).withSort(SortBuilders.fieldSort("peopleNum").order(SortOrder.DESC)).build();
        List<TouristSourceCountryData> overseas = elasticsearchTemplate.queryForList(seasQuery, TouristSourceCountryData.class);
        resultMap.put("overseas", overseas);
        return resultMap;
    }

    @Override
    public Map<String, Object> touristMonitor() {
        String indexName = "tourist_local_data";
        String indexType = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 实时洪雅县和各景区游客数
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "minute"));
        NativeSearchQuery realTimeQuery = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).build();
        List<TouristLocalData> realTimeList = elasticsearchTemplate.queryForList(realTimeQuery, TouristLocalData.class);
        // 洪雅县昨日累计游客
        String yesterday = DateUtil.getYesterday();
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("time", yesterday))
                .must(QueryBuilders.termQuery("flag", "day"));
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).build();
        List<TouristLocalData> dataList = elasticsearchTemplate.queryForList(query, TouristLocalData.class);
        Integer yesterdayPeopleNum = dataList.size() > 0 ? dataList.get(0).getPeopleNum() : 0;
        resultMap.put("yesterdayPeopleNum", yesterdayPeopleNum);
        // 今日县域高峰游客数和今日累计游客
        boolQueryBuilder = QueryBuilders.boolQuery();
        String startTime = DateUtil.getToday() + " 00:00:00";
        String endTime = DateUtil.getToday() + " 23:59:59";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "minute"));
        Page<TouristMinuteLocalData> page = touristMinuteLocalMapper.search(boolQueryBuilder, PageRequest.of(0, 5000, Sort.by(Sort.Direction.ASC, "time")));
        List<TouristMinuteLocalData> content = page.getContent();
        if (CollectionUtils.isEmpty(content)) {
            resultMap.put("maxPeopleNum", 0);
            resultMap.put("todayTotalPeopleNum", 0);
        } else {
            Map<String, Integer> map = new HashMap<>();
            String hyx = "洪雅县";
            setMap(map, content, hyx);
            resultMap.put("todayTotalPeopleNum", map.get(hyx + "_todayTotalPeopleNum") * todayWeight);
            resultMap.put("maxPeopleNum", map.get(hyx + "_maxPeopleNum"));
        }

        // 计算今日累计: 主城区, 柳江古镇, 七里坪, 槽渔滩, 主城区
        // 计算在园: 瓦屋山, 玉屏山
        List<String> calculateTodayTotal = new ArrayList<>(Arrays.asList("主城区", "柳江古镇", "七里坪", "槽渔滩", "瓦屋山", "玉屏山"));
        List<String> calculatePeopleInPark = new ArrayList<>(Arrays.asList("瓦屋山", "玉屏山"));
        List<TouristLocalDataVO> touristLocalDataVOS = new ArrayList<>();
        realTimeList.forEach(touristLocalData -> {
            Map<String, Integer> map = new HashMap<>(16);
            TouristLocalDataVO vo = new TouristLocalDataVO();
            BeanUtils.copyProperties(touristLocalData, vo);
            String scenicName = touristLocalData.getScenicName();
            if (calculateTodayTotal.contains(scenicName)) {
                setMap(map, content, scenicName);
                vo.setTodayTotalPeopleNum(map.get(scenicName + "_todayTotalPeopleNum") * todayWeight);
            }
            if (calculatePeopleInPark.contains(scenicName)) {
                calculatePeopleInPark(scenicName, vo);
            }
            touristLocalDataVOS.add(vo);
        });

        resultMap.put("realTimeList", touristLocalDataVOS);
//        resultMap.put("peopleInPark", getPeopleInPark());
        return resultMap;
    }

    /**
     * 查询最新瓦屋山丶玉屏山的在园游客数
     *
     * @return Map
     */
    @SuppressWarnings("Duplicates")
    private PeopleInParkVO getPeopleInPark() {
        log.info("查询最新瓦屋山丶玉屏山的在园游客数");
        PeopleInParkVO peopleInParkVO = new PeopleInParkVO();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket").withTypes("doc").withQuery(QueryBuilders.matchAllQuery());
        String[] scenicNames = {"玉屏山", "瓦屋山"};
        for (String scenicName : scenicNames) {
            searchQueryBuilder.withQuery(QueryBuilders.termQuery("scenicName", scenicName))
                    .withPageable(PageRequest.of(0, 1))
                    .withSort(SortBuilders.fieldSort("responseTime").order(SortOrder.DESC));
            List<TouristPassengerTicket> list = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristPassengerTicket.class);
            int peopleInPark = 0;
            int peopleToday = 0;
            if (list.size() > 0) {
                TouristPassengerTicket ticket = list.get(0);
                peopleInPark = ticket.getRealTimeTouristNum();
                peopleToday = ticket.getTodayTouristCount();
            }
            if (scenicName.equals(scenicNames[0])) {
                peopleInParkVO.setYpsRealCount(peopleInPark);
                peopleInParkVO.setYpsTodayCount(peopleToday);
            } else if (scenicName.equals(scenicNames[1])) {
                peopleInParkVO.setWwsRealCount(peopleInPark);
                peopleInParkVO.setWwsTodayCount(peopleToday);
            }
        }
        return peopleInParkVO;
    }

    @SuppressWarnings("Duplicates")
    private void calculatePeopleInPark(String scenicName, TouristLocalDataVO vo) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket").withTypes("doc")
                .withQuery(QueryBuilders.termQuery("scenicName", scenicName))
                .withPageable(PageRequest.of(0, 1))
                .withSort(SortBuilders.fieldSort("responseTime").order(SortOrder.DESC));
        List<TouristPassengerTicket> list = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristPassengerTicket.class);
        int peopleInPark = 0;
        int peopleToday = 0;
        if (list.size() > 0) {
            TouristPassengerTicket ticket = list.get(0);
            peopleInPark = ticket.getRealTimeTouristNum();
            peopleToday = ticket.getTodayTouristCount();
        }
        vo.setPeopleInPark(peopleInPark);
        vo.setTodayTotalPeopleInPark(peopleToday);

        // 20201019 09:57 应对检查, 临时展示修改
        // vo.setTodayTotalPeopleInPark(65);
        // vo.setPeopleInPark(53);
    }

    @Override
    public List<ScenicTouristVO> publicSentimentKeyword() {
        List<ScenicTouristVO> dataList = new ArrayList<>();
        // 近30天
        String startTime = DateUtil.format(DateUtil.getTime("day", new Date(), -29), DateUtil.PATTERN_YYYY_MM_DD);
        String endTime = DateUtil.getToday();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("adddate").gte(startTime).lte(endTime));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("message").field("content").size(20);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("reptile").withTypes("hy")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("message");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            Long value = bucket.getDocCount();
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(bucket.getKeyAsString(), value.doubleValue());
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    private void setMap(Map<String, Integer> map, List<TouristMinuteLocalData> content, String key) {
        List<TouristMinuteLocalData> list = content.stream().filter(item -> key.equals(item.getScenicName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            map.put(key + "_maxPeopleNum", 0);
            map.put(key + "_todayTotalPeopleNum", 0);
            return;
        }
        int maxPeopleNum = list.get(0).getPeopleNum();
        int todayTotalPeopleNum = list.get(0).getPeopleNum();
        for (int i = 1; i < list.size(); i++) {
            int c = list.get(i).getPeopleNum() - list.get(i - 1).getPeopleNum();
            todayTotalPeopleNum += Math.max(c, 0);
            maxPeopleNum = Math.max(maxPeopleNum, list.get(i).getPeopleNum());
        }
        map.put(key + "_maxPeopleNum", maxPeopleNum);
        map.put(key + "_todayTotalPeopleNum", todayTotalPeopleNum);
    }

    @Override
    public Map<String, Object> holidayTouristSourceCity(HolidayTypeEnum holiday) {
        String indexName = "tourist_source_city";
        String indexType = "doc";
        // 从枚举中获取节假日起始时间
        int year = LocalDate.now().getYear();
        HolidayVO yearHoliday = fetchHolidayData(holiday, year);
        Date startDate = new Date(yearHoliday.getStartDate());
        Date endDate = new Date(yearHoliday.getEndDate());
        String start = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String end = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termsQuery("countryName", "中国"))
                .must(QueryBuilders.termQuery("flag", "day"));
        resultMap.put("sourceCityTop", aggregation(indexName, indexType, boolQueryBuilder, "cityName", "peopleNum"));
        return resultMap;
    }

    private HolidayVO fetchHolidayData(HolidayTypeEnum holiday, int thisYear) {
        String redisData = RedisUtil.hashOperations().get(getHolidayKey(thisYear), holiday.getShortening());
        return JsonUtils.toObject(redisData, new TypeReference<HolidayVO>() {
        });
    }

    @Override
    public Map<String, Object> holidayTouristAgeSex(HolidayTypeEnum holiday) {
        Map<String, Object> resultMap = new HashMap<>();
        List<ScenicTouristVO> dataList = new ArrayList<>();
        // 从枚举中获取节假日起始时间
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;
        HolidayVO thisYearHoliday = fetchHolidayData(holiday, thisYear);
        HolidayVO lastYearHoliday = fetchHolidayData(holiday, lastYear);
        Date startDate = new Date(thisYearHoliday.getStartDate());
        Date endDate = new Date(thisYearHoliday.getEndDate());
        Date lastStartDate = new Date(lastYearHoliday.getStartDate());
        Date lastEndDate = new Date(lastYearHoliday.getEndDate());
        String start = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String end = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastStart = DateUtil.format(lastStartDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastEnd = DateUtil.format(lastEndDate, DateUtil.PATTERN_YYYY_MM_DD);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(start).lte(end));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("age_agg")
                .field("age");
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        LongTerms longTerms = temp.get("age_agg");
        // 获得所有的桶
        List<LongTerms.Bucket> buckets = longTerms.getBuckets();
        for (LongTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Double value = Double.parseDouble(String.valueOf(bucket.getDocCount()));
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(name, value);
            dataList.add(scenicTouristVO);
        }
        // 年龄扇形图（单独查询不属于“50后-00后”范围的人数）
        List<ScenicTouristVO> ageList = buildAgeData(dataList);
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
                .filter(QueryBuilders.rangeQuery("checkinTime").gte(start).lte(end))
                .filter(QueryBuilders.rangeQuery("age").gte(0).lt(50));
        NativeSearchQuery ageQuery = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        ageList.add(new ScenicTouristVO("其他", Double.parseDouble(String.valueOf(elasticsearchTemplate.count(ageQuery)))));
        resultMap.put("ageChart", ageList);
        // 洪雅县游客接待数
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        SumAggregationBuilder aggregationBuilder = AggregationBuilders.sum("people_count").field("peopleNum");
        resultMap.put("peopleCount", sumAggregation("tourist_local_data", "doc", boolQueryBuilder, aggregationBuilder));
        // 统计性别变化
        Double male = commonQuerySex(start, end, "男");
        Double lastMale = commonQuerySex(lastStart, lastEnd, "男");
        Double female = commonQuerySex(start, end, "女");
        Double lastFemale = commonQuerySex(lastStart, lastEnd, "女");
        resultMap.put("maleCount", male);
        resultMap.put("femaleCount", female);
        if (lastMale > 0 && lastFemale > 0) {
            // 同比增长或下降，保留两位小数
            Double maleDiff = male - lastMale;
            Double compareMale = new BigDecimal(maleDiff * 100 / lastMale)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            Double femaleDiff = female - lastFemale;
            Double compareFemale = new BigDecimal(femaleDiff * 100 / lastFemale)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareMale", compareMale);
            resultMap.put("compareFemale", compareFemale);
        } else {
            resultMap.put("compareMale", "");
            resultMap.put("compareFemale", "");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> holidayScenicTourist(HolidayTypeEnum holiday) {
        String indexName = "tourist_local_data";
        String indexType = "doc";
        // 从枚举中获取节假日起始时间
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;
        HolidayVO thisYearHoliday = fetchHolidayData(holiday, thisYear);
        HolidayVO lastYearHoliday = fetchHolidayData(holiday, lastYear);
        Date startDate = new Date(thisYearHoliday.getStartDate());
        Date endDate = new Date(thisYearHoliday.getEndDate());
        Date lastStartDate = new Date(lastYearHoliday.getStartDate());
        Date lastEndDate = new Date(lastYearHoliday.getEndDate());
        String start = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String end = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastStart = DateUtil.format(lastStartDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastEnd = DateUtil.format(lastEndDate, DateUtil.PATTERN_YYYY_MM_DD);
        Map<String, Object> resultMap = new HashMap<>();
        String[] scenicArr = {"主城区", "瓦屋山", "七里坪", "柳江古镇", "玉屏山", "槽渔滩"};
        // 查看景区游客排行
        // 今年景区游客接待数
        resultMap.put("scenicList", buildHolidayData(holidayCommonQuery(start, end, indexName, indexType), scenicArr));
        // 去年景区游客接待数
        resultMap.put("lastScenicList", buildHolidayData(holidayCommonQuery(lastStart, lastEnd, indexName, indexType), scenicArr));
        // 洪雅县游客接待数
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        List<ScenicTouristVO> touristCountList = commonTouristCount(start, end);
        resultMap.put("touristCount", touristCountList);
        // 同比变化
        List<ScenicTouristVO> lastTouristCountList = commonTouristCount(lastStart, lastEnd);
        Double compareTransAt = 0.0;
        if (touristCountList.size() > 0 && lastTouristCountList.size() > 0) {
            Double touristCount = touristCountList.get(0).getValue();
            Double lastTouristCount = lastTouristCountList.get(0).getValue();
            if (lastTouristCount > 0) {
                // 同比增长或下降，保留两位小数
                Double diff = touristCount - lastTouristCount;
                compareTransAt = new BigDecimal(diff * 100 / lastTouristCount)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }
            resultMap.put("compareTransAt", compareTransAt);
        } else {
            resultMap.put("compareTransAt", "");
        }
        return resultMap;
    }

    private List<ScenicTouristVO> commonTouristCount(String start, String end) {
        String indexName = "tourist_local_data";
        String indexType = "doc";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        return aggregation(indexName, indexType, boolQueryBuilder, "scenicName", "peopleNum");
    }

    /**
     * 酒店所有维度数据统计，都必须基于hotel_base_info中的酒店
     */
    private List<String> commonBaseHotelStationIdList() {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_base_info").withTypes("doc")
                .withPageable(PageRequest.of(0, 10000));
        List<HotelBaseInfo> list = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelBaseInfo.class);
        return list.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> hotelAnalyze() {
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        // 年度入住游客来源地top10（除以1.5减少部分系数）
        List<ScenicTouristVO> hotelSourceList = aggregation("hotel_tourist_source", "doc", boolQueryBuilder, "cityName", "cumulativeReception");
        resultMap.put("touristSourceTop", magicTransfer(hotelSourceList));
        // 酒店基础数据
        List<String> stationIdList = commonBaseHotelStationIdList();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("diy_hotel_reception").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        // 年度酒店累计接待人数top5
        List<ScenicTouristVO> hotelList = aggregation("diy_hotel_reception", "doc", boolQueryBuilder, "stationId", "cumulativeReception");
        hotelList.forEach(scenicTouristVO -> {
            Optional<HotelBaseInfo> optional = hotelBaseInfoMapper.findById(scenicTouristVO.getName());
            if (optional.isPresent()) {
                scenicTouristVO.setName(optional.get().getName());
            } else {
                scenicTouristVO.setName("其它");
            }
        });
        resultMap.put("hotelReceptionTop", hotelList);
        // 年度累计入住人数
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_count").field("cumulativeReception");
        query.addAggregation(sumAggregationBuilder);
        double cumulativeReception = elasticsearchTemplate.query(query, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return sum.getValue();
        });
        resultMap.put("cumulativeReception", cumulativeReception);
        return resultMap;
    }

    @Override
    public Map<String, Object> hotelTouristSource() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("inner", provTouristSource(true));
        resultMap.put("outer", provTouristSource(false));
        return resultMap;
    }

    @Override
    public Map<String, Object> touristSourceStatistics(TouristStatisticsRequest request) {
        String type = request.getType();
        LocalDateTime beginDate = request.getBeginDate();
        LocalDateTime endDate = request.getEndDate();
        if (TouristStatisticsRequest.TYPE_DAY.equals(type)) {
            return dayTouristSourceStatistics(beginDate, endDate);
        } else if (TouristStatisticsRequest.TYPE_MONTH.equals(type)) {
            return monthTouristSourceStatistics(beginDate, endDate);
        } else {
            throw BusinessException.fail(BusinessExceptionEnum.TYPE_ERROR).get();
        }
    }

    @Override
    public List<NameAndValueVO<Integer>> getScenicHeat(TouristPortraitReq req) {
        LocalDate beginDate = req.getBeginDate();
        LocalDate endDate = req.getEndDate();
        String beginDateString = beginDate.format(DateUtil.LOCAL_DATE);
        String endDateString = endDate.format(DateUtil.LOCAL_DATE);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "day"))
                .mustNot(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(
                        QueryBuilders.rangeQuery("time")
                                .gte(beginDateString)
                                .lte(endDateString)
                );
        return getTermsPeopleNumTop("tourist_local_data", boolQueryBuilder, "scenicName", 10);
    }

    /**
     * 获取分组游客数TOP
     *
     * @param indexName        索引名称
     * @param boolQueryBuilder 查询条件
     * @param termsField       分组字段名
     * @return 结果
     */
    private List<NameAndValueVO<Integer>> getTermsPeopleNumTop(String indexName, BoolQueryBuilder boolQueryBuilder, String termsField, int size) {
        SumAggregationBuilder peopleNumSumAggBuilder = AggregationBuilders.sum("peopleNum_sum").field("peopleNum");
        TermsAggregationBuilder termsAggBuilder = AggregationBuilders.terms("terms").field(termsField)
                .size(size)
                .order(BucketOrder.aggregation("peopleNum_sum", false))
                .subAggregation(peopleNumSumAggBuilder);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withIndices(indexName).withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(termsAggBuilder);
        return elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            StringTerms termsAgg = aggregations.get("terms");
            List<StringTerms.Bucket> buckets = termsAgg.getBuckets();
            return buckets.stream().map(bucket -> {
                String key = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                InternalSum peopleNumSumAgg = bucketAggregations.get("peopleNum_sum");
                double value = peopleNumSumAgg.getValue();
                return NameAndValueVO.create(key, Double.valueOf(value).intValue());
            }).collect(Collectors.toList());
        });
    }

    private Map<String, Object> dayTouristSourceStatistics(LocalDateTime beginDate, LocalDateTime endDate) {
        String beginDateString = beginDate.format(DateUtil.LOCAL_DATE);
        String endDateString = endDate.format(DateUtil.LOCAL_DATE);
        Map<String, Object> resultMap = new HashMap<>(6);
        String flag = "day";
        // 省内
        innerProv(flag, beginDateString, endDateString, resultMap);
        // 省外
        outerProv(flag, beginDateString, endDateString, resultMap);
        // 境外
        overseas(flag, beginDateString, endDateString, resultMap);

        return resultMap;
    }

    private Map<String, Object> monthTouristSourceStatistics(LocalDateTime beginDate, LocalDateTime endDate) {
        String beginDateString = beginDate.format(DateUtil.LOCAL_MONTH);
        String endDateString = endDate.format(DateUtil.LOCAL_MONTH);
        Map<String, Object> resultMap = new HashMap<>(6);
        String flag = "month";
        // 省内
        innerProv(flag, beginDateString, endDateString, resultMap);
        // 省外
        outerProv(flag, beginDateString, endDateString, resultMap);
        // 境外
        overseas(flag, beginDateString, endDateString, resultMap);

        return resultMap;
    }

    private void innerProv(String flag, String beginDateString, String endDateString, Map<String, Object> resultMap) {
        String indexName = "tourist_source_city";
        String termsFiled = "cityName";
        String totalFieldName = "innerProvPeopleNum";
        String top10FieldName = "innerProvTop10";
        QueryBuilder commonInnerProvQueryBuilder = getTouristSourceCommonInnerProvQueryBuilder(flag, beginDateString, endDateString);
        SearchQuery commonSearchQuery = getTouristSourceCommonSearchQuery(indexName, commonInnerProvQueryBuilder, termsFiled);
        ResultsExtractor<Object> commonResultExtractor = getTouristSourceCommonResultExtractor(totalFieldName, top10FieldName, resultMap);
        elasticsearchTemplate.query(commonSearchQuery, commonResultExtractor);
    }

    private void outerProv(String flag, String beginDateString, String endDateString, Map<String, Object> resultMap) {
        String indexName = "tourist_source_prov";
        String termsFiled = "provName";
        String totalFieldName = "outerProvPeopleNum";
        String top10FieldName = "outerProvTop10";
        QueryBuilder commonOuterProvQueryBuilder = getTouristSourceCommonOuterProvQueryBuilder(flag, beginDateString, endDateString);
        SearchQuery commonSearchQuery = getTouristSourceCommonSearchQuery(indexName, commonOuterProvQueryBuilder, termsFiled);
        ResultsExtractor<Object> commonResultExtractor = getTouristSourceCommonResultExtractor(totalFieldName, top10FieldName, resultMap);
        elasticsearchTemplate.query(commonSearchQuery, commonResultExtractor);
    }

    private void overseas(String flag, String beginDateString, String endDateString, Map<String, Object> resultMap) {
        String indexName = "tourist_source_country";
        String termsFiled = "countryName";
        String totalFieldName = "overseasPeopleNum";
        String top10FieldName = "overseasTop10";
        QueryBuilder commonOverseasQueryBuilder = getTouristSourceCommonOverseasQueryBuilder(flag, beginDateString, endDateString);
        SearchQuery commonSearchQuery = getTouristSourceCommonSearchQuery(indexName, commonOverseasQueryBuilder, termsFiled);
        ResultsExtractor<Object> commonResultExtractor = getTouristSourceCommonResultExtractor(totalFieldName, top10FieldName, resultMap);
        elasticsearchTemplate.query(commonSearchQuery, commonResultExtractor);
    }

    private QueryBuilder getTouristSourceCommonInnerProvQueryBuilder(String flag, Object beginDateString, Object endDateString) {
        BoolQueryBuilder queryBuilder = getTouristSourceCommonQueryBuilder(flag, beginDateString, endDateString);
        queryBuilder.must(QueryBuilders.termQuery("provName", "四川"));
        return queryBuilder;
    }

    private QueryBuilder getTouristSourceCommonOuterProvQueryBuilder(String flag, Object beginDateString, Object endDateString) {
        BoolQueryBuilder queryBuilder = getTouristSourceCommonQueryBuilder(flag, beginDateString, endDateString);
        queryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川"));
        return queryBuilder;
    }

    private QueryBuilder getTouristSourceCommonOverseasQueryBuilder(String flag, Object beginDateString, Object endDateString) {
        BoolQueryBuilder queryBuilder = getTouristSourceCommonQueryBuilder(flag, beginDateString, endDateString);
        queryBuilder.mustNot(QueryBuilders.termQuery("countryName", "中国"));
        return queryBuilder;
    }

    private BoolQueryBuilder getTouristSourceCommonQueryBuilder(String flag, Object beginDateString, Object endDateString) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDateString).lte(endDateString))
                .must(QueryBuilders.termQuery("flag", flag))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        return queryBuilder;
    }

    private SearchQuery getTouristSourceCommonSearchQuery(String indexName, QueryBuilder queryBuilder, String termsFiled) {
        SumAggregationBuilder peopleNumSumAggregationBuilder = AggregationBuilders.sum("peopleNum_sum")
                .field("peopleNum");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("terms")
                .field(termsFiled)
                .size(10)
                .subAggregation(peopleNumSumAggregationBuilder)
                .order(BucketOrder.aggregation("peopleNum_sum", false));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices(indexName).withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(peopleNumSumAggregationBuilder)
                .addAggregation(termsAggregationBuilder);
        return searchQueryBuilder.build();
    }

    private <T> ResultsExtractor<T> getTouristSourceCommonResultExtractor(String totalFieldName, String top10FieldName, Map<String, Object> resultMap) {
        return response -> {
            Aggregations aggregations = response.getAggregations();
            // 人数汇总
            InternalSum peopleNumSum = aggregations.get("peopleNum_sum");
            resultMap.put(totalFieldName, Double.valueOf(peopleNumSum.getValue()).intValue());
            // 排行TOP10
            List<NameAndValueVO<Integer>> top10 = new ArrayList<>(10);
            StringTerms terms = aggregations.get("terms");
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                String key = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                InternalSum bucketPeopleNumSum = bucketAggregations.get("peopleNum_sum");
                top10.add(NameAndValueVO.create(key, Double.valueOf(bucketPeopleNumSum.getValue()).intValue()));
            }
            resultMap.put(top10FieldName, top10);
            return null;
        };
    }

    /**
     * @param flag：true，代表省内；false，代表省外
     * @return
     */
    private List<ScenicTouristVO> provTouristSource(boolean flag) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        List<ScenicTouristVO> hotelSourceList;
        if (flag) {
            boolQueryBuilder.must(QueryBuilders.termQuery("provName", "四川省"));
            hotelSourceList = aggregation("hotel_tourist_source", "doc", boolQueryBuilder, "cityName", "cumulativeReception");
        } else {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川省"));
            hotelSourceList = aggregation("hotel_tourist_source", "doc", boolQueryBuilder, "provName", "cumulativeReception");
        }
        return magicTransfer(hotelSourceList);
    }

    /**
     * （除以1.5减少部分系数）
     *
     * @param hotelSourceList
     * @return
     */
    private List<ScenicTouristVO> magicTransfer(List<ScenicTouristVO> hotelSourceList) {
        hotelSourceList.forEach(scenicTouristVO -> {
            Double originCount = scenicTouristVO.getValue();
            BigDecimal bg = new BigDecimal(originCount);
            scenicTouristVO.setValue(bg.divide(new BigDecimal(1.5), 0, BigDecimal.ROUND_HALF_UP).doubleValue());
        });
        return hotelSourceList;
    }

    @Override
    public Map<String, Object> stayOvernight() {
        Map<String, Object> resultMap = new HashMap<>();
        List<ScenicTouristVO> dataList = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        // 计算平均过夜天数
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("overnight").gte(0));
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_overnight").field("overnight");
        // 计算总数，包含” 未过夜、1天、2天、3天、4天及以上 “
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("overnight_agg")
                .field("overnight");
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).addAggregation(avgAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        LongTerms longTerms = temp.get("overnight_agg");
        InternalAvg internalAvg = temp.get("avg_overnight");
        // 获得所有的桶
        List<LongTerms.Bucket> buckets = longTerms.getBuckets();
        for (LongTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Double value = Double.parseDouble(String.valueOf(bucket.getDocCount()));
            switch (name) {
                case "0":
                    ScenicTouristVO zero = new ScenicTouristVO("未过夜", value);
                    dataList.add(zero);
                    break;
                case "1":
                    ScenicTouristVO one = new ScenicTouristVO("1天", value);
                    dataList.add(one);
                    break;
                case "2":
                    ScenicTouristVO two = new ScenicTouristVO("2天", value);
                    dataList.add(two);
                    break;
                case "3":
                    ScenicTouristVO three = new ScenicTouristVO("3天", value);
                    dataList.add(three);
                    break;
                default:
                    if (paramMap.containsKey("four")) {
                        Double before = (Double) paramMap.get("four");
                        Double after = value + before;
                        paramMap.put("four", after);
                    } else {
                        paramMap.put("four", value);
                    }
                    break;
            }
        }
        if (paramMap.containsKey("four")) {
            dataList.add(new ScenicTouristVO("4天及以上", (Double) paramMap.get("four")));
        }
        resultMap.put("dataList", dataList);
        BigDecimal bg = new BigDecimal(internalAvg.getValue());
        BigDecimal avgOvernight = bg.divide(new BigDecimal(1), 1, BigDecimal.ROUND_HALF_UP);
        resultMap.put("avgOvernight", avgOvernight);
        return resultMap;
    }

    @Override
    public List<ScenicTouristVO> tourismAge() {
        List<ScenicTouristVO> dataList = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("age_agg")
                .field("age");
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        LongTerms longTerms = temp.get("age_agg");
        // 获得所有的桶
        List<LongTerms.Bucket> buckets = longTerms.getBuckets();
        for (LongTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Double value = Double.parseDouble(String.valueOf(bucket.getDocCount()));
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(name, value);
            dataList.add(scenicTouristVO);
        }
        return buildAgeData(dataList);
    }

    @Override
    public Map<String, Object> tourismSex() {
        Map<String, Object> resultMap = new HashMap<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        resultMap.put("maleCount", commonQuerySex(startTime, endTime, "男"));
        resultMap.put("femaleCount", commonQuerySex(startTime, endTime, "女"));
        return resultMap;
    }

    private Double commonQuerySex(String startTime, String endTime, String sex) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime))
                .must(QueryBuilders.termQuery("sex", sex));
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        return Double.parseDouble(String.valueOf(elasticsearchTemplate.count(query)));
    }

    private List<ScenicTouristVO> buildAgeData(List<ScenicTouristVO> list) {
        Map<String, Object> paramMap = new HashMap<>();
        String[] ageArr = {"50", "60", "70", "80", "90", "0"};
        List<ScenicTouristVO> dataList = new ArrayList<>();
        if (list != null) {
            list.forEach(chartVO -> {
                paramMap.put(chartVO.getName(), chartVO.getValue());
            });
        }
        for (String age : ageArr) {
            String ageFormatted = String.format("%02d", Integer.parseInt(age));
            ScenicTouristVO scenicTouristVO;
            if (paramMap.containsKey(age)) {
                Double peopleNum = (Double) paramMap.get(age);
                scenicTouristVO = new ScenicTouristVO(ageFormatted, peopleNum);
            } else {
                scenicTouristVO = new ScenicTouristVO(ageFormatted, 0.0);
            }
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    @Override
    public List<ScenicTouristVO> businessCircleTourismConsumption() {
        return aggregation("county_business_circle_consumption", "doc", commonYearQuery(), "cbdName", "transAt");
    }

    @Override
    public Map<String, Object> tourismConsumptionAnalyze() {
        Map<String, Object> resultMap = new HashMap<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int lastYear = year - 1;
        resultMap.put("tourismConsumption", commonTourismConsumption(year));
        resultMap.put("lastTourismConsumption", commonTourismConsumption(lastYear));
        return resultMap;
    }

    private List<ScenicTouristVO> commonTourismConsumption(int year) {
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ScenicTouristVO> dataList = new ArrayList<>();
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("transAt_agg").field("transAt");
        DateHistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_histogram")
                .field("dealDay")
                .dateHistogramInterval(DateHistogramInterval.MONTH).format("yyyy-MM")
                .subAggregation(sumAggregationBuilder);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("county_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(histogramAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        InternalDateHistogram dateHistogram = temp.get("time_histogram");
        // 获得所有的桶
        List<InternalDateHistogram.Bucket> buckets = dateHistogram.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (InternalDateHistogram.Bucket bucket : buckets) {
            // 聚合日期列表
            String month = bucket.getKeyAsString();
            InternalSum internalSum = (InternalSum) bucket.getAggregations().asMap().get("transAt_agg");
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(month, decimalTransfer(String.valueOf(internalSum.getValue()), "1"));
            dataList.add(scenicTouristVO);
        }
        return buildYearData(year, null, dataList);
    }

    @Override
    public List<ScenicTouristVO> industryConsumptionType() {
        return aggregation("county_industry_consumption", "doc", commonYearQuery(), "type", "transAt");
    }

    private BoolQueryBuilder commonYearQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        return boolQueryBuilder;
    }

    @Override
    public Map<String, Object> perConsumptionCity() {
        Map<String, Object> resultMap = new HashMap<>();
        List<BusinessCircleVO> list = perConsumptionAggregation(commonYearQuery());
        // 按照消费总金额降序排列
        Comparator<? super BusinessCircleVO> comparator = (a, b) -> {
            if (a.getTransAt() > b.getTransAt()) {
                return -1;
            } else if (a.getTransAt() < b.getTransAt()) {
                return 1;
            } else {
                return 0;
            }
        };
        // 客源地人均消费
        resultMap.put("cityConsumption", list.stream().sorted(comparator).collect(Collectors.toList()));
        // 年度旅游总收入
        SumAggregationBuilder aggregationBuilder = AggregationBuilders.sum("transAt_count").field("transAt");
        resultMap.put("transAtYear", sumAggregation("county_consumption", "doc", commonYearQuery(), aggregationBuilder));
        return resultMap;
    }

    @Override
    public Map<String, Object> hotelPassengerReception() {
        Map<String, Object> resultMap = new HashMap<>();
        // 常规统计
        resultMap.put("normal", normalHotelPassengerReception());
        // 针对重点酒店统计
        resultMap.put("focus", focusHotelPassengerReception());

        return resultMap;
    }

    private Map<String, Object> focusHotelPassengerReception() {
        Map<String, Object> resultMap = new HashMap<>();
        // 酒店数
        int hotelCount = 0;
        // 床位数目
        int bedNumCount = 0;
        // 实时入住人数
        int realTimeCheckInCount = 0;
        // 入住率
        BigDecimal estimateOccupancy = BigDecimal.ZERO;
        // 区域床位总数+实时入住人数柱状图
        List<HotelAreaVO> areaChart = new ArrayList<>();

        QueryBuilder hotelBaseQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("isFocus", true));
        NativeSearchQueryBuilder hotelBaseSearchQueryBuilder = new NativeSearchQueryBuilder();
        hotelBaseSearchQueryBuilder.withIndices("hotel_base_info").withTypes("doc")
                .withQuery(hotelBaseQueryBuilder)
                .withPageable(PageRequest.of(0, 10000));
        List<HotelBaseInfo> hotelBaseInfoList = elasticsearchTemplate.queryForList(hotelBaseSearchQueryBuilder.build(), HotelBaseInfo.class);
        if (!CollectionUtils.isEmpty(hotelBaseInfoList)) {
            // 酒店编码列表
            List<String> stationIdList = hotelBaseInfoList.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
            // 酒店数
            hotelCount = hotelBaseInfoList.size();
            // 床位数目
            bedNumCount = hotelBaseInfoList.stream().mapToInt(HotelBaseInfo::getBedNum).sum();
            // 实时入住人数
            realTimeCheckInCount = getRealTimeCheckInCount(stationIdList);
            // 入住率
            estimateOccupancy = BigDecimal.valueOf(realTimeCheckInCount).divide(BigDecimal.valueOf(bedNumCount), 4, RoundingMode.HALF_UP);
            if (estimateOccupancy.compareTo(BigDecimal.ONE) > 0) {
                // 入住率不超过100%
                estimateOccupancy = BigDecimal.ONE;
            }
            estimateOccupancy = estimateOccupancy.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            // 将酒店信息按商圈分组
            Map<String, List<HotelBaseInfo>> map = hotelBaseInfoList.stream().filter(hotelBaseInfo -> Objects.nonNull(hotelBaseInfo.getBusinessCircle())).collect(Collectors.groupingBy(HotelBaseInfo::getBusinessCircle));
            // 优先处理县城区域商圈
            List<HotelBaseInfo> list = map.get(COUNTY_BUSINESS_CIRCLE);
            if (!CollectionUtils.isEmpty(list)) {
                HotelAreaVO hotelAreaVO = getHotelAreaVO(list, COUNTY_BUSINESS_CIRCLE);
                areaChart.add(hotelAreaVO);
            }
            for (Map.Entry<String, List<HotelBaseInfo>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (COUNTY_BUSINESS_CIRCLE.equals(key)) {
                    continue;
                }
                List<HotelBaseInfo> dataList = entry.getValue();
                HotelAreaVO hotelAreaVO = getHotelAreaVO(dataList, key);
                areaChart.add(hotelAreaVO);
            }
        }

        resultMap.put("hotelCount", hotelCount);
        resultMap.put("bedNumCount", bedNumCount);
        resultMap.put("estimateOccupancy", estimateOccupancy.toString() + "%");
        resultMap.put("realTimeCheckInCount", realTimeCheckInCount);

        // 对areaChart按照名称排序
        resultMap.put("areaChart", sortedByAreaChartName(areaChart));
        return resultMap;
    }

    private HotelAreaVO getHotelAreaVO(List<HotelBaseInfo> list, String businessCircle) {
        HotelAreaVO hotelAreaVO = new HotelAreaVO();
        hotelAreaVO.setName(businessCircle);
        hotelAreaVO.setBedNum(list.stream().mapToInt(HotelBaseInfo::getBedNum).sum());
        hotelAreaVO.setCheckInNum(getRealTimeCheckInCount(list.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList())));
        return hotelAreaVO;
    }

    private int getRealTimeCheckInCount(List<String> stationIdList) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("stationId", stationIdList));
        SumAggregationBuilder checkinNumSumAggBuilder = AggregationBuilders.sum("checkinNum_sum").field("checkinNum");
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_inventory").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(checkinNumSumAggBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            InternalSum checkinNumSumAgg = aggregations.get("checkinNum_sum");
            return Double.valueOf(checkinNumSumAgg.getValue()).intValue();
        });
    }

    private Map<String, Object> normalHotelPassengerReception() {
        String index = "hotel_base_info";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        NativeSearchQuery hotelQuery = (new NativeSearchQueryBuilder()).withIndices(index).withTypes(type)
                .withQuery(boolQueryBuilder).build();
        Long hotelCount = elasticsearchTemplate.count(hotelQuery);
        resultMap.put("hotelCount", hotelCount);
        // 床位数目
        SumAggregationBuilder bedNumAggregationBuilder = AggregationBuilders.sum("bedNum_count").field("bedNum");
        Double bedNumCount = sumAggregation(index, type, boolQueryBuilder, bedNumAggregationBuilder);
        resultMap.put("bedNumCount", bedNumCount);
        // 酒店基础数据
        List<String> stationIdList = commonBaseHotelStationIdList();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        // 入住率
        SumAggregationBuilder checkInAggregationBuilder = AggregationBuilders.sum("checkIn_count").field("checkinNum");
        Double realTimeCheckInCount = sumAggregation("hotel_inventory", "doc", boolQueryBuilder, checkInAggregationBuilder);
        resultMap.put("estimateOccupancy", getPercent(String.valueOf(realTimeCheckInCount), String.valueOf(bedNumCount)));
        // 实时入住人数
        resultMap.put("realTimeCheckInCount", realTimeCheckInCount);
        // 区域床位总数+实时入住人数柱状图
        resultMap.put("areaChart", calculateBedNumAndCheckInNum());
        return resultMap;
    }

    /**
     * 产业运行监测 -> 住宿接待 -> 区域床位总数+实时入住人数
     */
    private List<HotelAreaVO> calculateBedNumAndCheckInNum() {
        List<HotelAreaVO> dataList = new ArrayList<>();
        // 用于存储县城区域商圈，放在第一个
        List<HotelAreaVO> tempList = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        // 查询所有实时入住
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_inventory").withTypes("doc")
                .withPageable(PageRequest.of(0, 10000));
        List<HotelInventory> hotelInventoryList = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelInventory.class);
        Map<String, HotelInventory> hotelInventoryMap = hotelInventoryList.stream().collect(Collectors.toMap(HotelInventory::getStationId, Function.identity()));
        // 区域，实时入住人数
        hotelBaseInfoMapper.findAll().forEach(hotelBaseInfo -> {
            String stationId = hotelBaseInfo.getStationId();
            String businessCircle = hotelBaseInfo.getBusinessCircle();
            HotelInventory hotelInventory = hotelInventoryMap.get(stationId);
            if (hotelInventory != null) {
                Integer checkInNum = hotelInventory.getCheckinNum();
                if (paramMap.containsKey(businessCircle)) {
                    Integer accumulativeCheckInNum = (Integer) paramMap.get(businessCircle);
                    paramMap.put(businessCircle, accumulativeCheckInNum + checkInNum);
                } else {
                    paramMap.put(businessCircle, checkInNum);
                }
            }
        });
        // 区域，床位数累计
        SumAggregationBuilder aggregationBuilder = AggregationBuilders.sum("bed_agg").field("bedNum");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("businessCircle_agg")
                .field("businessCircle")
                .size(100)
                .subAggregation(aggregationBuilder);
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_base_info").withTypes("doc")
                .withQuery(QueryBuilders.boolQuery()).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("businessCircle_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Sum sum = bucket.getAggregations().get("bed_agg");
            HotelAreaVO hotelAreaVO = new HotelAreaVO();
            hotelAreaVO.setName(name);
            hotelAreaVO.setBedNum(Double.valueOf(sum.getValue()).intValue());
            if (paramMap.containsKey(name)) {
                hotelAreaVO.setCheckInNum((Integer) paramMap.get(name));
            } else {
                hotelAreaVO.setCheckInNum(0);
            }
            if (COUNTY_BUSINESS_CIRCLE.equals(name)) {
                tempList.add(hotelAreaVO);
            } else {
                dataList.add(hotelAreaVO);
            }
        }
        // 县城区域商圈第一个展示
        tempList.addAll(dataList);

        // 按照商圈名称排序
        return sortedByAreaChartName(tempList);

    }

    @Override
    public Map<String, Object> tourismConsumptionTrend() {
        String index = "county_consumption";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 旅游收入趋势图，从有数据的那天开始，倒推两周
        String endTime = maxAggregation(index, type, boolQueryBuilder, AggregationBuilders.max("dealDay_max").field("dealDay"));
        String startTime = DateUtil.format(DateUtil.getTime("day", DateUtil.parse(endTime, DateUtil.PATTERN_YYYY_MM_DD), -13), DateUtil.PATTERN_YYYY_MM_DD);
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ScenicTouristVO> trendList = aggregation(index, type, boolQueryBuilder, 20, "dealDay", "transAt");
        resultMap.put("tourismConsumptionTrend", buildMixData(trendList, startTime, endTime));
        // 上周商圈总收入分析，从有数据的那天开始，倒推一周
        startTime = DateUtil.format(DateUtil.getTime("day", DateUtil.parse(endTime, DateUtil.PATTERN_YYYY_MM_DD), -6), DateUtil.PATTERN_YYYY_MM_DD);
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ScenicTouristVO> businessCircleList = aggregation("county_business_circle_consumption", type, boolQueryBuilder, "cbdName", "transAt");
        String[] scenicArr = {"县城区域商圈", "瓦屋山商圈", "七里坪商圈", "柳江商圈", "槽渔滩商圈"};
        resultMap.put("businessCircleList", buildHolidayData(businessCircleList, scenicArr));
        return resultMap;
    }

    @Override
    public Map<String, Object> holidayTourismConsumption(HolidayTypeEnum holiday) {
        Map<String, Object> resultMap = new HashMap<>();
        // 从枚举中获取节假日起始时间
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;
        HolidayVO thisYearHoliday = fetchHolidayData(holiday, thisYear);
        HolidayVO lastYearHoliday = fetchHolidayData(holiday, lastYear);
        Date startDate = new Date(thisYearHoliday.getStartDate());
        Date endDate = new Date(thisYearHoliday.getEndDate());
        Date lastStartDate = new Date(lastYearHoliday.getStartDate());
        Date lastEndDate = new Date(lastYearHoliday.getEndDate());
        String startTime = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String endTime = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastStartTime = DateUtil.format(lastStartDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastEndTime = DateUtil.format(lastEndDate, DateUtil.PATTERN_YYYY_MM_DD);
        // 今年节假日旅游收入
        Double transAtTotal = commonHolidayConsumption(startTime, endTime);
        resultMap.put("transAtTotal", transAtTotal);
        // 去年节假日旅游收入
        Double lastTransAtTotal = commonHolidayConsumption(lastStartTime, lastEndTime);
        Double compareTransAt = 0.0;
        if (lastTransAtTotal > 0) {
            // 同比增长或下降，保留两位小数
            Double diff = transAtTotal - lastTransAtTotal;
            compareTransAt = new BigDecimal(diff * 100 / lastTransAtTotal)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareTransAt", compareTransAt);
        } else {
            resultMap.put("compareTransAt", "");
        }
        // 今年旅游收入走势
        resultMap.put("consumptionTrend", commonHolidayTrend(startTime, endTime));
        // 去年旅游收入走势
        resultMap.put("lastConsumptionTrend", commonHolidayTrend(lastStartTime, lastEndTime));
        // 各行业旅游收入贡献占比
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        String[] industryArr = {"餐饮", "住宿", "购物", "娱乐", "景区", "交通", "旅游综合服务", "其他"};
        resultMap.put("industryConsumption", buildHolidayData(aggregation("county_industry_consumption", "doc", boolQueryBuilder, "type", "transAt"), industryArr));
        return resultMap;
    }

    @Override
    public Map<String, Object> holidayPassengerReception(HolidayTypeEnum holiday) {
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 从枚举中获取节假日起始时间
        int thisYear = LocalDate.now().getYear();
        int lastYear = thisYear - 1;
        HolidayVO thisYearHoliday = fetchHolidayData(holiday, thisYear);
        HolidayVO lastYearHoliday = fetchHolidayData(holiday, lastYear);
        Date startDate = new Date(thisYearHoliday.getStartDate());
        Date endDate = new Date(thisYearHoliday.getEndDate());
        Date lastStartDate = new Date(lastYearHoliday.getStartDate());
        Date lastEndDate = new Date(lastYearHoliday.getEndDate());
        String startTime = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String endTime = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastStartTime = DateUtil.format(lastStartDate, DateUtil.PATTERN_YYYY_MM_DD);
        String lastEndTime = DateUtil.format(lastEndDate, DateUtil.PATTERN_YYYY_MM_DD);
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        // 热门酒店排行top10
        List<ScenicTouristVO> hotelList = aggregation("diy_hotel_reception", "doc", boolQueryBuilder, "stationId", "cumulativeReception");
        hotelList.forEach(scenicTouristVO -> {
            Optional<HotelBaseInfo> optional = hotelBaseInfoMapper.findById(scenicTouristVO.getName());
            optional.ifPresent(hotelBaseInfo -> scenicTouristVO.setName(hotelBaseInfo.getName()));
        });
        resultMap.put("hotelReceptionTop", hotelList);
        Double passengerReception = commonPassengerReception(startTime, endTime);
        Double lastPassengerReception = commonPassengerReception(lastStartTime, lastEndTime);
        resultMap.put("passengerReception", passengerReception);
        Double compareTransAt;
        if (lastPassengerReception > 0) {
            // 同比增长或下降，保留两位小数
            Double diff = passengerReception - lastPassengerReception;
            compareTransAt = new BigDecimal(diff * 100 / lastPassengerReception)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareTransAt", compareTransAt);
        } else {
            resultMap.put("compareTransAt", "");
        }
        // 各商圈今年和去年接待人数
        String[] businessCircleArr = {"柳江商圈", "瓦屋山商圈", "七里坪商圈", "槽渔滩商圈", "县城区域商圈", "其它商圈"};
        resultMap.put("hotelAreaReception", holidayHotelAreaReception(startTime, endTime, businessCircleArr));
        resultMap.put("lastHotelAreaReception", holidayHotelAreaReception(lastStartTime, lastEndTime, businessCircleArr));
        return resultMap;
    }

    /*
     * 各区域游客入住人数（从diy_hotel_reception中获取酒店每日累计接待数据）
     */
    private List<ScenicTouristVO> holidayHotelAreaReception(String startTime, String endTime, String[] businessCircleArr) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        // 每个酒店累计接待数据
        List<ScenicTouristVO> hotelReceptionList = aggregation("diy_hotel_reception", "doc", boolQueryBuilder, 1000, "stationId", "cumulativeReception");
        Map<String, ScenicTouristVO> hotelReceptionMap = hotelReceptionList.stream().collect(Collectors.toMap(ScenicTouristVO::getName, Function.identity()));
        List<ScenicTouristVO> dataList = new ArrayList<>();
        Map<String, Long> paramMap = new HashMap<>();
        // 商圈，节假日入住人数
        hotelBaseInfoMapper.findAll().forEach(hotelBaseInfo -> {
            String stationId = hotelBaseInfo.getStationId();
            String businessCircle = hotelBaseInfo.getBusinessCircle();
            if (hotelReceptionMap.containsKey(stationId)) {
                Long checkInNum = hotelReceptionMap.get(stationId).getValue().longValue();
                if (paramMap.containsKey(businessCircle)) {
                    Long accumulativeCheckInNum = paramMap.get(businessCircle);
                    paramMap.put(businessCircle, accumulativeCheckInNum + checkInNum);
                } else {
                    paramMap.put(businessCircle, checkInNum);
                }
            }
        });
        // 构造成前端展示格式
        for (Map.Entry<String, Long> entry : paramMap.entrySet()) {
            dataList.add(new ScenicTouristVO(entry.getKey(), entry.getValue().doubleValue()));
        }
        return buildHolidayData(dataList, businessCircleArr);
    }

    @Override
    public Map<String, Object> industryConsumptionAnalyze() {
        Map<String, Object> resultMap = new HashMap<>();
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        Integer lastYear = year - 1;
        String[] industryArr = {"餐饮", "住宿", "购物", "娱乐", "景区", "交通", "旅游综合服务", "其他"};
        resultMap.put("industryConsumption", buildHolidayData(commonIndustryConsumption(year), industryArr));
        resultMap.put("lastIndustryConsumption", buildHolidayData(commonIndustryConsumption(lastYear), industryArr));
        return resultMap;
    }

    @Override
    public Map<String, Object> businessCircleConsumptionMonth() {
        Map<String, Object> resultMap = new HashMap<>();
        // 获取最新一条银联数据日期
        Map<String, String> timeMap = getTimeRange("county_business_circle_consumption");
        String startTime = timeMap.get("startTime");
        String endTime = timeMap.get("endTime");
        resultMap.putAll(timeMap);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 县域当月营收
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        SumAggregationBuilder aggregationBuilder = AggregationBuilders.sum("transAt_agg").field("transAt");
        resultMap.put("transAtTotal", sumAggregation("county_consumption", "doc", boolQueryBuilder, aggregationBuilder));
        // 各商圈当月营收数据
        List<ScenicTouristVO> dataList = new ArrayList<>();
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("cbdName_agg")
                .field("cbdName")
                .subAggregation(aggregationBuilder);
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("county_business_circle_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("cbdName_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Sum sum = bucket.getAggregations().get("transAt_agg");
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(name, decimalTransfer(String.valueOf(sum.getValue()), "1"));
            dataList.add(scenicTouristVO);
        }
        String[] scenicArr = {"县城区域商圈", "瓦屋山商圈", "七里坪商圈", "柳江商圈", "槽渔滩商圈"};
        resultMap.put("businessCircleConsumption", buildHolidayData(dataList, scenicArr));
        return resultMap;
    }

    // 解决银联延迟2天数据问题
    private Map<String, String> getTimeRange(String indexName) {
        Map<String, String> map = new HashMap<>(2);
        MaxAggregationBuilder aggregationBuilder = AggregationBuilders.max("dealDay_max").field("dealDay");
        String latestDay = maxAggregation(indexName, "doc", QueryBuilders.boolQuery(), aggregationBuilder);
        String[] split = latestDay.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        map.put("startTime", DateUtil.getFirstDay(year, month));
        map.put("endTime", DateUtil.getEndDay(year, month));
        map.put("year", String.valueOf(year));
        map.put("month", String.valueOf(month));
        return map;
    }

    @Override
    public List<BusinessCircleVO> businessCircleConsumptionYear() {
        // 获取最新一条银联数据日期
        Map<String, String> timeMap = getTimeRange("county_business_circle_consumption");
        String year = timeMap.get("year");
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        return perBusinessCircleAggregation(boolQueryBuilder);
    }

    @Override
    public Map<String, Object> businessConsumptionTrend() {
        String index = "county_consumption";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        String endTime = maxAggregation(index, type, QueryBuilders.boolQuery(), AggregationBuilders.max("dealDay_max").field("dealDay"));
        String startTime = DateUtil.format(DateUtil.getTime("day", DateUtil.parse(endTime, DateUtil.PATTERN_YYYY_MM_DD), -6), DateUtil.PATTERN_YYYY_MM_DD);
        String lastEndTime = DateUtil.format(DateUtil.getTime("day", DateUtil.parse(endTime, DateUtil.PATTERN_YYYY_MM_DD), -7), DateUtil.PATTERN_YYYY_MM_DD);
        String lastStartTime = DateUtil.format(DateUtil.getTime("day", DateUtil.parse(endTime, DateUtil.PATTERN_YYYY_MM_DD), -13), DateUtil.PATTERN_YYYY_MM_DD);
        System.out.println(lastStartTime + '-' + lastEndTime + "-" + startTime + "-" + endTime);
        resultMap.put("weekData", commonHolidayTrend(startTime, endTime));
        resultMap.put("lastWeekData", commonHolidayTrend(lastStartTime, lastEndTime));
        Double weekBusinessConsumption = commonHolidayConsumption(startTime, endTime);
        Double lastWeekBusinessConsumption = commonHolidayConsumption(lastStartTime, lastEndTime);
        resultMap.put("weekBusinessConsumption", weekBusinessConsumption);
        resultMap.put("lastWeekBusinessConsumption", lastWeekBusinessConsumption);
        Double compareTransAt = 0.0;
        if (weekBusinessConsumption > 0 && lastWeekBusinessConsumption > 0) {
            // 同比增长或下降，保留两位小数
            Double diff = weekBusinessConsumption - lastWeekBusinessConsumption;
            compareTransAt = new BigDecimal(diff * 100 / lastWeekBusinessConsumption)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareTransAt", compareTransAt);
        } else {
            resultMap.put("compareTransAt", "");
        }
        return resultMap;
    }

    @Override
    public List<DefaultHolidayVO> holiday() {
        List<DefaultHolidayVO> dataList = new ArrayList<>();
        int thisYear = LocalDate.now().getYear();
        HolidayTypeEnum[] values = HolidayTypeEnum.values();
        boolean flag = false;
        for (int i = values.length - 1; i >= 0; i--) {
            HolidayTypeEnum holidayTypeEnum = values[i];
            HolidayVO holidayVO = fetchHolidayData(holidayTypeEnum, thisYear);
            if (System.currentTimeMillis() > holidayVO.getEndDate() && !flag) {
                flag = true;
                dataList.add(new DefaultHolidayVO(holidayTypeEnum.getName(), holidayTypeEnum.getShortening().toUpperCase(), true));
            } else {
                dataList.add(new DefaultHolidayVO(holidayTypeEnum.getName(), holidayTypeEnum.getShortening().toUpperCase(), false));
            }
        }
        Collections.reverse(dataList);
        return dataList;
    }

    private List<ScenicTouristVO> commonIndustryConsumption(int year) {
        String startTime = year + "-01-01";
        String endTime = year + "-12-31";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ScenicTouristVO> dataList = new ArrayList<>();
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("transAt_agg").field("transAt");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("type_agg")
                .field("type")
                .subAggregation(sumAggregationBuilder);
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("county_industry_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("type_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Sum sum = bucket.getAggregations().get("transAt_agg");
            ScenicTouristVO scenicTouristVO = new ScenicTouristVO(name, decimalTransfer(String.valueOf(sum.getValue()), "1"));
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    private Double commonPassengerReception(String startTime, String endTime) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        // 年度累计入住人数
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_count").field("cumulativeReception");
        return sumAggregation("diy_hotel_reception", "doc", boolQueryBuilder, sumAggregationBuilder);
    }

    private Double commonHolidayConsumption(String startTime, String endTime) {
        String index = "county_consumption";
        String type = "doc";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        SumAggregationBuilder sumTransAt = AggregationBuilders.sum("transAt_count").field("transAt");
        return sumAggregation(index, type, boolQueryBuilder, sumTransAt);
    }

    private List<ScenicTouristVO> commonHolidayTrend(String startTime, String endTime) {
        String index = "county_consumption";
        String type = "doc";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ScenicTouristVO> list = aggregation(index, type, boolQueryBuilder, "dealDay", "transAt");
        return buildMixData(list, startTime, endTime);
    }

    private String maxAggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, MaxAggregationBuilder aggregationBuilder) {
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).addAggregation(aggregationBuilder).build();
        return elasticsearchTemplate.query(query, response -> {
            InternalMax max = (InternalMax) response.getAggregations().asList().get(0);
            return max.getValueAsString().substring(0, 10);
        });
    }

    private List<ScenicTouristVO> holidayCommonQuery(String start, String end, String indexName, String indexType) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(start).lte(end));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        return aggregation(indexName, indexType, boolQueryBuilder, "scenicName", "peopleNum");
    }

    private List<ScenicTouristVO> buildHolidayData(List<ScenicTouristVO> list, String[] scenicArr) {
        Map<String, Object> paramMap = new HashMap<>();
        List<ScenicTouristVO> dataList = new ArrayList<>();
        list.forEach(scenicTouristVO -> {
            paramMap.put(scenicTouristVO.getName(), scenicTouristVO.getValue());
        });
        for (String scenicName : scenicArr) {
            ScenicTouristVO scenicTouristVO;
            if (paramMap.containsKey(scenicName)) {
                scenicTouristVO = new ScenicTouristVO(scenicName, Double.parseDouble(String.valueOf(paramMap.get(scenicName))));
            } else {
                scenicTouristVO = new ScenicTouristVO(scenicName, 0.0);
            }
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    // 根据起始节假日，构建完整数据
    private List<ScenicTouristVO> buildMixData(List<ScenicTouristVO> list, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        List<String> dateList = DateUtil.getBetweenDate(startTime, endTime);
        List<ScenicTouristVO> dataList = new ArrayList<>();
        list.forEach(scenicTouristVO -> {
            paramMap.put(scenicTouristVO.getName(), scenicTouristVO.getValue());
        });
        for (String date : dateList) {
            ScenicTouristVO scenicTouristVO;
            if (paramMap.containsKey(date)) {
                scenicTouristVO = new ScenicTouristVO(date, Double.parseDouble(String.valueOf(paramMap.get(date))));
            } else {
                scenicTouristVO = new ScenicTouristVO(date, 0.0);
            }
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    private Double decimalTransfer(String divisor, String dividend) {
        BigDecimal bg = new BigDecimal(divisor);
        return bg.divide(new BigDecimal(dividend), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 用于计算酒店接口
    private String getPercent(String divisor, String dividend) {
        BigDecimal bg = new BigDecimal(divisor);
        Double percent = bg.divide(new BigDecimal(dividend), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00%");
        return percent >= 1 ? "100%" : df.format(percent);
    }

    private Double sumAggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, SumAggregationBuilder aggregationBuilder) {
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).addAggregation(aggregationBuilder).build();
        return elasticsearchTemplate.query(query, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return decimalTransfer(String.valueOf(sum.getValue()), "1");
        });
    }

    private List<ScenicTouristVO> aggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, String field, String agg) {
        return aggregation(indexName, indexType, boolQueryBuilder, null, field, agg);
    }

    private List<ScenicTouristVO> aggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, Integer size, String field, String agg) {
        List<ScenicTouristVO> dataList = new ArrayList<>();
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_count").field(agg);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("scenic_agg")
                .field(field)
                .order(BucketOrder.aggregation("people_count", false))
                .subAggregation(sumAggregationBuilder);
        if (size != null) {
            termsAggregationBuilder.size(size);
        }
        NativeSearchQuery yearQuery = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(yearQuery, SearchResponse::getAggregations);
        if ("dealDay".equals(field)) {
            LongTerms longTerms = temp.get("scenic_agg");
            // 获得所有的桶
            List<LongTerms.Bucket> buckets = longTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (LongTerms.Bucket bucket : buckets) {
                String dealDay = bucket.getKeyAsString();
                Sum sum = bucket.getAggregations().get("people_count");
                ScenicTouristVO scenicTouristVO = new ScenicTouristVO(dealDay.substring(0, 10), decimalTransfer(String.valueOf(sum.getValue()), "1"));
                dataList.add(scenicTouristVO);
            }
        } else {
            StringTerms stringTerms = temp.get("scenic_agg");
            // 获得所有的桶
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (StringTerms.Bucket bucket : buckets) {
                String name = bucket.getKeyAsString();
                Sum sum = bucket.getAggregations().get("people_count");
                ScenicTouristVO scenicTouristVO = new ScenicTouristVO(name, decimalTransfer(String.valueOf(sum.getValue()), "1"));
                dataList.add(scenicTouristVO);
            }
        }
        return dataList;
    }

    private List<BusinessCircleVO> perConsumptionAggregation(BoolQueryBuilder boolQueryBuilder) {
        List<BusinessCircleVO> dataList = new ArrayList<>();
        // 展示省内城市列表
        // 3表示眉山市（不含洪雅县本地），所以属于省内
        boolQueryBuilder.must(QueryBuilders.termsQuery("travellerType", "1", "3"));
        AggregationBuilder sumAggregationBuilderOne = AggregationBuilders.sum("transAt_agg").field("transAt");
        AggregationBuilder sumAggregationBuilderTwo = AggregationBuilders.sum("acctNum_agg").field("acctNum");
        Map<String, String> bucketsPathsMap = new HashMap<>();
        bucketsPathsMap.put("tmpA", "transAt_agg");
        bucketsPathsMap.put("tmpB", "acctNum_agg");
        Script script = new Script(ScriptType.INLINE,
                "painless",
                "params.tmpA/params.tmpB",
                Collections.emptyMap());
        BucketScriptPipelineAggregationBuilder bucketScript = new BucketScriptPipelineAggregationBuilder("bucket_agg", bucketsPathsMap, script);
        // 保留两位小数
        bucketScript.format("#.##");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("consume_agg")
                .field("sourceCity")
                .size(100)
                .subAggregation(sumAggregationBuilderOne)
                .subAggregation(sumAggregationBuilderTwo)
                .subAggregation(bucketScript);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("county_consumption_city").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("consume_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        List<ProvinceVO> provinceList = feignClient.listProvincesWithoutCounty().getData();
        ProvinceVO provinceVO = provinceList.stream().filter(item -> "510000".equals(item.getProvId())).findFirst().orElse(null);
        for (StringTerms.Bucket bucket : buckets) {
            String cityCode = bucket.getKeyAsString();
            Sum transAtSum = bucket.getAggregations().get("transAt_agg");
            Sum acctNumSum = bucket.getAggregations().get("acctNum_agg");
            InternalSimpleValue sumBucketAgg = bucket.getAggregations().get("bucket_agg");
            if (provinceVO != null) {
                CityVO cityVO = provinceVO.getCities().stream().filter(item -> cityCode.equals(item.getCityId())).findFirst().orElse(null);
                if (cityVO != null) {
                    BusinessCircleVO businessCircleVO = new BusinessCircleVO(cityVO.getCityName(), decimalTransfer(String.valueOf(transAtSum.getValue()), "1"),
                            acctNumSum.getValue(), decimalTransfer(String.valueOf(sumBucketAgg.getValue()), "1"));
                    dataList.add(businessCircleVO);
                }
            }
        }
        return dataList;
    }

    private List<BusinessCircleVO> perBusinessCircleAggregation(BoolQueryBuilder boolQueryBuilder) {
        List<BusinessCircleVO> dataList = new ArrayList<>();
        SumAggregationBuilder sumAggregationBuilderOne = AggregationBuilders.sum("transAt_agg").field("transAt");
        SumAggregationBuilder sumAggregationBuilderTwo = AggregationBuilders.sum("acctNum_agg").field("acctNum");
        Map<String, String> bucketsPathsMap = new HashMap<>();
        bucketsPathsMap.put("tmpA", "transAt_agg");
        bucketsPathsMap.put("tmpB", "acctNum_agg");
        Script script = new Script(ScriptType.INLINE,
                "painless",
                "params.tmpA/params.tmpB",
                Collections.emptyMap());
        BucketScriptPipelineAggregationBuilder bucketScript = new BucketScriptPipelineAggregationBuilder("bucket_agg", bucketsPathsMap, script);
        // 保留两位小数
        bucketScript.format("#.##");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("consume_agg")
                .field("cbdName")
                .size(100)
                .subAggregation(sumAggregationBuilderOne)
                .subAggregation(sumAggregationBuilderTwo)
                .subAggregation(bucketScript);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("county_business_circle_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("consume_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String cbdName = bucket.getKeyAsString();
            InternalSimpleValue sumBucketAgg = bucket.getAggregations().get("bucket_agg");
            Sum transAtSum = bucket.getAggregations().get("transAt_agg");
            Sum acctNumSum = bucket.getAggregations().get("acctNum_agg");
            BusinessCircleVO businessCircleVO = new BusinessCircleVO(cbdName, decimalTransfer(String.valueOf(transAtSum.getValue()), "1"), acctNumSum.getValue(), decimalTransfer(String.valueOf(sumBucketAgg.getValue()), "1"));
            dataList.add(businessCircleVO);
        }
        return dataList;
    }

    /**
     * 按照商圈名称排序
     *
     * @param list list
     * @return list sorted by area chart name
     */
    private static List<HotelAreaVO> sortedByAreaChartName(List<HotelAreaVO> list) {
        return list.stream().sorted((o1, o2) -> SortUtil.getAreaChartNameSortResult(o1.getName(), o2.getName())).collect(Collectors.toList());
    }
}
