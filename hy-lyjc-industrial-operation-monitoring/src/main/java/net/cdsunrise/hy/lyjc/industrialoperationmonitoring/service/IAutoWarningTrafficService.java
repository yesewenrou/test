package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;


import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningTraffic;

/**
 * @author funnylog
 */
public interface IAutoWarningTrafficService extends IService<AutoWarningTraffic> {

    /**
     * 根据预警ID查询交通预警信息
     * @param warningId 预警ID
     * @return 交通预警信息
     */
    AutoWarningTraffic selectByWarningId(Long warningId);

}
