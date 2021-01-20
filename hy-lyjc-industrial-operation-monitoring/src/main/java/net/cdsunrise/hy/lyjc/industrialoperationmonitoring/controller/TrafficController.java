package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.MonitorSiteListVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismTrafficService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign.TrafficFeignService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/traffic")
@Slf4j
public class TrafficController {

    @Autowired
    private TrafficFeignService trafficFeignService;
    @Autowired
    private TourismTrafficService tourismTrafficService;

    /**
     * 决策分析 -> 假日统计分析 -> 旅游交通 -> 节假日接待人数和走势图统计
     *
     * */
    @Auth(value = "traffic:graph")
    @GetMapping("graph")
    public Result graph(@RequestParam Long nowBeginTime, @RequestParam Long nowEndTime,
                        @RequestParam Long lastBeginTime, @RequestParam Long lastEndTime) {
        return trafficFeignService.getGraph(nowBeginTime, nowEndTime, lastBeginTime, lastEndTime);
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游交通 -> 按条件搜索分页
     *
     * */
    @Auth(value = "traffic:table")
    @GetMapping("table")
    public Result table(@RequestParam Long beginTime,
                        @RequestParam Long endTime,
                        @RequestParam(required = false) String provinceName,
                        @RequestParam(required = false) String cityName) {
        return trafficFeignService.getTable(beginTime, endTime, provinceName, cityName);
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游交通 -> 省份列表
     *
     * */
    @Auth("traffic:province")
    @GetMapping("province")
    public Result province() {
        return trafficFeignService.getProvinces();
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游交通 -> 城市列表
     *
     * */
    @Auth("traffic:city")
    @GetMapping("city")
    public Result city(@RequestParam String provinceName){
        return trafficFeignService.getCities(provinceName);
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游交通 -> 导出报表
     *
     * */
    @Auth(value = "traffic:export")
    @GetMapping("export")
    public ResponseEntity<byte[]> export(@RequestParam Long beginTime,
                                         @RequestParam Long endTime,
                                         @RequestParam(required = false) String provinceName,
                                         @RequestParam(required = false) String cityName) {
        return tourismTrafficService.export(beginTime, endTime, provinceName, cityName);
    }

    /**
     * 决策分析 -> 历史数据统计 -> 进城车辆 -> 按条件搜索分页
     *
     * */
    @Auth("traffic:historyByGraininess")
    @GetMapping("historyByGraininess")
    public Result historyByGraininess(@RequestParam Long beginTime,
                                  @RequestParam Long endTime,
                                  @RequestParam Integer graininess,
                                  @RequestParam(required = false) String provinceName,
                                  @RequestParam(required = false) String cityName){
        return trafficFeignService.getByGraininess(beginTime, endTime, graininess, provinceName, cityName);
    }

    /**
     * 决策分析 -> 历史数据统计 -> 进城车辆 -> 导出报表
     *
     * */
    @Auth(value = "traffic:historyExport")
    @GetMapping("historyExport")
    public ResponseEntity<byte[]> historyExport(@RequestParam Long beginTime,
                                                @RequestParam Long endTime,
                                                @RequestParam Integer graininess,
                                                @RequestParam(required = false) String provinceName,
                                                @RequestParam(required = false) String cityName){
        return tourismTrafficService.historyExport(beginTime, endTime, graininess, provinceName, cityName);
    }

    /**
     * 运行监测 -> 交通停车 -> 获取路段实时拥堵排行
     *
     * */
    @Auth("traffic:top")
    @GetMapping("top")
    public Result top() {
        return trafficFeignService.getTop();
    }

    /**
     * 运行监测 -> 交通停车 -> 获取入城车流量监测
     *
     * */
    @Auth("traffic:monitor")
    @GetMapping("monitor")
    public Result monitor() {
        return trafficFeignService.getMonitor();
    }

    /**
     * 运行监测 -> 交通停车 -> 获取当天入城车辆来源地流量统计数据
     *
     * */
    @Auth("traffic:source")
    @GetMapping("source")
    public Result source() {
        return trafficFeignService.getSource();
    }

    /**
     * 运行监测 -> 交通停车 -> 获取入城车流量监测
     *
     * */
    @Auth("traffic:monitor")
    @GetMapping("monitorSite")
    public Result<MonitorSiteListVO> monitorSite() {
        return trafficFeignService.getMonitorSite();
    }

    /**
     * 运行监测 -> 交通停车 -> 获取入城车流量监测
     *
     * */
    @GetMapping("provinceCity")
    @Auth("traffic::provinceCity")
    public Result getProvinceCity(@RequestParam String statisticsObject, @RequestParam Long statisticsBeginTime,
                                  @RequestParam Long statisticsEndTime) {
        // 因为前端页面是限制到天，所以这里对开始时间和结束时间做规整
        // 开始时间为每日的开始，结束时间为"结束时间对应日期的下一日的开始"，因为从旅游交通管理的源代码来看，是不包含结束时间的
        final LocalDateTime beginDateTime = DateUtil.longToLocalDateTime(statisticsBeginTime);
        final LocalDateTime endDateTime = DateUtil.longToLocalDateTime(statisticsEndTime);
        Long begin = DateUtil.dateTimeToLong(beginDateTime.toLocalDate().atStartOfDay());
        Long end = DateUtil.dateTimeToLong(endDateTime.plusDays(1).toLocalDate().atStartOfDay());


        return trafficFeignService.getProvinceCity(statisticsObject,begin,end);
    }
}
