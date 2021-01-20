package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.common.utility.vo.group.ConditionNotNullGroup;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeatureTourismResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import net.cdsunrise.hy.sso.starter.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.Date;

/**
 * @author Binke Zhang
 */
@RestController
@RequestMapping("/featureResource")
@Slf4j
public class FeatureTourismResourceController {

    private final IFeatureTourismResourceService featureTourismResourceService;
    private final SsoService ssoService;

    @Autowired
    public FeatureTourismResourceController(IFeatureTourismResourceService featureTourismResourceService,
                                            SsoService ssoService) {
        this.featureTourismResourceService = featureTourismResourceService;
        this.ssoService = ssoService;
    }

    /**
     * 产业管理 -> 特色旅游资源管理 -> 新增
     */
    @Auth("featureResource:add")
    @PostMapping("add")
    @SuppressWarnings("Duplicates")
    public Result add(@RequestHeader("token") String token, @Validated @RequestBody FeatureTourismResourceVO vo) {
        // 设置用户信息
        UserResp userResp = ssoService.fetchUser(token);
        vo.setUpdateBy(userResp.getId());
        vo.setUpdateName(userResp.getUserName());
        vo.setUpdateTime(new Date());
        return ResultUtil.buildSuccessResultWithData(featureTourismResourceService.save(vo));
    }

    /**
     * 产业管理 -> 特色旅游资源管理 -> 更新
     */
    @Auth("featureResource:update")
    @PostMapping("update")
    @SuppressWarnings("Duplicates")
    public Result update(@RequestHeader("token") String token, @Validated @RequestBody FeatureTourismResourceUpdateVO vo) {
        // 设置用户信息
        UserResp userResp = ssoService.fetchUser(token);
        vo.setUpdateBy(userResp.getId());
        vo.setUpdateName(userResp.getUserName());
        vo.setUpdateTime(new Date());
        return ResultUtil.buildSuccessResultWithData(featureTourismResourceService.update(vo));
    }

    /**
     * 产业管理 -> 特色旅游资源管理 -> 查看详情
     */
    @Auth(value = "featureResource:query")
    @GetMapping("query/{id}")
    @SuppressWarnings("all")
    public Result findById(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(featureTourismResourceService.getById(id));
    }

    /**
     * 产业管理 -> 特色旅游资源管理 -> 删除
     */
    @Auth(value = "featureResource:delete")
    @PostMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(featureTourismResourceService.delete(id));
    }

    /**
     * 产业管理 -> 特色旅游资源管理 -> 获取列表
     */
    @Auth(value = "featureResource:list")
    @PostMapping("list")
    public Result list(@Validated({ConditionNotNullGroup.class, Default.class}) @RequestBody PageRequest<FeatureTourismResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(featureTourismResourceService.page(pageRequest));
    }
}
