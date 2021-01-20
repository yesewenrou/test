package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.HandleResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.HandleResultVO;
import org.springframework.scheduling.annotation.Async;

/**
 * @author LHY
 */
public interface HandleResultService {

    /**
     * 同意受理
     */
    Long agreeHandle(Long complaintId);

    /**
     * 拒绝受理
     */
    Long disagreeHandle(HandleResult handleResult);

    /**
     * 完成最终受理
     */
    void finishHandle(HandleResultVO handleResultVO);

    /**
     * 根据投诉单状态，展示不同处理结果
     */
    HandleResultVO findById(Long complaintId);

    /**
     * 投诉结案后，记录被投诉方全称、投诉行业分类，支持商户管理平台能同步投诉记录。
     * @param complaintId 投诉ID
     * @param userId  用户ID
     * @param userName 用户名
     */
    void notifyAfterHandleCompleted(long complaintId, long userId, String userName, String token);
}
