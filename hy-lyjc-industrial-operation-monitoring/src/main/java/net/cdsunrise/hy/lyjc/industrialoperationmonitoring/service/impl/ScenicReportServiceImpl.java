package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ScenicReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IScenicReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author FangYunLong
 * @date in 2020/5/9
 */
@Slf4j
@Service
public class ScenicReportServiceImpl implements IScenicReportService {


    private final ElasticsearchTemplate elasticsearchTemplate;

    private static final DateTimeFormatter DATE_RANGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年M月d日");

    public ScenicReportServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public ScenicReportVO statisticsScenicReport(Long begin, Long end) {
        LocalDateTime beginDate = LocalDateTime.of(DateUtil.longToLocalDateTime(begin).toLocalDate(), DateUtil.getBeginLocalTime());
        LocalDateTime endDate = LocalDateTime.of(DateUtil.longToLocalDateTime(end).toLocalDate(), DateUtil.getEndLocalTime());
        log.info("统计报告, beginDate:{}, endDate:{}", beginDate, endDate);


        ScenicReportVO scenicReportVO = new ScenicReportVO();
        // 一 统计概况
        statisticsOverview(scenicReportVO, beginDate, endDate);
        // 二 客流量分析
        passengerFlowAnalysis(scenicReportVO, beginDate, endDate);
        // 统计县域游客饼图来源
        String stringBeginDate = beginDate.format(DateUtil.LOCAL_DATE);
        String stringEndDate = endDate.format(DateUtil.LOCAL_DATE);
        Integer provinceInner = provinceInner(stringBeginDate, stringEndDate);
        Integer provinceOuter = provinceOuter(stringBeginDate, stringEndDate);
        Integer countryOuter = countryOuter(stringBeginDate, stringEndDate);
        ScenicReportVO.PiePassengerSource piePassengerSource = new ScenicReportVO.PiePassengerSource();
        piePassengerSource.setProvinceInner(provinceInner);
        piePassengerSource.setProvinceOuter(provinceOuter);
        piePassengerSource.setCountryOuter(countryOuter);

        // 计算我省累计客流量占比
        Integer total = provinceInner + provinceOuter + countryOuter;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (total == 0) {
            scenicReportVO.getPFA().setPassengerInProvince("0");
            scenicReportVO.getPFA().setRatioInProvince("0");
        } else {

            scenicReportVO.getPFA().setPassengerInProvince(decimalFormat.format(provinceInner / 10000));
            Double ratio = Double.valueOf(provinceInner) / total * 100;
            scenicReportVO.getPFA().setRatioInProvince(decimalFormat.format(ratio));
        }
        scenicReportVO.getPFA().setPiePassengerSource(piePassengerSource);

        // 计算省内前5城市
        List<ScenicReportVO.LineChartVO> provinceInnerTop5 = calculateProvinceInnerTop5(stringBeginDate, stringEndDate);
        scenicReportVO.getPFA().setBarPassengerInProvince(provinceInnerTop5);
        StringJoiner barPassengerInProvince = new StringJoiner("、");
        for (ScenicReportVO.LineChartVO lineChartVO : provinceInnerTop5) {
            String peopleNum = decimalFormat.format(Double.valueOf(lineChartVO.getValue()) / 10000);
            barPassengerInProvince.add(lineChartVO.getName() + "（" + peopleNum + "）万人次");
        }
        scenicReportVO.getPFA().setTop5PassengerInProvince(barPassengerInProvince.toString());

        // 计算省外前5省份
        List<ScenicReportVO.LineChartVO> provinceOuterTop5 = calculateProvinceOuterTop5(stringBeginDate, stringEndDate);
        scenicReportVO.getPFA().setBarPassengerOutsideProvince(provinceOuterTop5);
        StringJoiner top5PassengerOutsideProvince = new StringJoiner("、");
        for (ScenicReportVO.LineChartVO lineChartVO : provinceOuterTop5) {
            Integer peopleNum = lineChartVO.getValue();
            top5PassengerOutsideProvince.add(lineChartVO.getName() + "（" + peopleNum + "）人次");
        }
        scenicReportVO.getPFA().setTop5PassengerOutsideProvince(top5PassengerOutsideProvince.toString());
        return scenicReportVO;
    }

