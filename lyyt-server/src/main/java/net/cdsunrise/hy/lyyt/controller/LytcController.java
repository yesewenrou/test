package net.cdsunrise.hy.lyyt.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.reids.ScenicAreaFullVO;
import net.cdsunrise.hy.lyyt.entity.vo.reids.ScenicAreaParkingSpaceResp;
import net.cdsunrise.hy.lyyt.entity.vo.reids.ScenicAreaParkingSpaceVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.service.ILytcService;
import net.cdsunrise.hy.lyyt.utils.SortUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lijiafeng
 * @date 2020/2/13 8:41
 */
@RestController
@RequestMapping("lytc")
@Slf4j
public class LytcController {

    private ILytcService lytcService;

    public LytcController(ILytcService lytcService) {
        this.lytcService = lytcService;
    }

    /**
     * 景区车位实时信息
     *
     * @return 结果
     */
    @GetMapping("scenicAreaParkingSpaceRealTime")
    @DataType({DataTypeEnum.LYTCJCSJ})
    public Object scenicAreaParkingSpaceRealTime() {


        final Object o = lytcService.scenicAreaParkingSpaceRealTime();

        // 尝试强制转换成明确对象，按照名称排序后返回
        try{
            final Map<String,Object> map = (Map<String,Object>) o;
            final List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("list");
            final List<ScenicAreaParkingSpaceVO> sortedList = list.stream().map(ScenicAreaParkingSpaceVO::fromMap)
                    .sorted((o1, o2) -> SortUtil.getParkingSummarySortResult(o1.getSummary(), o2.getSummary()))
                    .collect(Collectors.toList());
            map.put("list",sortedList);
            return map;

        } catch (Exception e){
            log.error("scenicAreaParkingSpaceRealTime error, cause ",e);
        }

        return o;
    }



    /**
     * 停车场类型汇总
     *
     * @return 结果
     */
    @GetMapping("parkingLotTypeSummary")
    public Object parkingLotTypeSummary() {
        return lytcService.parkingLotTypeSummary();
    }

    /**
     * 所有停车场车位信息
     *
     * @return 结果
     */
    @GetMapping("allParkingSpaceRealtime")
    public Object allParkingSpaceRealtime() {
        return lytcService.allParkingSpaceRealtime();
    }

    /**
     * 查询所有停车场车位信息汇总
     *
     * @return 结果
     */
    @GetMapping("parkingSpaceRealtimeSummary")
    public Object parkingSpaceRealtimeSummary() {
        return lytcService.parkingSpaceRealtimeSummary();
    }

    /**
     * 景区未来2小时预测
     *
     * @return 结果
     */
    @GetMapping("scenicRealTimeAndPrediction")
    public Object scenicRealTimeAndPrediction() {
        return lytcService.scenicRealTimeAndPrediction();
    }

    /**
     * 所有停车场饱和度统计
     *
     * @return 结果
     */
    @GetMapping("allParkingLotSaturation")
    public Object allParkingLotSaturation() {
        return lytcService.allParkingLotSaturation();
    }

    /**
     * 景区爆满本月及上月记录
     *
     * @return 结果
     */
    @GetMapping("scenicAreaFullRecord")
    public Object scenicAreaFullRecord() {


        final Object o = lytcService.scenicAreaFullRecord();


        try{
            final List<Map<String, Object>> list = (List<Map<String, Object>>) o;
            return list.stream()
                    .map(ScenicAreaFullVO::fromMap)
                    .sorted((o1, o2) -> SortUtil.getParkingSummarySortResult(o1.getScenicArea(), o2.getScenicArea()))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("scenicAreaFullRecord error, cause ",e);
        }
        return o;
    }

    /**
     * 当日爆满统计
     *
     * @return 结果
     */
    @GetMapping("todayScenicAreaParkingLotFullStatistics")
    public Object todayScenicAreaParkingLotFullStatistics() {
        return lytcService.todayScenicAreaParkingLotFullStatistics();
    }

    /**
     * 节假日爆满统计
     *
     * @return 结果
     */
    @GetMapping("holidayParkingLotFullStatistics")
    public Object holidayParkingLotFullStatistics(HolidayTypeEnum holiday) {
        return lytcService.holidayParkingLotFullStatistics(holiday);
    }

    /**
     * 今天和昨天所有停车场车位使用情况
     *
     * @return 结果
     */
    @GetMapping("todayAndYesterdayParkingSpaceHistory")
    public Object todayAndYesterdayParkingSpaceHistory() {
        return lytcService.todayAndYesterdayParkingSpaceHistory();
    }



}
