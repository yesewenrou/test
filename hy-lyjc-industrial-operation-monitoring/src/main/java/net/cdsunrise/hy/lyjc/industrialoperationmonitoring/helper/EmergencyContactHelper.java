package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyContact;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyContactMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author fang yun long
 * on 2021/1/19
 */
@Component
public class EmergencyContactHelper extends ServiceImpl<EmergencyContactMapper, EmergencyContact> {
    /**
     * 分页查询
     * @param current 当前页数
     * @param size 查询记录数
     * @return list
     */
    public IPage<EmergencyContact> pageSelect(Long current, Long size, String name) {
        LambdaQueryWrapper<EmergencyContact> wrapper = Wrappers.lambdaQuery();
        wrapper.like(!StringUtils.isEmpty(name), EmergencyContact::getName, name);
        return page(new Page<>(current, size), wrapper);
    }
}
