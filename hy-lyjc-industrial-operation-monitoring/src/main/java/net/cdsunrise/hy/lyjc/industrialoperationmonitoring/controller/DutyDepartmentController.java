package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyDepartmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyDepartmentCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyDepartmentVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * 值班部门管理
 * @author lixiao
 */
@RestController
@RequestMapping("duty-department")
public class DutyDepartmentController {

    private IDutyDepartmentService dutyDepartmentService;

    public DutyDepartmentController(IDutyDepartmentService dutyDepartmentService) {
        this.dutyDepartmentService = dutyDepartmentService;
    }

    /**
     * 新增
     * @param dutyDepartmentVO
     * @return
     */
//    @Auth("duty-department:save")
    @PostMapping("save")
    public Result saveDutyDepartment(@Validated @RequestBody DutyDepartmentVO dutyDepartmentVO) {
        return ResultUtil.buildSuccessResultWithData(dutyDepartmentService.saveDutyDepartment(dutyDepartmentVO));
    }

    /**
     * 删除
     * @param id
     * @return
     */
//    @Auth("duty-department:delete")
    @GetMapping("delete")
    public Result deleteDutyDepartment(@RequestParam("id") Long id) {
        boolean success = dutyDepartmentService.deleteDutyDepartment(id);
        return ResultUtil.buildResult(success, success ? "删除成功" : "删除失败");
    }

    /**
     * 获取
     * @param id
     * @return
     */
//    @Auth("duty-department:get")
    @GetMapping("get")
    public Result getDutyDepartment(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(dutyDepartmentService.getDutyDepartment(id));
    }

    /**
     * 更新
     * @param dutyDepartmentVO
     * @return
     */
//    @Auth("duty-department:update")
    @PostMapping("update")
    public Result updateDutyDepartment(@Validated({Default.class, UpdateCheckGroup.class})  @RequestBody DutyDepartmentVO dutyDepartmentVO) {
        boolean success = dutyDepartmentService.updateDutyDepartment(dutyDepartmentVO);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     * 分页查询
     * @param pageRequest
     * @return
     */
//    @Auth("duty-department:list")
    @PostMapping(value = "list")
    public Result listEmergencyResource(@Validated @RequestBody PageRequest<DutyDepartmentCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(dutyDepartmentService.listDepartment(pageRequest));
    }

    /**
     * 获取单位
     * @return
     */
//    @Auth("duty-department:getInstitutions")
    @GetMapping("getInstitutions")
    public Result getInstitutions() {
        return ResultUtil.buildSuccessResultWithData(dutyDepartmentService.getInstitutions());
    }

    /**
     * 根据单位获取部门
     * @param institutions
     * @return
     */
//    @Auth("duty-department:getDepartmentByInstitutions")
    @GetMapping("getDepartmentByInstitutions")
    public Result getDepartmentByInstitutions(@Validated @RequestParam("institutions") String institutions){
        return ResultUtil.buildSuccessResultWithData(dutyDepartmentService.getDepartmentByInstitutions(institutions));
    }

}