package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ParkingSpaceStatusMonitorVO;

/**
 * @author lijiafeng
 * @date 2019/12/10 15:51
 */
public interface IParkingLotService {

    /**
     * 获取当前停车场车位状态
     *
     * @return 结果
     */
    ParkingSpaceStatusMonitorVO getParkingSpaceStatus();
}
