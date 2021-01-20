package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.Result;

/**
 * @author fang yun long
 * @date 2020-03-03
 */
public interface IPneumaticService {

    /**
     * 查询外来人员
     * @param city 城市
     * @param dateType 日期类型  day month
     * @param begin 开始日期
     * @param end 结束日期
     * @return Result
     */
    Result peopleFromForeign(String city, String dateType, Long begin, Long end);

    /**
     * 查询外来车辆
     * @param province 省
     * @param city 城市
     * @param dateType 日期类型  day month
     * @param begin 开始日期
     * @param end 结束日期
     * @return Result
     */
    Result carFromForeign(String province, String city, String dateType, Long begin, Long end);

    /**
     * 疫情分析- 外来车辆 省市树状图
     * @return Result
     */
    Result carCities();

}
