package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IParkingLotService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2019/12/10 16:17
 */
@RestController
@RequestMapping("parking-lot")
public class ParkingLotController {

    private IParkingLotService parkingLotService;

    public ParkingLotController(IParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * 查询停车场车位状态
     *
     * @return 结果
     */
    @Auth("parking-lot:parking-space:status")
    @RequestMapping(value = "parking-space/status", method = RequestMethod.GET)
    public Result getParkingSpaceStatus() {
        return ResultUtil.buildSuccessResultWithData(parkingLotService.getParkingSpaceStatus());
    }
}
