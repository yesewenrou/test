package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.UserRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleMenuService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign.SsoFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import net.cdsunrise.hy.sso.starter.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SsoFeignService ssoFeignService;
    @Autowired
    private SsoService ssoService;

    @Value("${sso.service.defaultMoudle}")
    private String platformCode;
    @Autowired
    private RoleMenuService roleMenuService;

    @Auth(value = "um:user:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest pageRequest) {
        pageRequest.setPlatformCode(platformCode);
        return ResultUtil.success(userService.list(pageRequest));
    }

    @GetMapping("logout")
    public Result logout() {
        // ThreadLocal中获取用户信息
        UserResp userResp = CustomContext.getTokenInfo().getUser();
        return ssoFeignService.logout(userResp.getId());
    }

    @PostMapping("changePassword")
    public Result changePassword(@RequestBody UserRequest userRequest) {
        // ThreadLocal中获取用户信息
        UserResp userResp = CustomContext.getTokenInfo().getUser();
        return ssoService.changePassword(userResp.getId(), userRequest.getOldPassword(), userRequest.getNewPassword());
    }

    @GetMapping("findUserByMenuForWarningMessage")
    public Result findUserByMenuForWarningMessage(MessageMenuEnum messageMenuEnum) {
        return ResultUtil.success(roleMenuService.findUserByMenuForWarningMessage(messageMenuEnum));
    }
}
