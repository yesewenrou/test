package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyDrill;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyDrillAttachment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyDrillMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyDrillAttachmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyDrillService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:50
 */
@Service
public class EmergencyDrillServiceImpl extends ServiceImpl<EmergencyDrillMapper, EmergencyDrill> implements IEmergencyDrillService {

    private static final String EVENT_TYPE_PARENT_CODE = "EMERGENCY_EVENT_TYPE";
    private static final String EVENT_LEVEL_PARENT_CODE = "EMERGENCY_EVENT_LEVEL";
    private static final List<String> ORDER_COLUMNS = Arrays.asList("drill_date", "event_type", "event_level", "gmt_create");


    private IEmergencyDrillAttachmentService emergencyDrillAttachmentService;
    private DataDictionaryFeignClient dataDictionaryFeignClient;

    public EmergencyDrillServiceImpl(IEmergencyDrillAttachmentService emergencyDrillAttachmentService, DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.emergencyDrillAttachmentService = emergencyDrillAttachmentService;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEmergencyDrill(EmergencyDrillVO emergencyDrillVO) {
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyDrillVO);
        EmergencyDrill emergencyDrill = new EmergencyDrill();
        BeanUtils.copyProperties(emergencyDrillVO, emergencyDrill, "id", "gmtCreate");
        // 存储
        super.save(emergencyDrill);
        // 附件
        List<EmergencyDrillAttachment> emergencyDrillAttachmentList = getAttachments(emergencyDrill.getId(), emergencyDrillVO);
        emergencyDrillAttachmentService.saveBatch(emergencyDrillAttachmentList);

        return emergencyDrill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEmergencyDrill(Long id) {
        if (id == null) {
            throw new ParamErrorException("演练ID不能为空");
        }
        // 删除基础信息
        boolean success = super.removeById(id);
        // 删除附件
        if (success) {
            QueryWrapper<EmergencyDrillAttachment> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(EmergencyDrillAttachment::getDrillId, id);
            emergencyDrillAttachmentService.remove(queryWrapper);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEmergencyDrill(EmergencyDrillVO emergencyDrillVO) {
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        checkEventTypeAndLevel(allEmergencyEventTypeAndLevel, emergencyDrillVO);
        EmergencyDrill emergencyDrill = new EmergencyDrill();
        BeanUtils.copyProperties(emergencyDrillVO, emergencyDrill, "gmtCreate");
        // 附件
        List<EmergencyDrillAttachment> emergencyDrillAttachmentList = getAttachments(emergencyDrill.getId(), emergencyDrillVO);
        // 更新
        boolean success = super.updateById(emergencyDrill);
        if (success) {
            // 删除旧附件
            QueryWrapper<EmergencyDrillAttachment> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(EmergencyDrillAttachment::getDrillId, emergencyDrillVO.getId());
            emergencyDrillAttachmentService.remove(queryWrapper);
            // 重新存入附件
            emergencyDrillAttachmentService.saveBatch(emergencyDrillAttachmentList);
        }

        return success;
    }

    @Override
    public EmergencyDrillVO getEmergencyDrill(Long id) {
        if (id == null) {
            throw new ParamErrorException("演练ID不能为空");
        }
        // 基础信息
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        EmergencyDrill emergencyDrill = super.getById(id);
        if (emergencyDrill == null) {
            throw new ParamErrorException("演练不存在");
        }

        return convertToVO(allEmergencyEventTypeAndLevel, emergencyDrill);
    }

    @Override
    public IPage<EmergencyDrillVO> listEmergencyDrill(PageRequest<EmergencyDrillCondition> pageRequest) {
        // 分页
        Page<EmergencyDrill> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<EmergencyDrill> queryWrapper = Wrappers.query();
        EmergencyDrillCondition condition = pageRequest.getCondition();
        //筛选
        if (condition != null) {
            queryWrapper.lambda()
                    .like(StringUtils.hasText(condition.getDrillTitle()), EmergencyDrill::getDrillTitle, condition.getDrillTitle())
                    .ge(condition.getStartDrillDate() != null, EmergencyDrill::getDrillDate, condition.getStartDrillDate())
                    .le(condition.getEndDrillDate() != null, EmergencyDrill::getDrillDate, condition.getEndDrillDate())
                    .eq(StringUtils.hasText(condition.getEventType()), EmergencyDrill::getEventType, condition.getEventType())
                    .eq(StringUtils.hasText(condition.getEventLevel()), EmergencyDrill::getEventLevel, condition.getEventLevel());
        }
        // 排序
        PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, pageRequest.getOrderItemList());
        super.page(page, queryWrapper);
        // 转换结果
        Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel = getAllEmergencyEventTypeAndLevel();
        return page.convert(emergencyDrill -> convertToVO(allEmergencyEventTypeAndLevel, emergencyDrill));
    }

    private EmergencyDrillVO convertToVO(Map<String, Map<String, DataDictionaryVO>> allEmergencyEventTypeAndLevel, EmergencyDrill emergencyDrill) {
        EmergencyDrillVO emergencyDrillVO = new EmergencyDrillVO();
        BeanUtils.copyProperties(emergencyDrill, emergencyDrillVO);
        DataDictionaryVO eventType = getEventType(allEmergencyEventTypeAndLevel, emergencyDrill.getEventType());
        if (eventType != null) {
            emergencyDrillVO.setEventTypeDesc(eventType.getName());
        }
        DataDictionaryVO eventLevel = getEventLevel(allEmergencyEventTypeAndLevel, emergencyDrill.getEventLevel());
        if (eventLevel != null) {
            emergencyDrillVO.setEventLevelDesc(eventLevel.getName());
        }
        // 附件
        List<EmergencyDrillVO.Attachment> attachmentVos = getAttachmentVos(emergencyDrill);
        emergencyDrillVO.setAttachments(attachmentVos);

        return emergencyDrillVO;
    }

    private Map<String, Map<String, DataDictionaryVO>> getAllEmergencyEventTypeAndLevel() {
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{EVENT_TYPE_PARENT_CODE, EVENT_LEVEL_PARENT_CODE});
        Map<String, DataDictionaryVO> data = result.getData();
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("事件类型及事件等级未在数据字典定义");
        }
        Map<String, Map<String, DataDictionaryVO>> map = new HashMap<>(2);
        DataDictionaryVO eventTypeDataDictionaryVO = data.get(EVENT_TYPE_PARENT_CODE);
        if (eventTypeDataDictionaryVO == null) {
            throw new RuntimeException("事件类型未在数据字典定义");
        }
        DataDictionaryVO eventLevelDataDictionaryVO = data.get(EVENT_LEVEL_PARENT_CODE);
        if (eventLevelDataDictionaryVO == null) {
            throw new RuntimeException("事件等级未在数据字典定义");
        }
        map.put(EVENT_LEVEL_PARENT_CODE, eventLevelDataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, item -> item)));
        map.put(EVENT_TYPE_PARENT_CODE, eventTypeDataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, item -> item)));
        return map;
    }

    private DataDictionaryVO getDictionary(Map<String, DataDictionaryVO> allTypes, String type) {
        if (allTypes == null || allTypes.isEmpty()) {
            return null;
        }
        return allTypes.get(type);
    }

    private DataDictionaryVO getEventType(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        Map<String, DataDictionaryVO> allTypes = map.get(EVENT_TYPE_PARENT_CODE);
        return getDictionary(allTypes, type);
    }

    private DataDictionaryVO getEventLevel(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        Map<String, DataDictionaryVO> allTypes = map.get(EVENT_LEVEL_PARENT_CODE);
        return getDictionary(allTypes, type);
    }

    private void checkEventType(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        DataDictionaryVO dataDictionaryVO = getEventType(map, type);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("未知的事件类型编码: " + type);
        }
    }

    private void checkEventLevel(Map<String, Map<String, DataDictionaryVO>> map, String type) {
        DataDictionaryVO dataDictionaryVO = getEventLevel(map, type);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("未知的事件等级编码: " + type);
        }
    }

    private void checkEventTypeAndLevel(Map<String, Map<String, DataDictionaryVO>> map, EmergencyDrillVO emergencyDrillVO) {
        String eventType = emergencyDrillVO.getEventType();
        checkEventType(map, eventType);
        String eventLevel = emergencyDrillVO.getEventLevel();
        checkEventLevel(map, eventLevel);
    }

    private List<EmergencyDrillAttachment> getAttachments(Long drillId, EmergencyDrillVO emergencyDrillVO) {
        List<EmergencyDrillVO.Attachment> attachments = emergencyDrillVO.getAttachments();
        return attachments.stream().map(attachment -> {
            EmergencyDrillAttachment emergencyDrillAttachment = new EmergencyDrillAttachment();
            BeanUtils.copyProperties(attachment, emergencyDrillAttachment, "id");
            emergencyDrillAttachment.setDrillId(drillId);
            return emergencyDrillAttachment;
        }).collect(Collectors.toList());
    }

    private List<EmergencyDrillVO.Attachment> getAttachmentVos(EmergencyDrill emergencyDrill) {
        QueryWrapper<EmergencyDrillAttachment> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(EmergencyDrillAttachment::getDrillId, emergencyDrill.getId());
        List<EmergencyDrillAttachment> emergencyDrillAttachments = emergencyDrillAttachmentService.list(queryWrapper);
        if (CollectionUtils.isEmpty(emergencyDrillAttachments)) {
            return null;
        }
        return emergencyDrillAttachments.stream().map(emergencyDrillAttachment -> {
            EmergencyDrillVO.Attachment attachment = new EmergencyDrillVO.Attachment();
            BeanUtils.copyProperties(emergencyDrillAttachment, attachment);
            return attachment;
        }).collect(Collectors.toList());
    }
}
