package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyRosterService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lijiafeng
 * @date 2019/11/25 11:23
 */
@RestController
@RequestMapping("duty-roster")
public class DutyRosterController {

    private IDutyRosterService dutyRosterService;

    public DutyRosterController(IDutyRosterService dutyRosterService) {
        this.dutyRosterService = dutyRosterService;
    }

    /**
     * 查询值班表
     *
     * @return 结果
     */
//    @Auth("duty-roster:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listDutyRoster() {
        return ResultUtil.buildSuccessResultWithData(dutyRosterService.listDutyRoster());
    }

    /**
     * 更新值班表
     *
     * @param dutyRosterRequest 值班信息
     * @return 结果
     */
//    @Auth("duty-roster:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result updateDutyRoster(@Validated @RequestBody DutyRosterRequest dutyRosterRequest) {
        boolean success = dutyRosterService.updateDutyRoster(dutyRosterRequest);
        return ResultUtil.buildResult(success, success ? "更新成功" : "更新失败");
    }

    /**
     * 獲取人員
     * @param dutyTime
     * @return
     */
//    @Auth("duty-roster:get")
    @GetMapping("get")
    public Result getDutyRoster(@RequestParam("dutyTime") String dutyTime) {
        return ResultUtil.buildSuccessResultWithData(dutyRosterService.getDutyRoster(dutyTime));
    }
}
