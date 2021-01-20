package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeatureLabelService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelVO;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.MerchantLabelFeignClient;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.MerchantLabelResp;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import net.cdsunrise.hy.sso.starter.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binke Zhang
 */
@RestController
@RequestMapping("/featureLabel")
@Slf4j
public class FeatureLabelController {

    private final IFeatureLabelService featureLabelService;
    private final SsoService ssoService;
    private MerchantLabelFeignClient merchantLabelFeignClient;

    @Autowired
    public FeatureLabelController(IFeatureLabelService featureLabelService,
                                  SsoService ssoService, MerchantLabelFeignClient merchantLabelFeignClient) {
        this.featureLabelService = featureLabelService;
        this.ssoService = ssoService;
        this.merchantLabelFeignClient = merchantLabelFeignClient;
    }

    /**
     * 产业管理 -> 特色标签 -> 新增
     */
    @Auth("featureLabel:add")
    @PostMapping("add")
    @SuppressWarnings("Duplicates")
    public Result add(@RequestHeader("token") String token, @Validated @RequestBody FeatureLabelVO vo) {
        // 设置用户信息
        UserResp userResp = ssoService.fetchUser(token);
        vo.setUpdateBy(userResp.getId());
        vo.setUpdateName(userResp.getUserName());
        vo.setUpdateTime(new Date());
        return ResultUtil.buildSuccessResultWithData(featureLabelService.save(vo));
    }

    /**
     * 产业管理 -> 特色标签 -> 更新
     */
    @Auth("featureLabel:update")
    @PostMapping("update")
    @SuppressWarnings("Duplicates")
    public Result update(@RequestHeader("token") String token, @Validated @RequestBody FeatureLabelUpdateVO vo) {
        // 设置用户信息
        UserResp userResp = ssoService.fetchUser(token);
        vo.setUpdateBy(userResp.getId());
        vo.setUpdateName(userResp.getUserName());
        vo.setUpdateTime(new Date());
        return ResultUtil.buildSuccessResultWithData(featureLabelService.update(vo));
    }

    /**
     * 产业管理 -> 特色标签 -> 查看详情
     */
    @Auth(value = "featureLabel:query")
    @GetMapping("query/{id}")
    @SuppressWarnings("all")
    public Result findById(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(featureLabelService.getById(id));
    }

    /**
     * 产业管理 -> 特色标签 -> 获取列表
     */
    @Auth(value = "featureLabel:list")
    @GetMapping("list")
    public Result list() {
        return ResultUtil.buildSuccessResultWithData(featureLabelService.list());
    }

    /**
     * 产业管理 -> 特色标签 -> 删除
     */
    @Auth(value = "featureLabel:delete")
    @PostMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(featureLabelService.delete(id));
    }


    /**
     * 产业管理 -> 特色标签 -> 获取字典列表
     */
    @GetMapping("labelList")
    public Result labelList() {
        List<MerchantLabelResp> list = merchantLabelFeignClient.labelList().getData();
        return ResultUtil.buildSuccessResultWithData(
                list.stream().map(e -> new FeatureLabelResp(e.getLabelId(), e.getLabelName()))
                        .collect(Collectors.toList())
        );
    }

}
