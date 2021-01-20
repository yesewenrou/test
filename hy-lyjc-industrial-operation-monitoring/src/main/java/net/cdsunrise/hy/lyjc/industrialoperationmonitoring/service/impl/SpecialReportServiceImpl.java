package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.SpecialReport;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.DataCenterMonthlyReportReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ScenicReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.SpecialReportMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ConsumptionReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HotelReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IScenicReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ISpecialReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.TrafficReportUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.lyjtglserverstarter.entity.vo.NameValue;
import net.cdsunrise.hy.lyjtglserverstarter.feign.client.LyjtglServerClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2020/1/17 14:46
 */
@Slf4j
@Service
public class SpecialReportServiceImpl extends ServiceImpl<SpecialReportMapper, SpecialReport> implements ISpecialReportService {

    private final DataDictionaryFeignClient dataDictionaryFeignClient;
    private final ConsumptionReportService consumptionReportService;
    private final HotelReportService hotelReportService;
    private final IScenicReportService iScenicReportService;

    /** 旅游交通管理客户端*/
    private final LyjtglServerClient lyjtglServerClient;

    public SpecialReportServiceImpl(DataDictionaryFeignClient dataDictionaryFeignClient, ConsumptionReportService consumptionReportService, HotelReportService hotelReportService, IScenicReportService iScenicReportService, LyjtglServerClient lyjtglServerClient) {
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
        this.consumptionReportService = consumptionReportService;
        this.hotelReportService = hotelReportService;
        this.iScenicReportService = iScenicReportService;
        this.lyjtglServerClient = lyjtglServerClient;
    }


    @Override
    public IPage<SpecialReportVO> listSpecialReport(SpecialReportPageRequest request) {
        Page<SpecialReport> page = new Page<>(request.getPage(), request.getSize());
        QueryWrapper<SpecialReport> queryWrapper = Wrappers.query();
        queryWrapper.lambda()
                .like(StringUtils.hasText(request.getName()), SpecialReport::getName, request.getName())
                .eq(StringUtils.hasText(request.getType()), SpecialReport::getType, request.getType())
                .ge(Objects.nonNull(request.getStartTime()), SpecialReport::getReportTime, request.getStartTime())
                .le(Objects.nonNull(request.getEndTime()), SpecialReport::getReportTime, request.getEndTime())
                .orderByDesc(SpecialReport::getReportTime)
                .orderByDesc(SpecialReport::getId);
        super.page(page, queryWrapper);
        Map<String, String> specialReportType = getSpecialReportType();
        return page.convert(specialReport -> {
            SpecialReportVO specialReportVO = new SpecialReportVO();
            BeanUtils.copyProperties(specialReport, specialReportVO);
            // 数据字典
            specialReportVO.setTypeName(specialReportType.get(specialReport.getType()));
            return specialReportVO;
        });
    }

    @Override
    public Long addSpecialReport(SpecialReportVO specialReportVO) {
        checkSpecialReportType(specialReportVO.getType());
        SpecialReport specialReport = new SpecialReport();
        BeanUtils.copyProperties(specialReportVO, specialReport);
        super.save(specialReport);
        return specialReport.getId();
    }

    @Override
    public void deleteSpecialReport(SpecialReportVO specialReportVO) {
        super.removeById(specialReportVO.getId());
    }

    @Override
    public SpecialReportWordData getSpecialReportWordData(DataCenterMonthlyReportReq reportReq) {
        SpecialReportWordData specialReportWordData = new SpecialReportWordData();
        LocalDate beginDate = reportReq.getBeginDate().toLocalDate();
        LocalDate endDate = reportReq.getEndDate().toLocalDate();
        // 概述数据 overview
        SpecialReportWordData.Overview overview = new SpecialReportWordData.Overview();
        overview.setDateRange(beginDate.format(DateUtil.LOCAL_DATE) + "至" + endDate.format(DateUtil.LOCAL_DATE));
        // 概述消费数据 overview
        ConsumptionBusinessCircleReport consumptionBusinessCircleReport = consumptionReportService.getConsumptionBusinessCircleReport(beginDate, endDate);
        BeanUtils.copyProperties(consumptionBusinessCircleReport, overview);
        // 概述游客数据 overview
        ScenicReportVO scenicReportVO = iScenicReportService.statisticsScenicReport(DateUtil.localDateToLong(beginDate), DateUtil.localDateToLong(endDate));
        overview.setPassengerByScenic(scenicReportVO.getOverview().getPassengerByScenic());
        overview.setPassenger(scenicReportVO.getOverview().getPassenger());

        specialReportWordData.setOverview(overview);
        // 游客数据 PFA
        specialReportWordData.setPFA(scenicReportVO.getPFA());
        // 消费数据 TCA
        SpecialReportWordData.TourismConsumptionAnalysis tourismConsumptionAnalysis = new SpecialReportWordData.TourismConsumptionAnalysis();
        ConsumptionIndustryReport consumptionIndustryReport = consumptionReportService.getConsumptionIndustryReport(beginDate, endDate);
        ConsumptionSourceReport consumptionSourceReport = consumptionReportService.getConsumptionSourceReport(beginDate, endDate);
        BeanUtils.copyProperties(consumptionIndustryReport, tourismConsumptionAnalysis);
        BeanUtils.copyProperties(consumptionSourceReport, tourismConsumptionAnalysis);
        specialReportWordData.setTCA(tourismConsumptionAnalysis);

        // 交通数据
        final SpecialReportWordData.TrafficAnalysis trafficAnalysis = getTrafficAnalysis(beginDate, endDate);
        specialReportWordData.setTA(trafficAnalysis);

        // 酒店数据 HCA
        SpecialReportWordData.HotelAnalysis hotelAnalysis = new SpecialReportWordData.HotelAnalysis();
        HotelTrendReportVO reportVO = hotelReportService.hotelRecption(beginDate, endDate);
        TouristPortraitVO touristPortraitVO = hotelReportService.touristPortrait(beginDate, endDate);
        BeanUtils.copyProperties(reportVO, hotelAnalysis);
        BeanUtils.copyProperties(touristPortraitVO, hotelAnalysis);
        specialReportWordData.setHCA(hotelAnalysis);

        return specialReportWordData;
    }

    /**
     * 得到交通分析报告
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 交通分析对象
     */
    private SpecialReportWordData.TrafficAnalysis getTrafficAnalysis(LocalDate beginDate, LocalDate endDate){
        final long begin = DateUtil.dateTimeToLong(beginDate.atStartOfDay());
        final long end = DateUtil.dateTimeToLong(endDate.atStartOfDay());

        final Result<Map<String, List<NameValue<?, ?>>>> mapResult = lyjtglServerClient.allInOneReport(begin, end);
        return TrafficReportUtil.handleTrafficReportMap(mapResult.getData());
    }



    private Map<String, String> getSpecialReportType() {
        String typeCode = "SPECIAL_REPORT_TYPE";
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{typeCode});
        DataDictionaryVO dataDictionaryVO = result.getData().get(typeCode);
        return dataDictionaryVO.getChildren().stream()
                .collect(Collectors.toMap(DataDictionaryVO::getCode, DataDictionaryVO::getName));
    }

    private void checkSpecialReportType(String type) {
        Map<String, String> specialReportType = getSpecialReportType();
        String typeName = specialReportType.get(type);
        if (typeName == null) {
            throw new ParamErrorException("错误的报告类型: " + type);
        }
    }



}
