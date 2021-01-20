package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeVO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @Author: LHY
 * @Date: 2019/9/17 17:39
 */
public interface TourismIncomeService {

    /**统计数据**/
    Map statisticsData(TourismIncomeCondition condition);

    /**
     * 带搜索条件查询统计结果
     */
    Page<TourismIncomeVO> conditionStatisticsData(PageRequest<TourismIncomeCondition> pageRequest);

    /**
     * 历史数据统计，带搜索条件查询统计结果
     */
    Map<String,Object> historyConditionStatisticsData(PageRequest<TourismIncomeCondition> pageRequest);

    /**
     * 带搜索条件导出Excel
     */
    ResponseEntity<byte[]> export(PageRequest<TourismIncomeCondition> pageRequest);

    /**
     * 历史数据统计，带搜索条件导出Excel
     */
    ResponseEntity<byte[]> historyExport(PageRequest<TourismIncomeCondition> pageRequest);
}
