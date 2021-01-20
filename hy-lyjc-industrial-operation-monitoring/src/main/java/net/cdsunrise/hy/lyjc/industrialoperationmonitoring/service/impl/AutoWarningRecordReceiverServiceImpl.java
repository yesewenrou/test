package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecordReceiver;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningRecordReceiverMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningRecordReceiverService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 0:44
 */
@Service
public class AutoWarningRecordReceiverServiceImpl extends ServiceImpl<AutoWarningRecordReceiverMapper, AutoWarningRecordReceiver> implements IAutoWarningRecordReceiverService {

    @Override
    public List<AutoWarningRecordReceiver> selectByRecordId(Long recordId) {
        QueryWrapper<AutoWarningRecordReceiver> wrapper = new QueryWrapper<>();
        wrapper.select().eq("record_id", recordId);
        return super.baseMapper.selectList(wrapper);
    }
}
