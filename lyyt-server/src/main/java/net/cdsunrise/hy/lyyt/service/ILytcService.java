package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.reids.ScenicAreaParkingSpaceResp;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;

/**
 * @author lijiafeng
 * @date 2020/2/13 8:34
 */
public interface ILytcService {

    /**
     * 景区车位实时信息
     *
     * @return 结果
     */
    Object scenicAreaParkingSpaceRealTime();

    /**
     * 停车场类型汇总
     *
     * @return 结果
     */
    Object parkingLotTypeSummary();

    /**
     * 所有停车场车位信息
     *
     * @return 结果
     */
    Object allParkingSpaceRealtime();

    /**
     * 查询所有停车场车位信息汇总
     *
     * @return 结果
     */
    Object parkingSpaceRealtimeSummary();

    /**
     * 景区未来2小时预测
     *
     * @return 结果
     */
    Object scenicRealTimeAndPrediction();

    /**
     * 所有停车场饱和度统计
     *
     * @return 结果
     */
    Object allParkingLotSaturation();

    /**
     * 景区爆满本月及上月记录
     *
     * @return 结果
     */
    Object scenicAreaFullRecord();

    /**
     * 当日爆满统计
     *
     * @return 结果
     */
    Object todayScenicAreaParkingLotFullStatistics();

    /**
     * 节假日爆满统计
     *
     * @param holiday 节假日
     * @return 结果
     */
    Object holidayParkingLotFullStatistics(HolidayTypeEnum holiday);

    /**
     * 今天和昨天所有停车场车位使用情况
     *
     * @return 结果
     */
    Object todayAndYesterdayParkingSpaceHistory();
}
