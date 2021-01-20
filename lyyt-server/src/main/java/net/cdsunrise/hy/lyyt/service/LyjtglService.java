package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.domain.dto.TrafficBaiduFiveMinuteDTO;
import net.cdsunrise.hy.lyyt.domain.vo.*;
import net.cdsunrise.hy.lyyt.entity.resp.ForecastValueResponse;
import net.cdsunrise.hy.lyyt.entity.vo.MonitorCarCountVo;
import net.cdsunrise.hy.lyyt.entity.vo.RoadJamTimeLengthVo;
import net.cdsunrise.hy.lyyt.entity.vo.RoadSectionTpiForecastVO;
import net.cdsunrise.hy.lyyt.entity.vo.TrafficAnalyzeVo;
import net.cdsunrise.hy.lyyt.enums.HolidayTypeEnum;
import net.cdsunrise.hy.lyyt.enums.ImportantRoadSection;

import java.util.List;
import java.util.Set;

/**
 * @author YQ on 2019/11/5.
 */
public interface LyjtglService {
    /**
     * 旅游交通整体态势
     * @return vo
     */
    TrafficWholeSituationVo trafficWholeSituation();

    /**
     * 车辆来源top10
     * @return vo
     */
    CarSourceTopTenVo carSourceTopTen();

    /**
     * 进城车辆统计
     * @return vo
     */
    CrossCarCountVo crossCarCount();

    /**
     * 重要的路段
     * @return vo
     */
    List<ImportantRoadSection.ImportantRoadSectionClazz> importantRoad();

    /**
     * 重点监测单位柱状图数据
     * @return vo
     * @param monitorCode 监控点编码
     */
    ImportantHistogramVo importantHistogram(String monitorCode);

    /**
     * 本周车辆来源分析
     * @return vo
     */
    CarSourceRatioVo carSourceRatio();

    /**
     * 拥堵道路监测
     * @return data
     */
    RoadDetailMonitorVo oftenJamRoad();

    /**
     * 重点旅游路段监测
     * @return data
     */
    List<RoadSectionMonitorSiteVo> roadMonitorImportant();

    /**
     * 常发拥堵时间
     * @return data
     */
    List<OftenJamTimeVo> oftenJamTime();

    /**
     * 实时路况信息
     * @return data
     */
    List<RoadDetailVo> roadMonitor();

    /**
     * 常发拥堵时间,一天
     * @return data
     */
    List<OftenJamTimeVo> oftenJamTimeDay();

    /**
     * 获取所有卡口实时车流量
     * @return data
     */
    List<MonitorRealTimeData> getMonitorRealTimeData();

    /**
     * 旅游干道拥堵当月排行
     * @return data
     */
    List<RoadJamTimeLengthVo> roadSegJamRank();

    /**
     * 基础查询
     * @param sectionIds 包含的路段ID
     * @param startTime rangeTime > startTime
     * @param endTime rangeTime < endTime
     * @return 数据集合
     */
    List<TrafficBaiduFiveMinuteDTO> query(Set<String> sectionIds, Long startTime, Long endTime);

    /**
     * 当日拥堵次数
     * @return data
     */
    Integer jamCountToday();

    /**
     * 当日拥堵路段条数
     * @return data
     */
    JamRoadCountVo jamRoadCountToday();


    /**
     * 监控点列表
     * @return vo
     */
    List<MonitorDetailVo> monitorList();

    /**
     * 当月拥堵道路时长排行
     * @return vo
     */
    List<RoadDetailVo> roadJamTimeRankMonth();

    /**
     * 重要卡口车流量
     * @return vo
     */
    MonitorCarCountVo monitorCarCount();

    /**
     * 节假日专题 旅游交通分析
     * @return vo
     * @param holiday
     */
    TrafficAnalyzeVo trafficAnalyze(HolidayTypeEnum holiday);


    /**
     * 当前进城流量预测
     * @return resp
     */
    List<ForecastValueResponse<Integer>> currentInCityFlowForecast();

    /**
     * 当前拥堵路段预测
     * @param tpi 要过滤的tpi线，当大于等于这个tpi时才展示
     * @return resp
     */
    List<ForecastValueResponse<List<RoadSectionTpiForecastVO>>> currentJamRoleSectionForecast(Double tpi);

}
