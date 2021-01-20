package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DecisionPlan;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DecisionPlanAttachment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.DecisionPlanAttachmentHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.DecisionPlanHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DecisionPlanService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author fang yun long
 * on 2021/1/18
 */
@Service
@Slf4j
public class DecisionPlanServiceImpl implements DecisionPlanService {
    private final DecisionPlanHelper decisionPlanHelper;
    private final DecisionPlanAttachmentHelper decisionPlanAttachmentHelper;
    private final DataDictionaryFeignClient dataDictionaryFeignClient;

    public DecisionPlanServiceImpl(DecisionPlanHelper decisionPlanHelper,
                                   DecisionPlanAttachmentHelper decisionPlanAttachmentHelper,
                                   DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.decisionPlanHelper = decisionPlanHelper;
        this.decisionPlanAttachmentHelper = decisionPlanAttachmentHelper;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addDecisionPlan(DecisionPlanRequest.Add request) {
        log.info("addDecisionPlan ... [{}]", request.toString());
        DecisionPlan plan = new DecisionPlan();
        BeanUtils.copyProperties(request, plan);
        decisionPlanHelper.save(plan);
        long id = plan.getId();
        // 保存附件
        saveAttachment(id, request.getAttaches());
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateDecisionPlan(DecisionPlanRequest.Update request) {
        log.info("updateDecisionPlan ... [{}]", request.toString());
        Long id = request.getId();
        DecisionPlan decisionPlan = decisionPlanHelper.getById(id);
        if (Objects.isNull(decisionPlan)) {
            throw new ParamErrorException("数据不存在");
        }
        BeanUtils.copyProperties(request, decisionPlan);
        decisionPlanHelper.updateById(decisionPlan);
        // 附件
        decisionPlanAttachmentHelper.deleteByPlanId(id);
        saveAttachment(id, request.getAttaches());
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteDecisionPlan(Long id) {
        log.info("deleteDecisionPlan ... [{}]", id);
        decisionPlanHelper.removeById(id);
        decisionPlanAttachmentHelper.deleteByPlanId(id);
        return id;
    }

    @Override
    public DecisionPlanDetailVO detailDecisionPlan(Long id) {
        log.info("detailDecisionPlan ... [{}]", id);
        DecisionPlan byId = decisionPlanHelper.getById(id);
        if (Objects.isNull(byId)) {
            return null;
        }
        DecisionPlanDetailVO vo = new DecisionPlanDetailVO();
        BeanUtils.copyProperties(byId, vo);
        // 数据字典名称
        Result<Map<String, DataDictionaryVO>> getNameByCodes = dataDictionaryFeignClient.getByCodes(new String[]{vo.getEventLevel(), vo.getEventType()});
        if (getNameByCodes.getSuccess()) {
            vo.setEventLevelName(getNameByCodes.getData().get(vo.getEventLevel()).getName());
            vo.setEventTypeName(getNameByCodes.getData().get(vo.getEventType()).getName());
        }
        // 附件
        List<DecisionPlanAttachment> attachments = decisionPlanAttachmentHelper.selectByPlanId(id);
        List<AttachmentVO> attaches = attachments.stream().map(this::convertToAttachmentVO).collect(Collectors.toList());
        vo.setAttaches(attaches);
        return vo;
    }

    @Override
    public PageResult<DecisionPlanVO> pageDecisionPlan(PageRequest request) {
        log.info("pageDecisionPlan ... [{}]", request.toString());
        IPage<DecisionPlan> page = decisionPlanHelper.pageSelect(request.getCurrent(), request.getSize());
        List<DecisionPlan> records = page.getRecords();

        if (!CollectionUtils.isEmpty(records)) {
            Set<String> eventLevels = records.stream().map(DecisionPlan::getEventLevel).collect(Collectors.toSet());
            Set<String> eventTypes = records.stream().map(DecisionPlan::getEventType).collect(Collectors.toSet());
            eventLevels.addAll(eventTypes);
            String[] codes = new String[eventLevels.size()];
            Result<Map<String, DataDictionaryVO>> dataDic = dataDictionaryFeignClient.getByCodes(eventLevels.toArray(codes));
            if (dataDic.getSuccess()) {
                Map<String, DataDictionaryVO> data = dataDic.getData();
                return PageUtil.page(page, decisionPlan -> convertDecisionPlanVO(decisionPlan, data));
            } else {
                return PageUtil.page(page, this::convertDecisionPlanVO);
            }
        } else {
            return PageUtil.page(page, this::convertDecisionPlanVO);
        }
    }

    private DecisionPlanVO convertDecisionPlanVO(DecisionPlan plan, Map<String, DataDictionaryVO> data) {
        DecisionPlanVO result = Convert.convert(DecisionPlanVO.class, plan);
        DataDictionaryVO eventTypeDataDic = data.get(result.getEventType());
        String eventTypeName = Objects.isNull(eventTypeDataDic) ? "" : eventTypeDataDic.getName();
        result.setEventTypeName(eventTypeName);

        DataDictionaryVO eventLevelDataDic = data.get(result.getEventLevel());
        String eventLevelName = Objects.isNull(eventLevelDataDic) ? "" : eventLevelDataDic.getName();
        result.setEventLevelName(eventLevelName);
        // 附件
        List<DecisionPlanAttachment> attachments = decisionPlanAttachmentHelper.selectByPlanId(plan.getId());
        List<AttachmentVO> attaches = attachments.stream().map(this::convertToAttachmentVO).collect(Collectors.toList());
        result.setAttaches(attaches);
        return result;
    }

    private DecisionPlanVO convertDecisionPlanVO(DecisionPlan plan) {
        return  Convert.convert(DecisionPlanVO.class, plan);
    }

    private void saveAttachment(Long planId, List<AttachmentRequest> attaches) {
        if (!CollectionUtils.isEmpty(attaches)) {
            Set<DecisionPlanAttachment> attachments = attaches.stream()
                    .map(attach -> createDecisionPlanAttachment(planId, attach))
                    .collect(Collectors.toSet());
            decisionPlanAttachmentHelper.saveBatch(attachments);
        }
    }

    private DecisionPlanAttachment createDecisionPlanAttachment(long planId, AttachmentRequest request) {
        DecisionPlanAttachment attachment = new DecisionPlanAttachment();
        BeanUtils.copyProperties(request, attachment);
        attachment.setDecisionPlanId(planId);
        return attachment;
    }

    private AttachmentVO convertToAttachmentVO(DecisionPlanAttachment attachment) {
        return Convert.convert(AttachmentVO.class, attachment);
    }
}
