package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign.SsoFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/department")
public class DepartmentController {

    @Autowired
    private SsoFeignService ssoFeignService;

    @Auth(value = "um:department:findByCompanyId")
    @GetMapping("findByCompanyId/{companyId}")
    public Result findByCompanyId(@PathVariable long companyId) {
        return ssoFeignService.findByCompanyId(companyId);
    }

}
