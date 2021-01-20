package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.UserRoleMenuRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserRoleService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @Auth(value = "um:userRole:add")
    @PostMapping("add")
    public Result add(@RequestBody UserRoleMenuRequest userRoleMenuRequest) {
        userRoleService.update(userRoleMenuRequest.getUserId(), userRoleMenuRequest.getRoleIdList());
        return ResultUtil.success();
    }

}
