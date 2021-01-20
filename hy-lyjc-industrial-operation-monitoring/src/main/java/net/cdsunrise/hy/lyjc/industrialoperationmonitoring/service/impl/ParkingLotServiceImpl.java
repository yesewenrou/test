package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IParkingLotService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ParkingSpaceStatusMonitorVO;
import net.cdsunrise.hy.lytc.parkinglotmanage.starter.feign.ParkingSpaceRealtimeFeignClient;
import net.cdsunrise.hy.lytc.parkinglotmanage.starter.feign.vo.ScenicAreaParkingLotSaturationVO;
import net.cdsunrise.hy.lytc.parkinglotmanage.starter.feign.vo.SummaryParkingSpaceRealtimeVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/12/10 16:03
 */
@Service
public class ParkingLotServiceImpl implements IParkingLotService {

    private ParkingSpaceRealtimeFeignClient parkingSpaceRealtimeFeignClient;

    public ParkingLotServiceImpl(ParkingSpaceRealtimeFeignClient parkingSpaceRealtimeFeignClient) {
        this.parkingSpaceRealtimeFeignClient = parkingSpaceRealtimeFeignClient;
    }

    @Override
    public ParkingSpaceStatusMonitorVO getParkingSpaceStatus() {
        Result<ScenicAreaParkingLotSaturationVO> saturationVoResult = parkingSpaceRealtimeFeignClient.getScenicAreaParkingLotSaturation();
        Result<List<SummaryParkingSpaceRealtimeVO>> listResult = parkingSpaceRealtimeFeignClient.listSummaryParkingSpaceRealtime("scenic_area");
        ParkingSpaceStatusMonitorVO parkingSpaceStatusMonitorVO = new ParkingSpaceStatusMonitorVO();
        int parkingSpaceNumber = 0;
        int inUseNumber = 0;
        for (SummaryParkingSpaceRealtimeVO item : listResult.getData()) {
            parkingSpaceNumber += item.getParkingSpaceNumber();
            inUseNumber += item.getInUseNumber();
        }
        parkingSpaceStatusMonitorVO.setParkingSpaceNumber(parkingSpaceNumber);
        parkingSpaceStatusMonitorVO.setInUseNumber(inUseNumber);
        int remainingNumber = parkingSpaceNumber - inUseNumber;
        parkingSpaceStatusMonitorVO.setRemainingNumber(remainingNumber);
        double inUseDegree = new BigDecimal(inUseNumber * 1.0 / parkingSpaceNumber).setScale(4, RoundingMode.DOWN).doubleValue();
        parkingSpaceStatusMonitorVO.setInUseDegree(inUseDegree);
        parkingSpaceStatusMonitorVO.setFullNumber(saturationVoResult.getData().getFullNumber());
        parkingSpaceStatusMonitorVO.setSummaryParkingSpaceRealtimeList(listResult.getData());

        return parkingSpaceStatusMonitorVO;
    }
}
