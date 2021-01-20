package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm.*;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/9/26 11:19
 */
public interface IMeishanMobileService {

    /**
     * 返回眉山市洪雅县当前人流总数、游客人数和常驻人数
     *
     * @return 结果
     */
    MeishanMobileResult<List<MsHyLocalData>> getMsHyMinuteLocalData();

    /**
     * 按小时指定查询时间，反馈眉山市洪雅县小时客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHyLocalData>> getMsHyHourLocalData(String queryTime);

    /**
     * 按天指定查询时间，反馈眉山市洪雅县日客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHyLocalData>> getMsHyDayLocalData(String queryTime);

    /**
     * 按月指定查询时间，反馈眉山市洪雅县月客流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHyLocalData>> getMsHyMonthLocalData(String queryTime);

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各省来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceProvData>> getMsHyDaySourceProvData(String queryTime);

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各省来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceProvData>> getMsHyMonthSourceProvData(String queryTime);

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各地市来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCityData>> getMsHyDaySourceCityData(String queryTime);

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各地市来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCityData>> getMsHyMonthSourceCityData(String queryTime);

    /**
     * 按天指定查询时间，反馈眉山市洪雅县市内来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCountyData>> getMsHyDaySourceCountyData(String queryTime);

    /**
     * 按月指定查询时间，反馈眉山市洪雅县市内来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCountyData>> getMsHyMonthSourceCountyData(String queryTime);

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国际来源地日人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCountryData>> getMsHyDaySourceCountryData(String queryTime);

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国际来源地月人流数据
     *
     * @param queryTime 查询时间
     * @return 结果
     */
    MeishanMobileResult<List<MsHySourceCountryData>> getMsHyMonthSourceCountryData(String queryTime);

    /**
     * 返回眉山市洪雅县当前人流热力图
     *
     * @return 结果
     */
    MeishanMobileResult<List<MsHyPeopleHotData>> getMsHyMinutePeopleHotData();
}
