package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelBaseInfo;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 * @date 2019/11/13 16:14
 */
public interface HotelResourceService {

    /**
     * 酒店资源列表
     *
     */
    PageResult<HotelBaseInfoVO> list(Integer page, Integer size, HotelCondition condition);

    /**
     * 酒店入住统计数据
     *
     */
    HotelStatisticsVO hotelStatistics();

    /**
     * 酒店入住 -> 实时入住详情合计
     *
     */
    HotelStatisticsVO hotelDetailStatistics(HotelCondition condition);

    /**
     * 酒店入住 -> 实时入住详情列表（后端分页版本）
     *
     */
    PageResult<HotelStatisticsVO> hotelDetailList(Integer page, Integer size, HotelCondition condition);

    /**
     * 酒店入住 -> 实时入住详情列表（前端分页版本，按照实时入住人数倒序）
     *
     */
    List<HotelStatisticsVO> hotelDetailListOrderByPeopleDesc(HotelCondition condition);

    /**
     * 酒店接待数据 -> 旅客接待量
     *
     */
    Map<String,Object> hotelPassengerReceptionList(HotelCondition condition);

    /**
     * 酒店接待数据 -> 旅客来源地
     *
     */
    PageResult<HotelTouristSourceVO> hotelTouristSourceList(Integer page, Integer size, HotelCondition condition);

    /**
     * 决策分析 -> 游客画像分析 -> 游客过夜留宿占比
     *
     */
    List<ChartVO> stayOvernight(Integer year,Integer scope);

    /**
     * 决策分析 -> 游客画像分析 -> 游客年龄占比
     *
     */
    List<ChartVO> tourismAge(Integer year,Integer scope);

    /**
     * 根据酒店区域，转换所属商圈
     *
     * */
    void transferAreaToBusinessCircle();

    /**
     * 酒店详情
     *
     * */
    HotelBaseInfo detail(String stationId);

    /**
     * 利用scroll滚历史酒店入住退房记录
     *
     * */
    void parseHotel();

    /**
     * 根据酒店入住退房记录，生成每日酒店接待数据
     *
     * */
    void generateHotelReception(String beginDate,String endDate);

    /**
     * 根据酒店入住退房记录，生成每日酒店游客来源地数据
     *
     * */
    void generateHotelTouristSource(String beginDate,String endDate);

    /**
     * 根据hotel_statistics.json，生成酒店每日库存基准数据
     */
    void generateHotelStatistics();

    /**
     * 根据酒店入住退房记录，生成酒店每日接待数据（按照新统计逻辑索引：hotel_passenger_reception_plus）
     *
     * */
    void generateHotelReceptionPlus(String beginDate,String endDate);
}
