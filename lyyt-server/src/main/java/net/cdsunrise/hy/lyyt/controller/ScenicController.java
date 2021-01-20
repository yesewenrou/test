package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.TouristCountVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.IScenicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang yun long
 * @date 2020-01-16 14:29
 */
@RestController
@RequestMapping("scenic")
public class ScenicController {

    private final IScenicService iScenicService;

    public ScenicController(IScenicService iScenicService) {
        this.iScenicService = iScenicService;
    }

    /**
     * 统计景区本周和上周的总票数
     * @return Result
     */
    @GetMapping("statisticTicketsByWeek")
    @DataType({DataTypeEnum.PWSJ})
    public Result statisticScenicTicketsByWeek() {
        return iScenicService.statisticScenicTickets();
    }

    @GetMapping("statisticCurrentAndLastMonthPassenger")
    public Result<TouristCountVO> statisticCurrentAndLastMonthPassenger() {
        return iScenicService.statisticCurrentAndLastMonthPassenger();
    }
}
