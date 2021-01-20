package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.HotelTrendReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristPortraitVO;

import java.time.LocalDate;

/**
 * @author LHY
 * @date 2020/5/9 16:42
 */
public interface HotelReportService {

    /**
     * 酒店入住
     */
    HotelTrendReportVO hotelRecption(LocalDate beginDate,LocalDate endDate);

    /**
     * 旅客画像
     */
    TouristPortraitVO touristPortrait(LocalDate beginDate,LocalDate endDate);
}
