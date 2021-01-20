package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DataResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionCompareResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionStatisticsResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristRegionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DataResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DataResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
public interface DataResourceService extends IService<DataResource> {

    /**
     * 新增
     */
    void add(DataResource dataResource);

    Integer checkDataResourceExist(String countryName, String cityName, String time);

    /**
     * 带搜索条件查询分页（改造走ES查询）
     */
    Map<String, Object> list(PageRequest<DataResourceCondition> pageRequest);

    /**
     * 统计数据（改造走ES查询）
     **/
    Map statisticsData(DataResourceCondition condition);

    /**
     * 带搜索条件查询统计结果（改造走ES查询）
     */
    PageResult<DataResourceVO> conditionStatisticsData(PageRequest<DataResourceCondition> pageRequest);

    /**
     * 历史数据统计，带搜索条件查询统计结果（改造走ES查询）
     */
    List<DataResourceVO> historyConditionStatisticsData(DataResourceCondition condition);

    /**
     * 带搜索条件导出Excel
     */
    ResponseEntity<byte[]> export(PageRequest<DataResourceCondition> pageRequest);

    /**
     * 带搜索条件导出历史数据Excel
     */
    ResponseEntity<byte[]> historyExport(PageRequest<DataResourceCondition> pageRequest);

    /**
     * 游客来源地TOP10按年统计（改造走ES）
     **/
    Map sourceCityTop(Integer year, Integer scope);

    /**
     * 游客来源地TOP10按天统计
     **/
    Map sourceCityTopByDay();


    /**
     * 游客来源区域统计（按天）
     * @param beginDate 开始日期时间戳
     * @param endDate 结束日期时间戳
     * @param touristsScopeCode 游客区域编码（数据字典）
     * @return 游客区域统计响应
     */
    TouristRegionStatisticsResponse touristRegionStatistics(Long beginDate, Long endDate, String touristsScopeCode);


    /**
     * 游客来源对比统计分析（按天生成折线图）
     * @param beginDate 开始日期时间戳
     * @param endDate 结束日期时间戳
     * @param touristsScopeCode 游客区域编码（数据字典）
     * @param name 区域名称
     * @return TouristRegionCompareResponse
     */
    TouristRegionCompareResponse touristRegionCompareStatistics(Long beginDate, Long endDate, String touristsScopeCode, String name);

}
