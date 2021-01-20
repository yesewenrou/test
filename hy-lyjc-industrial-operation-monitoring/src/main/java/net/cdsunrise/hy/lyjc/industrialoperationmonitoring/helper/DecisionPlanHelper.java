package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DecisionPlan;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DecisionPlanMapper;
import org.springframework.stereotype.Component;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Component
public class DecisionPlanHelper extends ServiceImpl<DecisionPlanMapper, DecisionPlan> {

    /**
     * 分页查询
     * @param current 当前页数
     * @param size 查询记录数
     * @return list
     */
    public IPage<DecisionPlan> pageSelect(Long current, Long size) {
        return page(new Page<>(current, size));
    }
}
