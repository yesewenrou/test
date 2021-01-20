package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecord;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningRecordMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:43
 */
@Service
public class AutoWarningRecordServiceImpl  extends ServiceImpl<AutoWarningRecordMapper, AutoWarningRecord> implements IAutoWarningRecordService {


    @Override
    public List<AutoWarningRecord> queryByWarningId(Long warningId, Integer handleType) {
        QueryWrapper<AutoWarningRecord> wrapper = new QueryWrapper<>();
        wrapper.select()
                .eq(warningId != null && warningId != 0, "warning_id", warningId)
                .eq(handleType != null && handleType != 0, "handle_type", handleType)
                .orderByDesc("update_time");
        return super.baseMapper.selectList(wrapper);
    }
}
