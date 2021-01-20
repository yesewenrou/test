package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyMemberService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * 值班人员管理
 * @author lixiao
 */
@RestController
@RequestMapping("duty-member")
public class DutyMemberController {

    private IDutyMemberService dutyMemberService;

    public DutyMemberController(IDutyMemberService dutyMemberService) {
        this.dutyMemberService = dutyMemberService;
    }

    /**
     * 新增
     * @param dutyMemberRequest
     * @return
     */
//    @Auth("duty-member:save")
    @PostMapping("save")
    public Result saveDutyMember(@Validated @RequestBody DutyMemberRequest dutyMemberRequest) {
        return ResultUtil.buildSuccessResultWithData(dutyMemberService.saveDutyMember(dutyMemberRequest));
    }


    /**
     *删除
     * @param id
     * @return
     */
//    @Auth("duty-member:delete")
    @GetMapping("delete")
    public Result deleteDutyMember(@RequestParam("id") Long id) {
        boolean success = dutyMemberService.deleteDutyMember(id);
        return ResultUtil.buildResult(success, success ? "删除成功" : "删除失败");
    }

    /**
     *获取
     * @param id
     * @return
     */
//    @Auth("duty-member:get")
    @GetMapping("get")
    public Result getDutyMember(@RequestParam("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(dutyMemberService.getDutyMember(id));
    }

    /**
     *更新
     * @param dutyMemberRequest
     * @return
     */
//    @Auth("duty-member:update")
    @PostMapping("update")
    public Result updateDutyMember(@Validated({Default.class, UpdateCheckGroup.class})  @RequestBody DutyMemberRequest dutyMemberRequest) {
        boolean success = dutyMemberService.updateDutyMember(dutyMemberRequest);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     *分页查询
     * @param pageRequest
     * @return
     */
//    @Auth("duty-member:list")
    @PostMapping("list")
    public Result listDutyMember(@Validated @RequestBody PageRequest<DutyMemberCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(dutyMemberService.listDutyMember(pageRequest));
    }

    /**
     *根据部门获取人员
     * @param departmentId
     * @return
     */
//    @Auth("duty-member:getMemberByDepartment")
    @GetMapping("getMemberByDepartment")
    public Result getMemberByDepartment(@Validated @RequestParam("departmentId") Long departmentId){
        return ResultUtil.buildSuccessResultWithData(dutyMemberService.getMemberByDepartment(departmentId));
    }

    /**
     * 根据人员获取联系方式
     * @param memberId
     * @return
     */
//    @Auth("duty-member:getContactByMember")
    @GetMapping("getContactByMember")
    public Result getContactByMember(@Validated @RequestParam("memberId") Long memberId){
        return ResultUtil.buildSuccessResultWithData(dutyMemberService.getContactByMember(memberId));
    }

}
