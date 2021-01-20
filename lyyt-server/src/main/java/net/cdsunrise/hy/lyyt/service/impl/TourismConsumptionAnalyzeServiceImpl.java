package net.cdsunrise.hy.lyyt.service.impl;

import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.AdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.ProvinceVO;
import net.cdsunrise.hy.lyyt.entity.vo.*;
import net.cdsunrise.hy.lyyt.service.ITourismConsumptionAnalyzeService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2020/3/3 14:06
 */
@Service
public class TourismConsumptionAnalyzeServiceImpl implements ITourismConsumptionAnalyzeService {

    /**
     * 行业列表
     */
    private static final List<String> INDUSTRY_LIST = Arrays.asList("餐饮", "住宿", "娱乐", "购物");

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AdministrativeAreaFeignClient administrativeAreaFeignClient;

    public TourismConsumptionAnalyzeServiceImpl(ElasticsearchTemplate elasticsearchTemplate, AdministrativeAreaFeignClient administrativeAreaFeignClient) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.administrativeAreaFeignClient = administrativeAreaFeignClient;
    }

    @Override
    public TourismConsumptionSourceAnalyzeVO getTourismConsumptionSourceAnalyze(TourismConsumptionAnalyzeCondition condition) {
        TourismConsumptionAnalyzeCondition.TypeEnum type = condition.getType();
        LocalDate beginDate = condition.getBeginDate();
        LocalDate endDate = condition.getEndDate();
        LocalDate lastYearBeginDate;
        LocalDate lastYearEndDate;
        if (TourismConsumptionAnalyzeCondition.TypeEnum.MONTH.equals(type)) {
            beginDate = beginDate.withDayOfMonth(1);
            endDate = endDate.withDayOfMonth(1).plusMonths(1).plusDays(-1);
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1).withDayOfMonth(1).plusMonths(1).plusDays(-1);
        } else {
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1);
        }
        TourismConsumptionSourceAnalyzeVO tourismConsumptionSourceAnalyzeVO = new TourismConsumptionSourceAnalyzeVO();
        // 行政区划
        List<ProvinceVO> adminArea = getAdminArea();
        // 省内
        setInnerProv(beginDate, endDate, lastYearBeginDate, lastYearEndDate, adminArea, tourismConsumptionSourceAnalyzeVO);
        // 省外
        setOuterProv(beginDate, endDate, lastYearBeginDate, lastYearEndDate, adminArea, tourismConsumptionSourceAnalyzeVO);
        // 总金额
        tourismConsumptionSourceAnalyzeVO.setTotalTransAt(tourismConsumptionSourceAnalyzeVO.getInnerProvTransAt().add(tourismConsumptionSourceAnalyzeVO.getOuterProvTransAt()));
        return tourismConsumptionSourceAnalyzeVO;
    }

    @Override
    public List<TourismConsumptionIndustryAnalyzeVO> getTourismConsumptionIndustryAnalyze(TourismConsumptionAnalyzeCondition condition) {
        TourismConsumptionAnalyzeCondition.TypeEnum type = condition.getType();
        LocalDate beginDate = condition.getBeginDate();
        LocalDate endDate = condition.getEndDate();
        LocalDate lastYearBeginDate;
        LocalDate lastYearEndDate;
        if (TourismConsumptionAnalyzeCondition.TypeEnum.MONTH.equals(type)) {
            beginDate = beginDate.withDayOfMonth(1);
            endDate = endDate.withDayOfMonth(1).plusMonths(1).plusDays(-1);
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1).withDayOfMonth(1).plusMonths(1).plusDays(-1);
        } else {
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1);
        }

        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder transAtTotalSumAggBuilder = AggregationBuilders.sum("transAtTotal_sum").field("transAtTotal");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        TermsAggregationBuilder typeTermsAggBuilder = AggregationBuilders.terms("type_terms")
                .field("type")
                .size(20)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(transAtTotalSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_industry_consumption").withTypes("doc")
                .addAggregation(typeTermsAggBuilder);
        // 去年同期
        BoolQueryBuilder lastYearBoolQueryBuilder = QueryBuilders.boolQuery();
        lastYearBoolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(lastYearBeginDate.format(DateUtil.LOCAL_DATE))
                        .lte(lastYearEndDate.format(DateUtil.LOCAL_DATE))
        );
        searchQueryBuilder.withQuery(lastYearBoolQueryBuilder);
        Map<String, BigDecimal> lastYearTransAtMap = elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            StringTerms typeTermsAgg = aggregations.get("type_terms");
            List<StringTerms.Bucket> buckets = typeTermsAgg.getBuckets();
            return buckets.stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, bucket -> {
                Aggregations bucketAggregations = bucket.getAggregations();
                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                return new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
            }));
        });
        // 今年
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        );
        searchQueryBuilder.withQuery(boolQueryBuilder);

        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            StringTerms typeTermsAgg = aggregations.get("type_terms");
            List<StringTerms.Bucket> buckets = typeTermsAgg.getBuckets();
            List<TourismConsumptionIndustryAnalyzeVO> industryAnalyzeList = new ArrayList<>();
            BigDecimal otherTransAt = BigDecimal.ZERO;
            BigDecimal otherTransAtTotal = BigDecimal.ZERO;
            BigDecimal otherAcctNum = BigDecimal.ZERO;
            BigDecimal otherSameTimeLastYear = BigDecimal.ZERO;
            for (StringTerms.Bucket bucket : buckets) {
                String industry = bucket.getKeyAsString();
                Aggregations bucketAggregations = bucket.getAggregations();
                // 游客消费金额
                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                // 总消费金额
                InternalSum transAtTotalSumAgg = bucketAggregations.get("transAtTotal_sum");
                BigDecimal transAtTotal = new BigDecimal(transAtTotalSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                // 消费人次
                InternalSum acctNumSumAgg = bucketAggregations.get("acctNum_sum");
                BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
                // 去年同期消费
                BigDecimal sameTimeLastYear = lastYearTransAtMap.getOrDefault(industry, BigDecimal.ZERO);
                if (INDUSTRY_LIST.contains(industry)) {
                    TourismConsumptionIndustryAnalyzeVO tourismConsumptionIndustryAnalyzeVO = getTourismConsumptionIndustryAnalyzeVO(industry, transAt, transAtTotal, acctNum, sameTimeLastYear);
                    industryAnalyzeList.add(tourismConsumptionIndustryAnalyzeVO);
                } else {
                    otherTransAt = otherTransAt.add(transAt);
                    otherTransAtTotal = otherTransAtTotal.add(transAtTotal);
                    otherAcctNum = otherAcctNum.add(acctNum);
                    otherSameTimeLastYear = otherSameTimeLastYear.add(sameTimeLastYear);
                }
            }
            TourismConsumptionIndustryAnalyzeVO other = getTourismConsumptionIndustryAnalyzeVO("其他", otherTransAt, otherTransAtTotal, otherAcctNum, otherSameTimeLastYear);
            industryAnalyzeList.add(other);

            // 重新排序
            industryAnalyzeList.sort((a, b) -> b.getTransAt().compareTo(a.getTransAt()));

            return industryAnalyzeList;
        });
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

    private TourismConsumptionIndustryAnalyzeVO getTourismConsumptionIndustryAnalyzeVO(String industry, BigDecimal transAt, BigDecimal transAtTotal, BigDecimal acctNum, BigDecimal sameTimeLastYear) {
        // 游客贡献度
        BigDecimal transAtRatio = BigDecimal.ZERO;
        if (transAtTotal.compareTo(BigDecimal.ZERO) != 0) {
            transAtRatio = transAt.divide(transAtTotal, 4, RoundingMode.HALF_DOWN);
        }
        // 人均消费
        BigDecimal perAcctTransAt = BigDecimal.ZERO;
        if (acctNum.compareTo(BigDecimal.ZERO) != 0) {
            perAcctTransAt = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
        }
        // 去年同比
        BigDecimal yearOnYear = BigDecimal.ZERO;
        if (sameTimeLastYear.compareTo(BigDecimal.ZERO) != 0) {
            yearOnYear = transAt.subtract(sameTimeLastYear)
                    .divide(sameTimeLastYear, 4, RoundingMode.HALF_DOWN);
        } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
            yearOnYear = BigDecimal.ONE;
        }

        TourismConsumptionIndustryAnalyzeVO tourismConsumptionIndustryAnalyzeVO = new TourismConsumptionIndustryAnalyzeVO();
        tourismConsumptionIndustryAnalyzeVO.setIndustry(industry);
        tourismConsumptionIndustryAnalyzeVO.setTransAt(transAt);
        tourismConsumptionIndustryAnalyzeVO.setTransAtRatio(transAtRatio);
        tourismConsumptionIndustryAnalyzeVO.setAcctNum(acctNum.intValue());
        tourismConsumptionIndustryAnalyzeVO.setPerAcctTransAt(perAcctTransAt);
        tourismConsumptionIndustryAnalyzeVO.setSameTimeLastYear(sameTimeLastYear);
        tourismConsumptionIndustryAnalyzeVO.setYearOnYear(yearOnYear);
        return tourismConsumptionIndustryAnalyzeVO;
    }

    private void setInnerProv(LocalDate beginDate, LocalDate endDate, LocalDate lastYearBeginDate, LocalDate lastYearEndDate, List<ProvinceVO> adminArea, TourismConsumptionSourceAnalyzeVO tourismConsumptionSourceAnalyzeVO) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
                // 3表示眉山市（不含洪雅县本地），所以属于省内
        ).must(QueryBuilders.termsQuery("travellerType", "1", "3"));
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        TermsAggregationBuilder sourceCityTermsAggBuilder = AggregationBuilders.terms("sourceCity_terms")
                .field("sourceCity")
                .size(10)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(sourceCityTermsAggBuilder);
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            // 省内总金额
            InternalSum innerProvTransAtAgg = aggregations.get("transAt_sum");
            BigDecimal innerProvTransAt = new BigDecimal(innerProvTransAtAgg.getValueAsString())
                    .setScale(2, RoundingMode.HALF_UP);
            tourismConsumptionSourceAnalyzeVO.setInnerProvTransAt(innerProvTransAt);
            // 各来源地
            StringTerms sourceCityTermsAgg = aggregations.get("sourceCity_terms");
            List<StringTerms.Bucket> buckets = sourceCityTermsAgg.getBuckets();
            // 来源地列表
            List<String> sourceList = buckets.stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            // 去年同期
            BoolQueryBuilder lastYearQueryBuilder = QueryBuilders.boolQuery();
            lastYearQueryBuilder.must(QueryBuilders.termsQuery("sourceCity", sourceList))
                    .must(
                            QueryBuilders.rangeQuery("dealDay")
                                    .gte(lastYearBeginDate.format(DateUtil.LOCAL_DATE))
                                    .lte(lastYearEndDate.format(DateUtil.LOCAL_DATE))
                    );
            NativeSearchQueryBuilder lastYearSearchQueryBuilder = new NativeSearchQueryBuilder();
            lastYearSearchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                    .withQuery(lastYearQueryBuilder)
                    .addAggregation(sourceCityTermsAggBuilder);
            Map<String, BigDecimal> lastYearTransAtMap = elasticsearchTemplate.query(lastYearSearchQueryBuilder.build(), lastYearResponse -> {
                Aggregations lastYearResponseAggregations = lastYearResponse.getAggregations();
                // 各来源地
                StringTerms lastYearSourceProvinceTermsAgg = lastYearResponseAggregations.get("sourceCity_terms");
                List<StringTerms.Bucket> lastYearBuckets = lastYearSourceProvinceTermsAgg.getBuckets();
                return lastYearBuckets.stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, bucket -> {
                    Aggregations lastYearBucketAggregations = bucket.getAggregations();
                    InternalSum lastYearTransAtSumAgg = lastYearBucketAggregations.get("transAt_sum");
                    return new BigDecimal(lastYearTransAtSumAgg.getValueAsString())
                            .setScale(2, RoundingMode.HALF_UP);
                }));
            });
            List<TourismConsumptionSourceAnalyzeVO.TransInfo> innerProvTransInfoList = buckets.stream().map(bucket -> {
                TourismConsumptionSourceAnalyzeVO.TransInfo transInfo = new TourismConsumptionSourceAnalyzeVO.TransInfo();
                // 来源地
                String sourceId = bucket.getKeyAsString();
                String source = findAdminAreaByCityId(adminArea, sourceId);
                Aggregations bucketAggregations = bucket.getAggregations();
                // 消费金额
                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                // 占总消费比例
                BigDecimal proportion = transAt.divide(innerProvTransAt, 4, RoundingMode.HALF_DOWN);
                // 消费人次
                InternalSum acctNumSumAgg = bucketAggregations.get("acctNum_sum");
                BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
                // 人均消费
                BigDecimal perAcctTransAt = BigDecimal.ZERO;
                if (acctNum.compareTo(BigDecimal.ZERO) != 0) {
                    perAcctTransAt = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
                }
                // 去年同期消费
                BigDecimal sameTimeLastYear = lastYearTransAtMap.getOrDefault(sourceId, BigDecimal.ZERO);
                // 去年同比
                BigDecimal yearOnYear = BigDecimal.ZERO;
                if (sameTimeLastYear.compareTo(BigDecimal.ZERO) != 0) {
                    yearOnYear = transAt.subtract(sameTimeLastYear)
                            .divide(sameTimeLastYear, 4, RoundingMode.HALF_DOWN);
                } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
                    yearOnYear = BigDecimal.ONE;
                }

                transInfo.setSource(source);
                transInfo.setTransAt(transAt);
                transInfo.setProportion(proportion);
                transInfo.setAcctNum(acctNum.intValue());
                transInfo.setPerAcctTransAt(perAcctTransAt);
                transInfo.setSameTimeLastYear(sameTimeLastYear);
                transInfo.setYearOnYear(yearOnYear);
                return transInfo;
            }).collect(Collectors.toList());
            tourismConsumptionSourceAnalyzeVO.setInnerProvList(innerProvTransInfoList);

            return null;
        });
    }

    private void setOuterProv(LocalDate beginDate, LocalDate endDate, LocalDate lastYearBeginDate, LocalDate lastYearEndDate, List<ProvinceVO> adminArea, TourismConsumptionSourceAnalyzeVO tourismConsumptionSourceAnalyzeVO) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        ).must(QueryBuilders.termQuery("travellerType", "2"));
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        SumAggregationBuilder acctNumSumAggBuilder = AggregationBuilders.sum("acctNum_sum").field("acctNum");
        TermsAggregationBuilder sourceProvinceTermsAggBuilder = AggregationBuilders.terms("sourceProvince_terms")
                .field("sourceProvince")
                .size(10)
                .subAggregation(transAtSumAggBuilder)
                .subAggregation(acctNumSumAggBuilder)
                .order(BucketOrder.aggregation("transAt_sum", false));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder)
                .addAggregation(sourceProvinceTermsAggBuilder);
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            // 省外总金额
            InternalSum outerProvTransAtAgg = aggregations.get("transAt_sum");
            BigDecimal outerProvTransAt = new BigDecimal(outerProvTransAtAgg.getValueAsString())
                    .setScale(2, RoundingMode.HALF_UP);
            tourismConsumptionSourceAnalyzeVO.setOuterProvTransAt(outerProvTransAt);
            // 各来源地
            StringTerms sourceProvinceTermsAgg = aggregations.get("sourceProvince_terms");
            List<StringTerms.Bucket> buckets = sourceProvinceTermsAgg.getBuckets();
            // 来源地列表
            List<String> sourceList = buckets.stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList());
            // 去年同期
            BoolQueryBuilder lastYearQueryBuilder = QueryBuilders.boolQuery();
            lastYearQueryBuilder.must(QueryBuilders.termsQuery("sourceProvince", sourceList))
                    .must(
                            QueryBuilders.rangeQuery("dealDay")
                                    .gte(lastYearBeginDate.format(DateUtil.LOCAL_DATE))
                                    .lte(lastYearEndDate.format(DateUtil.LOCAL_DATE))
                    );
            NativeSearchQueryBuilder lastYearSearchQueryBuilder = new NativeSearchQueryBuilder();
            lastYearSearchQueryBuilder.withIndices("county_consumption_city").withTypes("doc")
                    .withQuery(lastYearQueryBuilder)
                    .addAggregation(sourceProvinceTermsAggBuilder);
            Map<String, BigDecimal> lastYearTransAtMap = elasticsearchTemplate.query(lastYearSearchQueryBuilder.build(), lastYearResponse -> {
                Aggregations lastYearResponseAggregations = lastYearResponse.getAggregations();
                // 各来源地
                StringTerms lastYearSourceProvinceTermsAgg = lastYearResponseAggregations.get("sourceProvince_terms");
                List<StringTerms.Bucket> lastYearBuckets = lastYearSourceProvinceTermsAgg.getBuckets();
                return lastYearBuckets.stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, bucket -> {
                    Aggregations lastYearBucketAggregations = bucket.getAggregations();
                    InternalSum lastYearTransAtSumAgg = lastYearBucketAggregations.get("transAt_sum");
                    return new BigDecimal(lastYearTransAtSumAgg.getValueAsString())
                            .setScale(2, RoundingMode.HALF_UP);
                }));
            });
            List<TourismConsumptionSourceAnalyzeVO.TransInfo> outerProvTransInfoList = buckets.stream().map(bucket -> {
                TourismConsumptionSourceAnalyzeVO.TransInfo transInfo = new TourismConsumptionSourceAnalyzeVO.TransInfo();
                // 来源地
                String sourceId = bucket.getKeyAsString();
                String source = findAdminAreaByProvinceId(adminArea, sourceId);
                Aggregations bucketAggregations = bucket.getAggregations();
                // 消费金额
                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                // 占总消费比例
                BigDecimal proportion = transAt.divide(outerProvTransAt, 4, RoundingMode.HALF_DOWN);
                // 消费人次
                InternalSum acctNumSumAgg = bucketAggregations.get("acctNum_sum");
                BigDecimal acctNum = new BigDecimal(acctNumSumAgg.getValueAsString());
                // 人均消费
                BigDecimal perAcctTransAt = BigDecimal.ZERO;
                if (acctNum.compareTo(BigDecimal.ZERO) != 0) {
                    perAcctTransAt = transAt.divide(acctNum, 2, RoundingMode.HALF_UP);
                }
                // 去年同期消费
                BigDecimal sameTimeLastYear = lastYearTransAtMap.getOrDefault(sourceId, BigDecimal.ZERO);
                // 去年同比
                BigDecimal yearOnYear = BigDecimal.ZERO;
                if (sameTimeLastYear.compareTo(BigDecimal.ZERO) != 0) {
                    yearOnYear = transAt.subtract(sameTimeLastYear)
                            .divide(sameTimeLastYear, 4, RoundingMode.HALF_DOWN);
                } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
                    yearOnYear = BigDecimal.ONE;
                }

                transInfo.setSource(source);
                transInfo.setTransAt(transAt);
                transInfo.setProportion(proportion);
                transInfo.setAcctNum(acctNum.intValue());
                transInfo.setPerAcctTransAt(perAcctTransAt);
                transInfo.setSameTimeLastYear(sameTimeLastYear);
                transInfo.setYearOnYear(yearOnYear);
                return transInfo;
            }).collect(Collectors.toList());
            tourismConsumptionSourceAnalyzeVO.setOuterProvList(outerProvTransInfoList);

            return null;
        });
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
