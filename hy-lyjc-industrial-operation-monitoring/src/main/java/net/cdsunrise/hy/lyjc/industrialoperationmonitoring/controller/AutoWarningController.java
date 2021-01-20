package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarning;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.AutoWarningCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.AutoWarningTrafficDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.WarningRecordDetailVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author funnylog
 */
@RestController
@RequestMapping("autoWarning")
public class AutoWarningController {

    private final IAutoWarningService iAutoWarningService;


    public AutoWarningController(IAutoWarningService iAutoWarningService) {
        this.iAutoWarningService = iAutoWarningService;
    }

    /**
     * 待确认列表丶待信息发布 分页查询列表
     * @param query query
     * @return IPage
     */
    @PostMapping("list")
    @Auth("autoWarning:list")
    public IPage<AutoWarning> list(@RequestBody @Validated AutoWarningCondition.Query query) {
        return iAutoWarningService.pageList(query);
    }

    /**
     * 申请发布 短信和诱导屏通用
     * @param requestPublic 请求信息
     * @return result
     */
    @PostMapping("requestPublic")
    @Auth("autoWarning:requestPublic")
    public Result requestPublic(@RequestBody @Validated AutoWarningCondition.RequestPublic requestPublic) {
         return iAutoWarningService.handleRequestPublic(requestPublic);
    }

    /**
     * 忽略预警
     * @param id id
     */
    @GetMapping("ignore")
    @Auth("autoWarning:ignore")
    public Result ignore(@RequestParam("id") Long id, @RequestParam("statusCode") String statusCode) {
        return iAutoWarningService.ignoreWarning(id, statusCode);
    }

    /**
     *
     * @param id 预警id
     * @return  WarningRecordDetailVO
     */
    @GetMapping("queryRecordDetail")
    @Auth("autoWarning:queryRecordDetail")
    public WarningRecordDetailVO queryRecordDetail(@RequestParam("id") Long id) {
        return iAutoWarningService.queryRecordDetail(id);
    }

    /**
     * 根据应急事件id查询预警详情
     * @param eventId 应急事件id
     * @return 预警详情
     */
    @GetMapping("queryRecordDetailByEventId")
    @Auth("autoWarning:queryRecordDetailByEventId")
    public WarningRecordDetailVO queryRecordDetailByEventId(@RequestParam("eventId") Long eventId) {
        return iAutoWarningService.queryRecordDetailByEventId(eventId);
    }


    @PostMapping("publish")
    @Auth("autoWarning:publish")
    public Result publish(@RequestBody @Validated AutoWarningCondition.PublishReq addReq) {
        return iAutoWarningService.publish(addReq);
    }

    @PostMapping("publishProgram")
    @Auth("autoWarning:publishProgram")
    public Result publishProgram(@RequestBody @Validated AutoWarningCondition.PublishProgramReq addReq) {
        return iAutoWarningService.publishProgram(addReq);
    }

    @GetMapping("querySentContact")
    @Auth("autoWarning:querySentContact")
    public Result querySentContact(@RequestParam("id") Long warningId) {
        return iAutoWarningService.querySentContact(warningId);
    }

    @GetMapping("statistics")
    @Auth("autoWarning:statistics")
    public Result statistics() {
        return iAutoWarningService.statistics();
    }

    /**
     * 交通预警
     * @param autoWarningTrafficDTO 预警信息
     * @return result
     */
    @PostMapping("trafficWarning")
    public Result trafficWarning(@RequestBody @Validated AutoWarningTrafficDTO autoWarningTrafficDTO) {
        return iAutoWarningService.trafficWarning(autoWarningTrafficDTO);
    }

}
