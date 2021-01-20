package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.HolidayFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.MsmAdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.HolidayVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionCompareResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionStatisticsResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristLocalDataAppVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DataResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristHeatMapService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DataResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SourceCityTopVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/dataResource")
@Slf4j
public class DataResourceController {

    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private HolidayFeignClient holidayFeignClient;
    @Autowired
    private DataDictionaryFeignClient dataDictionaryFeignClient;
    @Autowired
    private MsmAdministrativeAreaFeignClient msmAdministrativeAreaFeignClient;

    @Autowired
    private ITouristHeatMapService iTouristHeatMapService;

    /**
     * 资源管理 -> 数据资源 -> 列表
     *
     * */
    @Auth(value = "dataResource:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest<DataResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(dataResourceService.list(pageRequest));
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游数据 -> 节假日接待人数和走势图统计
     *
     * */
    @Auth(value = "dataResource:statisticsData")
    @PostMapping("statisticsData")
    public Result statisticsData(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody DataResourceCondition condition) {
        return ResultUtil.buildSuccessResultWithData(dataResourceService.statisticsData(condition));
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游数据 -> 按条件搜索分页
     *
     * */
    @Auth(value = "dataResource:conditionStatisticsData")
    @PostMapping("conditionStatisticsData")
    public Result conditionStatisticsData(@Validated @RequestBody PageRequest<DataResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(dataResourceService.conditionStatisticsData(pageRequest));
    }

    /**
     * 决策分析 -> 历史数据统计 -> 游客接待数 -> 按条件搜索分页
     *
     * */
    @Auth(value = "dataResource:historyConditionStatisticsData")
    @PostMapping("historyConditionStatisticsData")
    public Result historyConditionStatisticsData(@Validated @RequestBody DataResourceCondition condition) {
        return ResultUtil.buildSuccessResultWithData(dataResourceService.historyConditionStatisticsData(condition));
    }

    /**
     * 决策分析 -> 假日统计分析 -> 旅游数据 -> 导出报表
     *
     * */
    @Auth(value = "dataResource:export")
    @PostMapping("export")
    public ResponseEntity<byte[]> export(@Validated @RequestBody PageRequest<DataResourceCondition> pageRequest) {
        return dataResourceService.export(pageRequest);
    }

    /**
     * 决策分析 -> 历史数据统计 -> 游客接待数 -> 导出报表
     *
     * */
    @Auth(value = "dataResource:historyExport")
    @PostMapping("historyExport")
    public ResponseEntity<byte[]> historyExport(@Validated @RequestBody PageRequest<DataResourceCondition> pageRequest) {
        return dataResourceService.historyExport(pageRequest);
    }

    /**
     * 资源管理 -> 数据资源 -> 景区列表
     *
     * */
    @Auth(value = "dataResource:scenicList")
    @GetMapping("scenicList")
    public Result scenicList(){
        String[] codeParam = {ParamConst.SCENIC_AREA};
        return ResultUtil.buildSuccessResultWithData(dataDictionaryFeignClient.getByCodes(codeParam).getData()
                .get(ParamConst.SCENIC_AREA).getChildren());
    }

    /**
     * 资源管理 -> 数据资源 -> 获取去年和今年节假日
     *
     * */
    @SuppressWarnings(value = "all")
    @Auth(value = "dataResource:holiday")
    @GetMapping("holiday/{year}")
    public Result holiday(@PathVariable Integer year){
        Integer lastYear = year-1;
        Map resultMap = new HashMap();
        List<HolidayVO> holidayList = holidayFeignClient.queryByYear(year).getData();
        resultMap.put(year,holidayList);
        List<HolidayVO> lastHolidayList = holidayFeignClient.queryByYear(lastYear).getData();
        resultMap.put(lastYear,lastHolidayList);
        return ResultUtil.buildSuccessResultWithData(resultMap);
    }

    /**
     * 资源管理 -> 数据资源 -> 省份列表
     *
     * */
    @GetMapping("provinceList")
    public Result provinceList() {
        return msmAdministrativeAreaFeignClient.listProvinces();
    }

    /**
     * 决策分析 -> 游客画像统计 -> 游客来源地TOP10
     *
     * */
    @Auth(value = "dataResource:sourceCityTop")
    @PostMapping("sourceCityTop")
    public Result sourceCityTop(@Validated @RequestBody SourceCityTopVO sourceCityTopVO){
        return ResultUtil.buildSuccessResultWithData(dataResourceService.sourceCityTop(sourceCityTopVO.getYear(),sourceCityTopVO.getScope()));
    }

    /**
     * 游客来源地TOP10按天统计
     *
     * */
    @GetMapping("sourceCityTopByDay")
    public Result sourceCityTopByDay() {
        return ResultUtil.buildSuccessResultWithData(dataResourceService.sourceCityTopByDay());
    }


    @GetMapping("/tourist/region/statistics")
    public Result<TouristRegionStatisticsResponse> touristRegionStatistics(@RequestParam("beginDate") Long beginDate, @RequestParam("endDate") Long endDate, @RequestParam("touristsScopeCode")String touristsScopeCode){
        final TouristRegionStatisticsResponse response = dataResourceService.touristRegionStatistics(beginDate, endDate, touristsScopeCode);
        return ResultUtil.buildSuccessResultWithData(response);
    }

    @GetMapping("/tourist/region/compare")
    public Result<TouristRegionCompareResponse> touristRegionCompare(@RequestParam("beginDate") Long beginDate,
                                                                     @RequestParam("endDate") Long endDate,
                                                                     @RequestParam("touristsScopeCode")String touristsScopeCode,
                                                                     @RequestParam("fullName")String fullName){
        final TouristRegionCompareResponse response = dataResourceService.touristRegionCompareStatistics(beginDate, endDate, touristsScopeCode, fullName);
        return ResultUtil.buildSuccessResultWithData(response);
    }

    @GetMapping("touristStatisticsForApp")
    public TouristLocalDataAppVO touristStatisticsForApp() {
        return iTouristHeatMapService.touristStatisticsForApp();
    }
}
