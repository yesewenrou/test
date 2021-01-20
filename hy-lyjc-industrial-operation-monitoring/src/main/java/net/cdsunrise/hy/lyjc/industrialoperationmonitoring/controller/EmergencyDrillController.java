package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyDrillService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillVO;
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
@RequestMapping("emergency-drill")
public class EmergencyDrillController {

    private IEmergencyDrillService emergencyDrillService;

    public EmergencyDrillController(IEmergencyDrillService emergencyDrillService) {
        this.emergencyDrillService = emergencyDrillService;
    }

    /**
     * 新增应急演练
     *
     * @param emergencyDrillVO 演练
     * @return 结果
     */
    @Auth("emergency-drill:save")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Result saveEmergencyResource(@Validated @RequestBody EmergencyDrillVO emergencyDrillVO) {
        return ResultUtil.buildSuccessResultWithData(emergencyDrillService.saveEmergencyDrill(emergencyDrillVO));
    }

    /**
     * 删除应急演练
     *
     * @param id 演练ID
     * @return 结果
     */
    @Auth("emergency-drill:delete")
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result deleteEmergencyResource(@RequestParam("id") Long id) {
        boolean success = emergencyDrillService.deleteEmergencyDrill(id);
        return ResultUtil.buildResult(success, success ? "删除成功" : "删除失败");
    }

    /**
     * 更新应急演练
     *
     * @param emergencyDrillVO 演练
     * @return 结果
     */
    @Auth("emergency-drill:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateEmergencyResource(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody EmergencyDrillVO emergencyDrillVO) {
        boolean success = emergencyDrillService.updateEmergencyDrill(emergencyDrillVO);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     * 获取单个应急演练
     *
     * @param id 演练ID
     * @return 结果
     */
    @Auth("emergency-drill:get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Result getEmergencyResource(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyDrillService.getEmergencyDrill(id));
    }

    /**
     * 分页查询应急演练
     *
     * @param pageRequest 分页请求
     * @return 结果
     */
    @Auth("emergency-drill:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listEmergencyResource(@Validated @RequestBody PageRequest<EmergencyDrillCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(emergencyDrillService.listEmergencyDrill(pageRequest));
    }
}
