package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.IdCardAdministrativeAreaFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelBaseInfo;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HotelResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SourceCityTopVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.HotelCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.HotelStatisticsVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHY
 * @date 2019/11/13 16:12
 */
@RestController
@RequestMapping("/hotelResource")
@Slf4j
public class HotelResourceController {

    private HotelResourceService hotelResourceService;
    private IdCardAdministrativeAreaFeignClient feignClient;

    public HotelResourceController(HotelResourceService hotelResourceService, IdCardAdministrativeAreaFeignClient feignClient) {
        this.hotelResourceService = hotelResourceService;
        this.feignClient = feignClient;
    }

    @Auth(value = "hotelResource:list")
    @PostMapping("list")
    public Result list(@Validated @RequestBody PageRequest<HotelCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.list(Integer.parseInt(String.valueOf(pageRequest.getCurrent())),
                Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    @Auth(value = "hotelResource:hotelStatistics")
    @GetMapping("hotelStatistics")
    public Result hotelStatistics() {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.hotelStatistics());
    }

    @Auth(value = "hotelResource:hotelDetailList")
    @PostMapping("hotelDetailList")
    public Result hotelDetailList(@Validated @RequestBody PageRequest<HotelCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.hotelDetailList(Integer.parseInt(String.valueOf(pageRequest.getCurrent())),
                Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    /**
     * 酒店入住 -> 实时入住详情列表（前端分页版本，按照实时入住人数倒序）
     */
    @PostMapping("hotelDetailListOrderByPeopleDesc")
    public Result hotelDetailListOrderByPeopleDesc(@RequestBody HotelCondition condition) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.hotelDetailListOrderByPeopleDesc(condition));
    }

    @Auth(value = "hotelResource:hotelPassengerReceptionList")
    @PostMapping("hotelPassengerReceptionList")
    public Result hotelPassengerReceptionList(@Validated @RequestBody HotelCondition condition) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.hotelPassengerReceptionList(condition));
    }

    @Auth(value = "hotelResource:hotelTouristSourceList")
    @PostMapping("hotelTouristSourceList")
    public Result hotelTouristSourceList(@Validated @RequestBody PageRequest<HotelCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.hotelTouristSourceList(Integer.parseInt(String.valueOf(pageRequest.getCurrent())),
                Integer.parseInt(String.valueOf(pageRequest.getSize())), pageRequest.getCondition()));
    }

    @Auth(value = "hotelResource:stayOvernight")
    @PostMapping("stayOvernight")
    public Result stayOvernight(@Validated @RequestBody SourceCityTopVO sourceCityTopVO) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.stayOvernight(sourceCityTopVO.getYear(), sourceCityTopVO.getScope()));
    }

    @Auth(value = "hotelResource:tourismAge")
    @PostMapping("tourismAge")
    public Result tourismAge(@Validated @RequestBody SourceCityTopVO sourceCityTopVO) {
        return ResultUtil.buildSuccessResultWithData(hotelResourceService.tourismAge(sourceCityTopVO.getYear(), sourceCityTopVO.getScope()));
    }

    /**
     * 酒店省份列表
     */
    @Auth(value = "hotelResource:provinceList")
    @GetMapping("provinceList")
    public Result provinceList() {
        return feignClient.listProvincesWithoutCounty();
    }

    /**
     * 根据酒店区域，转换所属商圈
     */
    @GetMapping("transfer")
    public void transferAreaToBusinessCircle() {
        hotelResourceService.transferAreaToBusinessCircle();
    }

    /**
     * 酒店入住 -> 实时入住详情合计
     */
    @PostMapping("hotelDetailStatistics")
    public HotelStatisticsVO hotelDetailStatistics(@RequestBody HotelCondition condition) {
        return hotelResourceService.hotelDetailStatistics(condition);
    }

    /**
     * 酒店详情
     */
    @GetMapping("detail")
    public HotelBaseInfo detail(@RequestParam String stationId) {
        return hotelResourceService.detail(stationId);
    }

    /**
     * 利用scroll滚历史酒店入住退房记录
     */
    @GetMapping("parseHotel")
    public void parseHotel() {
        hotelResourceService.parseHotel();
    }

    /**
     * 根据酒店入住退房记录，生成酒店每日接待数据
     */
    @GetMapping("generateHotelReception")
    public void generateHotelReception(@RequestParam String beginDate, @RequestParam String endDate) {
        hotelResourceService.generateHotelReception(beginDate, endDate);
    }

    /**
     * 根据酒店入住退房记录，生成酒店每日接待数据
     */
    @GetMapping("generateHotelTouristSource")
    public void generateHotelTouristSource(@RequestParam String beginDate, @RequestParam String endDate) {
        hotelResourceService.generateHotelTouristSource(beginDate, endDate);
    }

    /**
     * 根据hotel-statistics.json，生成酒店每日库存基准数据
     */
    @GetMapping("generateHotelStatistics")
    public void generateHotelStatistics() {
        hotelResourceService.generateHotelStatistics();
    }

    /**
     * 根据酒店入住退房记录，生成酒店每日接待数据（按照新统计逻辑索引：hotel_passenger_reception_plus）
     */
    @GetMapping("generateHotelReceptionPlus")
    public void generateHotelReceptionPlus(@RequestParam String beginDate, @RequestParam String endDate) {
        hotelResourceService.generateHotelReceptionPlus(beginDate, endDate);
    }
}
