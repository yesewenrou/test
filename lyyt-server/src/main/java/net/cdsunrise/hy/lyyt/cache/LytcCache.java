package net.cdsunrise.hy.lyyt.cache;

import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lijiafeng
 * @date 2020/2/13 11:14
 */
@Service
public class LytcCache {

    private static final String CACHE_NAME = "LYTC";

    @Cacheable(cacheNames = CACHE_NAME, key = "'SCENIC_AREA_PARKING_SPACE_REALTIME'")
    public Object scenicAreaParkingSpaceRealTime() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'PARKING_LOT_TYPE_SUMMARY'")
    public Object parkingLotTypeSummary() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'ALL_PARKING_SPACE_REALTIME'")
    public Object allParkingSpaceRealtime() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'PARKING_SPACE_REALTIME_SUMMARY'")
    public Object parkingSpaceRealtimeSummary() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'SCENIC_AREA_PARKING_SPACE_TWO_HOUR_PREDICTION'")
    public Object scenicRealTimeAndPrediction() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'ALL_PARKING_LOT_SATURATION'")
    public Object allParkingLotSaturation() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'SCENIC_AREA_FULL_RECORD'")
    public Object scenicAreaFullRecord() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'TODAY_SCENIC_AREA_PARKING_LOT_FULL_STATISTICS'")
    public Object todayScenicAreaParkingLotFullStatistics() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'HOLIDAY_PARKING_LOT_FULL_STATISTICS'")
    public Map<String, Object> holidayParkingLotFullStatistics() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'TODAY_AND_YESTERDAY_PARKING_SPACE_HISTORY'")
    public Object todayAndYesterdayParkingSpaceHistory() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }
}
