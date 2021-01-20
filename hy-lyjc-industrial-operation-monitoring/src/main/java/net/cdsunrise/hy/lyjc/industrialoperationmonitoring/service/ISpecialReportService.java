package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.SpecialReport;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.DataCenterMonthlyReportReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportPageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportWordData;

import java.time.LocalDate;

/**
 * @author lijiafeng
 * @date 2020/1/17 14:45
 */
public interface ISpecialReportService extends IService<SpecialReport> {

    /**
     * 分页查询专题报告
     *
     * @param request 请求
     * @return 结果
     */
    IPage<SpecialReportVO> listSpecialReport(SpecialReportPageRequest request);

    /**
     * 添加专题报告
     *
     * @param specialReportVO 专题报告
     * @return 主键ID
     */
    Long addSpecialReport(SpecialReportVO specialReportVO);

    /**
     * 删除专题报告
     *
     * @param specialReportVO 专题报告
     */
    void deleteSpecialReport(SpecialReportVO specialReportVO);

    /**
     * 获取生成word所需数据
     *
     * @param reportReq
     * @return 结果
     */
    SpecialReportWordData getSpecialReportWordData(DataCenterMonthlyReportReq reportReq);
}
