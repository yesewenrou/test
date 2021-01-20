package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecordReceiver;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:40
 */
public interface IAutoWarningRecordReceiverService extends IService<AutoWarningRecordReceiver> {

    /**
     *
     * @param recordId 记录id
     * @return List
     */
    List<AutoWarningRecordReceiver> selectByRecordId(Long recordId);
}
