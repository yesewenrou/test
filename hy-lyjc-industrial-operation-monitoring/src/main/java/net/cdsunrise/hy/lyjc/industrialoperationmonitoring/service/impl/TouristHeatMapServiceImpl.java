package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristMinuteLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPeopleHotData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristLocalDataAppVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristMinuteLocalMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristPeopleHotMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristHeatMapService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristPeopleHotVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/29 11:09
 */
@Slf4j
@Service
public class TouristHeatMapServiceImpl implements ITouristHeatMapService {

    private TouristPeopleHotMapper touristPeopleHotMapper;
    private TouristMinuteLocalMapper touristMinuteLocalMapper;
    private ElasticsearchTemplate elasticsearchTemplate;


    public TouristHeatMapServiceImpl(TouristPeopleHotMapper touristPeopleHotMapper,
                                     TouristMinuteLocalMapper touristMinuteLocalMapper,
                                     ElasticsearchTemplate elasticsearchTemplate) {
        this.touristPeopleHotMapper = touristPeopleHotMapper;
        this.touristMinuteLocalMapper = touristMinuteLocalMapper;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public List<TouristPeopleHotVO> listAllTouristPeopleHot() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("scenicName", "洪雅县"));
        Iterable<TouristPeopleHotData> all = touristPeopleHotMapper.search(queryBuilder);
        List<TouristPeopleHotVO> touristPeopleHotVoList = new ArrayList<>();
        for (TouristPeopleHotData touristPeopleHotData : all) {
            TouristPeopleHotVO touristPeopleHotVO = new TouristPeopleHotVO();
            BeanUtils.copyProperties(touristPeopleHotData, touristPeopleHotVO);
            touristPeopleHotVoList.add(touristPeopleHotVO);
        }
        return touristPeopleHotVoList;
    }

    @Override
    public TouristLocalDataAppVO touristStatisticsForApp() {
        // 统计今日 tourist_local_data, flag: minute
        Map<String, Integer> realTime = touristStatisticsToday();
        // 统计昨日 tourist_local_data, flag: day
        Map<String, Integer> yesterday = touristStatisticsYesterday();
        // 统计今年 tourist_local_data, flag: day
        Map<String, Integer> currentYear = touristStatisticsCurrentYear();
        // 当日累计, tourist_minute_local_data: minute
        Map<String, Integer> currentDay = touristStatisticsCurrentDay();
        TouristLocalDataAppVO touristLocalDataAppVO = new TouristLocalDataAppVO();
        touristLocalDataAppVO.setRealTime(realTime);
        touristLocalDataAppVO.setYesterday(yesterday);
        touristLocalDataAppVO.setCurrentYear(currentYear);
        touristLocalDataAppVO.setCurrentDay(currentDay);
        return touristLocalDataAppVO;
    }

    /**
     * 统计今日累计
     */
    private Map<String, Integer> touristStatisticsCurrentDay() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String startTime = DateUtil.getToday() + " 00:00:00";
        String endTime = DateUtil.getToday() + " 23:59:59";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "minute"));
        Page<TouristMinuteLocalData> page = touristMinuteLocalMapper.search(boolQueryBuilder, PageRequest.of(0, 5000, Sort.by(Sort.Direction.ASC, "time")));
        List<TouristMinuteLocalData> content = page.getContent();
        String[] scenicNames = {"洪雅县","瓦屋山", "柳江古镇", "玉屏山", "七里坪", "槽渔滩"};
        Map<String, Integer> todayStatistics = new HashMap<>(8);
        String waWuShan = "瓦屋山";
        if (CollectionUtils.isEmpty(content)) {
            for (String scenicName : scenicNames) {
                if (waWuShan.equals(scenicName)) {
                    todayStatistics.put(scenicName + "区域", 0);
                } else {
                    todayStatistics.put(scenicName, 0);
                }
            }
        } else {
            for (String scenicName : scenicNames) {
                List<TouristMinuteLocalData> scenicTotal = content.stream().filter(item -> scenicName.equals(item.getScenicName())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(scenicTotal)) {
                    todayStatistics.put(scenicName, 0);
                } else {
                    int todayTotalPeopleNum = scenicTotal.get(0).getPeopleNum();
                    for (int i = 1; i < scenicTotal.size(); i++) {
                        int c = scenicTotal.get(i).getPeopleNum() - scenicTotal.get(i - 1).getPeopleNum();
                        todayTotalPeopleNum += Math.max(c, 0);
                    }
                    if (waWuShan.equals(scenicName)) {
                        todayStatistics.put(scenicName + "区域", todayTotalPeopleNum);
                    } else {
                        todayStatistics.put(scenicName, todayTotalPeopleNum);
                    }
                }
            }
        }
        // 瓦屋山入园统计
        waWuShanStatistics(todayStatistics, startTime.substring(0, 10), endTime.substring(0, 10), "瓦屋山", false);
        return todayStatistics;
    }

    /**
     * 统计今年 tourist_local_data, flag: day
     * @return
     */
    private Map<String, Integer> touristStatisticsCurrentYear() {
        String todayString = DateUtil.getTodayString("yyyy-MM-dd");
        String year = DateUtil.getTodayString("yyyy");
        String currentYearFirstDay = year + "-01-01";
        // 统计周边游客数 : "洪雅县", "柳江古镇", "玉屏山", "七里坪", "槽渔滩","主城区"
        Map<String, Integer> currentYear = touristStatisticsByRange(currentYearFirstDay, todayString, "day");
        // 统计在园人数丶 入园人数: 瓦屋山
        waWuShanStatistics(currentYear, currentYearFirstDay, todayString, "瓦屋山", false);
        return currentYear;
    }

    /**
     * 统计昨日 tourist_local_data, flag: day
     * @return
     */
    private Map<String, Integer> touristStatisticsYesterday() {
        String dateYesterday = DateUtil.getYesterdayString("yyyy-MM-dd");
        // 统计周边游客数 : "洪雅县", "柳江古镇", "玉屏山", "七里坪", "槽渔滩","主城区"
        Map<String, Integer> yesterday = touristStatisticsByRange(dateYesterday, dateYesterday, "day");
        // 统计在园人数丶 入园人数: 瓦屋山
        waWuShanStatistics(yesterday, dateYesterday, dateYesterday, "瓦屋山", false);
        return yesterday;
    }


    /**
     * 今日/实时 统计, tourist_local_data, flag: minute
     * @return Map<String, Integer>
     */
    private Map<String, Integer> touristStatisticsToday() {
        String dateToday = DateUtil.getToday();
        // 统计周边游客数 : "洪雅县", "柳江古镇", "玉屏山", "七里坪", "槽渔滩","主城区"
        Map<String, Integer> realTime = touristStatisticsByRange(dateToday, dateToday, "minute");
        // 统计在园人数丶 入园人数: 瓦屋山
        waWuShanStatistics(realTime, dateToday, dateToday, "瓦屋山", true);
        return realTime;
    }

    /**
     *
     * @param realTime
     * @param begin 开始时间
     * @param end 结束时间
     * @param scenicName 景区名称
     * @param isSelectInPark 是否查询在园游客数
     */
    private void waWuShanStatistics (Map<String, Integer> realTime, String begin, String end, String scenicName, Boolean isSelectInPark) {
        String startTime = begin + " 00:00:00";
        String endTime = end + " 23:59:59";

        // 按景区名称和时间范围 条件查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("scenicName", scenicName))
                .must(QueryBuilders.rangeQuery("responseTime").gte(startTime).lte(endTime));

        // 聚合对 realTimeTouristNum 和 lastTodayTouristCount 求和 
        String sumRealTimeTouristNum = "sumRealTimeTouristNum";
        String sumTodayTouristCount = "sumTodayTouristCount";
        SumAggregationBuilder sumRealTimeTouristNumAggregation = AggregationBuilders.sum(sumRealTimeTouristNum).field("realTimeTouristNum");

        SumAggregationBuilder sumTodayTouristCountAggregation = AggregationBuilders.sum(sumTodayTouristCount).field("lastTodayTouristCount");

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket_daily").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumRealTimeTouristNumAggregation)
                .addAggregation(sumTodayTouristCountAggregation);
        
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();

            InternalSum todayTouristCountSum = aggregations.get(sumTodayTouristCount);
            realTime.put(scenicName + "入园", Double.valueOf(todayTouristCountSum.getValue()).intValue());

            // 是否查询在园游客数
            if (isSelectInPark) {
                InternalSum realTimeTouristNumSum = aggregations.get(sumRealTimeTouristNum);
                realTime.put(scenicName + "在园", Double.valueOf(realTimeTouristNumSum.getValue()).intValue());
            }

            return null;
        });
    }

    /**
     * 统计周边游客数 : "洪雅县", "柳江古镇", "玉屏山", "七里坪", "槽渔滩","主城区"
     * @param begin yyyy-MM-dd
     * @param end yyyy-MM-dd
     * @param flag minute | day
     * @return Statistics
     */
    private Map<String, Integer> touristStatisticsByRange(String begin, String end, String flag) {
        String startTime = begin + " 00:00:00";
        String endTime = end + " 23:59:59";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", flag));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("aggByScenicName").field("scenicName");
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");
        termsAggregationBuilder.subAggregation(sumAggregationBuilder);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_local_data").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(termsAggregationBuilder);
        Map<String, Integer> map = new HashMap<>(8);
        List<String> scenicNames = new ArrayList<>(Arrays.asList("洪雅县", "柳江古镇", "玉屏山", "七里坪", "槽渔滩", "主城区", "瓦屋山"));
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
           StringTerms term = response.getAggregations().get("aggByScenicName");
            term.getBuckets().forEach(bucket -> {
                String scenicName = bucket.getKeyAsString();
                if (scenicNames.contains(scenicName)) {
                    Sum sum = bucket.getAggregations().get("sumPeopleNum");
                    Integer peopleNum = Double.valueOf(sum.getValue()).intValue();
                    String waWuShan = "瓦屋山";
                    if (waWuShan.equals(scenicName)) {
                        map.put(scenicName + "区域", peopleNum);
                    } else {
                        map.put(scenicName, peopleNum);
                    }
                }
            });
            return null;
        });
        return map;
    }


}
