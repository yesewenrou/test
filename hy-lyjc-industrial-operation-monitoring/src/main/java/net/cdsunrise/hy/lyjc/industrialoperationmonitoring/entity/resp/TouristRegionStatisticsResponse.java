package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristRegionStatisticsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * TouristRegionStatisticsResponse
 *
 * @author LiuYin
 * @date 2020/3/26 11:48
 */
@Data
public class TouristRegionStatisticsResponse {

    /** 客流合计*/
    private Long total;
    /** 数据来源*/
    private String from;
    /** 客流区域统计列表*/
    private List<TouristRegionStatisticsVO> list;
    /** 开始日期时间戳*/
    private Long beginDate;
    /** 结束日期时间戳*/
    private Long endDate;
    /** 景区范围code*/
    private String touristsScopeCode;


    public static TouristRegionStatisticsResponse createDefault(Long beginDate, Long endDate, String touristsScopeCode, String from){
        final TouristRegionStatisticsResponse response = new TouristRegionStatisticsResponse();
        response.setTotal(0L);
        response.setList(new ArrayList<>());

        response.setBeginDate(beginDate);
        response.setEndDate(endDate);
        response.setTouristsScopeCode(touristsScopeCode);
        response.setFrom(from);
        return response;
    }

}
