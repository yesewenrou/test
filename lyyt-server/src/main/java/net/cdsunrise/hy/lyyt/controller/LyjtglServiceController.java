package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.domain.vo.*;
import net.cdsunrise.hy.lyyt.entity.resp.ForecastValueResponse;
import net.cdsunrise.hy.lyyt.entity.vo.MonitorCarCountVo;
import net.cdsunrise.hy.lyyt.entity.vo.RoadJamTimeLengthVo;
import net.cdsunrise.hy.lyyt.entity.vo.RoadSectionTpiForecastVO;
import net.cdsunrise.hy.lyyt.entity.vo.TrafficAnalyzeVo;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.enums.ImportantRoadSection;
import net.cdsunrise.hy.lyyt.service.LyjtglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YQ on 2019/11/5.
 */
@RestController
@RequestMapping("/lyjtgl")
public class LyjtglServiceController {
    private final LyjtglService lyjtglService;

    @Autowired
    public LyjtglServiceController(LyjtglService lyjtglService) {
        this.lyjtglService = lyjtglService;
    }

    /**
     * 交通态势 reconstructed
     */
    @GetMapping("/traffic/whole/situation")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public TrafficWholeSituationVo trafficWholeSituation() {
        return lyjtglService.trafficWholeSituation();
    }

    /**
     * 车辆来源top 10 reconstructed
     */
    @GetMapping("/car/source/top/ten")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public CarSourceTopTenVo carSourceTopTen() {
        return lyjtglService.carSourceTopTen();
    }
    /**
     * 车流量 reconstructed
     */
    @GetMapping("/car/cross/count")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public CrossCarCountVo crossCarCount() {
        return lyjtglService.crossCarCount();
    }

    /**
     * 重点路段
     * reconstructed
     * @return 重点路段
     */
    @GetMapping("/important/road")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public List<ImportantRoadSection.ImportantRoadSectionClazz> importantRoad() {
        return lyjtglService.importantRoad();
    }

    /**
     * 重点路段某卡口今天和昨天的车流量 按小时分片
     * reconstructed
     */
    @GetMapping("/important/road/histogram/{monitorCode}")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public ImportantHistogramVo importantHistogram(@PathVariable String monitorCode) {
        return lyjtglService.importantHistogram(monitorCode);
    }

    /**
     * reconstructed
     */
    @GetMapping("/car/source/ratio")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public CarSourceRatioVo carSourceRatio() {
        return lyjtglService.carSourceRatio();
    }


    /**
     * 拥堵道路监测 reconstructed
     */
    @GetMapping("/often/jam/road")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public RoadDetailMonitorVo oftenJamRoad() {
        return lyjtglService.oftenJamRoad();
    }

    /**
     * 重点旅游路段监测 reconstructed
     */
    @GetMapping("/road/monitor/important")
    @DataType({DataTypeEnum.LYJTZYSJ})
    private List<RoadSectionMonitorSiteVo> roadMonitorImportan() {
        return lyjtglService.roadMonitorImportant();
    }

    /**
     * 重点旅游路段监测 reconstructed
     */
    @GetMapping("/road/monitor")
    @DataType({DataTypeEnum.LYJTZYSJ})
    private List<RoadDetailVo> roadMonitor() {
        return lyjtglService.roadMonitor();
    }

    /**
     * 常发拥堵时间,一个月 cancel construct
     */
    @GetMapping("/often/jam/time")
    @DataType({DataTypeEnum.LYJTZYSJ})
    private List<OftenJamTimeVo> oftenJamTime() {
        return lyjtglService.oftenJamTime();
    }

    /**
     * 常发拥堵时间,一天 cancel construct
     */
    @GetMapping("/often/jam/time/day")
    @DataType({DataTypeEnum.LYJTZYSJ})
    private List<OftenJamTimeVo> oftenJamTimeDay() {
        return lyjtglService.oftenJamTimeDay();
    }

    /**
     * 获取所有卡口实时车流量 reconstructed
     */
    @GetMapping("/realTime/monitor")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public List<MonitorRealTimeData> getMonitorRealTimeData() {
        return lyjtglService.getMonitorRealTimeData();
    }

    /**
     * 旅游干道拥堵当月排行 reconstructed
     */
    @GetMapping("/roadSeg/jam/rank")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public List<RoadJamTimeLengthVo> roadSegJamRank() {
        return lyjtglService.roadSegJamRank();
    }

    /**
     * 当日拥堵次数 reconstructed
     */
    @GetMapping("/jam/count/today")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public Integer jamCountToday() {
        return lyjtglService.jamCountToday();
    }

    /**
     * 当日拥堵路段条数 reconstructed
     */
    @GetMapping("/jam/road/count/today")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public JamRoadCountVo jamRoadCountToday() {
        return lyjtglService.jamRoadCountToday();
    }

    /**
     * 监控点列表 reconstructed
     */
    @GetMapping("/monitor/list")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public List<MonitorDetailVo> monitorList() {
        return lyjtglService.monitorList();
    }


    /**
     * 当月拥堵道路时长排行 reconstructed
     */
    @GetMapping("/road/jam/time/rank/month")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public List<RoadDetailVo> roadJamTimeRankMonth() {
        return lyjtglService.roadJamTimeRankMonth();
    }

    /**
     * 重要卡口车流量 reconstructed
     */
    @GetMapping("/monitor/car/count")
    @DataType({DataTypeEnum.LYJTZYSJ})
    public MonitorCarCountVo monitorCarCount() {
        return lyjtglService.monitorCarCount();
    }


    /**
     * 节假日专题 旅游交通分析
     * TODO YQ
     */
    @GetMapping("/traffic/analyze")
    @DataType({DataTypeEnum.LYJTZYSJ,DataTypeEnum.JJRZT})
    public TrafficAnalyzeVo trafficAnalyze(HolidayTypeEnum holiday) {
        return lyjtglService.trafficAnalyze(holiday);
    }


    @GetMapping("/forecast/flow/incity")
    public List<ForecastValueResponse<Integer>> getInCityForecastFlow(){
        return lyjtglService.currentInCityFlowForecast();
    }


    @GetMapping("/forecast/road/jam")
    public List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> getForecastJameRoad(@RequestParam (value = "tpi", required = false) Double tpi){
        return lyjtglService.currentJamRoleSectionForecast(tpi);
    }

}
