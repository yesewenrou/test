package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IKeyTravelRelatedResourcesService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyTravelRelatedResourcesCondition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2020/3/8 17:16
 */
@RestController
@RequestMapping("key-travel-related-resources")
public class KeyTravelRelatedResourcesController {

    private IKeyTravelRelatedResourcesService keyTravelRelatedResourcesService;

    public KeyTravelRelatedResourcesController(IKeyTravelRelatedResourcesService keyTravelRelatedResourcesService) {
        this.keyTravelRelatedResourcesService = keyTravelRelatedResourcesService;
    }

    /**
     * 分页查询重点涉旅资源
     *
     * @param condition 条件
     * @return 结果
     */
    @Auth("key-travel-related-resources:page")
    @GetMapping("page")
    public Result page(@Validated KeyTravelRelatedResourcesCondition condition) {
        return ResultUtil.buildSuccessResultWithData(keyTravelRelatedResourcesService.page(condition));
    }
}
