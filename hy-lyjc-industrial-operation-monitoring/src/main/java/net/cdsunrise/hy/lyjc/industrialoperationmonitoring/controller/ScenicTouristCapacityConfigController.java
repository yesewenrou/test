package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IScenicTouristCapacityConfigService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristCapacityConfigVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2019/11/20 16:09
 */
@RestController
@RequestMapping("scenic-tourist-capacity-config")
public class ScenicTouristCapacityConfigController {

    private IScenicTouristCapacityConfigService scenicTouristCapacityConfigService;

    public ScenicTouristCapacityConfigController(IScenicTouristCapacityConfigService scenicTouristCapacityConfigService) {
        this.scenicTouristCapacityConfigService = scenicTouristCapacityConfigService;
    }

    /**
     * 查询所有景区承载量配置
     *
     * @return 结果
     */
    @Auth("scenic-tourist-capacity-config:listAll")
    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    public Result listAll() {
        return ResultUtil.buildSuccessResultWithData(scenicTouristCapacityConfigService.listAllConfig());
    }

    /**
     * 更新单个景区承载量配置
     *
     * @return 结果
     */
    @Auth("scenic-tourist-capacity-config:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateConfig(@Validated @RequestBody ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVO) {
        scenicTouristCapacityConfigService.updateConfig(scenicTouristCapacityConfigVO);
        return ResultUtil.buildResult(true, "更新配置成功");
    }
}
