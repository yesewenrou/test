package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.MonitorSiteListVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.PneumoniaForeignPeopleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ProvinceCityVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author LHY
 */
@FeignClient(name = "hy-lyjtgl-lyjtgl-server", url = "${lyjt.service.lyjtServerAddress}")
public interface TrafficFeignService {

    @GetMapping(value = "/industry/graph")
    Result<ExternalGraphVO> getGraph(@RequestParam("nowBeginTime") Long nowBeginTime, @RequestParam("nowEndTime") Long nowEndTime,
                                     @RequestParam("lastBeginTime") Long lastBeginTime, @RequestParam("lastEndTime") Long lastEndTime);

    @GetMapping(value = "/industry/table")
    Result<List<ExternalTableVO>> getTable(@RequestParam("beginTime") Long beginTime, @RequestParam("endTime") Long endTime,
                                           @RequestParam("provinceName") String provinceName,
                                           @RequestParam("cityName") String cityName);

    @GetMapping(value = "industry/province")
    Result<List<String>> getProvinces();

    @GetMapping(value = "industry/city")
    Result<List<String>> getCities(@RequestParam("provinceName") String provinceName);

    @GetMapping(value = "industry/graininess")
    Result<IndustryRunGraininessVO> getByGraininess(@RequestParam("beginTime") Long beginTime, @RequestParam("endTime") Long endTime,
                                                    @RequestParam("graininess") Integer graininess,
                                                    @RequestParam("provinceName") String provinceName,
                                                    @RequestParam("cityName") String cityName);

    /**
     * 获取路段实时拥堵排行
     */
    @GetMapping(value = "industry/top")
    Result<List<RoadSectionInfoDetailVO>> getTop();

    /**
     * 获取入城车流量监测
     */
    @GetMapping(value = "industry/monitor")
    Result<InputFlowMonitorVO> getMonitor();

    /**
     * 获取当天入城车辆来源地流量统计数据
     */
    @GetMapping(value = "industry/source")
    Result<IndustryCarSourceVO> getSource();

    /**
     * 疫情检车模块 - 查询外来车辆
     *
     * @param statisticsBeginTime      开始时间
     * @param statisticsEndTime        结束时间
     * @param provinceName             省
     * @param cityName                 城市名
     * @param statisticsGraininessEnum 类型 DAY / MONTH
     * @return Result
     */
    @GetMapping(value = "/industry/epidemic")
    Result<PneumoniaForeignPeopleVO.FeignCarVO> industryEpidemic(@RequestParam("statisticsBeginTime") Long statisticsBeginTime,
                                                                 @RequestParam("statisticsEndTime") Long statisticsEndTime,
                                                                 @RequestParam("provinceName") String provinceName,
                                                                 @RequestParam("cityName") String cityName,
                                                                 @RequestParam("statisticsGraininessEnum") String statisticsGraininessEnum);

    /**
     * 疫情分析-外来车辆省市树状图
     *
     * @return Result
     */
    @GetMapping(value = "/industry/location")
    Result industryLocations();

    /**
     * 进县域车流监测
     */
    @GetMapping(value = "/industry/monitorSite")
    Result<MonitorSiteListVO> getMonitorSite();

    /**
     * 获取省份城市包含结构数据
     *
     * @param statisticsObject    统计对象
     * @param statisticsBeginTime 统计开始时间
     * @param statisticsEndTime   统计结束时间
     * @return 份城市包含结构数据
     */
    @GetMapping(value = "/industry/provinceCity")
    Result<ProvinceCityVO> getProvinceCity(@RequestParam("statisticsObject") String statisticsObject,
                                           @RequestParam("statisticsBeginTime") Long statisticsBeginTime,
                                           @RequestParam("statisticsEndTime") Long statisticsEndTime);
}
