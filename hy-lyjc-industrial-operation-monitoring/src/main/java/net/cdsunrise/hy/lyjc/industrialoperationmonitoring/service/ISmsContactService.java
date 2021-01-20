package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningContactConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SmsContactCondition;

import java.util.List;

/**
 * @author funnylog
 */
public interface ISmsContactService extends IService<AutoWarningContactConfig> {

    /**
     * 告警-联系人-编辑
     * @param edit info
     * @return ID
     */
    Long editContact(SmsContactCondition.Edit edit);

    /**
     * 分页查询列表
     * @param page 页数
     * @param size 条数
     * @return 分页查询结果
     */
    IPage<AutoWarningContactConfig> pageList(Long page, Long size);

    /**
     * 查询景区 需要自动发送短信的列表
     * @param scenicAuto Boolean
     * @return list
     */
    List<AutoWarningContactConfig> getScenicAuto(Boolean scenicAuto);

    /**
     * 查询交通 需要自动发送短信的列表
     * @param trafficAuto Boolean
     * @return list
     */
    List<AutoWarningContactConfig> getTrafficAuto(Boolean trafficAuto);

}
