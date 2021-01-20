package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.CompanyVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign.SsoFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/um/company")
public class CompanyController {

    @Autowired
    private SsoFeignService ssoFeignService;

    @Auth(value = "um:company:list")
    @GetMapping("list")
    public Result list() {
        // 此处不用分页，将size设置成-1即可
        Integer page = 1;
        Integer size = -1;
        Result<Page<CompanyVO>> pageResult = ssoFeignService.companyList(page, size);
        return ResultUtil.success(pageResult.getData().getRecords());
    }
}
