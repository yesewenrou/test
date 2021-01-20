package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.AdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionSourceTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.ConsumptionTrendResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.IndustryConsumptionResp;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismConsumptionService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.HolidayTourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.ProvinceConsumptionCondition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * @author LHY
 * @date 2019/11/28 14:10
 */
@RestController
@RequestMapping("/tourismConsumption")
@Slf4j
public class TourismConsumptionController {

    private TourismConsumptionService tourismConsumptionService;
    private AdministrativeAreaFeignClient feignClient;
    private DataDictionaryFeignClient dataDictionaryFeignClient;

    public TourismConsumptionController(TourismConsumptionService tourismConsumptionService, AdministrativeAreaFeignClient feignClient, DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.tourismConsumptionService = tourismConsumptionService;
        this.feignClient = feignClient;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    /**
     * 旅游消费数据 -> 消费汇总
     */
    @Auth(value = "tourismConsumption:consumptionSummary")
    @PostMapping("consumptionSummary")
    public Result consumptionSummary(@Validated @RequestBody PageRequest<TourismConsumptionCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.consumptionSummary(
                Integer.parseInt(String.valueOf(pageRequest.getCurrent())), Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    /**
     * 旅游消费数据 -> 消费来源地
     */
    @Auth(value = "tourismConsumption:consumptionSource")
    @PostMapping("consumptionSource")
    public Result consumptionSource(@Validated @RequestBody PageRequest<TourismConsumptionCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.consumptionSource(
                Integer.parseInt(String.valueOf(pageRequest.getCurrent())), Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    /**
     * 旅游消费数据 -> 行业贡献
     */
    @Auth(value = "tourismConsumption:industryContribution")
    @PostMapping("industryContribution")
    public Result industryContribution(@Validated @RequestBody PageRequest<TourismConsumptionCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.industryContribution(
                Integer.parseInt(String.valueOf(pageRequest.getCurrent())), Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    /**
     * 旅游消费 -> 省内省外消费列表
     */
    @Auth(value = "tourismConsumption:provinceConsumptionList")
    @PostMapping("provinceConsumptionList")
    public Result provinceConsumptionList(@Validated @RequestBody ProvinceConsumptionCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.provinceConsumptionList(condition));
    }

    /**
     * 旅游消费数据 -> 消费来源地 -> 省份城市接口
     */
    @GetMapping("provinceList")
    public Result provinceList() {
        return feignClient.listProvincesWithoutCounty();
    }

    /**
     * 假日统计分析 -> 旅游收入 -> 节假日收入数据和走势图统计
     */
    @Auth(value = "tourismConsumption:statisticsData")
    @PostMapping("statisticsData")
    public Result statisticsData(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody HolidayTourismIncomeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.statisticsData(condition));
    }

    /**
     * 假日统计分析 -> 旅游收入 -> 按条件搜索分页
     */
    @Auth(value = "tourismConsumption:conditionStatisticsData")
    @PostMapping("conditionStatisticsData")
    public Result conditionStatisticsData(@Validated @RequestBody HolidayTourismIncomeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.conditionStatisticsData(condition));
    }

    /**
     * 游客画像分析 -> 游客消费类型占比
     */
    @Auth(value = "tourismConsumption:industryType")
    @GetMapping("industryType/{year}")
    public Result industryType(@PathVariable Integer year) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.industryType(year));
    }

    /**
     * 历史数据 -> 旅游收入带搜索条件查询统计结果
     */
    @Auth(value = "tourismConsumption:historyConditionStatisticsData")
    @PostMapping("historyConditionStatisticsData")
    public Result historyConditionStatisticsData(@Validated @RequestBody HolidayTourismIncomeCondition condition) {
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionService.historyConditionStatisticsData(condition));
    }

    /**
     * 商圈范围列表
     */
    @Auth(value = "tourismConsumption:businessCircleList")
    @GetMapping("businessCircleList")
    public Result businessCircleList() {
        String codeParam[] = {ParamConst.HONGYA_BUSINESS_CIRCLE};
        return ResultUtil.buildSuccessResultWithData(dataDictionaryFeignClient.getByCodes(codeParam).getData()
                .get(ParamConst.HONGYA_BUSINESS_CIRCLE).getChildren());
    }

    /**
     * 旅游消费数据,消费汇总
     */
    @PostMapping("/statistics")
    public List<ConsumptionStatisticsVO> consumptionStatistics(@Validated @RequestBody ConsumptionStatisticsReq req) {
        return tourismConsumptionService.consumptionStatistics(req);
    }

    /**
     * 消费趋势
     */
    @PostMapping("/trend")
    public ConsumptionTrendResp consumptionTrend(@RequestBody ConsumptionTrendReq req) {
        return tourismConsumptionService.consumptionTrend(req);
    }

    /**
     * 业态分布
     */
    @PostMapping("/industry")
    public IndustryConsumptionResp industryConsumption(@Validated @RequestBody IndustryConsumptionReq req) {
        return tourismConsumptionService.industryConsumption(req);
    }

    /**
     * 消费来源地
     */
    @PostMapping("/source")
    public ConsumptionSourceResp sourceConsumption(@Validated @RequestBody ConsumptionSourceReq req) {
        return tourismConsumptionService.sourceConsumption(req);
    }

    /**
     * 消费来源地消费趋势
     */
    @PostMapping("/source/trend")
    public List<ConsumptionSourceTrendResp> sourceConsumptionTrend(@Validated @RequestBody ConsumptionSourceTrendReq req) {
        return tourismConsumptionService.sourceConsumptionTrend(req);
    }
}
