package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionBusinessCircleReport;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionIndustryReport;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ConsumptionSourceReport;

import java.time.LocalDate;

/**
 * @author lijiafeng
 * @date 2020/05/11 08:47
 */
public interface ConsumptionReportService {

    /**
     * 获取消费来源报告
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 结果
     */
    ConsumptionSourceReport getConsumptionSourceReport(LocalDate beginDate, LocalDate endDate);

    /**
     * 获取消费行业报告
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 结果
     */
    ConsumptionIndustryReport getConsumptionIndustryReport(LocalDate beginDate, LocalDate endDate);

    /**
     * 获取消费商圈报告
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 结果
     */
    ConsumptionBusinessCircleReport getConsumptionBusinessCircleReport(LocalDate beginDate, LocalDate endDate);
}
