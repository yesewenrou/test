package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.DataCenterMonthlyReportReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DataCenterReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResponseUtils;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zbk
 */
@RestController
@RequestMapping("/dataCenterMonthlyReport")
public class DataCenterMonthlyReportController {

    private final DataCenterReportService dataCenterReportService;

    public DataCenterMonthlyReportController(DataCenterReportService dataCenterReportService) {
        this.dataCenterReportService = dataCenterReportService;
    }

    /**
     * 生成大数据中心月报数据
     * @param beginDate
     * @param endDate
     * @return
     */
    @Auth(value = "dataCenterMonthlyReport:export")
    @GetMapping("export")
    public ResponseEntity<byte[]> export(@RequestParam("beginDate") Long beginDate,@RequestParam("endDate") Long endDate) {
        return ResponseUtils.buildResponseEntity(dataCenterReportService.export(
                new DataCenterMonthlyReportReq(DateUtil.longToLocalDateTime(beginDate),DateUtil.longToLocalDateTime(endDate))));
    }

    @GetMapping("queryReportData")
    public Result queryReportData(@RequestParam("beginDate") Long beginDate,@RequestParam("endDate") Long endDate) {
        return ResultUtil.buildSuccessResultWithData(dataCenterReportService.queryReportData(
                new DataCenterMonthlyReportReq(DateUtil.longToLocalDateTime(beginDate),DateUtil.longToLocalDateTime(endDate))));
    }

}
