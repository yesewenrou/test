package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author funnylog
 */
@Data
public class TicketAnalysisVO {

    /** 门票合计 **/
    private Integer totalTickets;
    /** 观光车合计 **/
    private Integer sightseeingCarTickets;
    /** 索道合计 **/
    private Integer cablewayTickets;


    /** 门票折线图 **/
    private List<LineChartVO> ticketLineChart;
    /** 观光车折线图 **/
    private List<LineChartVO> sightseeingCarLineChart;
    /** 索道折线图 **/
    private List<LineChartVO> cablewayLineChart;


    /** 门票统计 **/
    private TicketVO ticketVO;

    /** 索道统计 **/
    private CablewayVO cablewayVO;

    /** 观光车统计 **/
    private SightseeingCarVO sightseeingCarVO;

    /** 明日门票数预测 **/
    private Integer predicateTomorrowTickets;

    /**
     * 门票统计
     */
    @Data
    public static class TicketVO {
        /** 今日累计 **/
        private TicketCommonVO currentDay;
        /** 当月累计 **/
        private TicketCommonVO currentMonth;
        /** 当年累计 **/
        private TicketCommonVO currentYear;
    }

    /**
     * 门票通用
     */
    @Data
    public static class TicketCommonVO {
        /** 线上累计 **/
        private Integer onlineTickets;
        /** 线下累计 **/
        private Integer offlineTickets;
        /** 合计 **/
        private Integer total;

        public TicketCommonVO() {
            this.onlineTickets = 0;
            this.offlineTickets = 0;
            this.total = 0;
        }
    }

    @Data
    public static class LineChartVO {
        /** 日期 **/
        private String day;
        /** 票数 **/
        private Integer tickets;
    }

    @Data
    public static class CablewayVO {
        /** 今日累计 **/
        protected Integer dayTickets;
        /** 当月累计 **/
        protected Integer monthTickets;
        /** 当年累计 **/
        protected Integer yearTickets;

        public CablewayVO() {
            this.dayTickets = 0;
            this.monthTickets = 0;
            this.yearTickets = 0;
        }
    }

    @Data
    public static class SightseeingCarVO {
        /** 今日累计 **/
        protected Integer dayTickets;
        /** 当月累计 **/
        protected Integer monthTickets;
        /** 当年累计 **/
        protected Integer yearTickets;

        public SightseeingCarVO() {
            this.dayTickets = 0;
            this.monthTickets = 0;
            this.yearTickets = 0;
        }
    }

    @Data
    public static class StatisticsVO {
        private TicketCommonVO ticketCommonVO;
        private Integer cablewayTickets;
        private Integer sightseeingTickets;

        public StatisticsVO() {
            this.ticketCommonVO = new TicketCommonVO();
            this.cablewayTickets = 0;
            this.sightseeingTickets = 0;
        }
    }
}

