package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DecisionPlanService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanDetailVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@RestController
@RequestMapping("decision_plan")
public class DecisionPlanController {

    private final DecisionPlanService decisionPlanService;

    public DecisionPlanController(DecisionPlanService decisionPlanService) {
        this.decisionPlanService = decisionPlanService;
    }

    @PostMapping
    @Auth("decision_plan:add")
    public Result<Long> addDecisionPlan(@RequestBody @Validated DecisionPlanRequest.Add request) {
        return ResultUtil.buildSuccessResultWithData(decisionPlanService.addDecisionPlan(request));
    }

    @PutMapping
    @Auth("decision_plan:update")
    public Result<Long> updateDecisionPlan(@RequestBody @Validated DecisionPlanRequest.Update request) {
        return ResultUtil.buildSuccessResultWithData(decisionPlanService.updateDecisionPlan(request));
    }

    @DeleteMapping("{id}")
    @Auth("decision_plan:delete")
    public Result<Long> deleteDecisionPlan(@PathVariable("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(decisionPlanService.deleteDecisionPlan(id));
    }

    @GetMapping("{id}")
    public Result<DecisionPlanDetailVO> detailDecisionPlan(@PathVariable("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(decisionPlanService.detailDecisionPlan(id));
    }

    @GetMapping("list")
    public Result<PageResult<DecisionPlanVO>> pageDecisionPlan(@Validated PageRequest request) {
        return ResultUtil.buildSuccessResultWithData(decisionPlanService.pageDecisionPlan(request));
    }
}
