package net.cdsunrise.hy.lyyt.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.ConsumptionStatisticsReq;
import net.cdsunrise.hy.lyyt.entity.vo.TourismConsumptionAnalyzeCondition;
import net.cdsunrise.hy.lyyt.entity.vo.TouristPortraitReq;
import net.cdsunrise.hy.lyyt.entity.vo.TouristStatisticsRequest;
import net.cdsunrise.hy.lyyt.entity.vo.reids.ScenicAreaParkingSpaceVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.service.ITourismConsumptionAnalyzeService;
import net.cdsunrise.hy.lyyt.service.IndustrialMonitorService;
import net.cdsunrise.hy.lyyt.service.TouristPredictionService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import net.cdsunrise.hy.lyyt.utils.ResultUtil;
import net.cdsunrise.hy.lyyt.utils.SortUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author LHY
 * @date 2019/11/5 17:27
 */
@RestController
@RequestMapping("/industrialMonitor")
public class IndustrialMonitorController {

    private final IndustrialMonitorService industrialMonitorService;
    private final TouristPredictionService touristPredictionService;
    private final ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService;

    public IndustrialMonitorController(IndustrialMonitorService industrialMonitorService, TouristPredictionService touristPredictionService, ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService) {
        this.industrialMonitorService = industrialMonitorService;
        this.touristPredictionService = touristPredictionService;
        this.tourismConsumptionAnalyzeService = tourismConsumptionAnalyzeService;
    }

    /**
     * 洪雅旅游一张图 -> 景区游客分析（按月统计聚合）
     */
    @GetMapping("touristAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result touristAnalyze() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.touristAnalyze());
    }

