package net.cdsunrise.hy.lyyt.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyyt.entity.vo.ScenicVO;
import net.cdsunrise.hy.lyyt.entity.vo.TouristCountVO;
import net.cdsunrise.hy.lyyt.es.entity.TouristPassengerTicket;
import net.cdsunrise.hy.lyyt.service.IScenicService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import net.cdsunrise.hy.lyyt.utils.ResultUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fang yun long
 * @date 2020-01-16 14:32
 */
@Slf4j
@Service
public class ScenicServiceImpl implements IScenicService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ScenicServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * 定义 本周 和 上周 变量 LAST_WEEK  CURRENT_DAY
     **/
    private static final String CURRENT_DAY_SUB_1 = "CURRENT_DAY_SUB_1";
    private static final String CURRENT_DAY_SUB_8 = "CURRENT_DAY_SUB_8";
    private static final String CURRENT_DAY = "CURRENT_DAY";

    /**
     * 景区统计本周和上周的销售票数
     *
     * @return Result
     */
    @Override
    public Result statisticScenicTickets() {
        // 计算上周开始时间 、本周开始时间、当前时间
        LocalDateTime lastWeekStart = DateUtil.weekStart(-1).atStartOfDay();
        LocalDateTime currentWeekStart = DateUtil.weekStart(0).atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();

        log.info("统计景区本周和上周的销售票数, lastWeekStart:{}, currentWeekStart:{}, currentTime:{}", lastWeekStart, currentWeekStart, currentTime);

        // 按景区名称分组
        TermsAggregationBuilder scenicNameTermsBuilder = AggregationBuilders.terms("scenicName_terms").field("scenicName");
        NativeSearchQueryBuilder searchQueryBuilder = getSearchQueryBuilder(QueryBuilders.matchAllQuery());
        searchQueryBuilder.addAggregation(scenicNameTermsBuilder);

        // 将查询结果集计算后记录到Map中
        Map<String, ScenicVO.Statistics> statisticsMap = new HashMap<>(2);
        int currentDayOfWeek = currentTime.getDayOfWeek().getValue();
        // 统计景区 本周门票数
        calculateTickets(1, LocalDate.now().atStartOfDay(), CURRENT_DAY, searchQueryBuilder, statisticsMap);
        // 判断是否是星期一  是周一 则统计上周和上上周数据， 不是周一则统计 currentDayOfWeek - 1天的数据
        if (currentDayOfWeek == 1) {
            // 统计景区 t - 1 门票数
            calculateTickets(7, lastWeekStart, CURRENT_DAY_SUB_1, searchQueryBuilder, statisticsMap);
            // 统计景区 t -8 门票数
            calculateTickets(7, DateUtil.weekStart(-2).atStartOfDay(), CURRENT_DAY_SUB_8, searchQueryBuilder, statisticsMap);
        } else {
            // 统计景区 t - 1 门票数
            calculateTickets(currentDayOfWeek - 1, currentWeekStart, CURRENT_DAY_SUB_1, searchQueryBuilder, statisticsMap);
            // 统计景区 t -8 门票数
            calculateTickets(currentDayOfWeek - 1, lastWeekStart, CURRENT_DAY_SUB_8, searchQueryBuilder, statisticsMap);
        }
        return ResultUtil.buildSuccessResultWithData(statisticsMap.values());

    }

    @PostConstruct
    public void test() {
        statisticCurrentAndLastMonthPassenger();
    }

    @Override
    public Result<TouristCountVO> statisticCurrentAndLastMonthPassenger() {
        log.info("statisticCurrentAndLastMonthPassenger ... ");
        LocalDate currentTime = LocalDate.now();
        LocalDate currentBegin = LocalDate.of(currentTime.getYear(), currentTime.getMonth(), 1);
        LocalDate lastMonth = currentTime.plusMonths(-1);
        LocalDate lastMonthBegin = LocalDate.of(lastMonth.getYear(), lastMonth.getMonth(), 1);
        LocalDate lastMonthEnd = LocalDate.of(lastMonth.getYear(), lastMonth.getMonth(), lastMonth.lengthOfMonth());
        // 展示洪雅县 本月 上月的周边游客数
        String scenicName = "洪雅县";
        // 按天统计
        String flag = "day";
        // 本月
        Double currentMonthPeople = calculateTicketsInTimeRange(scenicName, flag, DateUtil.localDateToString(currentBegin), DateUtil.localDateToString(currentTime));
        // 上月
        Double lastMonthPeople = calculateTicketsInTimeRange(scenicName, flag, DateUtil.localDateToString(lastMonthBegin), DateUtil.localDateToString(lastMonthEnd));
        // 转换为万人
        TouristCountVO touristCountVO = new TouristCountVO();
        touristCountVO.setCurrentMonthTourists(convertToBigDecimal(currentMonthPeople));
        touristCountVO.setLastMonthTourists(convertToBigDecimal(lastMonthPeople));
        return ResultUtil.buildSuccessResultWithData(touristCountVO);
    }

    private BigDecimal convertToBigDecimal(Double doubleValue) {
        return new BigDecimal(doubleValue / 10000).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private Double calculateTicketsInTimeRange(String scenicName, String flag, String begin, String end) {
        String sumAggName = "sum_people";
        String indexName = "tourist_local_data";
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("scenicName", scenicName))
                .must(QueryBuilders.termQuery("flag", flag))
                .must(QueryBuilders.rangeQuery("time").gte(begin).lte(end));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum(sumAggName).field("peopleNum");
        NativeSearchQueryBuilder searchQueryBuilder = getSearchQueryBuilder(indexName, queryBuilder, sumAggregationBuilder);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            InternalSum internalSum = response.getAggregations().get(sumAggName);
            return internalSum.getValue();
        });
    }

    /**
     * 按天查询最后一次上报  得到当天的线上票数和线下票数
     *
     * @param totalDays           如果当前是星期一 则统计上周的完整数据 ， 如果当前是 星期二 则统计本周一的数据，   以此类推
     * @param mondayLocalDateTime 周一的日期、 yyyy-MM-dd 00:00:00
     * @param weekType            CURRENT_DAY_SUB_1 CURRENT_DAY_SUB_8  CURRENT_DAY
     * @param searchQueryBuilder  queryBuilder
     * @param statisticsMap       resultMap
     */
    private void calculateTickets(int totalDays, LocalDateTime mondayLocalDateTime, String weekType, NativeSearchQueryBuilder searchQueryBuilder, Map<String, ScenicVO.Statistics> statisticsMap) {
        LocalDateTime startDate = mondayLocalDateTime;
        LocalDateTime endDate = startDate.plusDays(1);
        // 如果当前是星期一 则统计上周的完整数据 ， 如果当前是 星期二 则统计本周一的数据，   以此类推
        log.info("weekType:{}", weekType);
        for (int i = 0; i < totalDays; i++) {
            String startDateString = DateUtil.localDateTimeToString(startDate);
            String endDateString = DateUtil.localDateTimeToString(endDate);
            // 按天查询最后一次上报  得到当天的线上票数和线下票数
            BoolQueryBuilder dayQueryBuilder = QueryBuilders.boolQuery();
            dayQueryBuilder.must(QueryBuilders.rangeQuery("responseTime").gte(startDateString).lt(endDateString));
            searchQueryBuilder.withQuery(dayQueryBuilder)
                    .withPageable(PageRequest.of(0, 1))
                    .withSort(SortBuilders.fieldSort("responseTime").order(SortOrder.DESC));
            List<TouristPassengerTicket> list = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristPassengerTicket.class);
            for (TouristPassengerTicket ticket : list) {
                ScenicVO.Statistics statistics = statisticsMap.get(ticket.getScenicName());
                statistics = statistics == null ? new ScenicVO.Statistics(ticket.getScenicName()) : statistics;
                int onlineTicketCount = ticket.getOnlineTicketCount() == null ? 0 : ticket.getOnlineTicketCount();
                int offlineTicketCount = ticket.getOfflineTicketCount() == null ? 0 : ticket.getOfflineTicketCount();
                if (CURRENT_DAY_SUB_1.equals(weekType)) {

                    int dayTotalTickets = statistics.getCurrentSub1Day() + onlineTicketCount + offlineTicketCount;
                    statistics.setCurrentSub1Day(dayTotalTickets);
                } else if (CURRENT_DAY_SUB_8.equals(weekType)) {
                    int dayTotalTickets = statistics.getCurrentSub8Day() + onlineTicketCount + offlineTicketCount;
                    statistics.setCurrentSub8Day(dayTotalTickets);
                } else if (CURRENT_DAY.equals(weekType)) {
                    log.info("onlineTicketCount + offlineTicketCount:{}", onlineTicketCount + offlineTicketCount);
                    int dayTotalTickets = statistics.getCurrentWeekTickets() + onlineTicketCount + offlineTicketCount;
                    statistics.setCurrentWeekTickets(dayTotalTickets);
                }
                statisticsMap.put(ticket.getScenicName(), statistics);
            }
            startDate = endDate;
            endDate = endDate.plusDays(1);
        }

    }

    private NativeSearchQueryBuilder getSearchQueryBuilder(QueryBuilder queryBuilder) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_passenger_ticket").withTypes("doc").withQuery(queryBuilder);
        return searchQueryBuilder;
    }

    private NativeSearchQueryBuilder getSearchQueryBuilder(String indexName, QueryBuilder queryBuilder) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices(indexName).withTypes("doc").withQuery(queryBuilder);
        return searchQueryBuilder;
    }

    private NativeSearchQueryBuilder getSearchQueryBuilder(String indexName, QueryBuilder queryBuilder, SumAggregationBuilder aggregationBuilder) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices(indexName)
                .withTypes("doc")
                .withQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        return searchQueryBuilder;
    }

}
