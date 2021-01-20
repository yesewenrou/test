package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecordAttach;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 2:13
 */
public interface IAutoWarningRecordAttachService extends IService<AutoWarningRecordAttach> {

    /**
     * 根据记录id查询附件
     * @param recordId recordId
     * @return List
     */
    List<AutoWarningRecordAttach> selectByRecordId(Long recordId);
}
