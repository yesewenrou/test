package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/22 11:11
 */
@Service
public class EmergencyResourceServiceImpl extends ServiceImpl<EmergencyResourceMapper, EmergencyResource> implements IEmergencyResourceService {

    /**
     * 可排序字段
     */
    private static final List<String> ORDER_COLUMNS = Arrays.asList("name", "type", "inventory", "gmt_modified");

    private DataDictionaryFeignClient dataDictionaryFeignClient;

    public EmergencyResourceServiceImpl(DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    @Override
    public Long saveEmergencyResource(EmergencyResourceVO emergencyResourceVO) {
        Map<String, DataDictionaryVO> allEmergencyResourceType = getAllEmergencyResourceType();
        checkType(allEmergencyResourceType, emergencyResourceVO.getType());
        EmergencyResource emergencyResource = new EmergencyResource();
        BeanUtils.copyProperties(emergencyResourceVO, emergencyResource, "id", "gmtModified");
        super.save(emergencyResource);
        return emergencyResource.getId();
    }

    @Override
    public boolean deleteEmergencyResource(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        return super.removeById(id);
    }

    @Override
    public boolean updateEmergencyResource(EmergencyResourceVO emergencyResourceVO) {
        Map<String, DataDictionaryVO> allEmergencyResourceType = getAllEmergencyResourceType();
        checkType(allEmergencyResourceType, emergencyResourceVO.getType());
        EmergencyResource emergencyResource = new EmergencyResource();
        BeanUtils.copyProperties(emergencyResourceVO, emergencyResource, "gmtModified");
        return super.updateById(emergencyResource);
    }

    @Override
    public EmergencyResourceVO getEmergencyResource(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        EmergencyResource emergencyResource = super.getById(id);
        if (emergencyResource == null) {
            throw new ParamErrorException("资源不存在");
        }
        EmergencyResourceVO emergencyResourceVO = new EmergencyResourceVO();
        BeanUtils.copyProperties(emergencyResource, emergencyResourceVO);
        Map<String, DataDictionaryVO> allEmergencyResourceType = getAllEmergencyResourceType();
        DataDictionaryVO dataDictionaryVO = getTypeDictionary(allEmergencyResourceType, emergencyResource.getType());
        if (dataDictionaryVO != null) {
            emergencyResourceVO.setTypeName(dataDictionaryVO.getName());
        }
        return emergencyResourceVO;
    }

    @Override
    public IPage<EmergencyResourceVO> listEmergencyResource(PageRequest<EmergencyResourceCondition> pageRequest) {
        //分页
        Page<EmergencyResource> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<EmergencyResource> queryWrapper = Wrappers.query();
        EmergencyResourceCondition condition = pageRequest.getCondition();
        //筛选
        if (condition != null) {
            queryWrapper.lambda()
                    .like(StringUtils.hasText(condition.getName()), EmergencyResource::getName, condition.getName())
                    .eq(StringUtils.hasText(condition.getType()), EmergencyResource::getType, condition.getType());
        }
        // 排序
        PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, pageRequest.getOrderItemList());
        super.page(page, queryWrapper);
        // 转换结果
        Map<String, DataDictionaryVO> allEmergencyResourceType = getAllEmergencyResourceType();
        return page.convert(emergencyResource -> {
            EmergencyResourceVO emergencyResourceVO = new EmergencyResourceVO();
            BeanUtils.copyProperties(emergencyResource, emergencyResourceVO);
            DataDictionaryVO dataDictionaryVO = getTypeDictionary(allEmergencyResourceType, emergencyResource.getType());
            if (dataDictionaryVO != null) {
                emergencyResourceVO.setTypeName(dataDictionaryVO.getName());
            }
            return emergencyResourceVO;
        });
    }

    private Map<String, DataDictionaryVO> getAllEmergencyResourceType() {
        String parentCode = "EMERGENCY_RESOURCE_TYPE";
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{parentCode});
        Map<String, DataDictionaryVO> data = result.getData();
        if (data == null || data.isEmpty()) {
            return null;
        }
        DataDictionaryVO dataDictionaryVO = data.get(parentCode);
        return dataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, item -> item));
    }

    private DataDictionaryVO getTypeDictionary(Map<String, DataDictionaryVO> allTypes, String type) {
        if (allTypes == null || allTypes.isEmpty()) {
            return null;
        }
        return allTypes.get(type);
    }

    private void checkType(Map<String, DataDictionaryVO> allTypes, String type) {
        DataDictionaryVO dataDictionaryVO = getTypeDictionary(allTypes, type);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("未知的资源类型编码");
        }
    }
}