    @SuppressWarnings("Duplicates")
    private List<ScenicReportVO.LineChartVO> calculateProvinceOuterTop5(String beginDate, String endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
                .must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .mustNot(QueryBuilders.termQuery("provName", "四川"));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupByProvName")
                .field("provName")
                .size(5)
                .subAggregation(sumAggregationBuilder)
                .order(BucketOrder.aggregation("sumPeopleNum", false));

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_prov").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder);

        List<ScenicReportVO.LineChartVO> list = new ArrayList<>();
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Terms terms = response.getAggregations().get("groupByProvName");
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String provName = bucket.getKeyAsString();
                InternalSum internalSum = bucket.getAggregations().get("sumPeopleNum");
                Integer peopleNum = Double.valueOf(internalSum.getValue()).intValue();
                ScenicReportVO.LineChartVO lineChartVO = new ScenicReportVO.LineChartVO(provName, peopleNum);
                list.add(lineChartVO);

            }
            return null;
        });
        return list;
    }

    /**
     * 省内游客前5城市排名
     */
    @SuppressWarnings("Duplicates")
    private List<ScenicReportVO.LineChartVO> calculateProvinceInnerTop5(String beginDate, String endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
                .must(QueryBuilders.termQuery("provName", "四川"))
                .must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupByCityName").field("cityName").size(10);
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        termsAggregationBuilder.subAggregation(sumAggregationBuilder)
                .order(BucketOrder.aggregation("sumPeopleNum", false))
                .size(5);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_city").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder);

        List<ScenicReportVO.LineChartVO> list = new ArrayList<>();
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Terms terms = response.getAggregations().get("groupByCityName");
            for (Terms.Bucket bucket : terms.getBuckets()) {
                String cityName = bucket.getKeyAsString();
                InternalSum sum = bucket.getAggregations().get("sumPeopleNum");
                Integer peopleNum = Double.valueOf(sum.getValue()).intValue();
                ScenicReportVO.LineChartVO lineChartVO = new ScenicReportVO.LineChartVO(cityName, peopleNum);
                list.add(lineChartVO);
            }
            return null;
        });
        return list;
    }

    /**
     * 统计省内游客数
     **/
    @SuppressWarnings("Duplicates")
    private Integer provinceInner(String beginDate, String endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
                .must(QueryBuilders.termQuery("provName", "四川"))
                .must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_city").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumPeopleNum");
            return Double.valueOf(sum.getValue()).intValue();
        });
    }

    /**
     * 统计省外游客数
     **/
    @SuppressWarnings("Duplicates")
    private Integer provinceOuter(String beginDate, String endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
                .mustNot(QueryBuilders.termQuery("provName", "四川"))
                .must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_prov").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumPeopleNum");
            return Double.valueOf(sum.getValue()).intValue();
        });
    }

    /**
     * 统计境外外游客数
     **/
    private Integer countryOuter(String beginDate, String endDate) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate).lte(endDate))
                .must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .mustNot(QueryBuilders.termQuery("countryName", "中国"));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_country").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumPeopleNum");
            return Double.valueOf(sum.getValue()).intValue();
        });
    }

    private void passengerFlowAnalysis(ScenicReportVO scenicReportVO, LocalDateTime beginDate, LocalDateTime endDate) {
        ScenicReportVO.PassengerFlowAnalysis pfa = new ScenicReportVO.PassengerFlowAnalysis();
        String begin = DateUtil.format(beginDate);
        String end = DateUtil.format(endDate);
        queryLinePassengerFlowByScenic(pfa, begin, end);
        Integer waWuShanTotalPeopleInPark = sumWaWuShanTotalInParkTourists(beginDate, endDate);
        pfa.setPassengerInWawu(waWuShanTotalPeopleInPark);
        scenicReportVO.setPFA(pfa);
    }

    /**
     * 查询景区游客趋势图
     *
     * @param begin   开始时间
     * @param end     结束时间
     */
    private void queryLinePassengerFlowByScenic(ScenicReportVO.PassengerFlowAnalysis pfa, String begin, String end) {
        log.info("begin:{}, end:{}", begin, end);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(begin).lte(end))
                .must(QueryBuilders.termQuery("flag", "day"));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupByScenicName").field("scenicName");

        DateHistogramAggregationBuilder dateHistogramAggregationBuilder =
                AggregationBuilders.dateHistogram("dateHistogram")
                        .field("time")
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .format("yyyy-MM-dd")
                        .extendedBounds(new ExtendedBounds(begin.substring(0, 10), end.substring(0, 10)));

        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");
        
        termsAggregationBuilder.subAggregation(dateHistogramAggregationBuilder);
        dateHistogramAggregationBuilder.subAggregation(sumAggregationBuilder);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_local_data")
                .withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder);

        List<ScenicReportVO.LineChartVO> qiLiPing = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> zhuChengQu = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> liuJiangGuZhen = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> caoYuTan = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> hongYaXian = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> yuPingShan = new ArrayList<>();
        List<ScenicReportVO.LineChartVO> waWuShan = new ArrayList<>();

        DateTimeFormatter monthDay = DateTimeFormatter.ofPattern("M-d");
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Terms terms = response.getAggregations().get("groupByScenicName");

            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            if (CollectionUtils.isEmpty(buckets)) {
                return null;
            }
            for (Terms.Bucket bucket : buckets) {
                String scenicName = bucket.getKeyAsString();
                Histogram histogram = bucket.getAggregations().get("dateHistogram");
                for (Histogram.Bucket histogramBucket : histogram.getBuckets()) {
                    String date = histogramBucket.getKeyAsString();
                    InternalSum sum = histogramBucket.getAggregations().get("sumPeopleNum");
                    Integer peopleNum = Double.valueOf(sum.getValue()).intValue();
                    LocalDate localDate = DateUtil.stringToLocalDate(date);
                    String dateAfterFormat = localDate.format(monthDay);
                    ScenicReportVO.LineChartVO lineChartVO = new ScenicReportVO.LineChartVO(dateAfterFormat, peopleNum);
                    switch (scenicName) {
                        case "七里坪":
                            qiLiPing.add(lineChartVO);
                            break;
                        case "主城区":
                            zhuChengQu.add(lineChartVO);
                            break;
                        case "柳江古镇":
                            liuJiangGuZhen.add(lineChartVO);
                            break;
                        case "槽渔滩":
                            caoYuTan.add(lineChartVO);
                            break;
                        case "洪雅县":
                            hongYaXian.add(lineChartVO);
                            break;
                        case "玉屏山":
                            yuPingShan.add(lineChartVO);
                            break;
                        case "瓦屋山":
                            waWuShan.add(lineChartVO);
                            break;
                        default:

                    }
                }
            }
            return null;
        });
        List<Basic> basicRankList = calculateScenicPeopleNumRank(qiLiPing, zhuChengQu, liuJiangGuZhen, caoYuTan, yuPingShan, waWuShan);
        ScenicReportVO.LinePassengerFlowByScenic linePassengerFlowByScenic = new ScenicReportVO.LinePassengerFlowByScenic();
        linePassengerFlowByScenic.setQiLiPing(qiLiPing);
        linePassengerFlowByScenic.setZhuChengQu(zhuChengQu);
        linePassengerFlowByScenic.setLiuJiangGuZhen(liuJiangGuZhen);
        linePassengerFlowByScenic.setCaoYuTan(caoYuTan);
        linePassengerFlowByScenic.setHongYaXian(hongYaXian);
        linePassengerFlowByScenic.setYuPingShan(yuPingShan);
        linePassengerFlowByScenic.setWaWuShan(waWuShan);
        pfa.setLinePassengerFlowByScenic(linePassengerFlowByScenic);
        pfa.setFirstFavoriteScenic(basicRankList.get(0).getName());
        pfa.setSecondFavoriteScenic(basicRankList.get(1).getName());
        pfa.setThirdFavoriteScenic(basicRankList.get(2).getName());
    }

    private List<Basic> calculateScenicPeopleNumRank(List<ScenicReportVO.LineChartVO> qiLiPing,
                                                     List<ScenicReportVO.LineChartVO> zhuChengQu,
                                                     List<ScenicReportVO.LineChartVO> liuJiangGuZhen,
                                                     List<ScenicReportVO.LineChartVO> caoYuTan,
                                                     List<ScenicReportVO.LineChartVO> yuPingShan,
                                                     List<ScenicReportVO.LineChartVO> waWuShan) {

        Integer qiLiPingTotal = qiLiPing.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();
        Integer zhuChengQuTotal = zhuChengQu.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();
        Integer liuJiangGuZhenTotal = liuJiangGuZhen.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();
        Integer caoYuTanTotal = caoYuTan.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();
        Integer yuPingShanTotal = yuPingShan.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();
        Integer waWuShanTotal = waWuShan.stream().mapToInt(ScenicReportVO.LineChartVO::getValue).sum();

        Basic qiLiPingBasic = new Basic("七里坪", qiLiPingTotal);
        Basic zhuChengQuBasic = new Basic("主城区", zhuChengQuTotal);
        Basic liuJiangGuZhenBasic = new Basic("柳江古镇", liuJiangGuZhenTotal);
        Basic caoYuTanBasic = new Basic("槽渔滩", caoYuTanTotal);
        Basic yuPingShanBasic = new Basic("玉屏山", yuPingShanTotal);
        Basic waWuShanBasic = new Basic("瓦屋山", waWuShanTotal);
        List<Basic> list = new ArrayList<>();
        list.add(qiLiPingBasic);
        list.add(zhuChengQuBasic);
        list.add(liuJiangGuZhenBasic);
        list.add(caoYuTanBasic);
        list.add(yuPingShanBasic);
        list.add(waWuShanBasic);
        Collections.sort(list);
        Collections.reverse(list);
        return list;
    }

    private Integer sumWaWuShanTotalInParkTourists(LocalDateTime beginDate, LocalDateTime endDate) {
        String begin = DateUtil.format(beginDate);
        String end = DateUtil.format(endDate);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("scenicName", "瓦屋山"))
                .must(QueryBuilders.rangeQuery("responseTime").gte(begin).lte(end));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumTodayTouristCount").field("todayTouristCount");
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket_daily")
                .withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumTodayTouristCount");
            return Double.valueOf(sum.getValue()).intValue();
        });
    }

    private void statisticsOverview(ScenicReportVO scenicReportVO, LocalDateTime beginDate, LocalDateTime endDate) {

        String dateRange = beginDate.format(DATE_RANGE_FORMATTER) + "至" + endDate.format(DATE_RANGE_FORMATTER);
        ScenicReportVO.Overview overview = new ScenicReportVO.Overview();

        // 洪雅县域客流量
        double passenger = 0D;
        // 各大景区范围客流量
        StringJoiner passengerByScenic = new StringJoiner(",");
        List<Basic> basics = queryPeopleNumByTimeRange(beginDate, endDate);
        if (!CollectionUtils.isEmpty(basics)) {
            for (Basic basic : basics) {
                if ("洪雅县".equals(basic.getName())) {
                    passenger = Double.valueOf(basic.getValue()) / 10000;
                } else {
                    Double value = Double.valueOf(basic.getValue()) / 10000;
                    String name = basic.getName();
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formatValue = df.format(value);
                    passengerByScenic.add(name + formatValue + "万人次");
                }
            }
        }
        overview.setDateRange(dateRange);
        overview.setPassenger(passenger);
        overview.setPassengerByScenic(passengerByScenic.toString());
        scenicReportVO.setOverview(overview);
    }


    private List<Basic> queryPeopleNumByTimeRange(LocalDateTime beginDate, LocalDateTime endDate) {
        String begin = DateUtil.format(beginDate);
        String end = DateUtil.format(endDate);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(begin).lte(end))
                .must(QueryBuilders.termQuery("flag", "day"));

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupByScenicName").field("scenicName");
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");
        termsAggregationBuilder.subAggregation(sumAggregationBuilder);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_local_data")
                .withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder);

        List<Basic> basics = new ArrayList<>();
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Terms groupByScenicName = response.getAggregations().get("groupByScenicName");
            List<? extends Terms.Bucket> buckets = groupByScenicName.getBuckets();
            if (CollectionUtils.isEmpty(buckets)) {
                return null;
            }
            buckets.forEach(bucket -> {
                InternalSum sum = bucket.getAggregations().get("sumPeopleNum");
                double value = sum.getValue();
                String keyAsString = bucket.getKeyAsString();
                Basic basic = new Basic(keyAsString, Double.valueOf(value).intValue());
                basics.add(basic);
            });
            return null;
        });
        Collections.sort(basics);
        Collections.reverse(basics);
        return basics;
    }

    @Data
    @AllArgsConstructor
    private static class Basic implements Comparable<Basic> {
        private String name;
        private Integer value;


        @Override
        public int compareTo(Basic another) {
            return value.compareTo(another.getValue());
        }
    }

}
