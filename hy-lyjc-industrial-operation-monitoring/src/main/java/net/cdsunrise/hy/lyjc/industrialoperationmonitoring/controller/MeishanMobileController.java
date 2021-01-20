package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IMeishanMobileService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/9/26 10:38
 */
@RestController
@RequestMapping("meishan-mobile")
public class MeishanMobileController {

    private IMeishanMobileService meishanMobileService;

    public MeishanMobileController(IMeishanMobileService meishanMobileService) {
        this.meishanMobileService = meishanMobileService;
    }

    /**
     * 返回眉山市洪雅县当前人流总数、游客人数和常驻人数
     *
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMinuteLocalData", method = RequestMethod.GET)
    public Result getMsHyMinuteLocalData() {
        MeishanMobileResult<List<MsHyLocalData>> meishanMobileResult = meishanMobileService.getMsHyMinuteLocalData();
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按小时指定查询时间，反馈眉山市洪雅县小时客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyHourLocalData", method = RequestMethod.GET)
    public Result getMsHyHourLocalData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHyLocalData>> meishanMobileResult = meishanMobileService.getMsHyHourLocalData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按天指定查询时间，反馈眉山市洪雅县日客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyDayLocalData", method = RequestMethod.GET)
    public Result getMsHyDayLocalData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHyLocalData>> meishanMobileResult = meishanMobileService.getMsHyDayLocalData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按月指定查询时间，反馈眉山市洪雅县月客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMonthLocalData", method = RequestMethod.GET)
    public Result getMsHyMonthLocalData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHyLocalData>> meishanMobileResult = meishanMobileService.getMsHyMonthLocalData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各省来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyDaySourceProvData", method = RequestMethod.GET)
    public Result getMsHyDaySourceProvData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceProvData>> meishanMobileResult = meishanMobileService.getMsHyDaySourceProvData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各省来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMonthSourceProvData", method = RequestMethod.GET)
    public Result getMsHyMonthSourceProvData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceProvData>> meishanMobileResult = meishanMobileService.getMsHyMonthSourceProvData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各地市来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyDaySourceCityData", method = RequestMethod.GET)
    public Result getMsHyDaySourceCityData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCityData>> meishanMobileResult = meishanMobileService.getMsHyDaySourceCityData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各地市来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMonthSourceCityData", method = RequestMethod.GET)
    public Result getMsHyMonthSourceCityData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCityData>> meishanMobileResult = meishanMobileService.getMsHyMonthSourceCityData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按天指定查询时间，反馈眉山市洪雅县市内来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyDaySourceCountyData", method = RequestMethod.GET)
    public Result getMsHyDaySourceCountyData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCountyData>> meishanMobileResult = meishanMobileService.getMsHyDaySourceCountyData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按月指定查询时间，反馈眉山市洪雅县市内来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMonthSourceCountyData", method = RequestMethod.GET)
    public Result getMsHyMonthSourceCountyData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCountyData>> meishanMobileResult = meishanMobileService.getMsHyMonthSourceCountyData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国际来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyDaySourceCountryData", method = RequestMethod.GET)
    public Result getMsHyDaySourceCountryData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCountryData>> meishanMobileResult = meishanMobileService.getMsHyDaySourceCountryData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国际来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMonthSourceCountryData", method = RequestMethod.GET)
    public Result getMsHyMonthSourceCountryData(@RequestParam("queryTime") String queryTime) {
        MeishanMobileResult<List<MsHySourceCountryData>> meishanMobileResult = meishanMobileService.getMsHyMonthSourceCountryData(queryTime);
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }

    /**
     * 返回眉山市洪雅县当前人流热力图
     *
     * @return 结果
     */
    @RequestMapping(value = "getMsHyMinutePeopleHotData", method = RequestMethod.GET)
    public Result getMsHyMinutePeopleHotData() {
        MeishanMobileResult<List<MsHyPeopleHotData>> meishanMobileResult = meishanMobileService.getMsHyMinutePeopleHotData();
        return ResultUtil.buildSuccessResultWithData(meishanMobileResult);
    }
}
