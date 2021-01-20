package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ISpecialReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportPageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.DeleteCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lijiafeng
 * @date 2020/1/17 15:54
 */
@RestController
@RequestMapping("special-report")
public class SpecialReportController {

    private ISpecialReportService specialReportService;

    public SpecialReportController(ISpecialReportService specialReportService) {
        this.specialReportService = specialReportService;
    }

    /**
     * 新增专题报告
     *
     * @param specialReportVO 专题报告
     * @return 结果
     */
    @Auth("special-report:add")
    @PostMapping
    public Result addSpecialReport(@Validated @RequestBody SpecialReportVO specialReportVO) {
        return ResultUtil.buildSuccessResultWithData(specialReportService.addSpecialReport(specialReportVO));
    }

    /**
     * 查询专题报告列表
     *
     * @param request 请求
     * @return 结果
     */
    @Auth("special-report:list")
    @GetMapping("list")
    public Result listSpecialReport(@Validated SpecialReportPageRequest request) {
        return ResultUtil.buildSuccessResultWithData(specialReportService.listSpecialReport(request));
    }

    /**
     * 删除专题报告列表
     *
     * @param specialReportVO 请求
     * @return 结果
     */
    @Auth("special-report:delete")
    @DeleteMapping
    public Result deleteSpecialReport(@Validated({DeleteCheckGroup.class}) SpecialReportVO specialReportVO) {
        specialReportService.deleteSpecialReport(specialReportVO);
        return ResultUtil.success();
    }
}
