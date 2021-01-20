package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lytc.parkinglotmanage.starter.feign.vo.SummaryParkingSpaceRealtimeVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/12/10 15:53
 */
@Data
public class ParkingSpaceStatusMonitorVO {

    /**
     * 停车位数量
     */
    private Integer parkingSpaceNumber;

    /**
     * 使用中数量
     */
    private Integer inUseNumber;

    /**
     * 剩余数量
     */
    private Integer remainingNumber;

    /**
     * 使用中百分比
     */
    private Double inUseDegree;

    /**
     * 无余位停车场个数
     */
    private Integer fullNumber;

    /**
     * 景区停车场信息
     */
    List<SummaryParkingSpaceRealtimeVO> summaryParkingSpaceRealtimeList;
}
