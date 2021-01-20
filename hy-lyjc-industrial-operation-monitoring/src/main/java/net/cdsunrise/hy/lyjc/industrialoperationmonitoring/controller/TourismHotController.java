package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismHotStatisticalService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LHY
 * @date 2019/11/25 13:55
 */
@RestController
@RequestMapping("/tourismHot")
@Slf4j
public class TourismHotController {

    private TourismHotStatisticalService tourismHotStatisticalService;

    public TourismHotController(TourismHotStatisticalService tourismHotStatisticalService) {
        this.tourismHotStatisticalService = tourismHotStatisticalService;
    }

    @Auth(value = "tourismHot:countyStatistical")
    @GetMapping("countyStatistical")
    public Result hotelStatistics(){
        return ResultUtil.buildSuccessResultWithData(tourismHotStatisticalService.countyStatistical());
    }

    @Auth(value = "tourismHot:countyTourismTrend")
    @GetMapping("countyTourismTrend")
    public Result countyTourismTrend(){
        return ResultUtil.buildSuccessResultWithData(tourismHotStatisticalService.countyTourismTrend());
    }

    @Auth(value = "tourismHot:scenicTourismTrend")
    @GetMapping("scenicTourismTrend")
    public Result scenicTourismTrend(){
        return ResultUtil.buildSuccessResultWithData(tourismHotStatisticalService.scenicTourismTrend());
    }
}
