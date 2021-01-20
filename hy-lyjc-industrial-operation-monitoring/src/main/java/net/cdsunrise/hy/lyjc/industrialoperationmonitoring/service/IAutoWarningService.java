package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarning;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;

import java.util.List;

/**
 * @author funnylog
 */
public interface IAutoWarningService extends IService<AutoWarning> {

    /**
     * 分待确认预警-页查询列表
     * @param query  queryCondition
     * @return 分页查询结果
     */
    IPage<AutoWarning> pageList(AutoWarningCondition.Query query);

    /**
     * 待确认预警 - 申请发布
     * @param requestPublic requestPublic
     * @return result
     */
    Result handleRequestPublic(AutoWarningCondition.RequestPublic requestPublic);

    /**
     * 忽略预警
     * @param id id
     * @param statusCode 状态编码
     * @return  result
     */
    Result ignoreWarning(Long id, String statusCode);


    /**
     * 查询告警详情
     * @param id id
     * @return  WarningRecordDetailVO
     */
    WarningRecordDetailVO queryRecordDetail(Long id);

    /**
     * 根据应急事件id, 查询预警详情
     * @param eventId 应急事件id
     * @return 预警详情
     */
    WarningRecordDetailVO queryRecordDetailByEventId(Long eventId);

    /**
     * 即时信息发布
     * @param addReq addReq
     * @return Result
     */
    Result publish(AutoWarningCondition.PublishReq addReq);

    /**
     * 节目信息发布
     * @param addReq addReq
     * @return Result
     */
    Result publishProgram(AutoWarningCondition.PublishProgramReq addReq);

    /**
     * 查询该预警已经通过短信发送给了哪些人
     * @param warningId warningId
     * @return String
     */
    Result querySentContact(Long warningId);

    /**
     * 按状态统计
     * @return result
     */
    Result statistics();

    /**
     * 景区客流量预警信息保存
     * @param scenicWarning info
     * @return Result
     */
    Result saveScenicAutoWarning(AutoWarningDTO.ScenicWarning scenicWarning);

    /**
     * 根据预警类型统计待确认状态的预警
     * @return List
     */
    List<WarningUnconfirmedVO> countUnconfirmedWarningByType();

    /**
     * 交通预警
     * @param autoWarningTrafficDTO 预警信息
     * @return result
     */
    Result trafficWarning(AutoWarningTrafficDTO autoWarningTrafficDTO);

    /**
     * 定时任务 - 预警自动过期
     */
    void warningAutoExpire();

}
