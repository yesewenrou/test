package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyEventService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/28 11:55
 */
@RestController
@RequestMapping("emergency-event")
public class EmergencyEventController {

    private IEmergencyEventService emergencyEventService;

    public EmergencyEventController(IEmergencyEventService emergencyEventService) {
        this.emergencyEventService = emergencyEventService;
    }

    /**
     * 添加事件
     *
     * @param emergencyEventAddVO 事件
     * @return 事件ID
     */
    @Auth("emergency-event:save")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Result saveEmergencyEvent(@Validated @RequestBody EmergencyEventAddVO emergencyEventAddVO) {
        return ResultUtil.buildSuccessResultWithData(emergencyEventService.saveEmergencyEvent(emergencyEventAddVO));
    }

    /**
     * 编辑事件
     *
     * @param emergencyEventEditVO 事件
     * @return 事件ID
     */
    @Auth("emergency-event:edit")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Result editEmergencyEvent(@Validated @RequestBody EmergencyEventEditVO emergencyEventEditVO) {
        emergencyEventService.editEmergencyEvent(emergencyEventEditVO);
        return ResultUtil.success();
    }

    /**
     * 审核事件
     *
     * @param emergencyEventCheckVO 审核信息
     * @return 结果
     */
    @Auth("emergency-event:check")
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public Result checkEmergencyEvent(@Validated @RequestBody EmergencyEventCheckVO emergencyEventCheckVO) {
        boolean success = emergencyEventService.checkEmergencyEvent(emergencyEventCheckVO);
        return ResultUtil.buildResult(success, success ? "审核成功" : "审核失败");
    }

    /**
     * 查询所有可以指派的用户列表
     * 拥有反馈权限的用户
     *
     * @return 结果
     */
    @Auth("emergency-event:assign-user:listAll")
    @RequestMapping(value = "assign-user/listAll", method = RequestMethod.GET)
    public Result listAllCanAssignUser() {
        return ResultUtil.buildSuccessResultWithData(emergencyEventService.listAllCanAssignUser());
    }

    /**
     * 处置事件
     *
     * @param emergencyEventAssignVO 处置信息
     * @return 结果
     */
    @Auth("emergency-event:assign")
    @RequestMapping(value = "assign", method = RequestMethod.POST)
    public Result assignEmergencyEvent(@Validated @RequestBody EmergencyEventAssignVO emergencyEventAssignVO) {
        boolean success = emergencyEventService.assignEmergencyEvent(emergencyEventAssignVO);
        return ResultUtil.buildResult(success, success ? "处置成功" : "处置失败");
    }

    /**
     * 反馈事件
     *
     * @param emergencyEventFeedbackVO 反馈信息
     * @return 结果
     */
    @Auth("emergency-event:feedback")
    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    public Result feedbackEmergencyEvent(@Validated @RequestBody EmergencyEventFeedbackVO emergencyEventFeedbackVO) {
        boolean success = emergencyEventService.feedbackEmergencyEvent(emergencyEventFeedbackVO);
        return ResultUtil.buildResult(success, success ? "反馈成功" : "反馈失败");
    }

    /**
     * 结案
     *
     * @param emergencyEventCloseVO 结案信息
     * @return 结果
     */
    @Auth("emergency-event:close")
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public Result closeEmergencyEvent(@Validated @RequestBody EmergencyEventCloseVO emergencyEventCloseVO) {
        boolean success = emergencyEventService.closeEmergencyEvent(emergencyEventCloseVO);
        return ResultUtil.buildResult(success, success ? "结案成功" : "结案失败");
    }

    /**
     * 查询单个事件详情
     *
     * @param id 事件ID
     * @return 结果
     */
    @Auth("emergency-event:get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Result getEmergencyEvent(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyEventService.getEmergencyEvent(id));
    }

    /**
     * 分页查询事件列表
     *
     * @param pageRequest 分页请求
     * @return 结果
     */
    @Auth("emergency-event:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listEmergencyEvent(@Validated @RequestBody PageRequest<EmergencyEventCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(emergencyEventService.listEmergencyEvent(pageRequest));
    }

    /**
     * 删除事件
     *
     * @param id 事件ID
     * @return 结果
     */
    @Auth("emergency-event:delete")
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result deleteEmergencyEvent(@RequestParam("id") Long id) {
        emergencyEventService.deleteEmergencyEvent(id);
        return ResultUtil.success();
    }

    /**
     * 获取事件评估结果
     *
     * @param id 事件ID
     * @return 结果
     */
    @GetMapping("{id}/evaluation")
    public Result<List<EmergencyEventEvaluationVO>> evaluation(@PathVariable("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyEventService.evaluate(id));
    }
}
