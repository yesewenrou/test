package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningRecordAttach;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningRecordAttachMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningRecordAttachService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fangyunlong
 * @date 2020/3/8 2:14
 */
@Service
public class AutoWarningRecordAttachServiceImpl extends ServiceImpl<AutoWarningRecordAttachMapper, AutoWarningRecordAttach> implements IAutoWarningRecordAttachService {
    @Override
    public List<AutoWarningRecordAttach> selectByRecordId(Long recordId) {
        QueryWrapper<AutoWarningRecordAttach> wrapper = new QueryWrapper<>();
        wrapper.select().eq("record_id", recordId);
        return super.baseMapper.selectList(wrapper);
    }
}
