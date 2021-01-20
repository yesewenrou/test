package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.StringUtil;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.AdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.AdministrativeAreaVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.ProvinceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyBusinessCircleConsumption;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyConsumptionCity;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyIndustryConsumption;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.IndustryConsumptionResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.AdminAreaLevelEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.CountyBusinessCircleConsumptionMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismConsumptionService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionMapVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyAndValue;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.*;
import net.cdsunrise.hy.record.starter.util.PageUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LHY
 * @date 2019/11/28 14:12
 */
@Service
@Slf4j
public class TourismConsumptionServiceImpl implements TourismConsumptionService {
    private static final String CHINA_ID = "CHINA";

    private ElasticsearchTemplate elasticsearchTemplate;
    private AdministrativeAreaFeignClient feignClient;
    private CountyBusinessCircleConsumptionMapper businessCircleConsumptionMapper;
    private AdministrativeAreaFeignClient administrativeAreaFeignClient;

    public TourismConsumptionServiceImpl(ElasticsearchTemplate elasticsearchTemplate, AdministrativeAreaFeignClient feignClient, CountyBusinessCircleConsumptionMapper businessCircleConsumptionMapper, AdministrativeAreaFeignClient administrativeAreaFeignClient) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.feignClient = feignClient;
        this.businessCircleConsumptionMapper = businessCircleConsumptionMapper;
        this.administrativeAreaFeignClient = administrativeAreaFeignClient;
    }

    @Override
    public Map<String, Object> consumptionSummary(Integer page, Integer size, TourismConsumptionCondition condition) {
        String index = "county_business_circle_consumption";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(condition.getStartTime()).lte(condition.getEndTime()));
        if (!StringUtil.isEmpty(condition.getCbdName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("cbdName", condition.getCbdName()));
        }
        if (!StringUtil.isEmpty(condition.getTravellerType())) {
            // 3表示眉山市（不含洪雅县本地），所以属于省内
            if ("1".equals(condition.getTravellerType())) {
                boolQueryBuilder.must(QueryBuilders.termsQuery("travellerType", "1", "3"));
            } else {
                boolQueryBuilder.must(QueryBuilders.termQuery("travellerType", condition.getTravellerType()));
            }
        }
        NativeSearchQuery query = commonQuery(index, type, boolQueryBuilder, page, size);
        Page<CountyBusinessCircleConsumption> businessCircleConsumptionPage = elasticsearchTemplate.queryForPage(query, CountyBusinessCircleConsumption.class);
        PageResult<CountyBusinessCircleConsumptionVO> pageResult = PageUtil.page(businessCircleConsumptionPage, businessCircleConsumption -> {
            CountyBusinessCircleConsumptionVO businessCircleConsumptionVO = new CountyBusinessCircleConsumptionVO();
            BeanUtils.copyProperties(businessCircleConsumption, businessCircleConsumptionVO);
            // 人均消费金额
            Double perConsumption = decimalTransfer(String.valueOf(businessCircleConsumption.getTransAt()), String.valueOf(businessCircleConsumption.getAcctNum()), 2);
            businessCircleConsumptionVO.setPerConsumption(perConsumption);
            // 人均消费笔数
            Double perConsumptionPens = decimalTransfer(String.valueOf(businessCircleConsumption.getTransNum()), String.valueOf(businessCircleConsumption.getAcctNum()), 2);
            businessCircleConsumptionVO.setPerConsumptionPens(perConsumptionPens);
            return businessCircleConsumptionVO;
        }, businessCircleConsumptionPage.getContent());
        resultMap.put("consumptionSummaryList", pageResult);
        // 各商圈金额合计
        resultMap.put("businessCircle", aggregation(index, type, boolQueryBuilder, "cbdName", "transAt", 10));
        return resultMap;
    }

    @Override
    public Map<String, Object> consumptionSource(Integer page, Integer size, TourismConsumptionCondition condition) {
        String index = "county_consumption_city";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(condition.getStartTime()).lte(condition.getEndTime()));
        if (!StringUtil.isEmpty(condition.getSourceProvince())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("sourceProvince", condition.getSourceProvince()));
        }
        if (!StringUtil.isEmpty(condition.getSourceCity())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("sourceCity", condition.getSourceCity()));
        }
        NativeSearchQuery query = commonQuery(index, type, boolQueryBuilder, page, size);
        Page<CountyConsumptionCity> countyConsumptionCityPage = elasticsearchTemplate.queryForPage(query, CountyConsumptionCity.class);
        PageResult<CountyConsumptionCityVO> pageResult = PageUtil.page(countyConsumptionCityPage, countyConsumptionCity -> {
            CountyConsumptionCityVO countyConsumptionCityVO = new CountyConsumptionCityVO();
            BeanUtils.copyProperties(countyConsumptionCity, countyConsumptionCityVO);
            Result<AdministrativeAreaVO> provinceByCity = feignClient.getProvinceByCityId(countyConsumptionCity.getSourceCity());
            AdministrativeAreaVO data = provinceByCity.getData();
            if (provinceByCity.getSuccess() && data != null) {
                countyConsumptionCityVO.setSourceProvince(data.getProvName());
                countyConsumptionCityVO.setSourceCity(data.getCityName());
            }
            return countyConsumptionCityVO;
        }, countyConsumptionCityPage.getContent());
        resultMap.put("consumptionSourceList", pageResult);
        // 综合数据统计
        SumAggregationBuilder sumTransAt = AggregationBuilders.sum("transAt_count").field("transAt");
        SumAggregationBuilder sumTransNum = AggregationBuilders.sum("transNum_count").field("transNum");
        SumAggregationBuilder sumAcctNum = AggregationBuilders.sum("acctNum_count").field("acctNum");
        Double transAtAmount = sumAggregation(index, type, boolQueryBuilder, sumTransAt);
        resultMap.put("transAtAmount", decimalTransfer(String.valueOf(transAtAmount), "1", 2));
        resultMap.put("transNumAmount", sumAggregation(index, type, boolQueryBuilder, sumTransNum));
        resultMap.put("acctNumAmount", sumAggregation(index, type, boolQueryBuilder, sumAcctNum));
        return resultMap;
    }

    @Override
    public Map<String, Object> industryContribution(Integer page, Integer size, TourismConsumptionCondition condition) {
        String index = "county_industry_consumption";
        String type = "doc";
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(condition.getStartTime()).lte(condition.getEndTime()));
        if (!StringUtil.isEmpty(condition.getType())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("type", condition.getType()));
        }
        NativeSearchQuery query = commonQuery(index, type, boolQueryBuilder, page, size);
        Page<CountyIndustryConsumption> industryConsumptionPage = elasticsearchTemplate.queryForPage(query, CountyIndustryConsumption.class);
        PageResult<CountyIndustryConsumptionVO> pageResult = PageUtil.page(industryConsumptionPage, industryConsumption -> {
            CountyIndustryConsumptionVO industryConsumptionVO = new CountyIndustryConsumptionVO();
            BeanUtils.copyProperties(industryConsumption, industryConsumptionVO);
            // 人均消费金额
            Double perConsumption = decimalTransfer(String.valueOf(industryConsumption.getTransAt()), String.valueOf(industryConsumption.getAcctNum()), 2);
            industryConsumptionVO.setPerConsumption(perConsumption);
            // 人均消费笔数
            Double perConsumptionPens = decimalTransfer(String.valueOf(industryConsumption.getTransNum()), String.valueOf(industryConsumption.getAcctNum()), 2);
            industryConsumptionVO.setPerConsumptionPens(perConsumptionPens);
            industryConsumptionVO.setTransAtRatio(decimalTransfer(String.valueOf(industryConsumption.getTransAtRatio()), "1", 4));
            return industryConsumptionVO;
        }, industryConsumptionPage.getContent());
        resultMap.put("industryContributionList", pageResult);
        // 综合数据统计
        SumAggregationBuilder sumTransAt = AggregationBuilders.sum("transAt_count").field("transAt");
        SumAggregationBuilder sumTransAtTotal = AggregationBuilders.sum("transAtTotal_count").field("transAtTotal");
        Double transAtAmount = sumAggregation(index, type, boolQueryBuilder, sumTransAt);
        Double transAtTotal = sumAggregation(index, type, boolQueryBuilder, sumTransAtTotal);
        Double transAtRatioAmount = 0.0;
        if (transAtTotal > 0) {
            transAtRatioAmount = decimalTransfer(String.valueOf(transAtAmount), String.valueOf(transAtTotal), 4);
        }
        resultMap.put("transAtAmount", decimalTransfer(String.valueOf(transAtAmount), "1", 2));
        resultMap.put("transAtTotal", decimalTransfer(String.valueOf(transAtTotal), "1", 2));
        resultMap.put("transAtRatioAmount", transAtRatioAmount);
        return resultMap;
    }

    @Override
    public ConsumptionMapVO provinceConsumptionList(ProvinceConsumptionCondition condition) {
        ConsumptionMapVO consumptionMapVO = new ConsumptionMapVO();

        LocalDateTime beginDate = condition.getBeginDate();
        LocalDateTime endDate = condition.getEndDate();
        if (Objects.isNull(beginDate) || Objects.isNull(endDate)) {
            endDate = getMaxDealDay().atStartOfDay();
            beginDate = endDate.withDayOfMonth(1);
        } else {
            beginDate = beginDate.toLocalDate().atStartOfDay();
            endDate = endDate.toLocalDate().atStartOfDay();
        }

        consumptionMapVO.setBeginDate(beginDate);
        consumptionMapVO.setEndDate(endDate);
        // 行政区划
        List<ProvinceVO> adminArea = getAdminArea();
        // 查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        );
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder transNumSumAggBuilder = AggregationBuilders.sum("transNum_sum").field("transNum");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        // 省内
        TermsAggregationBuilder sourceCityTermsAggBuilder = AggregationBuilders.terms("sourceCity_terms")
                .field("sourceCity")
                .size(100)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        FilterAggregationBuilder travellerType1FilterAggBuilder = AggregationBuilders.filter("travellerType_filter1",
                // 3表示眉山市（不含洪雅县本地），所以属于省内
                QueryBuilders.termsQuery("travellerType", "1", "3")
        ).subAggregation(sourceCityTermsAggBuilder);
        // 省外
        TermsAggregationBuilder sourceProvinceTermsAggBuilder = AggregationBuilders.terms("sourceProvince_terms")
                .field("sourceProvince")
                .size(100)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        FilterAggregationBuilder travellerType2FilterAggBuilder = AggregationBuilders.filter("travellerType_filter2",
                QueryBuilders.termQuery("travellerType", "2")
        ).subAggregation(sourceProvinceTermsAggBuilder);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(travellerType1FilterAggBuilder)
                .addAggregation(travellerType2FilterAggBuilder);

        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            // 消费总金额
            InternalSum transAtSumAgg = aggregations.get("transAt_sum");
            String totalTransAtString = transAtSumAgg.getValueAsString();
            BigDecimal totalTransAt = new BigDecimal(totalTransAtString).setScale(2, RoundingMode.HALF_UP);
            consumptionMapVO.setTotalTransAt(totalTransAt);
            // 省内
            consumptionMapVO.setInnerProvince(getConsumptionList(aggregations, "travellerType_filter1", "sourceCity_terms", true, adminArea));
            consumptionMapVO.setOuterProvince(getConsumptionList(aggregations, "travellerType_filter2", "sourceProvince_terms", false, adminArea));

            return null;
        });

        return consumptionMapVO;
    }


    @Override
    public List<ConsumptionStatisticsVO> consumptionStatistics(ConsumptionStatisticsReq req) {
        String beginDate = req.getBeginDate().format(DateUtil.LOCAL_DATE);
        String endDate = req.getEndDate().format(DateUtil.LOCAL_DATE);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(beginDate).lte(endDate));
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder transNumSumAggBuilder = AggregationBuilders.sum("transNum_sum").field("transNum");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        List<ConsumptionStatisticsVO> resultList = new ArrayList<>(16);
        // 洪雅县域
        NativeSearchQuery hySearchQuery = new NativeSearchQueryBuilder()
                .withIndices("county_consumption")
                .withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(transNumSumAggBuilder)
                .addAggregation(acctNumSumAggBuilder)
                .build();
        elasticsearchTemplate.query(hySearchQuery, response -> {
            dealConsumptionStatistics("洪雅县域", response.getAggregations(), resultList);
            return null;
        });
        // 各商圈
        TermsAggregationBuilder cbdNameTermsAggBuilder = AggregationBuilders.terms("cbdName_terms")
                .field("cbdName")
                .size(50)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        NativeSearchQuery bcSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("county_business_circle_consumption")
                .withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(cbdNameTermsAggBuilder)
                .build();
        elasticsearchTemplate.query(bcSearchQuery, response -> {
            Aggregations aggregations = response.getAggregations();
            StringTerms cbdNameTermsAgg = aggregations.get("cbdName_terms");
            List<StringTerms.Bucket> buckets = cbdNameTermsAgg.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                String cbdName = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                dealConsumptionStatistics(cbdName, bucketAggregations, resultList);
            }
            return null;
        });

        return resultList;
    }

    private void dealConsumptionStatistics(String cbdName, Aggregations aggregations, List<ConsumptionStatisticsVO> list) {
        InternalSum transAtSumAgg = aggregations.get("transAt_sum");
        InternalSum transNumSumAgg = aggregations.get("transNum_sum");
        InternalSum acctNumSumAgg = aggregations.get("acctNum_sum");
        BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal transNum = new BigDecimal(transNumSumAgg.getValueAsString());
        BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
        BigDecimal transAtAvg = BigDecimal.ZERO;
        BigDecimal transNumAvg = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(acctNum) != 0) {
            transAtAvg = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
            transNumAvg = transNum.divide(acctNum, 2, RoundingMode.HALF_UP);
        }

        ConsumptionStatisticsVO consumptionStatisticsVO = new ConsumptionStatisticsVO();
        consumptionStatisticsVO.setCbdName(cbdName);
        consumptionStatisticsVO.setTransAt(transAt);
        consumptionStatisticsVO.setTransNum(transNum.intValue());
        consumptionStatisticsVO.setAcctNum(acctNum.intValue());
        consumptionStatisticsVO.setTransAtAvg(transAtAvg);
        consumptionStatisticsVO.setTransNumAvg(transNumAvg);
        list.add(consumptionStatisticsVO);
    }

    @Override
    public ConsumptionTrendResp consumptionTrend(ConsumptionTrendReq req) {

        LocalDateTime beginDate = req.getBeginDate();
        LocalDateTime endDate = req.getEndDate();
        String cbdName = req.getCbdName();
        AssertUtil.notNull(beginDate, BusinessExceptionEnum.PARAM_ERROR.fail());
        AssertUtil.notNull(endDate, BusinessExceptionEnum.PARAM_ERROR.fail());
        AssertUtil.isTrue(beginDate.isBefore(endDate), BusinessExceptionEnum.PARAM_ERROR.fail());


        //今年数据
        Map<LocalDate, Double> thisYearHistogram = dateHistogram(req.fetchOffsetBeginDate(0), req.fetchOffsetEndDate(0), cbdName);
        //去年数据
        Map<LocalDate, Double> lastYearHistogram = dateHistogram(req.fetchOffsetBeginDate(1), req.fetchOffsetEndDate(1), cbdName);
        //前年数据
        Map<LocalDate, Double> lastTwoYearHistogram = dateHistogram(req.fetchOffsetBeginDate(2), req.fetchOffsetEndDate(2), cbdName);

        List<ConsumptionTrendResp.Detail> thisYearList = compareConsumptionTrend(thisYearHistogram, lastYearHistogram);
        List<ConsumptionTrendResp.Detail> lastYearList = compareConsumptionTrend(lastYearHistogram, lastTwoYearHistogram);

        ConsumptionTrendResp resp = new ConsumptionTrendResp();
        resp.setThisYearList(thisYearList);
        resp.setLastYearList(lastYearList);
        return resp;
    }

    @Override
    public IndustryConsumptionResp industryConsumption(IndustryConsumptionReq req) {
        String indexName = "county_industry_consumption";
        String indexType = "doc";
        String beginDate = req.getBeginDate().format(DateUtil.LOCAL_DATE);
        String endDate = req.getEndDate().format(DateUtil.LOCAL_DATE);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(beginDate).lte(endDate));
        SumAggregationBuilder transAtTotalSumAggBuilder = AggregationBuilders.sum("transAtTotal_sum").field("transAtTotal");
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder transNumSumAggBuilder = AggregationBuilders.sum("transNum_sum").field("transNum");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        TermsAggregationBuilder typeTermsAggBuilder = AggregationBuilders.terms("type_terms")
                .field("type")
                .size(50)
                .subAggregation(transAtTotalSumAggBuilder)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(typeTermsAggBuilder)
                .build();

        return elasticsearchTemplate.query(nativeSearchQuery, response -> {
            Aggregations aggregations = response.getAggregations();
            InternalSum totalTransAtSumAgg = aggregations.get("transAt_sum");
            BigDecimal totalTransAt = new BigDecimal(totalTransAtSumAgg.getValueAsString()).setScale(2, RoundingMode.HALF_UP);
            StringTerms typeTermsAgg = aggregations.get("type_terms");
            List<StringTerms.Bucket> buckets = typeTermsAgg.getBuckets();
            List<IndustryConsumptionResp.Detail> list = buckets.stream().map(bucket -> {
                String type = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                InternalSum transAtTotalSumAgg = bucketAggregations.get("transAtTotal_sum");
                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                InternalSum transNumSumAgg = bucketAggregations.get("transNum_sum");
                InternalSum acctNumSumAgg = bucketAggregations.get("acctNum_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal transAtTotal = new BigDecimal(transAtTotalSumAgg.getValueAsString()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal transNum = new BigDecimal(transNumSumAgg.getValueAsString());
                BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
                BigDecimal avgTransAt = BigDecimal.ZERO;
                BigDecimal avgTransNum = BigDecimal.ZERO;
                BigDecimal transAtRatio = BigDecimal.ZERO;
                if (BigDecimal.ZERO.compareTo(acctNum) != 0) {
                    avgTransAt = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
                    avgTransNum = transNum.divide(acctNum, 2, RoundingMode.HALF_UP);
                }
                if (BigDecimal.ZERO.compareTo(transAtTotal) != 0) {
                    transAtRatio = transAt.divide(transAtTotal, 2, RoundingMode.HALF_UP);
                }

                IndustryConsumptionResp.Detail detail = new IndustryConsumptionResp.Detail();
                detail.setType(type);
                detail.setTransAt(transAt);
                detail.setTransAtRatio(transAtRatio);
                detail.setTransNum(transNum.intValue());
                detail.setAcctNum(acctNum.intValue());
                detail.setAvgTransAt(avgTransAt);
                detail.setAvgTransNum(avgTransNum);

                return detail;
            }).collect(Collectors.toList());

            IndustryConsumptionResp resp = new IndustryConsumptionResp();
            resp.setTotalTransAt(totalTransAt);
            resp.setList(list);

            return resp;
        });
    }

    private Double fetchTotalTransAt() {
        String indexName = "county_industry_consumption";
        String indexType = "doc";
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .addAggregation(transAtSumAggBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);

        InternalSum transAtSum = aggregations.get("transAt");
        return transAtSum.getValue();
    }

    private List<ConsumptionTrendResp.Detail> compareConsumptionTrend(Map<LocalDate, Double> thisYearHistogram, Map<LocalDate, Double> lastYearHistogram) {
        if (!CollectionUtils.isEmpty(thisYearHistogram)) {
            return thisYearHistogram.entrySet().stream()
                    .map(e -> {
                        LocalDate time = e.getKey();
                        Double transAtThisYear = e.getValue();
                        Double transAtLastYear = null;
                        Integer compareLastYear = null;
                        Integer compareYesterday = null;
                        if (!CollectionUtils.isEmpty(lastYearHistogram)) {
                            transAtLastYear = lastYearHistogram.get(time.minusYears(1));
                            compareLastYear = (transAtThisYear.intValue() - transAtLastYear.intValue()) * 100 / Math.max(transAtThisYear.intValue(), transAtLastYear.intValue());
                            Double transAtYesterday = lastYearHistogram.get(time.minusDays(1));
                            if (transAtYesterday != null) {
                                compareYesterday = (transAtThisYear.intValue() - transAtYesterday.intValue()) * 100 / Math.max(transAtThisYear.intValue(), transAtYesterday.intValue());
                            }
                        }
                        return new ConsumptionTrendResp.Detail(time.atStartOfDay(), transAtThisYear, transAtLastYear, compareLastYear, compareYesterday);
                    }).collect(Collectors.toList());
        }
        return null;
    }

    private Map<LocalDate, Double> dateHistogram(String startTime, String endTime, String cbdName) {
        String indexName = "county_business_circle_consumption";
        String indexType = "doc";
        log.info("consumptionStatistics startTime={},endTime={}", startTime, endTime);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        if (!StringUtils.isEmpty(cbdName)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("cbdName", cbdName));
        }

        SumAggregationBuilder sumAggregation = AggregationBuilders.sum("sum_count").field("transAt");
        DateHistogramAggregationBuilder dateHistogram = AggregationBuilders.dateHistogram("date_histogram")
                .field("dealDay")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .subAggregation(sumAggregation);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(boolQueryBuilder)
                .addAggregation(dateHistogram)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);

        InternalDateHistogram dateHistogramAggs = aggregations.get("date_histogram");

        Map<LocalDate, Double> map = new LinkedHashMap<>();
        dateHistogramAggs.getBuckets()
                .forEach(e -> {
                    LocalDate date = DateUtil.convert(e.getKeyAsString()).toLocalDate();
                    InternalSum sumCount = e.getAggregations().get("sum_count");
                    Double sumCountValue = sumCount.getValue();
                    map.put(date, sumCountValue);
                });
        return map;
    }

    private List<ConsumptionMapVO.Consumption> getConsumptionList(Aggregations aggregations, String filterName, String termsName, boolean innerProvince, List<ProvinceVO> adminArea) {
        InternalFilter travellerTypeFilterAgg = aggregations.get(filterName);
        InternalAggregations filterAggregations = travellerTypeFilterAgg.getAggregations();
        StringTerms sourceTermsAgg = filterAggregations.get(termsName);
        List<StringTerms.Bucket> buckets = sourceTermsAgg.getBuckets();
        return buckets.stream().map(bucket -> {
            Aggregations bucketAggregations = bucket.getAggregations();
            // 来源地
            String sourceId = bucket.getKeyAsString();
            String source = innerProvince ? findAdminAreaByCityId(adminArea, sourceId) : findAdminAreaByProvinceId(adminArea, sourceId);
            // 消费金额
            InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
            String transAtString = transAtSumAgg.getValueAsString();
            BigDecimal transAt = new BigDecimal(transAtString).setScale(2, RoundingMode.HALF_UP);
            // 消费笔数
            InternalSum transNumSumAgg = bucketAggregations.get("transNum_sum");
            int transNum = Double.valueOf(transNumSumAgg.getValue()).intValue();
            // 消费人次
            InternalSum acctNumSumAgg = bucketAggregations.get("acctNum_sum");
            int acctNum = Double.valueOf(acctNumSumAgg.getValue()).intValue();
            // 人均消费金额
            BigDecimal perConsumption = transAt.divide(BigDecimal.valueOf(acctNum), 2, RoundingMode.HALF_UP);
            // 人均消费笔数
            BigDecimal perConsumptionPens = BigDecimal.valueOf(transNum).divide(BigDecimal.valueOf(acctNum), 2, RoundingMode.HALF_UP);

            ConsumptionMapVO.Consumption consumption = new ConsumptionMapVO.Consumption();
            consumption.setSource(source);
            consumption.setTransAt(transAt);
            consumption.setTransNum(transNum);
            consumption.setAcctNum(acctNum);
            consumption.setPerConsumption(perConsumption);
            consumption.setPerConsumptionPens(perConsumptionPens);
            return consumption;
        }).collect(Collectors.toList());
    }

    private LocalDate getMaxDealDay() {
        MaxAggregationBuilder dealDayMaxAggBuilder = AggregationBuilders.max("dealDay_max")
                .field("dealDay")
                .format("yyyy-MM-dd");
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                .addAggregation(dealDayMaxAggBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            InternalMax dealDayMaxAgg = aggregations.get("dealDay_max");
            String dealDayMaxString = dealDayMaxAgg.getValueAsString();
            if (StringUtils.hasText(dealDayMaxString)) {
                return LocalDate.parse(dealDayMaxString, DateUtil.LOCAL_DATE);
            } else {
                return LocalDate.now();
            }
        });
    }

    @Override
    public Map statisticsData(HolidayTourismIncomeCondition condition) {
        Map<String, Object> resultMap = new HashMap<>();
        // 今年节假日旅游收入
        Double transAtTotal = commonHolidayConsumption(condition.getStartTime(), condition.getEndTime());
        resultMap.put("transAtTotal", decimalTransfer(String.valueOf(transAtTotal), "1", 2));
        // 今年旅游收入走势
        resultMap.put("consumptionTrend", commonHolidayTrend(condition.getStartTime(), condition.getEndTime()));
        Double compareTransAt = 0.0;
        if (!StringUtil.isEmpty(condition.getLastStartTime()) && !StringUtil.isEmpty(condition.getLastEndTime())) {
            // 去年节假日旅游收入
            Double lastTransAtTotal = commonHolidayConsumption(condition.getLastStartTime(), condition.getLastEndTime());
            if (lastTransAtTotal > 0) {
                // 同比增长或下降，保留两位小数
                Double diff = transAtTotal - lastTransAtTotal;
                compareTransAt = new BigDecimal(diff * 100 / lastTransAtTotal)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }
            // 去年旅游收入走势
            resultMap.put("lastConsumptionTrend", commonHolidayTrend(condition.getLastStartTime(), condition.getLastEndTime()));
            resultMap.put("compareTransAt", compareTransAt);
        } else {
            resultMap.put("compareTransAt", "");
        }
        return resultMap;
    }

    // 由于elasticsearch不支持聚合分页，并且总数也很少，暂时打算由前端进行分页
    @Override
    public List<HolidayConsumptionVO> conditionStatisticsData(HolidayTourismIncomeCondition condition) {
        List<HolidayConsumptionVO> dataList = new ArrayList<>();
        // 计算总条数，因为script脚本分页需要设置size
        long count = businessCircleConsumptionMapper.count();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(condition.getStartTime()).lte(condition.getEndTime()));
        if (!StringUtil.isEmpty(condition.getCbdName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("cbdName", condition.getCbdName()));
        }
        Script script = new Script("doc.cbdName.value+'#'+doc.dealDay.value");
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("transAt_agg").field("transAt");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("cbd_day_agg")
                .order(BucketOrder.aggregation("transAt_agg", false))
                .script(script)
                .size(Integer.parseInt(String.valueOf(count)))
                .subAggregation(sumAggregationBuilder);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("county_business_circle_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = (StringTerms) temp.get("cbd_day_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            String[] split = name.split("#");
            String cbdName = split[0];
            String dealDay = split[1].substring(0, 10);
            Sum sum = bucket.getAggregations().get("transAt_agg");
            HolidayConsumptionVO consumptionVO = new HolidayConsumptionVO(cbdName, decimalTransfer(String.valueOf(sum.getValue()), "1", 2), dealDay);
            dataList.add(consumptionVO);
        }
        return dataList;
    }

    // 由于银联行业数据中，没有区分省内省外，因此这里只按照时间筛选即可
    @Override
    public List<ConsumptionVO> industryType(Integer year) {
        String start = "-01-01";
        String end = "-12-31";
        // 今年时间
        String startTime = year + start;
        String endTime = year + end;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        return aggregation("county_industry_consumption", "doc", boolQueryBuilder, "type", "transAt", 10);
    }

    @Override
    public List<HolidayConsumptionVO> historyConditionStatisticsData(HolidayTourismIncomeCondition condition) {
        List<HolidayConsumptionVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(condition.getStartTime()).lte(condition.getEndTime()));
        if (!StringUtil.isEmpty(condition.getCbdName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("cbdName", condition.getCbdName()));
        }
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("transAt_agg").field("transAt");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("cbd_agg")
                .field("cbdName")
                .order(BucketOrder.aggregation("transAt_agg", false))
                .subAggregation(sumAggregationBuilder);
        DateHistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_histogram")
                .field("dealDay")
                .subAggregation(termsAggregationBuilder);
        switch (condition.getFlag()) {
            case "month":
                histogramAggregationBuilder.dateHistogramInterval(DateHistogramInterval.MONTH).format("yyyy-MM");
                break;
            case "year":
                histogramAggregationBuilder.dateHistogramInterval(DateHistogramInterval.YEAR).format("yyyy");
                break;
            default:
                histogramAggregationBuilder.dateHistogramInterval(DateHistogramInterval.DAY).format("yyyy-MM-dd");
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("county_business_circle_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(histogramAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        InternalDateHistogram dateHistogram = (InternalDateHistogram) temp.get("time_histogram");
        // 获得所有的桶
        List<InternalDateHistogram.Bucket> buckets = dateHistogram.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (InternalDateHistogram.Bucket bucket : buckets) {
            // 聚合日期列表
            String dealDay = bucket.getKeyAsString();
            StringTerms stringTerms = (StringTerms) bucket.getAggregations().asMap().get("cbd_agg");
            // 获得所有的桶
            List<StringTerms.Bucket> aggBuckets = stringTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (StringTerms.Bucket aggBucket : aggBuckets) {
                String cbdName = aggBucket.getKeyAsString();
                Sum sum = aggBucket.getAggregations().get("transAt_agg");
                HolidayConsumptionVO consumptionVO = new HolidayConsumptionVO(cbdName, decimalTransfer(String.valueOf(sum.getValue()), "1", 2), dealDay);
                dataList.add(consumptionVO);
            }
        }
        return dataList;
    }

    @Override
    public ConsumptionSourceResp sourceConsumption(ConsumptionSourceReq req) {
        String indexName = "county_consumption_city";
        String indexType = "doc";
        String beginDate = req.getBeginDate().format(DateUtil.LOCAL_DATE);
        String endDate = req.getEndDate().format(DateUtil.LOCAL_DATE);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(beginDate).lte(endDate));
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder transNumSumAggBuilder = AggregationBuilders.sum("transNum_sum").field("transNum");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        TermsAggregationBuilder sourceCityTermsAggBuilder = AggregationBuilders.terms("sourceCity_terms")
                .field("sourceCity")
                .size(1000)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        TermsAggregationBuilder sourceProvinceTermsAggBuilder = AggregationBuilders.terms("sourceProvince_terms")
                .field("sourceProvince")
                .size(1000)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transNumSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .subAggregation(sourceCityTermsAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices(indexName)
                .withTypes(indexType)
                .withQuery(queryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(transNumSumAggBuilder)
                .addAggregation(acctNumSumAggBuilder)
                .addAggregation(sourceProvinceTermsAggBuilder)
                .build();

        return elasticsearchTemplate.query(nativeSearchQuery, response -> {
            Aggregations aggregations = response.getAggregations();
            List<ConsumptionSourceResp.Detail> countries = new ArrayList<>(1);
            // 中国  目前只有中国数据
            ConsumptionSourceResp.Detail chinaData = buildDetail(CHINA_ID, AdminAreaLevelEnum.COUNTRY, aggregations);
            chinaData.setSourceName("中国");
            // 省份数据
            StringTerms sourceProvinceTermsAgg = aggregations.get("sourceProvince_terms");
            List<StringTerms.Bucket> provinceBuckets = sourceProvinceTermsAgg.getBuckets();
            List<ConsumptionSourceResp.Detail> provincesData = new ArrayList<>();
            for (StringTerms.Bucket provinceBucket : provinceBuckets) {
                Aggregations provinceBucketAggregations = provinceBucket.getAggregations();
                ConsumptionSourceResp.Detail provinceData = buildDetail(provinceBucket.getKeyAsString(), AdminAreaLevelEnum.PROVINCE, provinceBucketAggregations);
                // 城市数据
                StringTerms sourceCityTermsAgg = provinceBucketAggregations.get("sourceCity_terms");
                List<StringTerms.Bucket> sourceCityTermsAggBuckets = sourceCityTermsAgg.getBuckets();
                List<ConsumptionSourceResp.Detail> citiesData = new ArrayList<>();
                for (StringTerms.Bucket sourceCityTermsAggBucket : sourceCityTermsAggBuckets) {
                    Aggregations sourceCityBucketAggregations = sourceCityTermsAggBucket.getAggregations();
                    ConsumptionSourceResp.Detail cityData = buildDetail(sourceCityTermsAggBucket.getKeyAsString(), AdminAreaLevelEnum.CITY, sourceCityBucketAggregations);
                    citiesData.add(cityData);
                }
                provinceData.setChildren(citiesData);
                provincesData.add(provinceData);
            }
            List<ConsumptionSourceResp.Detail> provinces = getProvinces(provincesData);
            chinaData.setChildren(provinces);
            countries.add(chinaData);

            ConsumptionSourceResp resp = new ConsumptionSourceResp();
            // 只有中国数据，所以中国消费总金额即是总金额
            resp.setTotalTransAt(chinaData.getTransAt());
            resp.setList(countries);

            return resp;
        });
    }

    @Override
    public List<ConsumptionSourceTrendResp> sourceConsumptionTrend(ConsumptionSourceTrendReq req) {
        // 消费
        String sourceId = req.getSourceId();
        AdminAreaLevelEnum level = req.getLevel();
        LocalDate beginDate = req.getBeginDate();
        LocalDate endDate = req.getEndDate();
        // 因为涉及与昨日对比所以开始日期减1天
        beginDate = beginDate.plusDays(-1);
        LocalDate lastYearBeginDate = beginDate.plusYears(-1);
        LocalDate lastYearEndDate = endDate.plusYears(-1);
        // 去年
        Map<LocalDate, BigDecimal> lastYearTransAtMap = getTransAtMap(sourceId, level, lastYearBeginDate, lastYearEndDate);
        // 今年
        List<ConsumptionSourceTrendResp> transDataList = new ArrayList<>();
        List<KeyAndValue<LocalDate, BigDecimal>> transAtList = getTransAtList(sourceId, level, beginDate, endDate);
        BigDecimal totalTransAt = BigDecimal.ZERO;
        for (int i = 1; i < transAtList.size(); i++) {
            ConsumptionSourceTrendResp transData = new ConsumptionSourceTrendResp();
            KeyAndValue<LocalDate, BigDecimal> keyAndValue = transAtList.get(i);
            // 当前日期
            LocalDate date = keyAndValue.getKey();
            // 当前消费
            BigDecimal transAt = keyAndValue.getValue();
            // 汇总消费金额
            totalTransAt = totalTransAt.add(transAt);
            KeyAndValue<LocalDate, BigDecimal> lastKeyAndValue = transAtList.get(i - 1);
            // 昨天或上月消费
            BigDecimal lastTransAt = lastKeyAndValue.getValue();
            // 环比
            BigDecimal periodOnPeriod = BigDecimal.ZERO;
            if (lastTransAt.compareTo(BigDecimal.ZERO) != 0) {
                periodOnPeriod = transAt.subtract(lastTransAt)
                        .divide(lastTransAt, 4, RoundingMode.HALF_DOWN);
            } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
                periodOnPeriod = BigDecimal.ONE;
            }
            // 去年同期消费
            BigDecimal lastYearTransAt = BigDecimal.ZERO;
            // 不是2月29号
            if (!date.isLeapYear() || !Month.FEBRUARY.equals(date.getMonth()) || date.getDayOfMonth() != 29) {
                lastYearTransAt = lastYearTransAtMap.getOrDefault(date.plusYears(-1), BigDecimal.ZERO);
            }
            // 同比
            BigDecimal yearOnYear = BigDecimal.ZERO;
            if (lastYearTransAt.compareTo(BigDecimal.ZERO) != 0) {
                yearOnYear = transAt.subtract(lastYearTransAt)
                        .divide(lastYearTransAt, 4, RoundingMode.HALF_DOWN);
            } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
                yearOnYear = BigDecimal.ONE;
            }

            transData.setDate(date.atStartOfDay());
            transData.setTransAt(transAt);
            transData.setLastTransAt(lastTransAt);
            transData.setPeriodOnPeriod(periodOnPeriod);
            transData.setSameTimeLastYear(lastYearTransAt);
            transData.setYearOnYear(yearOnYear);
            transDataList.add(transData);
        }

        return transDataList;
    }

    private Map<LocalDate, BigDecimal> getTransAtMap(String sourceId, AdminAreaLevelEnum level, LocalDate beginDate, LocalDate endDate) {
        List<KeyAndValue<LocalDate, BigDecimal>> transAtList = getTransAtList(sourceId, level, beginDate, endDate);

        return transAtList.stream().collect(Collectors.toMap(KeyAndValue::getKey, KeyAndValue::getValue));
    }

    private List<KeyAndValue<LocalDate, BigDecimal>> getTransAtList(String sourceId, AdminAreaLevelEnum level, LocalDate beginDate, LocalDate endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        String indexName = "county_consumption_city";
        if (AdminAreaLevelEnum.PROVINCE.equals(level)) {
            queryBuilder.must(QueryBuilders.termsQuery("sourceProvince", sourceId));
        } else if (AdminAreaLevelEnum.CITY.equals(level)) {
            queryBuilder.must(QueryBuilders.termsQuery("sourceCity", sourceId));
        }
        queryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        );
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        DateHistogramAggregationBuilder dealDayHisAggBuilder = AggregationBuilders.dateHistogram("dealDay_his")
                .field("dealDay");
        dealDayHisAggBuilder.dateHistogramInterval(DateHistogramInterval.DAY);
        dealDayHisAggBuilder.format("yyyy-MM-dd");
        dealDayHisAggBuilder.extendedBounds(new ExtendedBounds(beginDate.format(DateUtil.LOCAL_DATE), endDate.format(DateUtil.LOCAL_DATE)));
        dealDayHisAggBuilder.subAggregation(transAtSumAggBuilder);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices(indexName).withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(dealDayHisAggBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            InternalDateHistogram dealDayHisAgg = aggregations.get("dealDay_his");
            List<InternalDateHistogram.Bucket> buckets = dealDayHisAgg.getBuckets();
            return buckets.stream().map(bucket -> {
                LocalDate date = LocalDate.parse(bucket.getKeyAsString(), DateUtil.LOCAL_DATE);
                Aggregations bucketAggregations = bucket.getAggregations();

                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                return new KeyAndValue<>(date, transAt);
            }).collect(Collectors.toList());
        });
    }

    private List<ConsumptionSourceResp.Detail> getProvinces(List<ConsumptionSourceResp.Detail> provincesData) {
        List<ProvinceVO> adminArea = getAdminArea();
        List<ConsumptionSourceResp.Detail> provinces = new ArrayList<>(adminArea.size());
        Map<String, ConsumptionSourceResp.Detail> provincesMap = provincesData.stream()
                .collect(Collectors.toMap(ConsumptionSourceResp.Detail::getSourceId, Function.identity()));
        for (ProvinceVO provinceVO : adminArea) {
            String provId = provinceVO.getProvId();
            String provName = provinceVO.getProvName();
            ConsumptionSourceResp.Detail provinceData = provincesMap.getOrDefault(provId, ConsumptionSourceResp.Detail.getEmpty(provId, AdminAreaLevelEnum.PROVINCE));
            provinceData.setSourceName(provName);
            List<CityVO> cityList = provinceVO.getCities();
            List<ConsumptionSourceResp.Detail> cities = new ArrayList<>(cityList.size());
            Map<String, ConsumptionSourceResp.Detail> citiesMap = provinceData.getChildren().stream()
                    .collect(Collectors.toMap(ConsumptionSourceResp.Detail::getSourceId, Function.identity()));
            for (CityVO cityVO : cityList) {
                String cityId = cityVO.getCityId();
                String cityName = cityVO.getCityName();
                ConsumptionSourceResp.Detail cityData = citiesMap.getOrDefault(cityId, ConsumptionSourceResp.Detail.getEmpty(cityId, AdminAreaLevelEnum.CITY));
                cityData.setSourceName(cityName);
                cities.add(cityData);
            }
            cities.sort(Comparator.comparing(ConsumptionSourceResp.Detail::getTransAt).reversed());
            provinceData.setChildren(cities);
            provinces.add(provinceData);
        }
        provinces.sort(Comparator.comparing(ConsumptionSourceResp.Detail::getTransAt).reversed());

        return provinces;
    }

    private ConsumptionSourceResp.Detail buildDetail(String sourceId, AdminAreaLevelEnum level, Aggregations aggregations) {
        InternalSum transAtSumAgg = aggregations.get("transAt_sum");
        InternalSum transNumSumAgg = aggregations.get("transNum_sum");
        InternalSum acctNumSumAgg = aggregations.get("acctNum_sum");
        BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal transNum = new BigDecimal(transNumSumAgg.getValueAsString());
        BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
        BigDecimal avgTransAt = BigDecimal.ZERO;
        BigDecimal avgTransNum = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(acctNum) != 0) {
            avgTransAt = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
            avgTransNum = transNum.divide(acctNum, 2, RoundingMode.HALF_UP);
        }

        ConsumptionSourceResp.Detail detail = new ConsumptionSourceResp.Detail();
        detail.setSourceId(sourceId);
        detail.setLevel(level);
        detail.setTransAt(transAt);
        detail.setTransNum(transNum.intValue());
        detail.setAcctNum(acctNum.intValue());
        detail.setAvgTransAt(avgTransAt);
        detail.setAvgTransNum(avgTransNum);
        if (AdminAreaLevelEnum.CITY.equals(level)) {
            detail.setChildren(null);
        } else {
            detail.setChildren(new ArrayList<>(0));
        }

        return detail;
    }

    private List<ConsumptionVO> commonHolidayTrend(String startTime, String endTime) {
        String index = "county_consumption";
        String type = "doc";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        List<ConsumptionVO> list = aggregation(index, type, boolQueryBuilder, "dealDay", "transAt", 10);
        return buildHolidayData(list, startTime, endTime);
    }

    // 根据起始节假日，构建完整数据
    private List<ConsumptionVO> buildHolidayData(List<ConsumptionVO> list, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        List<String> dateList = DateUtil.getBetweenDate(startTime, endTime);
        List<ConsumptionVO> dataList = new ArrayList<>();
        list.forEach(consumptionVO -> {
            paramMap.put(consumptionVO.getName(), consumptionVO.getValue());
        });
        for (String date : dateList) {
            ConsumptionVO consumptionVO;
            if (paramMap.containsKey(date)) {
                consumptionVO = new ConsumptionVO(date, Double.parseDouble(String.valueOf(paramMap.get(date))));
            } else {
                consumptionVO = new ConsumptionVO(date, 0.0);
            }
            dataList.add(consumptionVO);
        }
        return dataList;
    }

    private Double commonHolidayConsumption(String startTime, String endTime) {
        String index = "county_consumption";
        String type = "doc";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("dealDay").gte(startTime).lte(endTime));
        SumAggregationBuilder sumTransAt = AggregationBuilders.sum("transAt_count").field("transAt");
        return sumAggregation(index, type, boolQueryBuilder, sumTransAt);
    }

    private NativeSearchQuery commonQuery(String index, String type, BoolQueryBuilder queryBuilder, Integer
            page, Integer size) {
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(index).withTypes(type)
                .withQuery(queryBuilder).build();
        // 增加按照日期倒序排列
        Sort descSort = Sort.by(Sort.Direction.DESC, "transAt");
        Sort idSort = Sort.by(Sort.Direction.DESC, "_id");
        query.addSort(descSort);
        query.addSort(idSort);
        if (page != null && size != null) {
            // 简单防止乱输参数
            page = page > 0 ? page - 1 : 0;
            size = size > 0 ? size : 10;
            PageRequest pageRequest = PageRequest.of(page, size);
            query.setPageable(pageRequest);
        }
        return query;
    }

    private Double decimalTransfer(String divisor, String dividend, int num) {
        BigDecimal bg = new BigDecimal(divisor);
        return bg.divide(new BigDecimal(dividend), num, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private Double sumAggregation(String indexName, String indexType, BoolQueryBuilder
            queryBuilder, SumAggregationBuilder aggregationBuilder) {
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(indexName).withTypes(indexType)
                .withQuery(queryBuilder).addAggregation(aggregationBuilder).build();
        return elasticsearchTemplate.query(query, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return sum.getValue();
        });
    }

    private List<ConsumptionVO> aggregation(String indexName, String indexType, BoolQueryBuilder
            queryBuilder, String field, String agg, int size) {
        List<ConsumptionVO> dataList = new ArrayList<>();
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_agg").field(agg);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("consume_agg")
                .field(field)
                .order(BucketOrder.aggregation("sum_agg", false))
                .size(size)
                .subAggregation(sumAggregationBuilder);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(queryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        if ("dealDay".equals(field)) {
            LongTerms longTerms = (LongTerms) temp.get("consume_agg");
            // 获得所有的桶
            List<LongTerms.Bucket> buckets = longTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (LongTerms.Bucket bucket : buckets) {
                String dealDay = bucket.getKeyAsString();
                Sum sum = bucket.getAggregations().get("sum_agg");
                ConsumptionVO consumptionVO = new ConsumptionVO(dealDay.substring(0, 10), decimalTransfer(String.valueOf(sum.getValue()), "1", 2));
                dataList.add(consumptionVO);
            }
        } else {
            StringTerms stringTerms = (StringTerms) temp.get("consume_agg");
            // 获得所有的桶
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
            for (StringTerms.Bucket bucket : buckets) {
                String name = bucket.getKeyAsString();
                Sum sum = bucket.getAggregations().get("sum_agg");
                ConsumptionVO consumptionVO = new ConsumptionVO(name, decimalTransfer(String.valueOf(sum.getValue()), "1", 2));
                dataList.add(consumptionVO);
            }
        }
        return dataList;
    }

    private List<ProvinceVO> getAdminArea() {
        return administrativeAreaFeignClient.listProvincesWithoutCounty().getData();
    }

    private String findAdminAreaByCityId(List<ProvinceVO> provinceList, String cityId) {
        for (ProvinceVO provinceVO : provinceList) {
            List<CityVO> cities = provinceVO.getCities();
            for (CityVO city : cities) {
                if (cityId.equals(city.getCityId())) {
                    return city.getCityName();
                }
            }
        }

        return cityId;
    }

    private String findAdminAreaByProvinceId(List<ProvinceVO> provinceList, String provId) {
        for (ProvinceVO provinceVO : provinceList) {
            if (provId.equals(provinceVO.getProvId())) {
                return provinceVO.getProvName();
            }
        }

        return provId;
    }
}
