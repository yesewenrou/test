package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.UserRoleMenuRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Auth(value = "um:role:add")
    @PostMapping("add")
    public Result add(@RequestBody UserRoleMenuRequest userRoleMenuRequest) {
        return ResultUtil.success(roleService.add(userRoleMenuRequest.getRoleName(), userRoleMenuRequest.getMenuIdList()));
    }

    @Auth(value = "um:role:update")
    @PostMapping("update")
    public Result update(@RequestBody UserRoleMenuRequest userRoleMenuRequest) {
        roleService.update(userRoleMenuRequest.getRoleId(), userRoleMenuRequest.getRoleName(),
                userRoleMenuRequest.getMenuIdList());
        return ResultUtil.success();
    }

    @Auth(value = "um:role:info")
    @GetMapping("info/{id}")
    public Result info(@PathVariable long id) {
        return ResultUtil.success(BusinessExceptionEnum.SUCCESS.getCode(), "", roleService.findById(id));
    }

    @Auth(value = "um:role:delete")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable long id) {
        roleService.delete(id);
        return ResultUtil.success();
    }

    @Auth(value = "um:role:enable")
    @GetMapping("enable/{id}")
    public Result enable(@PathVariable long id) {
        roleService.enable(id);
        return ResultUtil.success();
    }

    @Auth(value = "um:role:disable")
    @GetMapping("disable/{id}")
    public Result disable(@PathVariable long id) {
        roleService.disable(id);
        return ResultUtil.success();
    }

    @Auth(value = "um:role:findByUserId")
    @GetMapping("findByUserId/{userId}")
    public Result findByUserId(@PathVariable long userId) {
        return ResultUtil.success(roleService.findByUserId(userId));
    }

    @Auth(value = "um:role:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest pageRequest) {
        return ResultUtil.success(BusinessExceptionEnum.SUCCESS.getCode(), "", roleService.list(pageRequest.getPage(),
                pageRequest.getSize(), pageRequest.getRoleName()));
    }

}
