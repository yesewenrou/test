package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IApiManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManagePageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ApiManageVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2020/05/14 15:01
 */
@RestController
@RequestMapping("api-manage")
public class ApiManageController {

    private final IApiManageService apiManageService;

    public ApiManageController(IApiManageService apiManageService) {
        this.apiManageService = apiManageService;
    }

    /**
     * 查询API管理列表
     *
     * @param req 请求
     * @return 结果
     */
    @GetMapping("list")
    public Result<IPage<ApiManageVO>> listApiManage(@Validated ApiManagePageRequest req) {
        return ResultUtil.buildSuccessResultWithData(apiManageService.listApiManage(req));
    }
}
