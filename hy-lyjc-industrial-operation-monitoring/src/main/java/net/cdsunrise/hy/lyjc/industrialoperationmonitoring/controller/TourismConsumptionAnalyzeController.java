package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITourismConsumptionAnalyzeService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionAnalyzeCondition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2020/3/3 15:52
 */
@RestController
@RequestMapping("tourism-consumption")
public class TourismConsumptionAnalyzeController {

    private ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService;

    public TourismConsumptionAnalyzeController(ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService) {
        this.tourismConsumptionAnalyzeService = tourismConsumptionAnalyzeService;
    }

    /**
     * 旅游消费分析
     *
     * @return 结果
     */
    @Auth("tourism-consumption:analyze")
    @GetMapping("analyze")
    public Result getTourismConsumptionAnalyze() {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionAnalyze());
    }

    /**
     * 旅游消费来源地分析
     *
     * @return 结果
     */
    @Auth("tourism-consumption:analyze:source")
    @GetMapping("analyze/source")
    public Result getTourismConsumptionSourceAnalyze(@Validated TourismConsumptionAnalyzeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionSourceAnalyze(condition));
    }

    /**
     * 旅游消费行业分析
     *
     * @return 结果
     */
    @Auth("tourism-consumption:analyze:industry")
    @GetMapping("analyze/industry")
    public Result getTourismConsumptionIndustryAnalyze(@Validated TourismConsumptionAnalyzeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionIndustryAnalyze(condition));
    }

    /**
     * 旅游消费商圈分析
     *
     * @return 结果
     */
    @Auth("tourism-consumption:analyze:business-circle")
    @GetMapping("analyze/business-circle")
    public Result getTourismConsumptionBusinessCircleAnalyze(@Validated TourismConsumptionAnalyzeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionBusinessCircleAnalyze(condition));
    }
}
