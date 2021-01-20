package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismIncomeService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;

/**
 * @Author: LHY
 * @Date: 2019/9/17 19:05
 */
@RestController
@RequestMapping("/tourismIncome")
@Slf4j
public class TourismIncomeController {

    @Autowired
    private TourismIncomeService tourismIncomeService;

    @Auth(value = "tourismIncome:statisticsData")
    @PostMapping("statisticsData")
    public Result statisticsData(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody TourismIncomeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismIncomeService.statisticsData(condition));
    }

    @Auth(value = "tourismIncome:conditionStatisticsData")
    @PostMapping("conditionStatisticsData")
    public Result conditionStatisticsData(@Validated @RequestBody PageRequest<TourismIncomeCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(tourismIncomeService.conditionStatisticsData(pageRequest));
    }

    @Auth(value = "tourismIncome:export")
    @PostMapping("export")
    public ResponseEntity<byte[]> export(@Validated @RequestBody PageRequest<TourismIncomeCondition> pageRequest) {
        return tourismIncomeService.export(pageRequest);
    }

    @Auth(value = "tourismIncome:historyConditionStatisticsData")
    @PostMapping("historyConditionStatisticsData")
    public Result historyConditionStatisticsData(@Validated @RequestBody PageRequest<TourismIncomeCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(tourismIncomeService.historyConditionStatisticsData(pageRequest));
    }

    @Auth(value = "tourismIncome:historyExport")
    @PostMapping("historyExport")
    public ResponseEntity<byte[]> historyExport(@Validated @RequestBody PageRequest<TourismIncomeCondition> pageRequest) {
        return tourismIncomeService.historyExport(pageRequest);
    }
}
