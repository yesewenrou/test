package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;


import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TicketAnalysisVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristAnalysisVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristTicketService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author funnylog
 */
@RestController
@RequestMapping("touristTicketAnalysis")
public class TouristTicketAnalysisController {

    private final ITouristTicketService iTouristTicketService;

    public TouristTicketAnalysisController(ITouristTicketService iTouristTicketService) {
        this.iTouristTicketService = iTouristTicketService;
    }

    private static final String DATE_TYPE_DAY = "DAY";
    private static final String DATE_TYPE_MONTH = "MONTH";

    @GetMapping("ticket")
    public TicketAnalysisVO ticketAnalysis(@RequestParam(value = "beginDate") Long beginDate,
                                           @RequestParam(value = "endDate") Long endDate,
                                           @RequestParam(value = "scenicName") String scenicName,
                                           @RequestParam(value = "dateType", required = false, defaultValue = DATE_TYPE_DAY) String dateType) {
        if (dateType.equalsIgnoreCase(DATE_TYPE_DAY)) {
            return iTouristTicketService.ticketAnalysis(beginDate, endDate, scenicName);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_MONTH)) {
            return iTouristTicketService.ticketAnalysisByMonth(beginDate, endDate, scenicName);
        } else {
            throw new BusinessException("08000003", "日期类型不正确");
        }
    }

    @GetMapping("tourist")
    public TouristAnalysisVO touristAnalysis(@RequestParam(value = "beginDate") Long beginDate,
                                             @RequestParam(value = "endDate") Long endDate,
                                             @RequestParam(value = "scenicName") String scenicName,
                                             @RequestParam(value = "dateType", required = false, defaultValue = DATE_TYPE_DAY) String dateType) {
        if (dateType.equalsIgnoreCase(DATE_TYPE_DAY)) {
            return iTouristTicketService.touristAnalysis(beginDate, endDate, scenicName);
        } else if (dateType.equalsIgnoreCase(DATE_TYPE_MONTH)) {
            return iTouristTicketService.touristAnalysisByMonth(beginDate, endDate, scenicName);
        } else {
            throw new BusinessException("08000003", "日期类型不正确");
        }
    }


}
