package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.constant.CommonConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.MenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.MenuService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("add")
    public Result add(@RequestBody MenuDTO menuDTO) {
        return ResultUtil.success(menuService.add(menuDTO));
    }

    @GetMapping("show")
    public Result show() {
        return ResultUtil.success(menuService.getMenuList());
    }

    @GetMapping("findByUserId")
    public Result findByUserId() {
        // ThreadLocal中获取用户信息
        UserResp userResp = CustomContext.getTokenInfo().getUser();
        if (CommonConst.ADMIN_NAME.equals(userResp.getUserName())) {
            // 超管登录
            return ResultUtil.success(menuService.getMenuList());
        }
        return ResultUtil.success(menuService.findByUserId(userResp.getId()));
    }

    // 用于判断用户是否配置菜单权限
    @GetMapping("countByUserId")
    public boolean countByUserId() {
        UserResp userResp = CustomContext.getTokenInfo().getUser();
        return CommonConst.ADMIN_NAME.equals(userResp.getUserName()) || menuService.countByUserId(userResp.getId()) > 0;
    }
}
