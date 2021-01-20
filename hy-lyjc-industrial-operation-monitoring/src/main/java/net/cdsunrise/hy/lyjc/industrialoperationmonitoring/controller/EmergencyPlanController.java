package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author lijiafeng
 * @date 2019/11/26 11:01
 */
@RestController
@RequestMapping("emergency-plan")
public class EmergencyPlanController {

    private IEmergencyPlanService emergencyPlanService;

    public EmergencyPlanController(IEmergencyPlanService emergencyPlanService) {
        this.emergencyPlanService = emergencyPlanService;
    }

    /**
     * 新增应急预案
     *
     * @param emergencyPlanVO 预案
     * @return 结果
     */
    @Auth("emergency-plan:save")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Result saveEmergencyResource(@Validated @RequestBody EmergencyPlanVO emergencyPlanVO) {
        return ResultUtil.buildSuccessResultWithData(emergencyPlanService.saveEmergencyPlan(emergencyPlanVO));
    }

    /**
     * 删除应急预案
     *
     * @param id 预案ID
     * @return 结果
     */
    @Auth("emergency-plan:delete")
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result deleteEmergencyResource(@RequestParam("id") Long id) {
        boolean success = emergencyPlanService.deleteEmergencyPlan(id);
        return ResultUtil.buildResult(success, success ? "删除成功" : "删除失败");
    }

    /**
     * 更新应急预案
     *
     * @param emergencyPlanVO 预案
     * @return 结果
     */
    @Auth("emergency-plan:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateEmergencyResource(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody EmergencyPlanVO emergencyPlanVO) {
        boolean success = emergencyPlanService.updateEmergencyPlan(emergencyPlanVO);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     * 获取单个应急预案
     *
     * @param id 预案ID
     * @return 结果
     */
    @Auth("emergency-plan:get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Result getEmergencyResource(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyPlanService.getEmergencyPlan(id));
    }

    /**
     * 分页查询应急预案
     *
     * @param pageRequest 分页请求
     * @return 结果
     */
    @Auth("emergency-plan:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listEmergencyResource(@Validated @RequestBody PageRequest<EmergencyPlanCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(emergencyPlanService.listEmergencyPlan(pageRequest));
    }
}
