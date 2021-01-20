package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.DateCountVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.PneumoniaForeignPeopleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IPneumaticService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign.TrafficFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.TokenInfo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fang yun long
 * @date 2020-03-03 10:09
 */
@Slf4j
@Service
public class PneumaticServiceImpl implements IPneumaticService {

    private final static String DATE_TYPE_DAY = "day";
    private final static String DATE_TYPE_MONTH = "month";

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TrafficFeignService trafficFeignService;

    public PneumaticServiceImpl(ElasticsearchTemplate elasticsearchTemplate, TrafficFeignService trafficFeignService) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.trafficFeignService = trafficFeignService;
    }

    /**
     * 查询外来人员
     *
     * @param city     城市
     * @param dateType 日期类型  day month
     * @param begin    开始日期
     * @param end      结束日期
     * @return Result
     */
    @Override
    public Result peopleFromForeign(String city, String dateType, Long begin, Long end) {
        LocalDateTime beginDate = DateUtil.longToLocalDateTime(begin);
        LocalDateTime endDate = DateUtil.longToLocalDateTime(end);
        log.info("查询外来人员, city:{}, dateType:{}, beginDate:{}, endDate:{}", city, dateType, beginDate, endDate);

        if (StringUtils.isEmpty(city) || StringUtils.isEmpty(dateType)) {
            return ResultUtil.paramError("参数不能为空");
        }

        // 日期查询格式
        String queryDateFormat = "yyyy-MM-dd";
        LocalDate beginLocalDate = beginDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();
        dateType = dateType.toLowerCase();
        LocalDateTime lastBeginLocal;
        LocalDateTime lastEndLocal;
        if (DATE_TYPE_DAY.equals(dateType)) {
            beginDate = LocalDateTime.of(beginLocalDate, getBeginLocalTime());
            endDate = LocalDateTime.of(endDate.toLocalDate(), getEndLocalTime());

            lastBeginLocal = LocalDateTime.of(beginLocalDate.plusDays(-1), getBeginLocalTime());
            lastEndLocal = LocalDateTime.of(beginLocalDate.plusDays(-1), getEndLocalTime());

        } else if (DATE_TYPE_MONTH.equals(dateType)) {
            LocalDate tempBeginLocalDate = beginLocalDate;
            tempBeginLocalDate = LocalDate.of(tempBeginLocalDate.getYear(), tempBeginLocalDate.getMonth(), 1);
            beginDate = LocalDateTime.of(tempBeginLocalDate, getBeginLocalTime());
            endDate = LocalDateTime.of(LocalDate.of(endLocalDate.getYear(), endLocalDate.getMonth(), endLocalDate.lengthOfMonth()), getEndLocalTime());
            queryDateFormat = "yyyy-MM";

            LocalDate lastLocalDate = tempBeginLocalDate.plusMonths(-1);
            lastBeginLocal = LocalDateTime.of(lastLocalDate, getBeginLocalTime());
            lastEndLocal = LocalDateTime.of(LocalDate.of(lastLocalDate.getYear(), lastLocalDate.getMonth(), lastLocalDate.lengthOfMonth()), getEndLocalTime());
        } else {
            return ResultUtil.paramError("dateType类型不正确");
        }

        // 查询今年的数据
        List<DateCountVO> currentYear = queryTouristLocalData(city, beginDate, endDate, dateType, queryDateFormat);
        List<DateCountVO> lastYear = queryTouristLocalData(city, beginDate.plusYears(-1), endDate.plusYears(-1), dateType, queryDateFormat);
        List<PneumoniaForeignPeopleVO.LineChartVO> result = handleForeignPeople(currentYear, lastYear, dateType);

        // 查询 beginLocalDate 前一天的数据, 用作对比
        List<DateCountVO> lastDateCount = queryTouristLocalData(city, lastBeginLocal, lastEndLocal, dateType, queryDateFormat);
        if (!CollectionUtils.isEmpty(lastDateCount) && !CollectionUtils.isEmpty(result)) {
            DateCountVO lastCount = lastDateCount.get(0);
            PneumoniaForeignPeopleVO.LineChartVO currentCount = result.get(0);
            log.info("last count : {} , currentCount :{}", lastCount.getCount(), currentCount.getCurrentYear());
            currentCount.setCompareLast(compareLast(lastCount.getCount(), currentCount.getCurrentYear()));
        }

        PneumoniaForeignPeopleVO peopleVO = new PneumoniaForeignPeopleVO();
        peopleVO.setLineCharts(result);
        // 查询武汉人数
        Double whPeople = queryWhPeople("武汉市", dateType, beginDate, endDate, queryDateFormat);
        peopleVO.setWhCount(whPeople.longValue());
        // 查询湖北人数
        Double hbPeople = queryHbPeople("湖北", dateType, beginDate, endDate, queryDateFormat);
        peopleVO.setHbCount(hbPeople.longValue());

        return ResultUtil.buildSuccessResultWithData(peopleVO);
    }

    /**
     * 查询外来车辆
     *
     * @param city     城市
     * @param dateType 日期类型  day month
     * @param begin    开始日期
     * @param end      结束日期
     * @return Result
     */
    @Override
    public Result carFromForeign(String province, String city, String dateType, Long begin, Long end) {
        log.info("查询外来车辆, province:{}, city:{}, dateType:{}, begin:{}, end:{}", province, city, dateType, begin, end);
        Result<PneumoniaForeignPeopleVO.FeignCarVO> feignCarVOResult = trafficFeignService.industryEpidemic(begin, end, province, city, dateType.toUpperCase());
        log.info("外来车辆 Feign 查询结果 : {}", JsonUtil.toJsonString(feignCarVOResult));
        TokenInfo tokenInfo = CustomContext.getTokenInfo();
        log.info("token info is :{}", JsonUtil.toJsonString(tokenInfo));
        String format;

        if (DATE_TYPE_DAY.equals(dateType)) {
            format = "MM-dd";
        } else if (DATE_TYPE_MONTH.equals(dateType)) {
            format = "yyyy-MM";
        } else {
            return ResultUtil.paramError("dateType不正确");
        }
        if (feignCarVOResult.getSuccess()) {
            PneumoniaForeignPeopleVO peopleVO = new PneumoniaForeignPeopleVO();
            PneumoniaForeignPeopleVO.FeignCarVO feignCarVO = feignCarVOResult.getData();
            peopleVO.setHbCount(feignCarVO.getProvinceCount());
            peopleVO.setWhCount(feignCarVO.getCityCount());
            // 转换折线图
            List<PneumoniaForeignPeopleVO.FeignCarLineChartVO> feignCarLineChartVOS = feignCarVO.getEpidemicListList();

            if (CollectionUtils.isEmpty(feignCarLineChartVOS)) {
                peopleVO.setLineCharts(new ArrayList<>());
            } else {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                List<PneumoniaForeignPeopleVO.LineChartVO> lineCharts = feignCarLineChartVOS.stream().map(feignCarLineChartVO -> {
                    String date = formatter.format(DateUtil.longToLocalDateTime(feignCarLineChartVO.getTime()));
                    Long currentYear = feignCarLineChartVO.getNowCount();
                    Long lastYear = feignCarLineChartVO.getLastYearCount();
                    Double compareLastYear = feignCarLineChartVO.getLastYearPeriod();
                    Double compareLast = feignCarLineChartVO.getYesterdaySequential();

                    return new PneumoniaForeignPeopleVO.LineChartVO(date, currentYear, lastYear, compareLastYear, compareLast);
                }).collect(Collectors.toList());
                peopleVO.setLineCharts(lineCharts);
            }
            return ResultUtil.buildSuccessResultWithData(peopleVO);
        } else {
            feignCarVOResult.setMessage("查询外来车辆" + feignCarVOResult.getMessage());
        }
        return feignCarVOResult;
    }

    @Override
    public Result carCities() {
        return trafficFeignService.industryLocations();
    }

    @SuppressWarnings("Duplicates")
    private Double queryWhPeople(String city, String flag, LocalDateTime beginDate, LocalDateTime endDate, String dateFormat) {
        log.info("查询武汉总人数, beginDate:{}, endDate:{}, dateformat:{}", dateFormat);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String begin = formatter.format(beginDate);
        String end = formatter.format(endDate);
        // 查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(begin).lte(end));
        queryBuilder.must(QueryBuilders.termQuery("flag", flag));
        queryBuilder.must(QueryBuilders.termQuery("cityName", city));
        // 只需要查询洪雅县的人数
        queryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        searchQueryBuilder.withIndices("tourist_source_city").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        Double sumPeopleNum = elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumPeopleNum");
            return sum.getValue();
        });
        log.info("武汉总人数:{}", sumPeopleNum);
        return sumPeopleNum;
    }

    @SuppressWarnings("Duplicates")
    private Double queryHbPeople(String city, String flag, LocalDateTime beginDate, LocalDateTime endDate, String dateFormat) {
        log.info("查询湖北总人数, beginDate:{}, endDate:{}, dateformat:{}", dateFormat);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String begin = formatter.format(beginDate);
        String end = formatter.format(endDate);
        // 查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(begin).lte(end));
        queryBuilder.must(QueryBuilders.termQuery("flag", flag));
        queryBuilder.must(QueryBuilders.termQuery("provName", city));
        // 只需要查询洪雅县的人数
        queryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPeopleNum").field("peopleNum");

        searchQueryBuilder.withIndices("tourist_source_city").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(sumAggregationBuilder);
        Double sumPeopleNum = elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum sum = response.getAggregations().get("sumPeopleNum");
            return sum.getValue();
        });
        log.info("湖北总人数:{}", sumPeopleNum);
        return sumPeopleNum;
    }


    /**
     * 处理去年和今年的对比
     */
    private List<PneumoniaForeignPeopleVO.LineChartVO> handleForeignPeople(List<DateCountVO> currentYear, List<DateCountVO> lastYear, String dateType) {
        int currentYearSize = currentYear.size();
        int lastYearSize = lastYear.size();
        if (currentYearSize == lastYearSize) {
            log.info("当年数量和去年相等");
            return currentYearSizeEqualLastYearSize(currentYear, lastYear, dateType);
        } else if (currentYearSize > lastYearSize) {
            log.info("当年数量比去年数量多一天");
            Map<String, Long> map = dateCountVOListToMap(lastYear);
            return currentYearSizeGreatLastYearSize(currentYear, map, dateType);
        } else {
            log.info("当年数量比去年数量少一天");
            Map<String, Long> map = dateCountVOListToMap(currentYear);
            return currentYearSizeLessLastYearSize(lastYear, map, dateType);
        }
    }

    /**
     * 今年比去年少一天
     *
     * @param list     去年list
     * @param map      今年map
     * @param dateType 查询类型 day month
     * @return List
     */
    @SuppressWarnings("Duplicates")
    private List<PneumoniaForeignPeopleVO.LineChartVO> currentYearSizeLessLastYearSize(List<DateCountVO> list, Map<String, Long> map, String dateType) {

        List<PneumoniaForeignPeopleVO.LineChartVO> result = new ArrayList<>();
        Long yesterdayCount = 0L;
        String currentYearDate = "";
        for (DateCountVO dateCountVO : list) {
            PneumoniaForeignPeopleVO.LineChartVO lineChartVO = new PneumoniaForeignPeopleVO.LineChartVO();
            String date = "";
            if (DATE_TYPE_DAY.equals(dateType)) {
                Date currentDate = DateUtil.parse(dateCountVO.getDate(), "yyyy-MM-dd");
                date = DateUtil.format(currentDate, "MM-dd");
                currentYearDate = DateUtil.longToLocalDateTime(currentDate.getTime()).toLocalDate().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (DATE_TYPE_MONTH.equals(dateType)) {
                Date currentDate = DateUtil.parse(dateCountVO.getDate(), "yyyy-MM");
                date = DateUtil.format(currentDate, "yyyy-MM");
                currentYearDate = DateUtil.longToLocalDateTime(currentDate.getTime()).toLocalDate().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            // 处理去年
            lineChartVO.setDate(date);
            lineChartVO.setLastYear(dateCountVO.getCount());
            // 处理当年
            Long currentYearCount = null;
            if (map.get(currentYearDate) != null) {
                currentYearCount = map.get(currentYearDate);
            } else {
                log.info("当年今日为空:{}", currentYearDate);
            }
            lineChartVO.setCurrentYear(currentYearCount);
            // 同比去年
            lineChartVO.setCompareLastYear(compareLast(dateCountVO.getCount(), currentYearCount));
            // 环比昨日 或 上月 , 第一条数据的需要重新查询
            lineChartVO.setCompareLast(compareLast(yesterdayCount, currentYearCount));
            result.add(lineChartVO);
            yesterdayCount = currentYearCount;
        }
        return result;
    }

    /**
     * 今年比去年多一天
     *
     * @param list     今年list
     * @param map      去年map
     * @param dateType 查询类型 day month
     * @return List
     */
    @SuppressWarnings("Duplicates")
    private List<PneumoniaForeignPeopleVO.LineChartVO> currentYearSizeGreatLastYearSize(List<DateCountVO> list, Map<String, Long> map, String dateType) {

        List<PneumoniaForeignPeopleVO.LineChartVO> result = new ArrayList<>();
        Long yesterdayCount = 0L;
        String lastYearDate = "";
        for (DateCountVO dateCountVO : list) {
            PneumoniaForeignPeopleVO.LineChartVO lineChartVO = new PneumoniaForeignPeopleVO.LineChartVO();
            String date = "";
            if (DATE_TYPE_DAY.equals(dateType)) {
                Date currentDate = DateUtil.parse(dateCountVO.getDate(), "yyyy-MM-dd");
                date = DateUtil.format(currentDate, "MM-dd");
                lastYearDate = DateUtil.longToLocalDateTime(currentDate.getTime()).toLocalDate().plusYears(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (DATE_TYPE_MONTH.equals(dateType)) {
                Date currentDate = DateUtil.parse(dateCountVO.getDate(), "yyyy-MM");
                date = DateUtil.format(currentDate, "yyyy-MM");
                lastYearDate = DateUtil.longToLocalDateTime(currentDate.getTime()).toLocalDate().plusYears(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            // 处理当年
            lineChartVO.setDate(date);
            lineChartVO.setCurrentYear(dateCountVO.getCount());
            // 处理去年
            Long lastYearCount = null;
            if (map.get(lastYearDate) != null) {
                lastYearCount = map.get(lastYearDate);

            } else {
                log.info("去年今日为空:{}", lastYearDate);
            }
            lineChartVO.setLastYear(lastYearCount);
            // 同比去年
            lineChartVO.setCompareLastYear(compareLast(lastYearCount, dateCountVO.getCount()));
            // 环比昨日 或 上月 , 第一条数据的需要重新查询
            lineChartVO.setCompareLast(compareLast(yesterdayCount, dateCountVO.getCount()));
            result.add(lineChartVO);
            yesterdayCount = dateCountVO.getCount();
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    private List<PneumoniaForeignPeopleVO.LineChartVO> currentYearSizeEqualLastYearSize(List<DateCountVO> currentYear, List<DateCountVO> lastYear, String dateType) {
        log.info("currentYearSizeEqualLastYearSize:{}");
        List<PneumoniaForeignPeopleVO.LineChartVO> result = new ArrayList<>();
        Long lastCount = 0L;
        for (int i = 0; i < currentYear.size(); i++) {
            PneumoniaForeignPeopleVO.LineChartVO lineChartVO = new PneumoniaForeignPeopleVO.LineChartVO();
            // 处理当年
            DateCountVO currentYearCount = currentYear.get(i);
            String date = "";
            if (DATE_TYPE_DAY.equals(dateType)) {
                date = DateUtil.format(DateUtil.parse(currentYearCount.getDate(), "yyyy-MM-dd"), "MM-dd");
            } else if (DATE_TYPE_MONTH.equals(dateType)) {
                date = DateUtil.format(DateUtil.parse(currentYearCount.getDate(), "yyyy-MM"), "yyyy-MM");
            }
            lineChartVO.setDate(date);
            lineChartVO.setCurrentYear(currentYearCount.getCount());
            // 处理去年
            DateCountVO lastYearCount = lastYear.get(i);
            lineChartVO.setLastYear(lastYearCount.getCount());

            // 同比去年
            lineChartVO.setCompareLastYear(compareLast(lastYearCount.getCount(), currentYearCount.getCount()));

            // 环比昨日 或 上月, 第一条数据的需要重新查询
            lineChartVO.setCompareLast(compareLast(lastCount, currentYearCount.getCount()));

            result.add(lineChartVO);
            lastCount = currentYearCount.getCount();
        }
        return result;
    }


    /**
     * 比较 昨日丶上月丶去年
     */
    private Double compareLast(Long lastCount, Long currentCount) {

        if (lastCount == null || lastCount == 0) {
            if (currentCount != null && currentCount != 0) {
                return 1.0;
            }
            return null;
        }
        BigDecimal bigDecimal = new BigDecimal(1.0 * (currentCount - lastCount) / lastCount);
        return bigDecimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
    }


    private Map<String, Long> dateCountVOListToMap(List<DateCountVO> list) {
        return list.stream().collect(Collectors.toMap(DateCountVO::getDate, DateCountVO::getCount));
    }

    /**
     * @param city       城市名称
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param flag       标记  day / month
     * @param dateFormat 日期格式  yyyy-MM-dd / yyyy-MM
     * @return List<TouristSourceCityData>
     */
    @SuppressWarnings("Duplicates")
    private List<DateCountVO> queryTouristLocalData(String city, LocalDateTime beginDate, LocalDateTime endDate, String flag, String dateFormat) {
        log.info("查询elasticsearch, city:{}, beginDate:{}, endDate:{}, flag:{}, dateFormat:{}", city, beginDate, endDate, flag, dateFormat);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        String begin = formatter.format(beginDate);
        String end = formatter.format(endDate);
        // 查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(beginDate.format(DateUtil.LOCAL_DATE)).lte(endDate.format(DateUtil.LOCAL_DATE)));
        queryBuilder.must(QueryBuilders.termQuery("flag", flag));
        // 只需要查询洪雅县的人数
        queryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        queryBuilder.should(QueryBuilders.termQuery("provName", city))
                .should(QueryBuilders.termQuery("cityName", city))
                .minimumShouldMatch(1);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        // interval 默认 DateHistogramInterval.DAY
        DateHistogramInterval dateHistogramInterval = DateHistogramInterval.DAY;
        if (DATE_TYPE_MONTH.equals(flag)) {
            dateHistogramInterval = DateHistogramInterval.MONTH;
        }
        String aggNameForTime = "timeHistogram";
        String aggNameForSum = "sumPeopleNum";
        // 聚合
        DateHistogramAggregationBuilder aggregationBuilder = AggregationBuilders
                .dateHistogram(aggNameForTime)
                .field("time")
                .format(dateFormat)
                .dateHistogramInterval(dateHistogramInterval)
                .extendedBounds(new ExtendedBounds(begin, end));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(aggNameForSum).field("peopleNum");
        // 添加sum子聚合
        aggregationBuilder.subAggregation(sumAggregationBuilder);
        searchQueryBuilder.withIndices("tourist_source_city").withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Histogram histogram = response.getAggregations().get(aggNameForTime);
            List<DateCountVO> dateCountVOS = new ArrayList<>();
            histogram.getBuckets().forEach(bucket -> {
                String key = bucket.getKeyAsString();
                InternalSum sum = bucket.getAggregations().get(aggNameForSum);
                Double sumPeople = sum.getValue();
                DateCountVO dateCountVO = new DateCountVO(key, sumPeople.longValue());
                dateCountVOS.add(dateCountVO);
            });
            return dateCountVOS;
        });
    }

    private LocalTime getBeginLocalTime() {
        return LocalTime.of(0, 0, 0, 0);
    }

    private LocalTime getEndLocalTime() {
        return LocalTime.of(23, 59, 59, 999999999);
    }


}
