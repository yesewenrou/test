package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ScenicReportVO;

/**
 * @author FangYunLong
 * @date in 2020/5/9
 */
public interface IScenicReportService {

    /**
     * 报告游客统计
     * @param begin 开始日期
     * @param end 结束日期
     * @return 统计结果
     */
    ScenicReportVO statisticsScenicReport(Long begin, Long end);
}
