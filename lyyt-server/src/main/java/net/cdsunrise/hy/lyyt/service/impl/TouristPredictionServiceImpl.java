package net.cdsunrise.hy.lyyt.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyyt.config.TouristPredictionConfig;
import net.cdsunrise.hy.lyyt.entity.vo.HolidayVO;
import net.cdsunrise.hy.lyyt.entity.vo.TouristPredictionVO;
import net.cdsunrise.hy.lyyt.enums.PredictionWeatherEnum;
import net.cdsunrise.hy.lyyt.enums.TouristPredictionFactorEnum;
import net.cdsunrise.hy.lyyt.service.TouristPredictionService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2020/04/22 16:18
 */
@Service
public class TouristPredictionServiceImpl implements TouristPredictionService {

    private final TouristPredictionConfig touristPredictionConfig;
    private final DataDictionaryFeignClient dataDictionaryFeignClient;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public TouristPredictionServiceImpl(TouristPredictionConfig touristPredictionConfig, DataDictionaryFeignClient dataDictionaryFeignClient, ElasticsearchTemplate elasticsearchTemplate) {
        this.touristPredictionConfig = touristPredictionConfig;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public TouristPredictionVO<? extends TouristPredictionVO.ScenicVO> prediction(LocalDate day) {
        // 拉取游客范围数据字典
        List<String> touristScopes = getTouristScopes();
        // 根据是否是节假日，进行不同的预测方式
        HolidayVO holiday = isHoliday(day);
        if (holiday == null) {
            return normalDayTouristPrediction(day, touristScopes);
        } else {
            return holidayTouristPrediction(holiday, touristScopes);
        }
    }

    /**
     * 判断是否是节假日
     *
     * @param day 日期
     * @return 节假日对象，如果不是节假日则为null
     */
    private HolidayVO isHoliday(LocalDate day) {
        return null;
    }

    /**
     * 节假日预测
     *
     * @param holiday       节假日对象
     * @param touristScopes 游客范围
     * @return 结果
     */
    private TouristPredictionVO<TouristPredictionVO.HolidayScenicVO> holidayTouristPrediction(HolidayVO holiday, List<String> touristScopes) {
        return null;
    }

    /**
     * 普通日期预测
     *
     * @param day           日期
     * @param touristScopes 游客范围
     * @return 结果
     */
    private TouristPredictionVO<TouristPredictionVO.NormalScenicVO> normalDayTouristPrediction(LocalDate day, List<String> touristScopes) {
        LocalDate tomorrow = day.plusDays(1);
        Map<TouristPredictionFactorEnum, Double> factorWeights = touristPredictionConfig.getFactorWeights();
        if (CollectionUtils.isEmpty(factorWeights)) {
            factorWeights = TouristPredictionFactorEnum.getDefaultFactors();
        }
        Map<TouristPredictionFactorEnum, List<Scenic>> todayFactorPeopleNums = new HashMap<>(factorWeights.size());
        Map<TouristPredictionFactorEnum, List<Scenic>> tomorrowFactorPeopleNums = new HashMap<>(factorWeights.size());
        // 轮询所有因子
        factorWeights.keySet().forEach(touristPredictionFactorEnum -> {
            List<Scenic> todayFactorPeopleNum = null;
            List<Scenic> tomorrowFactorPeopleNum = null;
            // 根据因子类型调用不同算法
            switch (touristPredictionFactorEnum) {
                case YESTERDAY: {
                    // 这个因子只处理今日的昨日游客数，明日的昨日游客数为今日的预测游客数
                    todayFactorPeopleNum = getYesterdayPeopleNum(day);
                    break;
                }
                case LAST_WEEK_SAME_PERIOD: {
                    todayFactorPeopleNum = getLastWeekSamePeriodPeopleNum(day);
                    tomorrowFactorPeopleNum = getLastWeekSamePeriodPeopleNum(tomorrow);
                    break;
                }
                case TWO_WEEKS_AGO_SAME_PERIOD: {
                    todayFactorPeopleNum = getTwoWeeksAgoSamePeriodPeopleNum(day);
                    tomorrowFactorPeopleNum = getTwoWeeksAgoSamePeriodPeopleNum(tomorrow);
                    break;
                }
                case LAST_MONTH_SAME_PERIOD: {
                    todayFactorPeopleNum = getLastMonthSamePeriodPeopleNum(day);
                    tomorrowFactorPeopleNum = getLastMonthSamePeriodPeopleNum(tomorrow);
                    break;
                }
                case LAST_WEEK_AVG: {
                    todayFactorPeopleNum = getLastWeekAvgPeopleNum(day);
                    tomorrowFactorPeopleNum = getLastWeekAvgPeopleNum(tomorrow);
                    break;
                }
                case LAST_MONTH_AVG: {
                    todayFactorPeopleNum = getLastMonthAvgPeopleNum(day);
                    tomorrowFactorPeopleNum = getLastMonthAvgPeopleNum(tomorrow);
                    break;
                }
                case THIS_MONTH_AVG: {
                    todayFactorPeopleNum = getThisMonthAvgPeopleNum(day);
                    tomorrowFactorPeopleNum = getThisMonthAvgPeopleNum(tomorrow);
                    break;
                }
                default:
            }
            todayFactorPeopleNums.put(touristPredictionFactorEnum, todayFactorPeopleNum);
            tomorrowFactorPeopleNums.put(touristPredictionFactorEnum, tomorrowFactorPeopleNum);
        });
        // 开始预测今日游客数
        List<Scenic> todayPredictions = calcPredictionPeopleNum(todayFactorPeopleNums, factorWeights);
        Map<String, Integer> todayPredictionMap = todayPredictions.stream().collect(Collectors.toMap(Scenic::getScenicName, Scenic::getPeopleNum));
        if (factorWeights.containsKey(TouristPredictionFactorEnum.YESTERDAY)) {
            /* 明日的昨日游客数为今日的预测游客数
             * 这种方式共用一个List，如果其他地方修改里面的内容，会出现问题
             */
            tomorrowFactorPeopleNums.put(TouristPredictionFactorEnum.YESTERDAY, todayPredictions);
        }
        // 开始预测明日游客数
        List<Scenic> tomorrowPredictions = calcPredictionPeopleNum(tomorrowFactorPeopleNums, factorWeights);
        Map<String, Integer> tomorrowPredictionMap = tomorrowPredictions.stream().collect(Collectors.toMap(Scenic::getScenicName, Scenic::getPeopleNum));
        // 组装预测结果
        List<TouristPredictionVO.NormalScenicVO> predictions = touristScopes.stream().map(scenicName -> {
            Integer todayPeopleNum = todayPredictionMap.getOrDefault(scenicName, 0);
            Integer tomorrowPeopleNum = tomorrowPredictionMap.getOrDefault(scenicName, 0);
            TouristPredictionVO.NormalScenicVO scenicVO = new TouristPredictionVO.NormalScenicVO();
            scenicVO.setScenicName(scenicName);
            scenicVO.setTodayPeopleNum(todayPeopleNum);
            scenicVO.setTomorrowPeopleNum(tomorrowPeopleNum);
            return scenicVO;
        }).collect(Collectors.toList());

        TouristPredictionVO<TouristPredictionVO.NormalScenicVO> touristPredictionVO = new TouristPredictionVO<>();
        touristPredictionVO.setIsHoliday(false);
        touristPredictionVO.setPredictions(predictions);
        return touristPredictionVO;
    }

    /**
     * 获取某日的当月平均游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getThisMonthAvgPeopleNum(LocalDate day) {
        LocalDate monthBegin = day.withDayOfMonth(1);
        LocalDate monthEnd = monthBegin.plusMonths(1).plusDays(-1);
        return commonPeopleNum(monthBegin, monthEnd, false);
    }

    /**
     * 获取某日的上月平均游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getLastMonthAvgPeopleNum(LocalDate day) {
        LocalDate lastMonth = day.plusMonths(-1);
        LocalDate lastMonthBegin = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthEnd = day.withDayOfMonth(1).plusDays(-1);
        return commonPeopleNum(lastMonthBegin, lastMonthEnd, false);
    }

    /**
     * 获取某日的上周平均游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getLastWeekAvgPeopleNum(LocalDate day) {
        LocalDate lastWeek = day.plusWeeks(-1);
        LocalDate lastWeekMonday = lastWeek.with(DayOfWeek.MONDAY);
        LocalDate lastWeekSunday = lastWeek.with(DayOfWeek.SUNDAY);
        return commonPeopleNum(lastWeekMonday, lastWeekSunday, false);
    }

    /**
     * 获取某日的上月同期游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getLastMonthSamePeriodPeopleNum(LocalDate day) {
        LocalDate lastMonth = day.plusMonths(-1);
        return commonPeopleNum(lastMonth, lastMonth, true);
    }

    /**
     * 获取某日的上上周同期游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getTwoWeeksAgoSamePeriodPeopleNum(LocalDate day) {
        LocalDate twoWeeksAgo = day.plusWeeks(-2);
        return commonPeopleNum(twoWeeksAgo, twoWeeksAgo, true);
    }

    /**
     * 获取某日的上周同期游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getLastWeekSamePeriodPeopleNum(LocalDate day) {
        LocalDate lastWeek = day.plusWeeks(-1);
        return commonPeopleNum(lastWeek, lastWeek, true);
    }

    /**
     * 获取某日的昨日游客数
     *
     * @param day 日期
     * @return 结果
     */
    private List<Scenic> getYesterdayPeopleNum(LocalDate day) {
        LocalDate yesterday = day.plusDays(-1);
        return commonPeopleNum(yesterday, yesterday, true);
    }

    /**
     * 获取游客数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 结果
     */
    private List<Scenic> commonPeopleNum(LocalDate beginDate, LocalDate endDate, boolean sumOrAvg) {
        String beginString = beginDate.format(DateUtil.LOCAL_DATE);
        String endString = endDate.format(DateUtil.LOCAL_DATE);
        List<Scenic> dataList = new ArrayList<>();
        String termAggName = "scenic_agg";
        String sumAggName = "sum_people_count";
        String avgAggName = "avg_people_count";
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(sumAggName).field("peopleNum");
        AggregationBuilder avgAggregationBuilder = AggregationBuilders.avg(avgAggName).field("peopleNum");
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms(termAggName)
                .field("scenicName")
                .subAggregation(sumOrAvg ? sumAggregationBuilder : avgAggregationBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "day"))
                .filter(QueryBuilders.rangeQuery("time").gte(beginString).lte(endString));
        NativeSearchQuery yearQuery = new NativeSearchQueryBuilder().withIndices("tourist_local_data").withTypes("doc")
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(yearQuery, SearchResponse::getAggregations);
        StringTerms stringTerms = temp.get(termAggName);
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Integer peopleNum;
            if (sumOrAvg) {
                Sum sum = bucket.getAggregations().get(sumAggName);
                peopleNum = Double.valueOf(sum.getValue()).intValue();
            } else {
                Avg avg = bucket.getAggregations().get(avgAggName);
                peopleNum = Double.valueOf(avg.getValue()).intValue();
            }
            Scenic scenic = new Scenic(name, peopleNum);
            dataList.add(scenic);
        }
        return dataList;
    }

    /**
     * 计算各景区预测游客数
     *
     * @param factorPeopleNums 各因子游客
     * @param factorWeights    因子权重
     * @return 各景区今日、明日预测游客数
     */
    private List<Scenic> calcPredictionPeopleNum(Map<TouristPredictionFactorEnum, List<Scenic>> factorPeopleNums,
                                                 Map<TouristPredictionFactorEnum, Double> factorWeights) {
        List<Scenic> dataList = new ArrayList<>();
        Map<String, List<Factor>> map = new HashMap<>();
        factorPeopleNums.forEach((factorEnum, scenicList) -> scenicList.stream().filter(scenic -> scenic.getPeopleNum() > 0).forEach(scenic -> {
            Factor factor = new Factor(scenic.getPeopleNum(), factorWeights.get(factorEnum));
            String scenicName = scenic.getScenicName();
            List<Factor> factorList = map.getOrDefault(scenicName, new ArrayList<>());
            factorList.add(factor);
            map.put(scenicName, factorList);
        }));
        map.forEach((scenicName, factorList) -> dataList.add(forecastPeopleNum(scenicName, factorList)));
        return dataList;
    }

    private Scenic forecastPeopleNum(String scenicName, List<Factor> factorList) {
        Double total = factorList.stream().mapToDouble(Factor::getWeight).sum();
        Double peopleNum = 0.0;
        for (Factor factor : factorList) {
            Double percent = factor.getWeight() / total;
            peopleNum += factor.getPeopleNum() * percent;
        }
        // 乘以天气系数
        double weatherCoefficient = getWeatherCoefficient();
        peopleNum = peopleNum * weatherCoefficient;
        return new Scenic(scenicName, peopleNum.intValue());
    }

    /**
     * 获取天气系数
     *
     * @return 结果
     */
    private double getWeatherCoefficient() {
        if (!touristPredictionConfig.getUseWeatherFactor()) {
            return 1;
        }
        Map<PredictionWeatherEnum, Double> weatherCoefficients = touristPredictionConfig.getWeatherCoefficients();
        if (CollectionUtils.isEmpty(weatherCoefficients)) {
            weatherCoefficients = PredictionWeatherEnum.getDefaultCoefficients();
        }
        // TODO 获取天气情况
        return weatherCoefficients.getOrDefault(PredictionWeatherEnum.GOOD, PredictionWeatherEnum.GOOD.getDefaultCoefficient());
    }

    private List<String> getTouristScopes() {
        String touristsScope = "TOURISTS_SCOPE";
        String[] codes = {touristsScope};
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(codes);
        DataDictionaryVO dataDictionaryVO = result.getData().get(touristsScope);
        return dataDictionaryVO.getChildren().stream().map(DataDictionaryVO::getName).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Scenic {

        /**
         * 景区名称
         */
        private String scenicName;

        /**
         * 游客数
         */
        private Integer peopleNum;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Factor {

        /**
         * 游客数
         */
        private Integer peopleNum;


        /**
         * 因子权重
         */
        private Double weight;
    }
}
