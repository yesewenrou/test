package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.config.ScenicTicketsPredicationConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicketDaily;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TicketAnalysisVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristAnalysisVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ScenicDictionaryEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ScenicTicketsPredicationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IHotMapFixService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristTicketService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author funnylog
 */
@Service
@Slf4j
public class TouristTicketServiceImpl implements ITouristTicketService {


    private final ElasticsearchTemplate elasticsearchTemplate;

    private final DataDictionaryFeignClient dataDictionaryFeignClient;

    private final IHotMapFixService iHotMapFixService;

    private final ScenicTicketsPredicationConfig scenicTicketsPredicationConfig;

    public TouristTicketServiceImpl(ElasticsearchTemplate elasticsearchTemplate,
                                    DataDictionaryFeignClient dataDictionaryFeignClient,
                                    IHotMapFixService iHotMapFixService,
                                    ScenicTicketsPredicationConfig scenicTicketsPredicationConfig) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.iHotMapFixService = iHotMapFixService;
        this.scenicTicketsPredicationConfig = scenicTicketsPredicationConfig;
    }

    public static final DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("M月d日");
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy年M月");
    private static final String RESPONSE_TIME_AGG = "responseTimeAgg";
    private static final String SUM_TODAY_TOURIST_COUNT_BY_MONTH = "sumTodayTouristCountByMonth";

    /**
     * 票务分析
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    @Override
    @SuppressWarnings("Duplicates")
    public TicketAnalysisVO ticketAnalysis(Long beginDate, Long endDate, String scenicName) {
        LocalDateTime begin = DateUtil.longToLocalDateTime(beginDate);
        begin = LocalDateTime.of(begin.toLocalDate(), DateUtil.getBeginLocalTime());
        LocalDateTime end = DateUtil.longToLocalDateTime(endDate);
        end = LocalDateTime.of(end.toLocalDate(), DateUtil.getEndLocalTime());
        log.info("票务分析查询:beginDate={}, endDate={}, scenicName={}", begin, end, scenicName);

        TicketAnalysisVO ticketAnalysisVO = new TicketAnalysisVO();
        // 统计票务分析左边的折线图
        List<TouristPassengerTicketDaily> rangeTicketList = selectDaysLastRecord(begin, end, scenicName);
        ticketAnalysisVO = analysisTicketLineChartBuckets(ticketAnalysisVO, rangeTicketList, MONTH_DAY_FORMATTER);

        // 票务分析-统计: 门票、索道、观光车
        LocalDate now = LocalDate.now();
        ticketStatistics(ticketAnalysisVO, scenicName, now);

        // 预测明日售票数
        Integer predicateTickets = tomorrowTicketPredicate(scenicName);
        ticketAnalysisVO.setPredicateTomorrowTickets(predicateTickets);
        return ticketAnalysisVO;
    }
    /**
     * 明日景区门票数预测
     * @param scenicName 景区名称
     * @return 预测数
     */
    private Integer tomorrowTicketPredicate(String scenicName) {
        List<ScenicTicketsPredication> list = new ArrayList<>();
        Map<ScenicTicketsPredicationEnum, Double> weightConfig = scenicTicketsPredicationConfig.getWeightConfig();
        if (weightConfig == null) {
            weightConfig = ScenicTicketsPredicationEnum.getDefaultWeights();
        }
        for (Map.Entry<ScenicTicketsPredicationEnum, Double> entry : weightConfig.entrySet()) {
            ScenicTicketsPredicationEnum key = entry.getKey();
            Double value = entry.getValue();
            // 根据ScenicTicketsPredicationEnum获取对应的开始/结束日期
            Map<String, String> timeRange = getTimeRange(key);
            String begin = timeRange.get("begin");
            String end = timeRange.get("end");
            int totalTickets = queryTicketsByScenicNameAndTimeRange(scenicName, begin, end);
            ScenicTicketsPredication predication = new ScenicTicketsPredication(key, totalTickets, value);
            list.add(predication);
        }
        // 过滤门票数为 0 的选项
        list = list.stream().filter(predication -> predication.getTickets() > 0).collect(Collectors.toList());
        // 计算剩余占比之和
        double weight = list.stream().mapToDouble(ScenicTicketsPredication::getWeight).sum();
        Double predicates = list.stream().mapToDouble(predication -> predication.getTickets() * (predication.getWeight() / weight)).sum();
        return predicates.intValue();
    }

    private Map<String, String> getTimeRange(ScenicTicketsPredicationEnum key) {
        LocalDate now = LocalDate.now();
        String begin = "", end = "";
        switch (key) {
            case YESTERDAY_TICKETS:
                String yesterday = now.plusDays(-1).format(DateUtil.LOCAL_DATE);
                begin = yesterday + " 00:00:00";
                end = yesterday + " 23:59:59";
                break;
            case LAST_WEEK_SAME_DAY_TICKETS:
                String lastWeekDay = now.plusWeeks(-1).format(DateUtil.LOCAL_DATE);
                begin = lastWeekDay + " 00:00:00";
                end = lastWeekDay + " 23:59:59";
                break;
            case LAST_TWO_WEEK_SAME_DAY_TICKETS:
                String lastTwoWeekDay = now.plusWeeks(-2).format(DateUtil.LOCAL_DATE);
                begin = lastTwoWeekDay + " 00:00:00";
                end = lastTwoWeekDay + " 23:59:59";
                break;
            case LAST_MONTH_SAME_DAY_TICKETS:
                String lastMonthSameDay = now.plusMonths(-1).format(DateUtil.LOCAL_DATE);
                begin = lastMonthSameDay + " 00:00:00";
                end = lastMonthSameDay + " 23:59:59";
                break;
            case LAST_WEEK_AVG_TICKETS:
                int dayOfWeek = now.getDayOfWeek().getValue();
                LocalDate lastWeek = now.plusWeeks(-1);
                begin = lastWeek.plusDays(-(dayOfWeek-1)).format(DateUtil.LOCAL_DATE) + " 00:00:00";
                end = lastWeek.plusDays(7 - dayOfWeek).format(DateUtil.LOCAL_DATE) + " 23:59:59";
                break;
            case LAST_MONTH_AVG_TICKETS:
                LocalDate lastMonthDay = now.plusMonths(-1);
                begin = LocalDate.of(lastMonthDay.getYear(), lastMonthDay.getMonth(), 1).format(DateUtil.LOCAL_DATE) + " 00:00:00";
                end = LocalDate.of(lastMonthDay.getYear(), lastMonthDay.getMonth(), lastMonthDay.lengthOfMonth()).format(DateUtil.LOCAL_DATE) + " 23:59:59";
                break;
            case CURRENT_MONTH_AVG_TICKETS:
                begin = LocalDate.of(now.getYear(), now.getMonth(), 1).format(DateUtil.LOCAL_DATE) + " 00:00:00";
                end = now.plusDays(-1).format(DateUtil.LOCAL_DATE) + " 23:59:59";
                break;
            default:
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("begin", begin);
        map.put("end", end);
        return map;
    }

    private Integer queryTicketsByScenicNameAndTimeRange(String scenicName, String begin, String end) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("scenicName", scenicName))
                .must(QueryBuilders.rangeQuery("responseTime").gte(begin).lte(end));

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket_daily").withTypes("doc")
                .withQuery(queryBuilder);
        List<TouristPassengerTicketDaily> touristPassengerTicketDailies = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristPassengerTicketDaily.class);
        Integer sum = touristPassengerTicketDailies.stream()
                .map(touristPassengerTicketDaily -> {
                    Integer onlineTicketCount = touristPassengerTicketDaily.getOnlineTicketCount() == null ? 0 : touristPassengerTicketDaily.getOnlineTicketCount();
                    Double ticketPercent = touristPassengerTicketDaily.getTicketPercent();
                    if (onlineTicketCount == 0 || ticketPercent == 0) {
                        return 0;
                    }
                    Double totalTickets = onlineTicketCount / ticketPercent * 100;
                    return totalTickets.intValue();
                }).mapToInt(Integer::intValue).sum();

        // 求平均数, 如果只有一天也没影响(比如求昨天, 上周同期, 上月同期等)

        LocalDateTime beginTime = DateUtil.convert(begin);
        LocalDateTime endTime = DateUtil.convert(end);
        int days = Period.between(beginTime.toLocalDate(), endTime.toLocalDate()).getDays();
        days = days < 0 ? -days + 1 : days + 1;
        return sum / days;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ScenicTicketsPredication {
        /** key **/
        private ScenicTicketsPredicationEnum key;
        /** 门票数 **/
        private Integer tickets;
        /** 预测时占比 **/
        private Double weight;
    }

    /**
     * ticketAnalysisByMonth 和 ticketAnalysis方法一样, 只不过多了按月分数统计
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return
     */
    @Override
    public TicketAnalysisVO ticketAnalysisByMonth(Long beginDate, Long endDate, String scenicName) {
        LocalDateTime begin = DateUtil.getFirstDayOfMonth(beginDate);
        LocalDateTime end = DateUtil.getLastDayOfMonth(endDate);

        log.info("票务分析按月统计查询:beginDate={}, endDate={}, scenicName={}", begin, end, scenicName);
        TicketAnalysisVO ticketAnalysisVO = new TicketAnalysisVO();

        // 统计票务分析左边的折线图
        List<TouristPassengerTicketDaily> rangeTicketList = selectDaysLastRecord(begin, end, scenicName);
        ticketAnalysisVO = analysisTicketLineChartBuckets(ticketAnalysisVO, rangeTicketList, YEAR_MONTH_FORMATTER);

        //
        List<TicketAnalysisVO.LineChartVO> cablewayLineChart = ticketAnalysisVO.getCablewayLineChart();
        List<TicketAnalysisVO.LineChartVO> sightseeingCarLineChart = ticketAnalysisVO.getSightseeingCarLineChart();
        List<TicketAnalysisVO.LineChartVO> ticketLineChart = ticketAnalysisVO.getTicketLineChart();

        Map<String, Integer> cablewayLineChartMap = cablewayLineChart.stream().collect(Collectors.groupingBy(TicketAnalysisVO.LineChartVO::getDay, Collectors.summingInt(TicketAnalysisVO.LineChartVO::getTickets)));
        Map<String, Integer> sightseeingCarLineChartMap = sightseeingCarLineChart.stream().collect(Collectors.groupingBy(TicketAnalysisVO.LineChartVO::getDay, Collectors.summingInt(TicketAnalysisVO.LineChartVO::getTickets)));
        Map<String, Integer> ticketLineChartMap = ticketLineChart.stream().collect(Collectors.groupingBy(TicketAnalysisVO.LineChartVO::getDay, Collectors.summingInt(TicketAnalysisVO.LineChartVO::getTickets)));
        ticketAnalysisVO.setCablewayLineChart(mapToTicketAnalysisVOLineChartVOList(cablewayLineChartMap));
        ticketAnalysisVO.setSightseeingCarLineChart(mapToTicketAnalysisVOLineChartVOList(sightseeingCarLineChartMap));
        ticketAnalysisVO.setTicketLineChart(mapToTicketAnalysisVOLineChartVOList(ticketLineChartMap));


        // 票务分析-统计: 门票、索道、观光车
        LocalDate now = LocalDate.now();
        ticketStatistics(ticketAnalysisVO, scenicName, now);

        // 预测明日售票数
        Integer predicateTickets = tomorrowTicketPredicate(scenicName);
        ticketAnalysisVO.setPredicateTomorrowTickets(predicateTickets);
        return ticketAnalysisVO;
    }

    private List<TicketAnalysisVO.LineChartVO> mapToTicketAnalysisVOLineChartVOList(Map<String, Integer> map) {
        List<TicketAnalysisVO.LineChartVO> list = new ArrayList<>();
        map.forEach((key, value) -> {
            TicketAnalysisVO.LineChartVO lineChartVO = new TicketAnalysisVO.LineChartVO();
            lineChartVO.setDay(key);
            lineChartVO.setTickets(value);
            list.add(lineChartVO);
        });
        return list;
    }

    /**
     * 客流分析
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    @Override
    @SuppressWarnings("Duplicates")
    public TouristAnalysisVO touristAnalysis(Long beginDate, Long endDate, String scenicName) {
        LocalDateTime begin = DateUtil.longToLocalDateTime(beginDate);
        begin = LocalDateTime.of(begin.toLocalDate(), DateUtil.getBeginLocalTime());
        LocalDateTime end = DateUtil.longToLocalDateTime(endDate);
        end = LocalDateTime.of(end.toLocalDate(), DateUtil.getEndLocalTime());
        log.info("客流分析查询:beginDate={}, endDate={}, scenicName={}", begin, end, scenicName);

        TouristAnalysisVO touristAnalysisVO = new TouristAnalysisVO();

        // 统计客流分析左边的折线图
        List<TouristPassengerTicketDaily> rangeTicketList = selectDaysLastRecord(begin, end, scenicName);
        touristAnalysisVO = analysisTouristLineChartBuckets(touristAnalysisVO, rangeTicketList);

        // 游客数与去年同期对比
        compareLastYear(touristAnalysisVO, scenicName);

        return touristAnalysisVO;
    }


    @Override
    public TouristAnalysisVO touristAnalysisByMonth(Long beginDate, Long endDate, String scenicName) {
        LocalDateTime begin = DateUtil.getFirstDayOfMonth(beginDate);
        LocalDateTime end = DateUtil.getLastDayOfMonth(endDate);
        log.info("客流分析按月统计查询:beginDate={}, endDate={}, scenicName={}", begin, end, scenicName);

        TouristAnalysisVO touristAnalysisVO = new TouristAnalysisVO();

        // 统计客流分析左边的折线图
        analysisTouristLineChartByMonth(touristAnalysisVO, begin, end, scenicName);


        // 游客数与去年同期对比
        compareLastYear(touristAnalysisVO, scenicName);

        return touristAnalysisVO;
    }


    /**
     * 同比去年同期统计
     *
     * @param touristAnalysisVO resultVO
     * @param scenicName        景区名称
     */
    @SuppressWarnings("Duplicates")
    private void compareLastYear(TouristAnalysisVO touristAnalysisVO, String scenicName) {

        LocalDate now = LocalDate.now();
        Integer currentTimeTourist = 0;
        TouristAnalysisVO.TouristCompareVO touristCompareVO = new TouristAnalysisVO.TouristCompareVO();
        // 同比去年今日累计
        LocalDateTime begin = LocalDateTime.of(now, DateUtil.getBeginLocalTime());
        LocalDateTime end = LocalDateTime.of(now, DateUtil.getEndLocalTime());
        TouristAnalysisVO.TouristCompareCommonVO todayCompareVO = compareLastYearByTimeRange(scenicName, begin, end);

        // 同比去年当月累计
        int year = now.getYear();
        int month = now.getMonthValue();
        begin = LocalDateTime.of(LocalDate.of(year, month, 1), DateUtil.getBeginLocalTime());
        TouristAnalysisVO.TouristCompareCommonVO monthCompareVO = compareLastYearByTimeRange(scenicName, begin, end);

        // 同比去年累计
        begin = LocalDateTime.of(LocalDate.of(year, 1, 1), DateUtil.getBeginLocalTime());
        TouristAnalysisVO.TouristCompareCommonVO yearCompareVO = compareLastYearByTimeRange(scenicName, begin, end);

        // 查询今日实时游客数
        Long nowLong = System.currentTimeMillis();
        LocalDateTime nowLocalDateTime = DateUtil.longToLocalDateTime(nowLong);
        LocalDateTime dayBeginLocalDate = LocalDateTime.of(nowLocalDateTime.toLocalDate(), DateUtil.getBeginLocalTime());
        NativeSearchQueryBuilder commonNativeSearchQueryBuilder = getCommonNativeSearchQueryBuilder(dayBeginLocalDate, nowLocalDateTime, scenicName);
        List<TouristPassengerTicketDaily> list = elasticsearchTemplate.queryForList(commonNativeSearchQueryBuilder.build(), TouristPassengerTicketDaily.class);
        if (CollectionUtils.isNotEmpty(list)) {
            currentTimeTourist = list.get(0).getRealTimeTouristNum();
            currentTimeTourist = currentTimeTourist == null ? 0 : currentTimeTourist;
        }
        // 计算舒适度
        String comfortableStatus = calculateComfortableStatus(scenicName, currentTimeTourist);

        touristCompareVO.setComfortableStatus(comfortableStatus);
        touristCompareVO.setCurrentTimeTourists(currentTimeTourist);
        touristCompareVO.setToday(todayCompareVO);
        touristCompareVO.setCurrentMonth(monthCompareVO);
        touristCompareVO.setCurrentYear(yearCompareVO);
        touristAnalysisVO.setTouristCompareVO(touristCompareVO);

    }

    private String calculateComfortableStatus(String scenicName, Integer peopleNum) {
        String scenicCode = ScenicDictionaryEnum.getCodeByName(scenicName);
        return iHotMapFixService.getScenicStatus(scenicCode, peopleNum).getDesc();
    }

    /**
     * 根据时间范围同比去年
     *
     * @param scenicName 景区名称
     * @param begin      开始时间
     * @param end        结束时间
     * @return TouristAnalysisVO.TouristCompareCommonVO
     */
    @SuppressWarnings("Duplicates")
    private TouristAnalysisVO.TouristCompareCommonVO compareLastYearByTimeRange(String scenicName, LocalDateTime begin, LocalDateTime end) {
        List<TouristPassengerTicketDaily> rangeTicketList = selectDaysLastRecord(begin, end, scenicName);
        TouristAnalysisVO.TouristCompareCommonVO commonVO = new TouristAnalysisVO.TouristCompareCommonVO();
        int currentTotalTourists = 0;
        int lastYearTotalTourists = 0;
        for (TouristPassengerTicketDaily ticket : rangeTicketList) {
            currentTotalTourists += (ticket.getTodayTouristCount() == null ? 0 : ticket.getTodayTouristCount());
            lastYearTotalTourists += (ticket.getLastTodayTouristCount() == null ? 0 : ticket.getLastTodayTouristCount());
        }
        int extendType;
        double differ, compareLast;

        if (lastYearTotalTourists == 0) {
            commonVO.setCurrentTotalTourists(currentTotalTourists);
            commonVO.setLastYearTotalTourists(lastYearTotalTourists);
            commonVO.setCompareLastExtendType(1);
            commonVO.setCompareLastCount(100D);
            return commonVO;
        }
        differ = currentTotalTourists - lastYearTotalTourists;
        extendType = differ == 0 ? 0 : (differ > 0 ? 1 : -1);
        compareLast = Math.abs(differ) / lastYearTotalTourists;
        compareLast *= 100;
        compareLast = Double.parseDouble(String.format("%.1f", compareLast));

        commonVO.setCurrentTotalTourists(currentTotalTourists);
        commonVO.setLastYearTotalTourists(lastYearTotalTourists);
        commonVO.setCompareLastExtendType(extendType);
        commonVO.setCompareLastCount(compareLast);
        return commonVO;

    }

    /**
     * 按月解析游客分析折线图
     *
     * @param touristAnalysisVO touristAnalysisVO
     * @return TouristAnalysisVO
     */
    private TouristAnalysisVO analysisTouristLineChartByMonth(TouristAnalysisVO touristAnalysisVO, LocalDateTime begin, LocalDateTime end, String scenicName) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = sumTodayTouristCountByMonth(begin, end, scenicName);
        List<TouristAnalysisVO.LineChartVO> list = new ArrayList<>();


        elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), response -> {
            Histogram histogram = response.getAggregations().get(RESPONSE_TIME_AGG);
            histogram.getBuckets().forEach(bucket -> {
                TouristAnalysisVO.LineChartVO lineChartVO = new TouristAnalysisVO.LineChartVO();
                lineChartVO.setDay(bucket.getKeyAsString());
                Sum sum = bucket.getAggregations().get(SUM_TODAY_TOURIST_COUNT_BY_MONTH);
                int monthTodayTourist = Double.valueOf(sum.getValue() + "").intValue();
                lineChartVO.setTourists(monthTodayTourist);
                list.add(lineChartVO);
            });
            return list;
        });
        int dateRangeTotalTourist = list.stream().collect(Collectors.summingInt(TouristAnalysisVO.LineChartVO::getTourists));
        touristAnalysisVO.setDateRangeTotalTourist(dateRangeTotalTourist);
        touristAnalysisVO.setTouristsLineChartVO(list);
        return touristAnalysisVO;
    }

    @SuppressWarnings("Duplicates")
    private TouristAnalysisVO analysisTouristLineChartBuckets(TouristAnalysisVO touristAnalysisVO, List<TouristPassengerTicketDaily> rangeTicketList) {

        int totalTourists = 0;
        List<TouristAnalysisVO.LineChartVO> ticketLineChart = new ArrayList<>();

        for (TouristPassengerTicketDaily ticket : rangeTicketList) {

            String date = MONTH_DAY_FORMATTER.format(DateUtil.convert(ticket.getResponseTime()));

            TouristAnalysisVO.LineChartVO lineTicket = new TouristAnalysisVO.LineChartVO();

            // 如果某日游客总数为 null || 0
            Integer todayTouristCount = ticket.getTodayTouristCount();
            if (todayTouristCount == null || todayTouristCount == 0) {
                lineTicket.setDay(date);
                lineTicket.setTourists(0);
            } else {
                lineTicket.setDay(date);
                lineTicket.setTourists(todayTouristCount);
                totalTourists += todayTouristCount;
            }
            ticketLineChart.add(lineTicket);
        }

        touristAnalysisVO.setDateRangeTotalTourist(totalTourists);
        touristAnalysisVO.setTouristsLineChartVO(ticketLineChart);
        return touristAnalysisVO;
    }


    /**
     * 票务分析-统计
     *
     * @param scenicName 景区名称
     * @param now        LocalDate.now
     * @return TicketAnalysisVO
     */
    @SuppressWarnings("Duplicates")
    private void ticketStatistics(TicketAnalysisVO ticketAnalysisVO, String scenicName, LocalDate now) {


        TicketAnalysisVO.TicketVO ticketVO = new TicketAnalysisVO.TicketVO();
        TicketAnalysisVO.CablewayVO cablewayVO = new TicketAnalysisVO.CablewayVO();
        TicketAnalysisVO.SightseeingCarVO sightseeingCarVO = new TicketAnalysisVO.SightseeingCarVO();

        // 1.今日累计
        List<TouristPassengerTicketDaily> ticketCurrentDay = selectCurrentDaysTickets(scenicName, now);
        TicketAnalysisVO.StatisticsVO currentDayStatistics = calculateStatistics(ticketCurrentDay);

        ticketVO.setCurrentDay(currentDayStatistics.getTicketCommonVO());
        cablewayVO.setDayTickets(currentDayStatistics.getCablewayTickets());
        sightseeingCarVO.setDayTickets(currentDayStatistics.getSightseeingTickets());

        // 2.当月累计
        List<TouristPassengerTicketDaily> ticketCurrentMonth = selectCurrentMonthTickets(scenicName, now);
        TicketAnalysisVO.StatisticsVO currentMonthStatistics = calculateStatistics(ticketCurrentMonth);

        ticketVO.setCurrentMonth(currentMonthStatistics.getTicketCommonVO());
        cablewayVO.setMonthTickets(currentMonthStatistics.getCablewayTickets());
        sightseeingCarVO.setMonthTickets(currentMonthStatistics.getSightseeingTickets());

        // 3.当年累计
        List<TouristPassengerTicketDaily> ticketCurrentYear = selectCurrentYearTickets(scenicName, now);
        TicketAnalysisVO.StatisticsVO currentYearStatistics = calculateStatistics(ticketCurrentYear);

        ticketVO.setCurrentYear(currentYearStatistics.getTicketCommonVO());
        cablewayVO.setYearTickets(currentYearStatistics.getCablewayTickets());
        sightseeingCarVO.setYearTickets(currentYearStatistics.getSightseeingTickets());

        ticketAnalysisVO.setTicketVO(ticketVO);
        ticketAnalysisVO.setCablewayVO(cablewayVO);
        ticketAnalysisVO.setSightseeingCarVO(sightseeingCarVO);
    }


    /**
     * 计算当天的、当月的、当年的统计
     *
     * @param ticketsInfo 门票列表  当天的、当月的、当年的
     * @return StatisticsVO
     */
    @SuppressWarnings("Duplicates")
    private TicketAnalysisVO.StatisticsVO calculateStatistics(List<TouristPassengerTicketDaily> ticketsInfo) {

        TicketAnalysisVO.StatisticsVO statisticsVO = new TicketAnalysisVO.StatisticsVO();
        TicketAnalysisVO.TicketCommonVO ticketCommonVO = new TicketAnalysisVO.TicketCommonVO();

        ticketsInfo.forEach(ticket -> {
            int onlineTickets = ticket.getOnlineTicketCount() == null ? 0 : ticket.getOnlineTicketCount();
            int offLineTickets = ticket.getOfflineTicketCount() == null ? 0 : ticket.getOfflineTicketCount();

            ticketCommonVO.setOnlineTickets(ticketCommonVO.getOnlineTickets() + onlineTickets);
            ticketCommonVO.setOfflineTickets(ticketCommonVO.getOfflineTickets() + offLineTickets);

            double ticketPercent = ticket.getTicketPercent() == null ? 0 : ticket.getTicketPercent();
            double cablewayPercent = ticket.getCablewayPercent() == null ? 0 : ticket.getCablewayPercent();
            double sightseeingCarPercent = ticket.getSightseeingCarPercent() == null ? 0 : ticket.getSightseeingCarPercent();

            Double onlineTotal = onlineTickets / ticketPercent * 100;
            int cablewayTickets = (int) (onlineTotal * cablewayPercent / 100);
            int sightseeingCarTickets = (int) (onlineTotal * sightseeingCarPercent / 100);

            statisticsVO.setCablewayTickets(cablewayTickets + statisticsVO.getCablewayTickets());
            statisticsVO.setSightseeingTickets(sightseeingCarTickets + statisticsVO.getSightseeingTickets());

        });
        ticketCommonVO.setTotal(ticketCommonVO.getOnlineTickets() + ticketCommonVO.getOfflineTickets());
        statisticsVO.setTicketCommonVO(ticketCommonVO);
        return statisticsVO;
    }


    /**
     * 解析指定范围内的 观光车门票数、索道门票数、及总门票数
     *
     * @param ticketAnalysisVO result
     * @param rangeTicketList  查询结果buckets
     * @return ticketAnalysisVO
     */
    private TicketAnalysisVO analysisTicketLineChartBuckets(TicketAnalysisVO ticketAnalysisVO, List<TouristPassengerTicketDaily> rangeTicketList, DateTimeFormatter dateTimeFormatter) {

        int totalTickets = 0;
        int totalSightseeingCarTickets = 0;
        int totalCablewayTickets = 0;

        // 门票折线图
        List<TicketAnalysisVO.LineChartVO> ticketLineChart = new ArrayList<>();
        // 观光车折线图
        List<TicketAnalysisVO.LineChartVO> sightseeingCarLineChart = new ArrayList<>();
        // 索道折线图
        List<TicketAnalysisVO.LineChartVO> cablewayLineChart = new ArrayList<>();
        for (TouristPassengerTicketDaily ticket : rangeTicketList) {

            String date = dateTimeFormatter.format(DateUtil.convert(ticket.getResponseTime()));

            TicketAnalysisVO.LineChartVO lineTicket = new TicketAnalysisVO.LineChartVO();
            TicketAnalysisVO.LineChartVO lineSightseeingCar = new TicketAnalysisVO.LineChartVO();
            TicketAnalysisVO.LineChartVO lineCableway = new TicketAnalysisVO.LineChartVO();

            // 如果线上门票占比为 null || 0
            Double ticketPercent;
            if ((ticketPercent = ticket.getTicketPercent()) == null || ticketPercent == 0) {
                lineTicket.setDay(date);
                lineTicket.setTickets(0);

                lineSightseeingCar.setDay(date);
                lineSightseeingCar.setTickets(0);

                lineCableway.setDay(date);
                lineCableway.setTickets(0);

            } else {
                // 计算线上索道票数 = 取整【 线上门票总数 / 线上门票数占比 * 线上索道票数占比 】
                int onlineTicketCount = ticket.getOnlineTicketCount() == null ? 0 : ticket.getOnlineTicketCount();
                double cablewayPercent = ticket.getCablewayPercent() == null ? 0 : ticket.getCablewayPercent();
                double sightseeingCarPercent = ticket.getSightseeingCarPercent() == null ? 0 : ticket.getSightseeingCarPercent();

                // 当日总门票数 ticketPercent 是没有百分号的 ，所以 * 100
                Double currentTotalTickets = onlineTicketCount / ticketPercent * 100;

                // 当日线上索道票数
                int currentCablewayTickets = (int) (currentTotalTickets * cablewayPercent / 100);

                // 当日线上观光车票数
                int currentSightseeingCar = (int) (currentTotalTickets * sightseeingCarPercent / 100);


                lineTicket.setDay(date);
                lineTicket.setTickets(onlineTicketCount + ticket.getOfflineTicketCount());

                lineSightseeingCar.setDay(date);
                lineSightseeingCar.setTickets(currentSightseeingCar);

                lineCableway.setDay(date);
                lineCableway.setTickets(currentCablewayTickets);

                // 合计
                totalTickets += lineTicket.getTickets();
                totalSightseeingCarTickets += lineSightseeingCar.getTickets();
                totalCablewayTickets += lineCableway.getTickets();

            }
            ticketLineChart.add(lineTicket);
            sightseeingCarLineChart.add(lineSightseeingCar);
            cablewayLineChart.add(lineCableway);
        }
        // 合计统计完毕
        ticketAnalysisVO.setTotalTickets(totalTickets);
        ticketAnalysisVO.setCablewayTickets(totalCablewayTickets);
        ticketAnalysisVO.setSightseeingCarTickets(totalSightseeingCarTickets);

        // 折线图统计完毕
        ticketAnalysisVO.setSightseeingCarLineChart(sightseeingCarLineChart);
        ticketAnalysisVO.setCablewayLineChart(cablewayLineChart);
        ticketAnalysisVO.setTicketLineChart(ticketLineChart);

        return ticketAnalysisVO;
    }

    /**
     * 票务分析-统计-今日累计
     *
     * @param scenicName 景区名称
     * @param now        当前时间
     * @return List
     */
    private List<TouristPassengerTicketDaily> selectCurrentDaysTickets(String scenicName, LocalDate now) {
        LocalDateTime begin = LocalDateTime.of(now, DateUtil.getBeginLocalTime());
        LocalDateTime end = LocalDateTime.of(now, DateUtil.getEndLocalTime());
        return selectDaysLastRecord(begin, end, scenicName);
    }

    /**
     * 票务分析-统计-当月累计
     *
     * @param scenicName 景区名称
     * @param now        当前时间
     * @return List
     */
    @SuppressWarnings("Duplicates")
    private List<TouristPassengerTicketDaily> selectCurrentMonthTickets(String scenicName, LocalDate now) {
        int year = now.getYear();
        int month = now.getMonthValue();
        LocalDateTime begin = LocalDateTime.of(LocalDate.of(year, month, 1), DateUtil.getBeginLocalTime());
        LocalDateTime end = LocalDateTime.of(now, DateUtil.getEndLocalTime());
        return selectDaysLastRecord(begin, end, scenicName);
    }

    /**
     * 票务分析-统计-当年累计
     *
     * @param scenicName 景区名称
     * @param now        当前时间
     * @return List
     */
    @SuppressWarnings("Duplicates")
    private List<TouristPassengerTicketDaily> selectCurrentYearTickets(String scenicName, LocalDate now) {
        int year = now.getYear();
        LocalDateTime begin = LocalDateTime.of(LocalDate.of(year, 1, 1), DateUtil.getBeginLocalTime());
        LocalDateTime end = LocalDateTime.of(now, DateUtil.getEndLocalTime());
        return selectDaysLastRecord(begin, end, scenicName);
    }


    /**
     * 票务分析 通用查询
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param scenicName 景区名称
     * @return List<TouristPassengerTicket>
     */
    @SuppressWarnings("Duplicates")
    private List<TouristPassengerTicketDaily> selectDaysLastRecord(LocalDateTime begin, LocalDateTime end, String scenicName) {
        NativeSearchQueryBuilder searchQueryBuilder = queryByDateRangeAndScenicName(begin, end, scenicName);
        // 查询
        return elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristPassengerTicketDaily.class);
    }

    private NativeSearchQueryBuilder queryByDateRangeAndScenicName(LocalDateTime begin, LocalDateTime end, String scenicName) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 时间范围
        String startDateString = DateUtil.format(begin);
        String endDateString = DateUtil.format(end);
        queryBuilder.must(QueryBuilders.rangeQuery("responseTime").gte(startDateString).lte(endDateString));
        // 查询指定景区名
        queryBuilder.must(QueryBuilders.termQuery("scenicName", scenicName));
        // 索引及类型
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket_daily").withTypes("doc")
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 1000))
                .withSort(SortBuilders.fieldSort("responseTime").order(SortOrder.ASC));
        return searchQueryBuilder;
    }


    /**
     * 通用查询
     *
     * @param begin      begin
     * @param end        end
     * @param scenicName scenicName
     * @return NativeSearchQueryBuilder
     */
    private NativeSearchQueryBuilder getCommonNativeSearchQueryBuilder(LocalDateTime begin, LocalDateTime end, String scenicName) {
        BoolQueryBuilder queryBuilder = getCommonQueryBuilder(begin, end);
        queryBuilder.must(QueryBuilders.termQuery("scenicName", scenicName));
        return getBaseNativeSearchQueryBuilder(queryBuilder);
    }

    /**
     * 按月统计 todayTouristCountByMonth 之和
     *
     * @param begin      begin
     * @param end        end
     * @param scenicName scenicName
     * @return NativeSearchQueryBuilder
     */
    private NativeSearchQueryBuilder sumTodayTouristCountByMonth(LocalDateTime begin, LocalDateTime end, String scenicName) {
        NativeSearchQueryBuilder commonNativeSearchQueryBuilder = getCommonNativeSearchQueryBuilder(begin, end, scenicName);
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram(RESPONSE_TIME_AGG)
                .field("responseTime")
                .dateHistogramInterval(DateHistogramInterval.MONTH)
                .format("yyyy-MM");
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(SUM_TODAY_TOURIST_COUNT_BY_MONTH)
                .field("todayTouristCount");
        dateHistogramAggregationBuilder.subAggregation(sumAggregationBuilder);
        commonNativeSearchQueryBuilder.addAggregation(dateHistogramAggregationBuilder);
        return commonNativeSearchQueryBuilder;
    }

    @SuppressWarnings("Duplicates")
    private BoolQueryBuilder getCommonQueryBuilder(LocalDateTime begin, LocalDateTime end) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        String startDateString = DateUtil.format(begin);
        String endDateString = DateUtil.format(end);
        queryBuilder.must(QueryBuilders.rangeQuery("responseTime").gte(startDateString).lte(endDateString));
        return queryBuilder;
    }


    private NativeSearchQueryBuilder getBaseNativeSearchQueryBuilder(QueryBuilder queryBuilder) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket_daily").withTypes("doc")
                .withQuery(queryBuilder);
        return searchQueryBuilder;
    }


}
