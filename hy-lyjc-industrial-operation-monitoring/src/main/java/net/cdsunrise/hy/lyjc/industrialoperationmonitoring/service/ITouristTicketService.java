package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;


import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TicketAnalysisVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristAnalysisVO;

/**
 * @author funnylog
 */
public interface ITouristTicketService {

    /**
     * 票务分析 - 按天
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    TicketAnalysisVO ticketAnalysis(Long beginDate, Long endDate, String scenicName);

    /**
     * 客流分析 - 按天
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    TouristAnalysisVO touristAnalysis(Long beginDate, Long endDate, String scenicName);

    /**
     * 票务分析 - 按月
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    TicketAnalysisVO ticketAnalysisByMonth(Long beginDate, Long endDate, String scenicName);

    /**
     * 客流分析 - 按月
     *
     * @param beginDate  开始时间
     * @param endDate    结束时间
     * @param scenicName 景区名称
     * @return TicketAnalysisVO
     */
    TouristAnalysisVO touristAnalysisByMonth(Long beginDate, Long endDate, String scenicName);
}
