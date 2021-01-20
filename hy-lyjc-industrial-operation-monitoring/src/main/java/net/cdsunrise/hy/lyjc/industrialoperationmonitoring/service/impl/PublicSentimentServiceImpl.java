package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.PublicSentiment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.SentimentEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.PublicSentimentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentTrendVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentVO;
import net.cdsunrise.hy.record.starter.util.PageUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: LHY
 * @Date: 2019/9/25 15:40
 */
@Service
@Slf4j
public class PublicSentimentServiceImpl implements PublicSentimentService {

    private String index = "reptile";
    private String type = "hy";

    private ElasticsearchTemplate elasticsearchTemplate;

    public PublicSentimentServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @SuppressWarnings("all")
    @Override
    public PageResult<PublicSentimentVO> list(Integer page, Integer size, PublicSentimentCondition condition) {
        if (condition == null) {
            condition = new PublicSentimentCondition();
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(condition.getTitle())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", condition.getTitle());
            boolQueryBuilder.must(matchQueryBuilder);
        }
        if (condition.getSentiment() != null) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("sentiment", condition.getSentiment());
            boolQueryBuilder.must(termQueryBuilder);
        }
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(index).withTypes(type)
                .withQuery(boolQueryBuilder).build();
        // 增加按照日期倒序排列
        Sort descSort = Sort.by(Sort.Direction.DESC, "adddate");
        query.addSort(descSort);
        if (page != null && size != null) {
            // 简单防止乱输参数
            page = page > 0 ? page - 1 : 0;
            size = size > 0 ? size : 10;
            PageRequest pageRequest = PageRequest.of(page, size);
            query.setPageable(pageRequest);
        }
        Page<PublicSentiment> publicSentimentPage = elasticsearchTemplate.queryForPage(query, PublicSentiment.class);
        List<PublicSentiment> collect = publicSentimentPage.getContent();
        return PageUtil.page(publicSentimentPage, publicSentiment -> {
            PublicSentimentVO publicSentimentVO = new PublicSentimentVO();
            String[] temp = publicSentiment.getAdddate().split(" ");
            BeanUtils.copyProperties(publicSentiment, publicSentimentVO);
            publicSentimentVO.setAddDate(temp[0]);
            return publicSentimentVO;
        }, collect);
    }

    /**
     * 舆情最近走势（7日舆情和30日舆情）
     */
    @Override
    public Map<String, Object> publicSentimentTrend(String startTime, String endTime) {
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("trend", dateAggregation(startTime, endTime));
        // 情感分析
        String[] sentimentArr = {SentimentEnum.POSITIVE.getName(), SentimentEnum.NEUTRAL.getName(), SentimentEnum.NEGATIVE.getName()};
        List<PublicSentimentTrendVO> sentimentAnalyze = buildData(new ArrayList<>(Arrays.asList(sentimentArr)), sumAggregation(startTime, endTime));
        resultMap.put("sentimentAnalyze", sentimentAnalyze);
        // 关键词
        resultMap.put("keyword", wordFrequencyAggregation(startTime, endTime));
        return resultMap;
    }


    /**
     * 舆情最近走势（自定义筛选时间）
     */
    @Override
    public Map<String, Object> publicSentimentTrend(Long beginDate, Long endDate) {
        String startTime = DateUtil.format(new Date(beginDate), DateUtil.PATTERN_YYYY_MM_DD);
        String endTime = DateUtil.format(new Date(endDate), DateUtil.PATTERN_YYYY_MM_DD);
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("trend", dateAggregation(startTime, endTime));
        // 情感分析
        String[] sentimentArr = {SentimentEnum.POSITIVE.getName(), SentimentEnum.NEUTRAL.getName(), SentimentEnum.NEGATIVE.getName()};
        List<PublicSentimentTrendVO> sentimentAnalyze = buildData(new ArrayList<>(Arrays.asList(sentimentArr)), sumAggregation(startTime, endTime));
        resultMap.put("sentimentAnalyze", sentimentAnalyze);
        // 关键词
        resultMap.put("keyword", wordFrequencyAggregation(startTime, endTime));
        // 舆情柱状图比例
        resultMap.put("sentimentAnalyzePercent", sentimentAnalyzePercent(sentimentAnalyze));
        return resultMap;
    }

    private List<PublicSentimentTrendVO> sentimentAnalyzePercent(List<PublicSentimentTrendVO> sentimentAnalyze) {
        // 舆情按固定比例分类 涉旅企业 0.05, 公共服务0.25, 景区评价0.1, 旅游产品0.45, 其他0.15
        PublicSentimentTrendVO company = new PublicSentimentTrendVO("涉旅企业", 0L);
        PublicSentimentTrendVO publicService = new PublicSentimentTrendVO("公共服务", 0L);
        PublicSentimentTrendVO comment = new PublicSentimentTrendVO("景区评价", 0L);
        PublicSentimentTrendVO travel = new PublicSentimentTrendVO("旅游产品", 0L);
        PublicSentimentTrendVO other = new PublicSentimentTrendVO("其他", 0L);
        if (!CollectionUtils.isEmpty(sentimentAnalyze)) {
            long total = sentimentAnalyze.stream().mapToLong(PublicSentimentTrendVO::getValue).sum();
            long companyCount = (long) (total * 0.05);
            long publicServiceCount = (long) (total * 0.25);
            long commentCount = (long) (total * 0.1);
            long travelCount = (long) (total * 0.45);
            long otherCount = total - commentCount - publicServiceCount - commentCount - travelCount;
            company.setValue(companyCount);
            publicService.setValue(publicServiceCount);
            comment.setValue(commentCount);
            travel.setValue(travelCount);
            other.setValue(otherCount);
        }
        return Arrays.asList(company, publicService, comment, travel, other);
    }

    // ES中执行单纯sum操作
    private List<PublicSentimentTrendVO> sumAggregation(String startTime, String endTime) {
        List<PublicSentimentTrendVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("adddate").gte(startTime).lte(endTime));
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("temp_aggs")
                .field("sentiment");
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(index).withTypes(type)
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        LongTerms longTerms = temp.get("temp_aggs");
        // 获得所有的桶
        List<LongTerms.Bucket> buckets = longTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (LongTerms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            String name = SentimentEnum.getByNumber(key) != null ? SentimentEnum.getByNumber(key).getName() : key;
            PublicSentimentTrendVO trendVO = new PublicSentimentTrendVO(name, bucket.getDocCount());
            dataList.add(trendVO);
        }
        return dataList;
    }

    private List<PublicSentimentTrendVO> buildData(List<String> originList, List<PublicSentimentTrendVO> list) {
        Map<String, Object> paramMap = new HashMap<>();
        // 之所以定义dataList，是为了保证最终生成的结构是按照日期进行排序的
        List<PublicSentimentTrendVO> dataList = new ArrayList<>();
        list.forEach(publicSentimentTrendVO -> {
            paramMap.put(publicSentimentTrendVO.getName(), publicSentimentTrendVO.getValue());
        });
        for (String str : originList) {
            PublicSentimentTrendVO trendVO;
            if (!paramMap.containsKey(str)) {
                trendVO = new PublicSentimentTrendVO(str, 0L);
            } else {
                trendVO = new PublicSentimentTrendVO(str, Long.parseLong(String.valueOf(paramMap.get(str))));
            }
            dataList.add(trendVO);
        }
        return dataList;
    }

    /**
     * extendedBounds属性：当查询2.25-3.2这个时间段数据时，假如只有26和27号有数据，那么加上该属性后，可以自动将25,28,29,1,2号补全为0
     */
    private List<PublicSentimentTrendVO> dateAggregation(String startTime, String endTime) {
        List<PublicSentimentTrendVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("adddate").gte(startTime).lte(endTime));
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("time_histogram")
                .field("adddate")
                .format("yyyy-MM-dd")
                .extendedBounds(new ExtendedBounds(startTime, endTime))
                .dateHistogramInterval(DateHistogramInterval.DAY);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(index).withTypes(type)
                .withQuery(boolQueryBuilder).addAggregation(dateHistogram).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        InternalDateHistogram internalDateHistogram = temp.get("time_histogram");
        // 获得所有的桶
        List<InternalDateHistogram.Bucket> buckets = internalDateHistogram.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (InternalDateHistogram.Bucket bucket : buckets) {
            PublicSentimentTrendVO scenicTouristVO = new PublicSentimentTrendVO(bucket.getKeyAsString(), bucket.getDocCount());
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }

    private List<PublicSentimentTrendVO> wordFrequencyAggregation(String startTime, String endTime) {
        List<PublicSentimentTrendVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("adddate").gte(startTime).lte(endTime));
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("message").field("content").size(20);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(index).withTypes(type)
                .withQuery(boolQueryBuilder).addAggregation(aggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get("message");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            PublicSentimentTrendVO scenicTouristVO = new PublicSentimentTrendVO(bucket.getKeyAsString(), bucket.getDocCount());
            dataList.add(scenicTouristVO);
        }
        return dataList;
    }
}
