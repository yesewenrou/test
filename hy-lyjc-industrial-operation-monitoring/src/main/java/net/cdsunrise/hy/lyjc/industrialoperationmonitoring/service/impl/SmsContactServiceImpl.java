package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningContactConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.AutoWarningContactConfigMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ISmsContactService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SmsContactCondition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author funnylog
 */
@Slf4j
@Service
public class SmsContactServiceImpl extends ServiceImpl<AutoWarningContactConfigMapper, AutoWarningContactConfig> implements ISmsContactService {


    /**
     * 告警-联系人-编辑
     *
     * @param edit info
     * @return ID
     */
    @Override
    public Long editContact(SmsContactCondition.Edit edit) {
        log.info("告警-联系人-编辑:{}", edit.toString());
        // 判断手机号是否已存在
        AutoWarningContactConfig autoWarningContactConfig = baseMapper.selectByPhone(edit.getPhone());
        if (autoWarningContactConfig == null) {
            // 不存在则新增
            autoWarningContactConfig = new AutoWarningContactConfig();
            BeanUtils.copyProperties(edit, autoWarningContactConfig);
            super.save(autoWarningContactConfig);
        } else {
            // 已存在则修改
            BeanUtils.copyProperties(edit, autoWarningContactConfig);
            super.updateById(autoWarningContactConfig);
        }
        return autoWarningContactConfig.getId();
    }

    /**
     * 分页查询列表
     *
     * @param page 页数
     * @param size 条数
     * @return 分页查询结果
     */
    @Override
    public IPage<AutoWarningContactConfig> pageList(Long page, Long size) {
        log.info("告警-联系人-分页查询, page:{}, size:{}", page, size);
        size = size == null ? -1L : size;
        Page<AutoWarningContactConfig> pageRequest= new Page<>(page, size);
        IPage<AutoWarningContactConfig> pageResult = baseMapper.selectPage(pageRequest, null);
        return pageResult;
    }

    /**
     * 查询景区 需要自动发送短信的列表
     *
     * @param scenicAuto Boolean
     * @return list
     */
    @Override
    public List<AutoWarningContactConfig> getScenicAuto(Boolean scenicAuto) {
        QueryWrapper<AutoWarningContactConfig> warapper = new QueryWrapper<>();
        warapper.select()
                .eq("scenic_auto", scenicAuto);
        return baseMapper.selectList(warapper);
    }

    /**
     * 查询交通 需要自动发送短信的列表
     *
     * @param trafficAuto Boolean
     * @return list
     */
    @Override
    public List<AutoWarningContactConfig> getTrafficAuto(Boolean trafficAuto) {
        QueryWrapper<AutoWarningContactConfig> warapper = new QueryWrapper<>();
        warapper.select()
                .eq("traffic_auto", trafficAuto);
        return baseMapper.selectList(warapper);
    }


}
