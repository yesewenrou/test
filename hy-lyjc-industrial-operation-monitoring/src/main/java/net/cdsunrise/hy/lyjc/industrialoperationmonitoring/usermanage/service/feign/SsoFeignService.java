package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.CompanyVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.DepartmentVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.UserVO;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LHY
 * 去单点登录系统获取用户信息
 */
@FeignClient(name = "hy-hydd", url = "${sso.service.ssoServerAddress}")
public interface SsoFeignService {

    @PostMapping(value = "/user/list")
    Result<Page<UserVO>> userList(@RequestBody PageRequest pageRequest);

    @GetMapping("/user/find/{id}")
    Result<UserResp> find(@PathVariable(value = "id", required = true) Long id);

    @GetMapping(value = "/company/list")
    Result<Page<CompanyVO>> companyList(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @GetMapping(value = "/department/findByCompanyId/{companyId}")
    Result<List<DepartmentVO>> findByCompanyId(@PathVariable(value = "companyId", required = true) long companyId);

    @GetMapping("/login/logout/{id}")
    Result logout(@PathVariable(value = "id", required = true) Long userId);
}
