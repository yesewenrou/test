package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
public interface ComplaintManageService {

    /**
     * 新增
     */
    Long add(ComplaintManage complaintManage);

    /**
     * 根据主键查询
     */
    ComplaintManage findById(Long id);

    ComplaintManage getById(Long id);

    /**
     * 更新投诉单状态
     */
    void updateStatus(Long id,Integer status);

    /**
     * 搜索条件分页
     */
    Page<ComplaintManage> list(PageRequest<ComplaintManageCondition> pageRequest);

    /**
     * 生成唯一投诉编号
     */
    String generateComplaintNumber();

    /**
     * 统计投诉总数
     */
    Map statisticsComplaint();

    /**
     * 图表展示投诉总数
     */
    Map chartComplaint();

    /**
     * 带搜索条件查询统计结果
     */
    Map<String,Object> conditionStatisticsComplaint(PageRequest<StatisticsComplaintCondition> pageRequest);

    List<StatisticsVO> statisticsComplaintList(PageRequest<StatisticsComplaintCondition> pageRequest);

    /**
     * 带搜索条件导出Excel
     */
    ResponseEntity<byte[]> export(PageRequest<StatisticsComplaintCondition> pageRequest);
    /**
     * 根据商家名，获取投诉列表
     */
    List<MerchantComplaintVO> findByComplaintObject(String complaintObject);

    /**
     * 营销系统 -> 按时间范围，统计投诉总数
     */
    Map statisticsComplaintByTime(String beginTime,String endTime);

    /**
     * 营销系统 -> 按时间范围，图表展示投诉总数
     */
    Map chartComplaintByTime(String beginTime,String endTime);

    /**
     * 营销系统 -> 展示查询月份区间投诉走势
     */
    List<ChartVO> complaintTrend(String beginTime,String endTime);
}
