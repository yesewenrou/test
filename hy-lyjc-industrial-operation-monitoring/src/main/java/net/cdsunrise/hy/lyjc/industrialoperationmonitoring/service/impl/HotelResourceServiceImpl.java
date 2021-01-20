package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.IdCardAdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CountyVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.ProvinceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.HotelBaseInfoMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.HotelInventoryMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HotelResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyAndValue;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.*;
import net.cdsunrise.hy.record.starter.util.PageUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LHY
 * @date 2019/11/13 16:14
 */
@Service
@Slf4j
public class HotelResourceServiceImpl implements HotelResourceService {

    private ElasticsearchTemplate elasticsearchTemplate;
    private HotelInventoryMapper hotelInventoryMapper;
    private HotelBaseInfoMapper hotelBaseInfoMapper;
    private IdCardAdministrativeAreaFeignClient feignClient;

    private List<HotelStatistics> dataList;

    public HotelResourceServiceImpl(ElasticsearchTemplate elasticsearchTemplate, HotelInventoryMapper hotelInventoryMapper, HotelBaseInfoMapper hotelBaseInfoMapper, IdCardAdministrativeAreaFeignClient feignClient) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.hotelInventoryMapper = hotelInventoryMapper;
        this.hotelBaseInfoMapper = hotelBaseInfoMapper;
        this.feignClient = feignClient;
    }

    @Override
    public PageResult<HotelBaseInfoVO> list(Integer page, Integer size, HotelCondition condition) {
        Page<HotelBaseInfo> hotelBaseInfoPage = commonQuery(page, size, condition);
        return PageUtil.page(hotelBaseInfoPage, hotelBaseInfo -> {
            HotelBaseInfoVO hotelBaseInfoVO = new HotelBaseInfoVO();
            BeanUtils.copyProperties(hotelBaseInfo, hotelBaseInfoVO);
            return hotelBaseInfoVO;
        }, hotelBaseInfoPage.getContent());
    }

    private BoolQueryBuilder baseQuery(HotelCondition condition) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(condition.getStationId())) {
            TermQueryBuilder queryBuilder = QueryBuilders.termQuery("stationId", condition.getStationId());
            boolQueryBuilder.must(queryBuilder);
        }
        if (!StringUtils.isEmpty(condition.getName())) {
            // ES利用正则匹配，实现MySQL中like语句效果
            String regexp = ".*" + condition.getName() + ".*";
            RegexpQueryBuilder queryBuilder = QueryBuilders.regexpQuery("name.keyword", regexp);
            boolQueryBuilder.must(queryBuilder);
        }
        if (!StringUtils.isEmpty(condition.getArea())) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("area", condition.getArea());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (!StringUtils.isEmpty(condition.getBusinessCircle())) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("businessCircle", condition.getBusinessCircle());
            boolQueryBuilder.must(termQueryBuilder);
        }
        if (!StringUtils.isEmpty(condition.getAddress())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", condition.getAddress());
            boolQueryBuilder.must(matchQueryBuilder);
        }
        return boolQueryBuilder;
    }

    private Page<HotelBaseInfo> commonQuery(Integer page, Integer size, HotelCondition condition) {
        if (condition == null) {
            condition = new HotelCondition();
        }
        BoolQueryBuilder boolQueryBuilder = baseQuery(condition);
        // 利用酒店索引hotel_base_info的别名进行查询，方便后期索引不停机更新
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("alter_ego").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        // 增加床位总数降序排列
        Sort descSort = Sort.by(Sort.Direction.DESC, "bedNum");
        query.addSort(descSort);
        if (page != null && size != null) {
            // 简单防止乱输参数
            page = page > 0 ? page - 1 : 0;
            size = size > 0 ? size : 10;
            PageRequest pageRequest = PageRequest.of(page, size);
            query.setPageable(pageRequest);
        }
        return elasticsearchTemplate.queryForPage(query, HotelBaseInfo.class);
    }

    @Override
    public HotelStatisticsVO hotelStatistics() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        NativeSearchQuery hotelQuery = (new NativeSearchQueryBuilder()).withIndices("hotel_base_info").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        Long hotelCount = elasticsearchTemplate.count(hotelQuery);
        // 基础酒店范围
        List<String> stationIdList = commonBaseHotelStationIdList(new HotelCondition());
        // 估算住满酒店数
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList))
                .must(QueryBuilders.termQuery("status", 1));
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_inventory").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        Long estimateFull = elasticsearchTemplate.count(query);
        Double bedNum = sumAggregation("hotel_base_info", QueryBuilders.boolQuery(), "bed_aggs", "bedNum");
        Double realTimeCheckinCount = sumAggregation("hotel_inventory", QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("stationId", stationIdList)), "checkin_aggs", "checkinNum");
        String estimateOccupancy = getPercent(realTimeCheckinCount, bedNum);
        String startStr = " 00:00:00";
        String endStr = " 23:59:59";
        String todayString = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD);
        String yesterdayString = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
        String startTime = todayString + startStr;
        String endTime = todayString + endStr;
        String yesterdayStartTime = yesterdayString + startStr;
        String yesterdayEndTime = yesterdayString + endStr;
        Long todayReceptionCount = cumulativeReceptionPlus(yesterdayString, startTime, endTime, stationIdList);
        // 昨日累计接待
        Long yesterdayReceptionCount = historyCumulativeReception(yesterdayStartTime, yesterdayEndTime, stationIdList);
        return new HotelStatisticsVO(hotelCount, estimateFull, bedNum, realTimeCheckinCount, estimateOccupancy, todayReceptionCount, yesterdayReceptionCount);
    }

    /**
     * 酒店所有维度数据统计，都必须基于hotel_base_info中的酒店
     */
    private List<String> commonBaseHotelStationIdList(HotelCondition condition) {
        List<HotelBaseInfo> list = commonBaseHotelList(condition);
        return list.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
    }

    private List<HotelBaseInfo> commonBaseHotelList(HotelCondition condition) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_base_info").withTypes("doc")
                .withQuery(baseQuery(condition))
                .withPageable(PageRequest.of(0, 10000));
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelBaseInfo.class);
    }

    private List<HotelInventory> commonHotelInventoryList(List<String> stationIdList) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_inventory").withTypes("doc")
                .withQuery(QueryBuilders.termsQuery("stationId", stationIdList))
                .withPageable(PageRequest.of(0, 10000));
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelInventory.class);
    }

    private List<HotelStatistics> commonHotelStatisticsList(String day) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("statisticalDate", day));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_statistics").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 10000));
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelStatistics.class);
    }

    @Override
    public HotelStatisticsVO hotelDetailStatistics(HotelCondition condition) {
        HotelStatisticsVO hotelStatisticsVO = new HotelStatisticsVO();
        List<String> stationIdList = commonBaseHotelStationIdList(condition);
        if (CollectionUtils.isEmpty(stationIdList)) {
            return new HotelStatisticsVO(0.0, 0L, 0L);
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
            // 实时入住人数
            Double realTimeCheckinCount = sumAggregation("hotel_inventory", boolQueryBuilder, "checkin_aggs", "checkinNum");
            hotelStatisticsVO.setRealTimeCheckinCount(realTimeCheckinCount);
            // 昨日和今日累计接待
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String todayString = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD);
            String yesterdayString = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
            String startTime = todayString + startStr;
            String endTime = todayString + endStr;
            String yesterdayStartTime = yesterdayString + startStr;
            String yesterdayEndTime = yesterdayString + endStr;
            hotelStatisticsVO.setTodayReceptionCount(cumulativeReceptionPlus(yesterdayString, startTime, endTime, stationIdList));
            hotelStatisticsVO.setYesterdayReceptionCount(historyCumulativeReception(yesterdayStartTime, yesterdayEndTime, stationIdList));
            return hotelStatisticsVO;
        }
    }

    private String getPercent(Double realTimeCheckinCount, Double bedNum) {
        BigDecimal bg = new BigDecimal(realTimeCheckinCount);
        Double percent = bg.divide(new BigDecimal(bedNum), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00%");
        return percent >= 1 ? "100%" : df.format(percent);
    }

    @Override
    public PageResult<HotelStatisticsVO> hotelDetailList(Integer page, Integer size, HotelCondition condition) {
        Page<HotelBaseInfo> hotelBaseInfoPage = commonQuery(page, size, condition);
        return PageUtil.page(hotelBaseInfoPage, hotelBaseInfo -> {
            List<String> stationIdList = new ArrayList<>();
            stationIdList.add(hotelBaseInfo.getStationId());
            Double bedNum = Double.parseDouble(String.valueOf(hotelBaseInfo.getBedNum()));
            // 计算酒店实时入住人数
            Optional<HotelInventory> optional = hotelInventoryMapper.findById(hotelBaseInfo.getStationId());
            Double realTimeCheckinCount = optional.map(hotelInventory -> Double.parseDouble(String.valueOf(hotelInventory.getCheckinNum()))).orElse(0.0);
            String estimateOccupancy = getPercent(realTimeCheckinCount, bedNum);
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String startTime = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD) + startStr;
            String endTime = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD) + endStr;
            String yesterdayStartTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD) + startStr;
            String yesterdayEndTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD) + endStr;
            Long todayReceptionCount = cumulativeReception(startTime, endTime, stationIdList);
            Long yesterdayReceptionCount = historyCumulativeReception(yesterdayStartTime, yesterdayEndTime, stationIdList);
            return new HotelStatisticsVO(hotelBaseInfo.getStationId(), hotelBaseInfo.getName(), hotelBaseInfo.getArea(), hotelBaseInfo.getBusinessCircle(), hotelBaseInfo.getAddress(), bedNum,
                    realTimeCheckinCount, estimateOccupancy, todayReceptionCount, yesterdayReceptionCount);
        }, hotelBaseInfoPage.getContent());
    }

    /**
     * 酒店入住 -> 实时入住详情列表（前端分页版本，按照实时入住人数倒序）
     */
    @Override
    public List<HotelStatisticsVO> hotelDetailListOrderByPeopleDesc(HotelCondition condition) {
        List<HotelStatisticsVO> dataList = new ArrayList<>();
        String todayString = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD);
        String yesterdayString = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
        List<HotelBaseInfo> hotelBaseInfoList = commonBaseHotelList(condition);
        if (CollectionUtils.isEmpty(hotelBaseInfoList)) {
            return dataList;
        } else {
            List<String> stationIdList = hotelBaseInfoList.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
            List<HotelInventory> hotelInventoryList = commonHotelInventoryList(stationIdList);
            Map<String, HotelInventory> hotelInventoryMap = hotelInventoryList.stream().collect(Collectors.toMap(HotelInventory::getStationId, Function.identity()));
            // 凌晨校准酒店库存
            List<HotelStatistics> hotelStatisticsList = commonHotelStatisticsList(yesterdayString);
            Map<String, HotelStatistics> hotelStatisticsMap = hotelStatisticsList.stream().collect(Collectors.toMap(HotelStatistics::getStationId, Function.identity()));
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String yesterdayStartTime = yesterdayString + startStr;
            String yesterdayEndTime = yesterdayString + endStr;
            // 昨日累计接待列表
            List<HotelReceptionCommon> hotelPassengerReceptionList = historyCumulativeReceptionListPlus(yesterdayStartTime, yesterdayEndTime, stationIdList);
            Map<String, HotelReceptionCommon> historyHotelPassengerReceptionMap = hotelPassengerReceptionList.stream()
                    .collect(Collectors.toMap(HotelReceptionCommon::getStationId, Function.identity()));
            // 实时累计接待列表
            String startTime = todayString + startStr;
            String endTime = todayString + endStr;
            // 酒店今日入住人数
            Map<String, Long> todayReceptionMap = cumulativeReceptionListPlus(startTime, endTime, stationIdList);
            hotelBaseInfoList.forEach(hotelBaseInfo -> {
                String stationId = hotelBaseInfo.getStationId();
                Double bedNum = Double.parseDouble(String.valueOf(hotelBaseInfo.getBedNum()));
                // 计算酒店实时入住人数
                Double realTimeCheckinCount = 0.0;
                if (hotelInventoryMap.containsKey(stationId)) {
                    HotelInventory hotelInventory = hotelInventoryMap.get(stationId);
                    realTimeCheckinCount = hotelInventory.getCheckinNum().doubleValue();
                }
                String estimateOccupancy = getPercent(realTimeCheckinCount, bedNum);
                // 今日累计入住
                Long todayReceptionCount = 0L;
                Long yesterdayReceptionCount = 0L;
                // 计算酒店昨日累计入住人数
                if (historyHotelPassengerReceptionMap.containsKey(stationId)) {
                    HotelReceptionCommon hotelPassengerReception = historyHotelPassengerReceptionMap.get(stationId);
                    yesterdayReceptionCount = hotelPassengerReception.getCumulativeReception().longValue();
                }
                // 计算酒店今日累计入住人数 = 凌晨校准酒店库存 + 今日入住人数
                if (hotelStatisticsMap.containsKey(stationId)) {
                    todayReceptionCount += hotelStatisticsMap.get(stationId).getCheckinNum();
                }
                if (todayReceptionMap.containsKey(stationId)) {
                    todayReceptionCount += todayReceptionMap.get(stationId);
                }
                dataList.add(new HotelStatisticsVO(hotelBaseInfo.getStationId(), hotelBaseInfo.getName(), hotelBaseInfo.getArea(), hotelBaseInfo.getBusinessCircle(), hotelBaseInfo.getAddress(), bedNum,
                        realTimeCheckinCount, estimateOccupancy, todayReceptionCount, yesterdayReceptionCount));
            });
            // 按照实时入住人数降序排列
            Comparator<? super HotelStatisticsVO> comparator = (a, b) -> {
                if (a.getRealTimeCheckinCount() > b.getRealTimeCheckinCount()) {
                    return -1;
                } else if (a.getRealTimeCheckinCount() < b.getRealTimeCheckinCount()) {
                    return 1;
                } else {
                    return 0;
                }
            };
            return dataList.stream().sorted(comparator).collect(Collectors.toList());
        }
    }

    /**
     * 前端进行分页
     **/
    @SuppressWarnings("all")
    @Override
    public Map<String, Object> hotelPassengerReceptionList(HotelCondition condition) {
        Map<String, Object> resultMap = new HashMap<>();
        // 累计接待人数
        Long cumulativeReception = 0L;
        List<HotelPassengerReceptionNewVO> dataList = new ArrayList<>();
        String startTime;
        String endTime;
        if (StringUtils.isEmpty(condition.getStartTime()) || StringUtils.isEmpty(condition.getEndTime())) {
            // 默认查询昨天数据，因为累计分析有1天延迟
            startTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
            endTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
        } else {
            startTime = condition.getStartTime();
            endTime = condition.getEndTime();
        }
        // 基础酒店范围
        List<HotelBaseInfo> hotelBaseInfoList = commonBaseHotelList(condition);
        if (!CollectionUtils.isEmpty(hotelBaseInfoList)) {
            Map<String, HotelBaseInfo> hotelMap = hotelBaseInfoList.stream().collect(Collectors.toMap(HotelBaseInfo::getStationId, Function.identity()));
            List<String> stationIdList = hotelBaseInfoList.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
            if (!CollectionUtils.isEmpty(stationIdList)) {
                boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
            }
            String sumName = "cumulativeReception_count";
            AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(sumName).field("cumulativeReception");
            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                    .terms("stationId_agg")
                    .field("stationId")
                    .size(10000)
                    .order(BucketOrder.aggregation(sumName, false))
                    .subAggregation(sumAggregationBuilder);
            NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("diy_hotel_reception").withTypes("doc")
                    .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
            Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
            StringTerms stringTerms = temp.get("stationId_agg");
            // 获得所有的桶
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (StringTerms.Bucket bucket : buckets) {
                String stationId = bucket.getKeyAsString();
                Sum sum = bucket.getAggregations().get(sumName);
                Double value = sum.getValue();
                HotelBaseInfo hotelBaseInfo = hotelMap.get(stationId);
                HotelPassengerReceptionNewVO hotelPassengerReceptionNewVO = new HotelPassengerReceptionNewVO(hotelBaseInfo.getName(), hotelBaseInfo.getArea(), hotelBaseInfo.getBusinessCircle(),
                        hotelBaseInfo.getAddress(), value.longValue());
                dataList.add(hotelPassengerReceptionNewVO);
            }
            // 累计接待人数
            cumulativeReception = historyCumulativeReception(startTime, endTime, stationIdList);
        }
        resultMap.put("dataList", dataList);
        resultMap.put("cumulativeReception", cumulativeReception);
        return resultMap;
    }

    @Override
    public PageResult<HotelTouristSourceVO> hotelTouristSourceList(Integer page, Integer size, HotelCondition condition) {
        if (condition == null) {
            condition = new HotelCondition();
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isEmpty(condition.getStartTime()) || StringUtils.isEmpty(condition.getEndTime())) {
            // 默认查询昨天数据，因为累计分析有1天延迟
            String yesterday = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD);
            boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(yesterday).lte(yesterday));
        } else {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(condition.getStartTime()).lte(condition.getEndTime()));
        }
        if (!StringUtils.isEmpty(condition.getProvName())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("provName", condition.getProvName());
            boolQueryBuilder.must(matchQueryBuilder);
        }
        if (!StringUtils.isEmpty(condition.getCityName())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("cityName", condition.getCityName());
            boolQueryBuilder.must(matchQueryBuilder);
        }
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_tourist_source").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        // 增加统计日期降序排列
        Sort descSort = Sort.by(Sort.Direction.DESC, "cumulativeReception");
        query.addSort(descSort);
        if (page != null && size != null) {
            // 简单防止乱输参数
            page = page > 0 ? page - 1 : 0;
            size = size > 0 ? size : 10;
            PageRequest pageRequest = PageRequest.of(page, size);
            query.setPageable(pageRequest);
        }
        Page<HotelTouristSource> touristSourcePage = elasticsearchTemplate.queryForPage(query, HotelTouristSource.class);
        return PageUtil.page(touristSourcePage, hotelTouristSource -> {
            HotelTouristSourceVO hotelTouristSourceVO = new HotelTouristSourceVO();
            BeanUtils.copyProperties(hotelTouristSource, hotelTouristSourceVO);
            return hotelTouristSourceVO;
        }, touristSourcePage.getContent());
    }

    @Override
    public List<ChartVO> stayOvernight(Integer year, Integer scope) {
        List<ChartVO> dataList = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        String start = "-01-01";
        String end = "-12-31";
        String startTime = year + start;
        String endTime = year + end;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        if (scope == 1) {
            boolQueryBuilder.must(QueryBuilders.termQuery("provName", "四川省"));
        } else if (scope == 2) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川省"));
        }
        // 计算总数，包含” 未过夜、1天、2天、3天、4天及以上 “
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("overnight_agg")
                .field("overnight");
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        LongTerms longTerms = temp.get("overnight_agg");
        // 获得所有的桶
        List<LongTerms.Bucket> buckets = longTerms.getBuckets();
        if (buckets.size() > 0) {
            for (LongTerms.Bucket bucket : buckets) {
                String name = bucket.getKeyAsString();
                Integer value = Integer.parseInt(String.valueOf(bucket.getDocCount()));
                switch (name) {
                    case "0":
                        ChartVO zero = new ChartVO("未过夜", value);
                        dataList.add(zero);
                        break;
                    case "1":
                        ChartVO one = new ChartVO("1天", value);
                        dataList.add(one);
                        break;
                    case "2":
                        ChartVO two = new ChartVO("2天", value);
                        dataList.add(two);
                        break;
                    case "3":
                        ChartVO three = new ChartVO("3天", value);
                        dataList.add(three);
                        break;
                    default:
                        if (paramMap.containsKey("four")) {
                            Integer before = (Integer) paramMap.get("four");
                            Integer after = value + before;
                            paramMap.put("four", after);
                        } else {
                            paramMap.put("four", value);
                        }
                        break;
                }
            }
            if (paramMap.containsKey("four")) {
                dataList.add(new ChartVO("4天及以上", (Integer) paramMap.get("four")));
            }
        } else {
            dataList.add(new ChartVO("未过夜", 0));
            dataList.add(new ChartVO("1天", 0));
            dataList.add(new ChartVO("2天", 0));
            dataList.add(new ChartVO("3天", 0));
            dataList.add(new ChartVO("4天及以上", 0));
        }
        return dataList;
    }

    @Override
    public List<ChartVO> tourismAge(Integer year, Integer scope) {
        List<ChartVO> dataList = new ArrayList<>();
        String start = "-01-01";
        String end = "-12-31";
        String startTime = year + start;
        String endTime = year + end;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        if (scope == 1) {
            boolQueryBuilder.must(QueryBuilders.termQuery("provName", "四川省"));
        } else if (scope == 2) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川省"));
        }
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
            Integer value = Integer.parseInt(String.valueOf(bucket.getDocCount()));
            ChartVO chartVO = new ChartVO(name, value);
            dataList.add(chartVO);
        }
        return buildData(dataList);
    }

    @SuppressWarnings("all")
    @Override
    public void transferAreaToBusinessCircle() {
        // 首先存储区域和商圈对应关系
        Map<String, String> map = new HashMap();
        map.put("七里坪镇", "七里坪商圈");
        map.put("高庙镇", "七里坪商圈");
        map.put("瓦屋山镇", "瓦屋山商圈");
        map.put("柳江镇", "柳江商圈");
        map.put("洪川镇", "县城区域商圈");
        map.put("止戈镇", "县城区域商圈");
        map.put("将军镇", "县城区域商圈");
        map.put("余坪镇", "其它商圈");
        map.put("中保镇", "其它商圈");
        map.put("东岳镇", "其它商圈");
        map.put("中山镇", "其它商圈");
        map.put("桃园乡", "其它商圈");
        map.put("槽渔滩镇", "槽渔滩商圈");
        List<HotelBaseInfo> dataList = new ArrayList<>();
        hotelBaseInfoMapper.findAll().forEach(hotelBaseInfo -> {
            String area = hotelBaseInfo.getArea();
            if (map.containsKey(area)) {
                hotelBaseInfo.setBusinessCircle(map.get(area));
                dataList.add(hotelBaseInfo);
            } else {
                log.error("失败的酒店公安编码：" + hotelBaseInfo.getStationId());
            }
        });
        hotelBaseInfoMapper.saveAll(dataList);
        log.info("区域转换商圈成功");
    }

    @Override
    public HotelBaseInfo detail(String stationId) {
        Optional<HotelBaseInfo> optional = hotelBaseInfoMapper.findById(stationId);
        return optional.orElse(null);
    }

    /**
     * 利用scroll滚历史酒店入住退房记录
     * 通过withPageable(PageRequest.of(0,10000))配置，定义了每次scan数目：10000条数据
     */
    @Override
    public void parseHotel() {
        String indexName = "hotel_checkin_record";
        String indexType = "doc";
        Set<HotelCheckinRecord> dataList = new HashSet<>();
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName).withTypes(indexType)
                .withQuery(QueryBuilders.boolQuery())
                .withPageable(PageRequest.of(0, 10000))
                .build();
        Long scrollTimeInMillis = 500L;
        ScrolledPage<HotelCheckinRecord> scroll = (ScrolledPage<HotelCheckinRecord>) elasticsearchTemplate.startScroll(scrollTimeInMillis, searchQuery, HotelCheckinRecord.class);
        while (scroll.hasContent()) {
            System.out.println("===正在滚：" + scroll.getContent().size() + "，" + System.currentTimeMillis());
            dataList.addAll(scroll.getContent());
            // 取下一页
            scroll = (ScrolledPage<HotelCheckinRecord>) elasticsearchTemplate.continueScroll(scroll.getScrollId(), scrollTimeInMillis, HotelCheckinRecord.class);
        }
        System.out.println("===集合长度：" + dataList.size());
        List<ProvinceVO> adminArea = getAdminArea();
        List<IndexQuery> collect = dataList.stream().map(hotelCheckinRecord -> {
            hotelCheckinRecord.setCreateTime(DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
            /** 国内游客idNum：前6位是地区，中间4位是生日，最后1位是性别 **/
            String idNum = hotelCheckinRecord.getIdNum();
            if (idNum.length() == 11) {
                String regionCode = idNum.substring(0, 6);
                // 省市县综合体
                String temp = findAdminAreaByCountyId(adminArea, regionCode);
                if (Objects.nonNull(temp)) {
                    String[] split = temp.split("-");
                    hotelCheckinRecord.setCountryName("中国");
                    hotelCheckinRecord.setProvName(split[0]);
                    hotelCheckinRecord.setCityName(split[1]);
                    hotelCheckinRecord.setCountyName(split[2]);
                } else {
                    log.info("国内地区编码：" + idNum + "，无法解析");
                }
                Integer sexCode = Integer.parseInt(idNum.substring(10));
                if (sexCode % 2 == 0) {
                    hotelCheckinRecord.setSex("女");
                } else {
                    hotelCheckinRecord.setSex("男");
                }
                Integer age = Integer.parseInt(idNum.substring(8, 9) + "0");
                hotelCheckinRecord.setAge(age);
                if (hotelCheckinRecord.getCheckoutTime() != null) {
                    // 计算住宿过夜天数
                    Integer overnight = DateUtil.calculateTotalTime(hotelCheckinRecord.getCheckinTime(), hotelCheckinRecord.getCheckoutTime());
                    hotelCheckinRecord.setOvernight(overnight);
                }
            } else {
                // 境外游客，地区、年龄、性别规则待定
                log.info("境外游客待解析......");
            }
            return new IndexQueryBuilder().withIndexName(indexName).withType(indexType).withObject(hotelCheckinRecord).build();
        }).collect(Collectors.toList());
        elasticsearchTemplate.bulkIndex(collect);
    }

    /**
     * 根据酒店入住退房记录，生成酒店每日接待数据
     */
    @Override
    public void generateHotelReception(String beginDate, String endDate) {
        List<HotelPassengerReception> dataList = new ArrayList<>();
        // 例如：生成从2020-01-01到2020-03-30的每日接待数据
        List<String> dateList = DateUtil.getBetweenDate(beginDate, endDate);
        for (String day : dateList) {
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String startTime = day + startStr;
            String endTime = day + endStr;
            List<KeyAndValue<String, Long>> list = cumulativeReception(startTime, endTime);
            list.forEach(keyAndValue -> {
                String stationId = keyAndValue.getKey();
                Double yesterdayReceptionCount = keyAndValue.getValue().doubleValue();
                HotelPassengerReception passengerReception = new HotelPassengerReception();
                passengerReception.setCreateTime(DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
                passengerReception.setId(stationId + "_" + day);
                passengerReception.setStationId(stationId);
                passengerReception.setStatisticalDate(day);
                passengerReception.setCumulativeReception(yesterdayReceptionCount);
                dataList.add(passengerReception);
            });
            log.info("===完成了 " + day + " 累计接待统计");
        }
        if (dataList.size() > 0) {
            List<IndexQuery> collect = dataList.stream().map(hotelPassengerReception -> new IndexQueryBuilder().withIndexName("hotel_passenger_reception").withType("doc").withObject(hotelPassengerReception).build()).collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(collect);
        }
    }

    private List<KeyAndValue<String, Long>> cumulativeReception(String startTime, String endTime) {
        List<KeyAndValue<String, Long>> dataList = new ArrayList<>();
        String aggName = "stationId_agg";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").lte(endTime));
        boolQueryBuilder.minimumShouldMatch(1);
        boolQueryBuilder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("checkoutTime")));
        boolQueryBuilder.should(QueryBuilders.rangeQuery("checkoutTime").gte(startTime));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms(aggName)
                .size(Integer.MAX_VALUE)
                .field("stationId");
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get(aggName);
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            dataList.add(new KeyAndValue<>(bucket.getKeyAsString(), bucket.getDocCount()));
        }
        return dataList;
    }

    /**
     * 根据酒店入住退房记录，生成每日酒店游客来源地数据
     */
    @Override
    public void generateHotelTouristSource(String beginDate, String endDate) {
        List<HotelTouristSource> dataList = new ArrayList<>();
        // 例如：生成从2020-01-01到2020-03-30的每日接待数据
        List<String> dateList = DateUtil.getBetweenDate(beginDate, endDate);
        for (String day : dateList) {
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String startTime = day + startStr;
            String endTime = day + endStr;
            List<HotelTouristSource> list = aggCumulativeReception(day, startTime, endTime);
            dataList.addAll(list);
            log.info("===完成了 " + day + " 累计接待统计");
        }
        if (dataList.size() > 0) {
            List<IndexQuery> collect = dataList.stream().map(hotelTouristSource -> new IndexQueryBuilder().withIndexName("hotel_tourist_source").withType("doc").withObject(hotelTouristSource).build()).collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(collect);
        }
    }

    /**
     * json文件中的statisticalDate字段是T-1效果
     * 假如需要手动生成2020-05-01数据，那statisticalDate应被设置成“2020-04-30”，因为这才是5.1开始的基准数据
     * @throws Exception
     */
    @PostConstruct
    private void loadHotelStatistics() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("config/hotel_statistics.json");
        try (
                InputStream in = classPathResource.getInputStream()
        ) {
            dataList = JsonUtil.parse(in, new TypeReference<List<HotelStatistics>>() {
            });
            log.info("加载酒店基准库存json...完成");
        }
    }

    @Override
    public void generateHotelStatistics() {
        // 基础酒店范围
        List<HotelBaseInfo> hotelBaseInfoList = commonBaseHotelList(new HotelCondition());
        Map<String, HotelBaseInfo> hotelMap = hotelBaseInfoList.stream().collect(Collectors.toMap(HotelBaseInfo::getStationId, Function.identity()));
        dataList.forEach(hotelStatistics -> {
            String stationId = hotelStatistics.getStationId();
            hotelStatistics.setId(stationId + "_" + hotelStatistics.getStatisticalDate());
            HotelBaseInfo hotelBaseInfo = hotelMap.getOrDefault(stationId, null);
            hotelStatistics.setCreateTime(DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
            if (Objects.nonNull(hotelBaseInfo)) {
                Integer checkInNum = hotelStatistics.getCheckinNum();
                BigDecimal bg = new BigDecimal(checkInNum);
                double checkInPercent = bg.divide(new BigDecimal(hotelBaseInfo.getBedNum()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                hotelStatistics.setCheckInPercent(checkInPercent);
            }
        });
        if (!CollectionUtils.isEmpty(dataList)) {
            List<IndexQuery> collect = dataList.stream().map(hotelStatistics -> new IndexQueryBuilder().withIndexName("hotel_statistics").withType("doc").withObject(hotelStatistics).build()).collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(collect);
        }
    }

    @Override
    public void generateHotelReceptionPlus(String beginDate, String endDate) {
        List<HotelPassengerReceptionPlus> dataList = new ArrayList<>();
        // 例如：生成从2020-01-01到2020-03-30的每日接待数据
        List<String> dateList = DateUtil.getBetweenDate(beginDate, endDate);
        for (String day : dateList) {
            String startStr = " 00:00:00";
            String endStr = " 23:59:59";
            String startTime = day + startStr;
            String endTime = day + endStr;
            // 酒店基础数据
            List<HotelBaseInfo> hotelBaseInfoList = commonBaseHotelList(new HotelCondition());
            List<String> stationIdList = hotelBaseInfoList.stream().map(HotelBaseInfo::getStationId).collect(Collectors.toList());
            // 根据日期day，获取校准库存后的酒店数据（时间：T-1）,作为当日累计接待的开始
            Date parse = DateUtil.getTime("day", DateUtil.parse(day, DateUtil.PATTERN_YYYY_MM_DD), -1);
            String yesterday = DateUtil.format(parse, DateUtil.PATTERN_YYYY_MM_DD);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("statisticalDate", yesterday));
            NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
            searchQueryBuilder.withIndices("hotel_statistics").withTypes("doc")
                    .withQuery(boolQueryBuilder)
                    .withPageable(PageRequest.of(0, 10000));
            List<HotelStatistics> hotelStatisticsList = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelStatistics.class);
            Map<String, HotelStatistics> hotelStatisticsMap = hotelStatisticsList.stream().collect(Collectors.toMap(HotelStatistics::getStationId, Function.identity()));
            // 统计某日入住数据（可能只有少部分酒店有入住数据）
            Map<String, Long> map = cumulativeReceptionListPlus(startTime, endTime, stationIdList);
            hotelBaseInfoList.forEach(hotelBaseInfo -> {
                // 酒店昨日凌晨校准库存数，即一天累计入住起点
                int inventoryNum = 0;
                // 酒店昨日入住人数
                int yesterdayCheckIn = 0;
                String stationId = hotelBaseInfo.getStationId();
                HotelStatistics hotelStatistics = hotelStatisticsMap.getOrDefault(stationId, null);
                if (Objects.nonNull(hotelStatistics)){
                    // 说明凌晨该酒店有人入住，即拥有第二天开始累计接待人数的校准基数
                    inventoryNum = hotelStatistics.getCheckinNum();
                }
                if (map.containsKey(stationId)){
                    yesterdayCheckIn = map.get(stationId).intValue();
                }
                // 酒店昨日累计接待 = 酒店昨日凌晨校准库存数 + 酒店昨日入住人数
                Integer yesterdayReceptionCount = inventoryNum + yesterdayCheckIn;
                HotelPassengerReceptionPlus passengerReceptionPlus = new HotelPassengerReceptionPlus();
                passengerReceptionPlus.setId(stationId + "_" + day);
                passengerReceptionPlus.setStationId(stationId);
                passengerReceptionPlus.setCumulativeReception(yesterdayReceptionCount);
                passengerReceptionPlus.setStatisticalDate(day);
                passengerReceptionPlus.setCreateTime(DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
                dataList.add(passengerReceptionPlus);
            });
            log.info("===完成了 " + day + " 累计接待统计");
        }
        if (dataList.size() > 0) {
            List<IndexQuery> collect = dataList.stream().map(passengerReceptionPlus -> new IndexQueryBuilder().withIndexName("hotel_passenger_reception_plus").withType("doc").withObject(passengerReceptionPlus).build()).collect(Collectors.toList());
            elasticsearchTemplate.bulkIndex(collect);
        }
    }

    private List<HotelTouristSource> aggCumulativeReception(String day, String startTime, String endTime) {
        List<HotelTouristSource> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").lte(endTime));
        boolQueryBuilder.minimumShouldMatch(1);
        boolQueryBuilder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("checkoutTime")));
        boolQueryBuilder.should(QueryBuilders.rangeQuery("checkoutTime").gte(startTime));
        // 多字段聚合
        Script script = new Script("doc['countryName'].value +'-'+ doc['provName'].value +'-'+ doc['cityName'].value");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("city_aggs")
                .script(script)
                .size(Integer.MAX_VALUE);
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("city_aggs");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            HotelTouristSource hotelTouristSource = new HotelTouristSource();
            hotelTouristSource.setCreateTime(DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
            String[] str = bucket.getKeyAsString().split("-");
            String countryName = str[0];
            String provName = str[1];
            String cityName = str[2];
            hotelTouristSource.setCountryName(countryName);
            hotelTouristSource.setProvName(provName);
            hotelTouristSource.setCityName(cityName);
            hotelTouristSource.setStatisticalDate(day);
            hotelTouristSource.setId(countryName + "_" + provName + "_" + cityName + "_" + day);
            hotelTouristSource.setCumulativeReception(Double.parseDouble(String.valueOf(bucket.getDocCount())));
            dataList.add(hotelTouristSource);
        }
        return dataList;
    }

    private List<ProvinceVO> getAdminArea() {
        return feignClient.listProvinces().getData();
    }

    private String findAdminAreaByCountyId(List<ProvinceVO> provinceList, String countyId) {
        for (ProvinceVO province : provinceList) {
            List<CityVO> cities = province.getCities();
            for (CityVO city : cities) {
                List<CountyVO> counties = city.getCounties();
                for (CountyVO county : counties) {
                    if (countyId.equals(county.getCountyId())) {
                        return province.getProvName() + "-" + city.getCityName() + "-" + county.getCountyName();
                    }
                }
            }
        }

        return null;
    }

    private List<ChartVO> buildData(List<ChartVO> list) {
        Map<String, Object> paramMap = new HashMap<>();
        String[] ageArr = {"50", "60", "70", "80", "90", "00"};
        List<ChartVO> dataList = new ArrayList<>();
        if (list != null) {
            list.forEach(chartVO -> {
                paramMap.put(chartVO.getName(), chartVO.getValue());
            });
        }
        for (String age : ageArr) {
            ChartVO chartVO;
            if (paramMap.containsKey(age)) {
                Integer peopleNum = (Integer) paramMap.get(age);
                chartVO = new ChartVO(age, peopleNum);
            } else {
                chartVO = new ChartVO(age, 0);
            }
            dataList.add(chartVO);
        }
        return dataList;
    }

    // ES中执行单纯sum操作
    private Double sumAggregation(String indexName, BoolQueryBuilder boolQueryBuilder, String aggs, String field) {
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(aggs).field(field);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withIndices(indexName).withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(sumAggregationBuilder)
                .build();
        return elasticsearchTemplate.query(query, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return sum.getValue();
        });
    }

    /**
     * 昨日累积接待：为保证几处显示数据一致，历史接待数据统一从hotel_passenger_reception(别名：diy_hotel_reception)进行查询（历史接待查询）
     */
    private Long historyCumulativeReception(String startTime, String endTime, List<String> stationIdList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(stationIdList)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_agg").field("cumulativeReception");
        NativeSearchQuery peopleCountQuery = new NativeSearchQueryBuilder().withIndices("diy_hotel_reception").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(sumAggregationBuilder).build();
        Double receptionCount = elasticsearchTemplate.query(peopleCountQuery, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return sum.getValue();
        });
        return receptionCount.longValue();
    }

    /**
     * 某日累积接待条件：checkin小于等于当日23：59：59，并且（checkout为空或checkout大于等于当日00:00:00）（今日累计接待查询）
     */
    @Deprecated
    private Long cumulativeReception(String startTime, String endTime, List<String> stationIdList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(stationIdList)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").lte(endTime));
        boolQueryBuilder.minimumShouldMatch(1);
        boolQueryBuilder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("checkoutTime")));
        boolQueryBuilder.should(QueryBuilders.rangeQuery("checkoutTime").gte(startTime));
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        return elasticsearchTemplate.count(query);
    }

    /**
     * 某日累积接待条件：凌晨校准库存+当日入住记录（新规则，解决大量入住记录无法退房问题）
     * day：T-1
     */
    private Long cumulativeReceptionPlus(String day, String startTime, String endTime, List<String> stationIdList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 当日入住记录
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        long checkInNum = elasticsearchTemplate.count(query);
        // 凌晨校准库存
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        boolQueryBuilder.must(QueryBuilders.termQuery("statisticalDate", day));
        long inventoryNum = sumAggregation("hotel_statistics", boolQueryBuilder, "reception_agg", "checkinNum").longValue();
        return checkInNum + inventoryNum;
    }

    /**
     * 昨日累积接待列表（历史接待查询）
     */
    @Deprecated
    private List<HotelPassengerReception> historyCumulativeReceptionList(String startTime, String endTime, List<String> stationIdList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(stationIdList)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        }
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("hotel_passenger_reception").withTypes("doc")
                .withQuery(boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime)))
                .withPageable(PageRequest.of(0, 10000));
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelPassengerReception.class);
    }

    /**
     * 昨日累积接待列表（历史接待查询）
     */
    private List<HotelReceptionCommon> historyCumulativeReceptionListPlus(String startTime, String endTime, List<String> stationIdList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("diy_hotel_reception").withTypes("doc")
                .withQuery(boolQueryBuilder.must(QueryBuilders.rangeQuery("statisticalDate").gte(startTime).lte(endTime)))
                .withPageable(PageRequest.of(0, 10000));
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), HotelReceptionCommon.class);
    }

    /**
     * 今日累积接待列表（实时接待查询）
     */
    private Map<String, Long> cumulativeReceptionListPlus(String startTime, String endTime, List<String> stationIdList) {
        Map<String, Long> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("checkinTime").gte(startTime).lte(endTime));
        String aggName = "stationId_agg";
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms(aggName)
                .field("stationId")
                .size(10000);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get(aggName);
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            String stationId = bucket.getKeyAsString();
            Long value = bucket.getDocCount();
            resultMap.put(stationId, value);
        }
        return resultMap;
    }

    /**
     * 今日累积接待列表（实时接待查询）
     */
    @Deprecated
    private Map<String, Long> cumulativeReceptionList(String startTime, String endTime, List<String> stationIdList) {
        Map<String, Long> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(stationIdList)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("stationId", stationIdList));
        }
        boolQueryBuilder.must(QueryBuilders.rangeQuery("checkinTime").lte(endTime));
        boolQueryBuilder.minimumShouldMatch(1);
        boolQueryBuilder.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("checkoutTime")));
        boolQueryBuilder.should(QueryBuilders.rangeQuery("checkoutTime").gte(startTime));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("stationId_agg")
                .field("stationId")
                .size(10000);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("hotel_checkin_record").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("stationId_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            String stationId = bucket.getKeyAsString();
            Long value = bucket.getDocCount();
            resultMap.put(stationId, value);
        }
        return resultMap;
    }
}