    /**
     * 洪雅旅游一张图 -> 游客来源地TOP10（按月统计聚合）
     */
    @GetMapping("touristSourceCity")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result touristSourceCity() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.touristSourceCity());
    }

    /**
     * 产业运行监测 -> 游客来源地TOP10（统计昨天）
     */
    @GetMapping("latestTouristSourceCity")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result latestTouristSourceCity() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.latestTouristSourceCity());
    }

    /**
     * 产业运行监测 -> 游客人数监测（实时统计）
     */
    @GetMapping("touristMonitor")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.CYSSSJ})
    public Result touristMonitor() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.touristMonitor());
    }

    /**
     * 产业运行监测 -> 舆情关键词（实时统计）
     */
    @GetMapping("publicSentimentKeyword")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.CYSSSJ})
    public Result publicSentimentKeyword() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.publicSentimentKeyword());
    }

    /**
     * 节假日专题 -> 游客画像分析 -> 游客来源地TOP10（按节日统计）
     */
    @GetMapping("holidayTouristSourceCity")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holidayTouristSourceCity(HolidayTypeEnum holiday) {
        return ResultUtil.buildSuccessResultWithData(
                industrialMonitorService.holidayTouristSourceCity(holiday));
    }

    /**
     * 节假日专题 -> 游客画像分析 -> 年龄和性别（按节日统计）
     */
    @GetMapping("holidayTouristAgeSex")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holidayTouristAgeSex(HolidayTypeEnum holiday) {
        return ResultUtil.buildSuccessResultWithData(
                industrialMonitorService.holidayTouristAgeSex(holiday));
    }

    /**
     * 节假日专题 -> 景区游客接待分析（按节日统计）
     */
    @GetMapping("holidayScenicTourist")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holidayScenicTourist(HolidayTypeEnum holiday) {
        return ResultUtil.buildSuccessResultWithData(
                industrialMonitorService.holidayScenicTourist(holiday));
    }

    /**
     * 节假日专题 -> 获取去年和今年所有法定节假日
     */
    @GetMapping("holiday")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holiday() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.holiday());
    }

    /**
     * 洪雅旅游一张图 -> 酒店入住分析（年度数据统计）
     */
    @GetMapping("hotelAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result hotelAnalyze() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.hotelAnalyze());
    }

    /**
     * 洪雅旅游一张图 -> 酒店画像（省内省外游客）
     */
    @GetMapping("hotelTouristSource")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result hotelTouristSource() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.hotelTouristSource());
    }

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 过夜天数
     */
    @GetMapping("stayOvernight")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result stayOvernight() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.stayOvernight());
    }

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 游客年龄
     */
    @GetMapping("tourismAge")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result tourismAge() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.tourismAge());
    }

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 游客性别
     */
    @GetMapping("tourismSex")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result tourismSex() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.tourismSex());
    }

    /**
     * 洪雅旅游一张图 -> 县域旅游收入分析 -> 各景区旅游收入占比（景区即商圈）
     */
    @GetMapping("businessCircleTourismConsumption")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result businessCircleTourismConsumption() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.businessCircleTourismConsumption());
    }

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 消费行为分析（行业消费类型）
     */
    @GetMapping("industryConsumptionType")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result industryConsumptionType() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.industryConsumptionType());
    }

    /**
     * 洪雅旅游一张图 -> 游客画像分析 -> 客源地人均消费和年度旅游总收入
     */
    @GetMapping("perConsumptionCity")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result perConsumptionCity() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.perConsumptionCity());
    }

    /**
     * 产业运行监测 -> 住宿接待
     */
    @GetMapping("hotelPassengerReception")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result hotelPassengerReception() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.hotelPassengerReception());
    }

    /**
     * 产业运行监测 -> 旅游收入趋势（从有数据的日期开始，倒推2周）+ 上周收入总分析（从有数据的日期开始，倒推1周）
     */
    @GetMapping("tourismConsumptionTrend")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result tourismConsumptionTrend() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.tourismConsumptionTrend());
    }

    /**
     * 节假日专题 -> 旅游收入分析（按节日统计）
     */
    @GetMapping("holidayTourismConsumption")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holidayTourismConsumption(HolidayTypeEnum holiday) {
        return ResultUtil.buildSuccessResultWithData(
                industrialMonitorService.holidayTourismConsumption(holiday));
    }

    /**
     * 节假日专题 -> 住宿接待分析（按节日统计）
     */
    @GetMapping("holidayPassengerReception")
    @DataType({DataTypeEnum.CYJYSJ,DataTypeEnum.JJRZT})
    public Result holidayPassengerReception(HolidayTypeEnum holiday) {
        return ResultUtil.buildSuccessResultWithData(
                industrialMonitorService.holidayPassengerReception(holiday));
    }

    /**
     * 洪雅旅游一张图 -> 县域旅游收入分析 -> 县域收入分析（按月统计年度）
     */
    @GetMapping("tourismConsumptionAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result tourismConsumptionAnalyze() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.tourismConsumptionAnalyze());
    }

    /**
     * 旅游商户专题 -> 涉旅行业收入分析（今年和去年）
     */
    @GetMapping("industryConsumptionAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result industryConsumptionAnalyze() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.industryConsumptionAnalyze());
    }

    /**
     * 旅游商户专题 -> 各区域月度营收（默认查询当月）
     */
    @GetMapping("businessCircleConsumptionMonth")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result businessCircleConsumptionMonth() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.businessCircleConsumptionMonth());
    }

    /**
     * 旅游商户专题 -> 各区域年度累计收入和消费人次
     */
    @GetMapping("businessCircleConsumptionYear")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result businessCircleConsumptionYear() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.businessCircleConsumptionYear());
    }

    /**
     * 旅游商户专题 -> 本周商户营收走势（本周和上周）
     */
    @GetMapping("businessConsumptionTrend")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result businessConsumptionTrend() {
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.businessConsumptionTrend());
    }

    /**
     * 游客预测
     *
     * @param date 预测日期
     * @return 结果
     */
    @GetMapping("tourist/prediction")
    public Result touristPrediction(@RequestParam(required = false) String date) {
        LocalDate day;
        if (date == null) {
            day = LocalDate.now();
        } else {
            day = LocalDate.parse(date, DateUtil.LOCAL_DATE);
        }
        return ResultUtil.buildSuccessResultWithData(touristPredictionService.prediction(day));
    }

    /**
     * 旅游一张图-》游客画像分析-》消费画像-》消费来源地
     *
     * @return 结果
     */
    @GetMapping("consumption/getTourismConsumptionSourceAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result getTourismConsumptionSourceAnalyze() {
        TourismConsumptionAnalyzeCondition condition = new TourismConsumptionAnalyzeCondition();
        condition.setType(TourismConsumptionAnalyzeCondition.TypeEnum.MONTH);
        condition.setBeginDate(LocalDate.now().withMonth(1).withDayOfMonth(1));
        condition.setEndDate(LocalDate.now().withMonth(12).withDayOfMonth(31));
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionSourceAnalyze(condition));
    }

    /**
     * 旅游一张图-》游客画像分析-》消费画像-》消费业态
     *
     * @return 结果
     */
    @GetMapping("consumption/getTourismConsumptionIndustryAnalyze")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result getTourismConsumptionIndustryAnalyze() {
        TourismConsumptionAnalyzeCondition condition = new TourismConsumptionAnalyzeCondition();
        condition.setType(TourismConsumptionAnalyzeCondition.TypeEnum.MONTH);
        condition.setBeginDate(LocalDate.now().withMonth(1).withDayOfMonth(1));
        condition.setEndDate(LocalDate.now().withMonth(12).withDayOfMonth(31));
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.getTourismConsumptionIndustryAnalyze(condition));
    }

    /**
     * 旅游一张图-》游客画像分析-》消费画像-》消费商圈
     *
     * @return 结果
     */
    @GetMapping("consumption/consumptionStatistics")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result consumptionStatistics() {
        ConsumptionStatisticsReq req = new ConsumptionStatisticsReq();
        req.setBeginDate(LocalDate.now().withMonth(1).withDayOfMonth(1).atStartOfDay());
        req.setEndDate(LocalDate.now().withMonth(12).withDayOfMonth(31).atStartOfDay());
        return ResultUtil.buildSuccessResultWithData(tourismConsumptionAnalyzeService.consumptionStatistics(req));
    }

    /**
     * 旅游一张图-》游客画像分析-》游客画像-》来源地
     *
     * @return 结果
     */
    @GetMapping("tourist/touristSourceStatistics")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result touristSourceStatistics() {
        TouristStatisticsRequest req = new TouristStatisticsRequest();
        req.setType(TouristStatisticsRequest.TYPE_DAY);
        req.setBeginDate(LocalDate.now().withMonth(1).withDayOfMonth(1).atStartOfDay());
        req.setEndDate(LocalDate.now().withMonth(12).withDayOfMonth(31).atStartOfDay());
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.touristSourceStatistics(req));
    }

    /**
     * 旅游一张图-》游客画像分析-》游客画像-》景区热度
     *
     * @return 结果
     */
    @GetMapping("tourist/getScenicHeat")
    @DataType({DataTypeEnum.CYJYSJ})
    public Result getScenicHeat() {
        TouristPortraitReq req = new TouristPortraitReq();
        req.setBeginDate(LocalDate.now().withMonth(1).withDayOfMonth(1));
        req.setEndDate(LocalDate.now().withMonth(12).withDayOfMonth(31));
        return ResultUtil.buildSuccessResultWithData(industrialMonitorService.getScenicHeat(req));
    }
}
