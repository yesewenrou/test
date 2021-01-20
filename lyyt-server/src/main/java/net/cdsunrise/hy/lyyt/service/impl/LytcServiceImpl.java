package net.cdsunrise.hy.lyyt.service.impl;

import net.cdsunrise.hy.lyyt.cache.LytcCache;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import net.cdsunrise.hy.lyyt.service.ILytcService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author lijiafeng
 * @date 2020/2/13 8:35
 */
@Service
public class LytcServiceImpl implements ILytcService {

    private LytcCache lytcCache;

    public LytcServiceImpl(LytcCache lytcCache) {
        this.lytcCache = lytcCache;
    }

    @Override
    public Object scenicAreaParkingSpaceRealTime() {
        return lytcCache.scenicAreaParkingSpaceRealTime();
    }

    @Override
    public Object parkingLotTypeSummary() {
        return lytcCache.parkingLotTypeSummary();
    }

    @Override
    public Object allParkingSpaceRealtime() {
        return lytcCache.allParkingSpaceRealtime();
    }

    @Override
    public Object parkingSpaceRealtimeSummary() {
        return lytcCache.parkingSpaceRealtimeSummary();
    }

    @Override
    public Object scenicRealTimeAndPrediction() {
        return lytcCache.scenicRealTimeAndPrediction();
    }

    @Override
    public Object allParkingLotSaturation() {
        return lytcCache.allParkingLotSaturation();
    }

    @Override
    public Object scenicAreaFullRecord() {
        return lytcCache.scenicAreaFullRecord();
    }

    @Override
    public Object todayScenicAreaParkingLotFullStatistics() {
        return lytcCache.todayScenicAreaParkingLotFullStatistics();
    }

    @Override
    public Object holidayParkingLotFullStatistics(HolidayTypeEnum holiday) {
        if (Objects.isNull(holiday)) {
            throw BusinessException.fail(BusinessExceptionEnum.HOLIDAY_IS_NULL).get();
        }
        Map<String, Object> holidayMap = lytcCache.holidayParkingLotFullStatistics();
        return holidayMap.get(holiday.getName());
    }

    @Override
    public Object todayAndYesterdayParkingSpaceHistory() {
        return lytcCache.todayAndYesterdayParkingSpaceHistory();
    }
}
