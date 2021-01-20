package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecord;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:40
 */
public interface IAutoWarningRecordService extends IService<AutoWarningRecord> {
    /**
     * 查询操作记录
     * @param warningId 预警id
     * @param handleType 1 申请发布 2 发送短信
     * @return List
     */
    List<AutoWarningRecord> queryByWarningId(Long warningId, Integer handleType);
}
