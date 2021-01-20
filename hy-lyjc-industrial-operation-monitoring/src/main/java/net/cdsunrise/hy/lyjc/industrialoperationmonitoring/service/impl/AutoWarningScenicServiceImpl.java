package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningScenic;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningScenicMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningScenicService;
import org.springframework.stereotype.Service;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:46
 */
@Service
public class AutoWarningScenicServiceImpl extends ServiceImpl<AutoWarningScenicMapper, AutoWarningScenic> implements IAutoWarningScenicService {


    @Override
    public AutoWarningScenic queryByWarningId(Long warningId) {
        QueryWrapper<AutoWarningScenic> wrapper = new QueryWrapper<>();
        wrapper.select().eq("warning_id", warningId);
        return super.baseMapper.selectOne(wrapper);
    }
}
