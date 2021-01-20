package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DecisionPlanAttachment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DecisionPlanAttachmentMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Component
public class DecisionPlanAttachmentHelper extends ServiceImpl<DecisionPlanAttachmentMapper, DecisionPlanAttachment> {

    /**
     * 根据方案ID删除
     * @param planId planId
     */
    public void deleteByPlanId(Long planId) {
        LambdaQueryWrapper<DecisionPlanAttachment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DecisionPlanAttachment::getDecisionPlanId, planId);
        remove(wrapper);
    }

    /**
     * 根据方案ID删除
     * @param planId planId
     */
    public List<DecisionPlanAttachment> selectByPlanId(Long planId) {
        LambdaQueryWrapper<DecisionPlanAttachment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DecisionPlanAttachment::getDecisionPlanId, planId);
        return list(wrapper);
    }
}
