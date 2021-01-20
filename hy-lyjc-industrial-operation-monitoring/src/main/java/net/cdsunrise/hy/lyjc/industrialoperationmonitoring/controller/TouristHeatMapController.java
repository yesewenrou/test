package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IHotMapFixService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristHeatMapService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2019/11/29 13:59
 */
@RestController
@RequestMapping("tourist-heat-map")
public class TouristHeatMapController {

    private ITouristHeatMapService touristHeatMapService;
    private IHotMapFixService iHotMapFixService;

    public TouristHeatMapController(ITouristHeatMapService touristHeatMapService, IHotMapFixService iHotMapFixService) {
        this.touristHeatMapService = touristHeatMapService;
        this.iHotMapFixService = iHotMapFixService;
    }

    /**
     * 查询县域游客热力数据
     *
     * @return 结果
     */
    @Auth("tourist-heat-map:listAll")
    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    public Result listAllTouristPeopleHot() {
        return ResultUtil.buildSuccessResultWithData(touristHeatMapService.listAllTouristPeopleHot());
    }

    /**
     * 查询景区运营状态
     *
     * @return 结果
     */
    @Auth("tourist-heat-map:scenic-status:listAll")
    @RequestMapping(value = "scenic-status/listAll", method = RequestMethod.GET)
    public Result listAllScenicTouristNewestWithStatus() {
        return ResultUtil.buildSuccessResultWithData(iHotMapFixService.scenicTouristHotMapStatistics());
    }
}
