package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author lijiafeng
 * @date 2019/11/22 11:47
 */
@RestController
@RequestMapping("emergency-resource")
public class EmergencyResourceController {

    private IEmergencyResourceService emergencyResourceService;

    public EmergencyResourceController(IEmergencyResourceService emergencyResourceService) {
        this.emergencyResourceService = emergencyResourceService;
    }

    /**
     * 新增应急资源
     *
     * @param emergencyResourceVO 资源
     * @return 结果
     */
    @Auth("emergency-resource:save")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Result saveEmergencyResource(@Validated @RequestBody EmergencyResourceVO emergencyResourceVO) {
        return ResultUtil.buildSuccessResultWithData(emergencyResourceService.saveEmergencyResource(emergencyResourceVO));
    }

    /**
     * 删除应急资源
     *
     * @param id 资源ID
     * @return 结果
     */
    @Auth("emergency-resource:delete")
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result deleteEmergencyResource(@RequestParam("id") Long id) {
        boolean success = emergencyResourceService.deleteEmergencyResource(id);
        return ResultUtil.buildResult(success, success ? "删除成功" : "删除失败");
    }

    /**
     * 更新应急资源
     *
     * @param emergencyResourceVO 资源
     * @return 结果
     */
    @Auth("emergency-resource:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateEmergencyResource(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody EmergencyResourceVO emergencyResourceVO) {
        boolean success = emergencyResourceService.updateEmergencyResource(emergencyResourceVO);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     * 获取单个应急资源
     *
     * @param id 资源ID
     * @return 结果
     */
    @Auth("emergency-resource:get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public Result getEmergencyResource(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyResourceService.getEmergencyResource(id));
    }

    /**
     * 分页查询应急资源
     *
     * @param pageRequest 分页请求
     * @return 结果
     */
    @Auth("emergency-resource:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listEmergencyResource(@Validated @RequestBody PageRequest<EmergencyResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(emergencyResourceService.listEmergencyResource(pageRequest));
    }
}
