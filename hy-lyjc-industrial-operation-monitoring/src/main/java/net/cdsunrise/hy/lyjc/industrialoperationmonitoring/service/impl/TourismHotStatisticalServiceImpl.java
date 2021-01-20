package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismHotStatisticalService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author LHY
 * @date 2019/11/25 11:16
 */
@Service
@Slf4j
public class TourismHotStatisticalServiceImpl implements TourismHotStatisticalService{

    private ElasticsearchTemplate elasticsearchTemplate;

    public TourismHotStatisticalServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Map<String, Object> countyStatistical() {
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("flag","minute"))
                .must(QueryBuilders.termQuery("scenicName","洪雅县"));
        SearchQuery query = (new NativeSearchQueryBuilder()).withIndices("tourist_local_data").withTypes("doc")
                .withQuery(boolQueryBuilder).build();
        List<TouristLocalData> dataList = elasticsearchTemplate.queryForList(query, TouristLocalData.class);
        // 县域实时游客数
        Integer realTimePeopleNum = dataList.size()>0?dataList.get(0).getPeopleNum():0;
        // 县域昨日高峰游客数
        String startTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD)+" 00:00:00";
        String endTime = DateUtil.getYesterdayString(DateUtil.PATTERN_YYYY_MM_DD)+" 23:59:59";
        boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag","hour"))
                .must(QueryBuilders.termQuery("scenicName","洪雅县"));
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("max_agg").field("peopleNum");
        NativeSearchQuery peopleNumQuery = (new NativeSearchQueryBuilder()).withIndices("tourist_local_data").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(maxAggregationBuilder).build();
        double maxPeopleNum = elasticsearchTemplate.query(peopleNumQuery, response -> {
            InternalMax max = (InternalMax)response.getAggregations().asList().get(0);
            return max.getValue();
        });
        resultMap.put("realTimePeopleNum",realTimePeopleNum);
        resultMap.put("maxPeopleNum",maxPeopleNum);
        return resultMap;
    }

    @Override
    public List<ChartVO> countyTourismTrend() {
        return commonQuery("洪雅县");
    }

    private List<ChartVO> commonQuery(String scenicName){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String startTime = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD)+" 00:00:00";
        String endTime = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD)+" 23:59:59";
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag","hour"))
                .must(QueryBuilders.termQuery("scenicName",scenicName));
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices("tourist_local_data").withTypes("doc")
                .withQuery(boolQueryBuilder).withPageable(PageRequest.of(0,50)).build();
        List<TouristLocalData> list = elasticsearchTemplate.queryForList(query, TouristLocalData.class);
        return buildYearData(list);
    }

    @Override
    public Map<String, List<ChartVO>> scenicTourismTrend() {
        Map<String, List<ChartVO>> resultMap = new HashMap<>();
        String[] scenicArr = {"瓦屋山","七里坪","柳江古镇","玉屏山","槽渔滩","主城区"};
        for (String s : scenicArr) {
            resultMap.put(s,commonQuery(s));
        }
        return resultMap;
    }

    private List<ChartVO> buildYearData(List<TouristLocalData> list){
        List<ChartVO> dataList = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        String[] hourArr = new String[24];
        for (int i=0;i<24;i++){
            if (i>9){
                hourArr[i] = String.valueOf(i);
            }else{
                hourArr[i] = "0"+i;
            }
        }
        list.forEach(touristLocalData -> {
            int index = touristLocalData.getTime().indexOf(" ");
            paramMap.put(touristLocalData.getTime().substring(index+1,index+3),touristLocalData.getPeopleNum());
        });
        for (String hour : hourArr) {
            ChartVO chartVO = new ChartVO();
            if (paramMap.containsKey(hour)){
                chartVO = new ChartVO(hour,(Integer) paramMap.get(hour));
            }else{
                chartVO = new ChartVO(hour,0);
            }
            dataList.add(chartVO);
        }
        return dataList;
    }
}
