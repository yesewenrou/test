package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HandleResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ComplaintManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HandleResultService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DisagreeHandleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.HandleResultVO;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.TokenInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/handleResult")
@Slf4j
public class HandleResultController {

    @Autowired
    private HandleResultService resultService;
    @Autowired
    private ComplaintManageService manageService;

    /**
     * 产业管理 -> 投诉管理 -> 同意处理
     *
     * */
    @Auth(value = "handleResult:agreeHandle")
    @GetMapping("agreeHandle/{complaintId}")
    public Result agreeHandle(@PathVariable Long complaintId) {
        if (manageService.findById(complaintId).getStatus() != ParamConst.NOT_HANDLE_STATUS){
            return ResultUtil.buildResult(false,ParamConst.COMPLAINT_STATUS_ERROR);
        }
        return ResultUtil.buildSuccessResultWithData(resultService.agreeHandle(complaintId));
    }

    /**
     * 产业管理 -> 投诉管理 -> 拒绝处理
     *
     * */
    @Auth(value = "handleResult:disagreeHandle")
    @PostMapping("disagreeHandle")
    public Result disagreeHandle(@Validated @RequestBody DisagreeHandleVO handleVO){
        if (manageService.findById(handleVO.getComplaintId()).getStatus() != ParamConst.NOT_HANDLE_STATUS){
            return ResultUtil.buildResult(false,ParamConst.COMPLAINT_STATUS_ERROR);
        }
        HandleResult handleResult = new HandleResult();
        BeanUtils.copyProperties(handleVO,handleResult);
        return ResultUtil.buildSuccessResultWithData(resultService.disagreeHandle(handleResult));
    }

    /**
     * 产业管理 -> 投诉管理 -> 完成处理
     *
     * */
    @Auth(value = "handleResult:finishHandle")
    @PostMapping("finishHandle")
    public Result finishHandle(@Validated @RequestBody HandleResultVO resultVO){
        if (manageService.findById(resultVO.getComplaintId()).getStatus() != ParamConst.BEING_HANDLE_STATUS){
            return ResultUtil.buildResult(false,ParamConst.COMPLAINT_STATUS_ERROR);
        }
        resultService.finishHandle(resultVO);

        try {
            // 投诉流程完成后通知商户
            TokenInfo tokenInfo = CustomContext.getTokenInfo();
            String userName = tokenInfo.getUser().getUserName();
            Long userId = tokenInfo.getUser().getId();
            resultService.notifyAfterHandleCompleted(resultVO.getComplaintId(), userId, userName, tokenInfo.getToken());
        } catch (Exception e) {
            log.error("通知商户失败", e);
        }

        return ResultUtil.buildResult(true, ParamConst.OPERATE_SUCCESS);
    }
}
