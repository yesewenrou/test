package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.AdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.CityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.ProvinceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITourismConsumptionAnalyzeService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.MerchantInfoFeignClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
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
    private static final String HY_COUNTY = "洪雅县域";

    private ElasticsearchTemplate elasticsearchTemplate;
    private AdministrativeAreaFeignClient administrativeAreaFeignClient;
    private MerchantInfoFeignClient merchantInfoFeignClient;

    public TourismConsumptionAnalyzeServiceImpl(ElasticsearchTemplate elasticsearchTemplate, AdministrativeAreaFeignClient administrativeAreaFeignClient, MerchantInfoFeignClient merchantInfoFeignClient) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.administrativeAreaFeignClient = administrativeAreaFeignClient;
        this.merchantInfoFeignClient = merchantInfoFeignClient;
    }

    @Override
    public TourismConsumptionAnalyzeVO getTourismConsumptionAnalyze() {
        TourismConsumptionAnalyzeVO tourismConsumptionAnalyze = new TourismConsumptionAnalyzeVO();
        // 当月
        tourismConsumptionAnalyze.setCurrentMonth(getCurrentMonthTransInfo());
        // 上月
        tourismConsumptionAnalyze.setLastMonth(getLastMonthTransInfo());
        // 当年
        tourismConsumptionAnalyze.setCurrentYear(getCurrentYearTransInfo());
        return tourismConsumptionAnalyze;
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
    public TourismConsumptionBusinessCircleAnalyzeVO getTourismConsumptionBusinessCircleAnalyze(TourismConsumptionAnalyzeCondition condition) {
        String cbdName = condition.getCbdName();
        if (!StringUtils.hasText(cbdName)) {
            throw new ParamErrorException("商圈名称不能为空");
        }
        // 涉旅商户数
        Integer merchantNum;
        if (!HY_COUNTY.equals(cbdName)) {
            merchantNum = merchantInfoFeignClient.countByCircle(cbdName).getData();
        } else {
            // 空字符串表示查询全部
            merchantNum = merchantInfoFeignClient.countByCircle("").getData();
        }
        merchantNum = Objects.isNull(merchantNum) ? 0 : merchantNum;
        // 消费
        TourismConsumptionAnalyzeCondition.TypeEnum type = condition.getType();
        LocalDate beginDate = condition.getBeginDate();
        LocalDate endDate = condition.getEndDate();
        LocalDate lastYearBeginDate;
        LocalDate lastYearEndDate;
        if (TourismConsumptionAnalyzeCondition.TypeEnum.MONTH.equals(type)) {
            // 因为涉及与上月对比所以开始日期减1月
            beginDate = beginDate.withDayOfMonth(1).plusMonths(-1);
            endDate = endDate.withDayOfMonth(1).plusMonths(1).plusDays(-1);
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1).withDayOfMonth(1).plusMonths(1).plusDays(-1);
        } else {
            // 因为涉及与昨日对比所以开始日期减1天
            beginDate = beginDate.plusDays(-1);
            lastYearBeginDate = beginDate.plusYears(-1);
            lastYearEndDate = endDate.plusYears(-1);
        }
        // 去年
        Map<LocalDate, BigDecimal> lastYearTransAtMap = getTransAtMap(cbdName, type, lastYearBeginDate, lastYearEndDate);
        // 今年
        TourismConsumptionBusinessCircleAnalyzeVO result = new TourismConsumptionBusinessCircleAnalyzeVO();
        List<TourismConsumptionBusinessCircleAnalyzeVO.TransData> transDataList = new ArrayList<>();
        List<KeyAndValue<LocalDate, BigDecimal>> transAtList = getTransAtList(cbdName, type, beginDate, endDate);
        BigDecimal totalTransAt = BigDecimal.ZERO;
        for (int i = 1; i < transAtList.size(); i++) {
            TourismConsumptionBusinessCircleAnalyzeVO.TransData transData = new TourismConsumptionBusinessCircleAnalyzeVO.TransData();
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
        result.setTotalTransAt(totalTransAt);
        result.setMerchantNum(merchantNum);
        result.setList(transDataList);

        return result;
    }

    private Map<LocalDate, BigDecimal> getTransAtMap(String cbdName, TourismConsumptionAnalyzeCondition.TypeEnum type, LocalDate beginDate, LocalDate endDate) {
        List<KeyAndValue<LocalDate, BigDecimal>> transAtList = getTransAtList(cbdName, type, beginDate, endDate);

        return transAtList.stream().collect(Collectors.toMap(KeyAndValue::getKey, KeyAndValue::getValue));
    }

    private List<KeyAndValue<LocalDate, BigDecimal>> getTransAtList(String cbdName, TourismConsumptionAnalyzeCondition.TypeEnum type, LocalDate beginDate, LocalDate endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        String indexName;
        if (!HY_COUNTY.equals(cbdName)) {
            queryBuilder.must(QueryBuilders.termsQuery("cbdName", cbdName));
            indexName = "county_business_circle_consumption";
        } else {
            indexName = "county_consumption";
        }
        queryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        );
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        DateHistogramAggregationBuilder dealDayHisAggBuilder = AggregationBuilders.dateHistogram("dealDay_his")
                .field("dealDay");
        if (TourismConsumptionAnalyzeCondition.TypeEnum.MONTH.equals(type)) {
            dealDayHisAggBuilder.dateHistogramInterval(DateHistogramInterval.MONTH);
            dealDayHisAggBuilder.format("yyyy-MM");
            dealDayHisAggBuilder.extendedBounds(new ExtendedBounds(beginDate.format(DateUtil.LOCAL_DATE_MONTH), endDate.format(DateUtil.LOCAL_DATE_MONTH)));
        } else {
            dealDayHisAggBuilder.dateHistogramInterval(DateHistogramInterval.DAY);
            dealDayHisAggBuilder.format("yyyy-MM-dd");
            dealDayHisAggBuilder.extendedBounds(new ExtendedBounds(beginDate.format(DateUtil.LOCAL_DATE), endDate.format(DateUtil.LOCAL_DATE)));
        }
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
                LocalDate date;
                if (TourismConsumptionAnalyzeCondition.TypeEnum.MONTH.equals(type)) {
                    date = LocalDate.parse(bucket.getKeyAsString() + "-01", DateUtil.LOCAL_DATE);
                } else {
                    date = LocalDate.parse(bucket.getKeyAsString(), DateUtil.LOCAL_DATE);
                }
                Aggregations bucketAggregations = bucket.getAggregations();

                InternalSum transAtSumAgg = bucketAggregations.get("transAt_sum");
                BigDecimal transAt = new BigDecimal(transAtSumAgg.getValueAsString())
                        .setScale(2, RoundingMode.HALF_UP);
                return new KeyAndValue<>(date, transAt);
            }).collect(Collectors.toList());
        });
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

    TourismConsumptionAnalyzeVO.TransInfo getCurrentMonthTransInfo() {
        TourismConsumptionAnalyzeVO.TransInfo transInfo = new TourismConsumptionAnalyzeVO.TransInfo();
        LocalDate now = LocalDate.now();
        LocalDate beginDate = now.withDayOfMonth(1);
        // 数据源T-2
        LocalDate endDate = now.plusDays(-2);
        // 当月还没有数据
        if (endDate.isBefore(beginDate)) {
            transInfo.setTransAt(BigDecimal.ZERO);
            transInfo.setSameTimeLastYear(BigDecimal.ZERO);
            transInfo.setYearOnYear(null);
            return transInfo;
        }
        LocalDate lastYearBeginDate = beginDate.plusYears(-1);
        LocalDate lastYearEndDate = endDate.plusYears(-1);

        return getTransInfo(transInfo, beginDate, endDate, lastYearBeginDate, lastYearEndDate);
    }


    TourismConsumptionAnalyzeVO.TransInfo getLastMonthTransInfo() {
        TourismConsumptionAnalyzeVO.TransInfo transInfo = new TourismConsumptionAnalyzeVO.TransInfo();
        LocalDate now = LocalDate.now();
        LocalDate beginDate = now.withDayOfMonth(1).plusMonths(-1);
        LocalDate endDate = beginDate.plusMonths(1).plusDays(-1);
        LocalDate lastYearBeginDate = beginDate.plusYears(-1);
        LocalDate lastYearEndDate = lastYearBeginDate.plusMonths(1).plusDays(-1);

        return getTransInfo(transInfo, beginDate, endDate, lastYearBeginDate, lastYearEndDate);
    }

    TourismConsumptionAnalyzeVO.TransInfo getCurrentYearTransInfo() {
        TourismConsumptionAnalyzeVO.TransInfo transInfo = new TourismConsumptionAnalyzeVO.TransInfo();
        LocalDate now = LocalDate.now();
        LocalDate beginDate = now.withMonth(1).withDayOfMonth(1);
        // 数据源T-2
        LocalDate endDate = now.plusDays(-2);
        // 当年还没有数据
        if (endDate.isBefore(beginDate)) {
            transInfo.setTransAt(BigDecimal.ZERO);
            transInfo.setSameTimeLastYear(BigDecimal.ZERO);
            transInfo.setYearOnYear(null);
            return transInfo;
        }
        LocalDate lastYearBeginDate = beginDate.plusYears(-1);
        LocalDate lastYearEndDate = endDate.plusYears(-1);

        return getTransInfo(transInfo, beginDate, endDate, lastYearBeginDate, lastYearEndDate);
    }

    private TourismConsumptionAnalyzeVO.TransInfo getTransInfo(TourismConsumptionAnalyzeVO.TransInfo transInfo, LocalDate beginDate, LocalDate endDate, LocalDate lastYearBeginDate, LocalDate lastYearEndDate) {
        BigDecimal transAt = getTransAt(beginDate, endDate);
        BigDecimal sameTimeLastYear = getTransAt(lastYearBeginDate, lastYearEndDate);
        transInfo.setTransAt(transAt);
        transInfo.setSameTimeLastYear(sameTimeLastYear);
        transInfo.setYearOnYear(BigDecimal.ZERO);
        if (sameTimeLastYear.compareTo(BigDecimal.ZERO) != 0) {
            transInfo.setYearOnYear(transAt.subtract(sameTimeLastYear).divide(sameTimeLastYear, 4, RoundingMode.HALF_DOWN));
        } else if (transAt.compareTo(BigDecimal.ZERO) != 0) {
            transInfo.setYearOnYear(BigDecimal.ONE);
        }

        return transInfo;
    }

    private BigDecimal getTransAt(LocalDate beginDate, LocalDate endDate) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(
                QueryBuilders.rangeQuery("dealDay")
                        .gte(beginDate.format(DateUtil.LOCAL_DATE))
                        .lte(endDate.format(DateUtil.LOCAL_DATE))
        );
        SumAggregationBuilder transAtSumAggBuilder = AggregationBuilders.sum("transAt_sum").field("transAt");
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("county_consumption").withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(transAtSumAggBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum transAtSumAgg = response.getAggregations().get("transAt_sum");
            String valueAsString = transAtSumAgg.getValueAsString();
            return new BigDecimal(valueAsString).setScale(2, RoundingMode.HALF_UP);
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
