package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.ConsumptionStatisticsReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ConsumptionReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITourismConsumptionAnalyzeService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismConsumptionService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DigitalUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/05/11 08:55
 */
@Service
public class ConsumptionReportServiceImpl implements ConsumptionReportService {

    private final ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService;
    private final TourismConsumptionService tourismConsumptionService;

    public ConsumptionReportServiceImpl(ITourismConsumptionAnalyzeService tourismConsumptionAnalyzeService, TourismConsumptionService tourismConsumptionService) {
        this.tourismConsumptionAnalyzeService = tourismConsumptionAnalyzeService;
        this.tourismConsumptionService = tourismConsumptionService;
    }

    @Override
    public ConsumptionSourceReport getConsumptionSourceReport(LocalDate beginDate, LocalDate endDate) {
        ConsumptionSourceReport consumptionSourceReport = new ConsumptionSourceReport();
        // 数据查询
        TourismConsumptionAnalyzeCondition condition = new TourismConsumptionAnalyzeCondition();
        condition.setType(TourismConsumptionAnalyzeCondition.TypeEnum.DAY);
        condition.setBeginDateOrigin(beginDate);
        condition.setEndDateOrigin(endDate);
        TourismConsumptionSourceAnalyzeVO tourismConsumptionSourceAnalyze = tourismConsumptionAnalyzeService.getTourismConsumptionSourceAnalyze(condition);
        // 原始数据
        consumptionSourceReport.setSourceData(tourismConsumptionSourceAnalyze);
        // 分析数据
        BigDecimal totalTransAt = tourismConsumptionSourceAnalyze.getTotalTransAt();
        BigDecimal innerProvTransAt = tourismConsumptionSourceAnalyze.getInnerProvTransAt();
        BigDecimal outerProvTransAt = tourismConsumptionSourceAnalyze.getOuterProvTransAt();
        consumptionSourceReport.setConsumptionInProvince(DigitalUtil.toTenThousand(innerProvTransAt));
        consumptionSourceReport.setConsumptionInProvinceRatio(DigitalUtil.calcPercent(innerProvTransAt, totalTransAt));
        consumptionSourceReport.setConsumptionOutsideProvince(DigitalUtil.toTenThousand(outerProvTransAt));
        consumptionSourceReport.setConsumptionOutsideProvinceRatio(DigitalUtil.calcPercent(outerProvTransAt, totalTransAt));
        consumptionSourceReport.setTop5ConsumptionInProvince(getTop5String(tourismConsumptionSourceAnalyze.getInnerProvList()));
        consumptionSourceReport.setTop5ConsumptionOutsideProvince(getTop5String(tourismConsumptionSourceAnalyze.getOuterProvList()));

        return consumptionSourceReport;
    }

    @Override
    public ConsumptionIndustryReport getConsumptionIndustryReport(LocalDate beginDate, LocalDate endDate) {
        ConsumptionIndustryReport consumptionIndustryReport = new ConsumptionIndustryReport();
        // 数据查询
        TourismConsumptionAnalyzeCondition condition = new TourismConsumptionAnalyzeCondition();
        condition.setType(TourismConsumptionAnalyzeCondition.TypeEnum.DAY);
        condition.setBeginDateOrigin(beginDate);
        condition.setEndDateOrigin(endDate);
        List<TourismConsumptionIndustryAnalyzeVO> data = tourismConsumptionAnalyzeService.getTourismConsumptionIndustryAnalyze(condition);
        // 原始数据
        consumptionIndustryReport.setIndustryData(data);
        // 分析数据
        BigDecimal totalTransAt = BigDecimal.ZERO;
        for (TourismConsumptionIndustryAnalyzeVO vo : data) {
            totalTransAt = totalTransAt.add(vo.getTransAt());
        }
        // 最高消费行业
        if (data.size() > 0) {
            TourismConsumptionIndustryAnalyzeVO industryAnalyze = data.get(0);
            consumptionIndustryReport.setHighestBusiness(industryAnalyze.getIndustry());
            consumptionIndustryReport.setHighestBusinessConsumption(DigitalUtil.toTenThousand(industryAnalyze.getTransAt()));
            consumptionIndustryReport.setHighestBusinessRatio(DigitalUtil.calcPercent(industryAnalyze.getTransAt(), totalTransAt));
        }
        // 次高消费行业
        if (data.size() > 1) {
            TourismConsumptionIndustryAnalyzeVO industryAnalyze = data.get(1);
            consumptionIndustryReport.setHigherBusiness(industryAnalyze.getIndustry());
            consumptionIndustryReport.setHigherBusinessConsumption(DigitalUtil.toTenThousand(industryAnalyze.getTransAt()));
            consumptionIndustryReport.setHigherBusinessRatio(DigitalUtil.calcPercent(industryAnalyze.getTransAt(), totalTransAt));
        }

        return consumptionIndustryReport;
    }

    @Override
    public ConsumptionBusinessCircleReport getConsumptionBusinessCircleReport(LocalDate beginDate, LocalDate endDate) {
        ConsumptionBusinessCircleReport consumptionBusinessCircleReport = new ConsumptionBusinessCircleReport();
        // 数据查询
        ConsumptionStatisticsReq req = new ConsumptionStatisticsReq();
        req.setBeginDate(beginDate.atStartOfDay());
        req.setEndDate(endDate.atStartOfDay());
        List<ConsumptionStatisticsVO> data = tourismConsumptionService.consumptionStatistics(req);
        // 原始数据
        consumptionBusinessCircleReport.setBusinessCircleData(data);
        // 分析数据
        // 县域数据
        if (data.size() > 0) {
            ConsumptionStatisticsVO hyData = data.get(0);
            consumptionBusinessCircleReport.setConsumption(DigitalUtil.toTenThousand(hyData.getTransAt()));
        }
        // 各商圈数据
        consumptionBusinessCircleReport.setConsumptionByBusinessCircle(getBusinessCircleString(data));

        return consumptionBusinessCircleReport;
    }

    /**
     * 商圈数据组成字符串
     *
     * @param transInfoList 各商圈数据
     * @return 结果
     */
    private String getBusinessCircleString(List<ConsumptionStatisticsVO> transInfoList) {
        StringBuilder builder = new StringBuilder();
        // 从第二个开始取，第一个是洪雅县域
        if (transInfoList.size() > 1) {
            for (int i = 1; i < transInfoList.size(); i++) {
                ConsumptionStatisticsVO transInfo = transInfoList.get(i);
                builder.append(String.format("%s%.2f万元、", transInfo.getCbdName(), DigitalUtil.toTenThousand(transInfo.getTransAt())));
            }
        }
        // 最后、替换为。
        int length = builder.length();
        if (length > 0) {
            builder.replace(length - 1, length, "。");
        }

        return builder.toString();
    }

    /**
     * 消费来源地TOP数据组成字符串
     *
     * @param transInfoList 消费来源地TOP数据
     * @return 结果
     */
    private String getTop5String(List<TourismConsumptionSourceAnalyzeVO.TransInfo> transInfoList) {
        StringBuilder builder = new StringBuilder();
        // 只取前五
        int top = 5;
        for (int i = 0; i < transInfoList.size() && i < top; i++) {
            TourismConsumptionSourceAnalyzeVO.TransInfo transInfo = transInfoList.get(i);
            builder.append(String.format("%s（%.2f万元）、", transInfo.getSource(), DigitalUtil.toTenThousand(transInfo.getTransAt())));
        }
        // 最后、替换为。
        int length = builder.length();
        if (length > 0) {
            builder.replace(length - 1, length, "。");
        }

        return builder.toString();
    }
}
