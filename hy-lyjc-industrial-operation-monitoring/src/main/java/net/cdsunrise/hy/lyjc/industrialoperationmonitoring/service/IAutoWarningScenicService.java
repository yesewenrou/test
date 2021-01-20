package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningScenic;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:41
 */
public interface IAutoWarningScenicService extends IService<AutoWarningScenic> {

    /**
     * 通过预警id查询景区预警信息
     * @param  warningId warningId
     * @return AutoWarningScenic
     */
    AutoWarningScenic queryByWarningId(Long warningId);
}
