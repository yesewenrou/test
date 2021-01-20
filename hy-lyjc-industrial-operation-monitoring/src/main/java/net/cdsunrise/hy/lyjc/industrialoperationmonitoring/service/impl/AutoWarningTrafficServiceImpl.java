package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningTraffic;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningTrafficMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningTrafficService;
import org.springframework.stereotype.Service;

/**
 * @author funnylog
 */
@Service
public class AutoWarningTrafficServiceImpl extends ServiceImpl<AutoWarningTrafficMapper, AutoWarningTraffic> implements IAutoWarningTrafficService {
    /**
     * 根据预警ID查询交通预警信息
     *
     * @param warningId 预警ID
     * @return 交通预警信息
     */
    @Override
    public AutoWarningTraffic selectByWarningId(Long warningId) {
        return super.baseMapper.selectByWarningId(warningId);
    }
}
