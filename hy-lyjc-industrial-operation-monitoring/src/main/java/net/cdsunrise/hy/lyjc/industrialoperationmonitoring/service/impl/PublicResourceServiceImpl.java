package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.PublicResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.PublicResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.PublicResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceVO;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
public class PublicResourceServiceImpl implements PublicResourceService{

    @Autowired
    private PublicResourceMapper resourceMapper;
    @Autowired
    private DataDictionaryFeignClient feignClient;
    @Autowired
    private RecordService recordService;

    @Override
    public Long add(ResourceVO resourceVO) {
        PublicResource publicResource = new PublicResource();
        BeanUtils.copyProperties(resourceVO,publicResource);
        resourceMapper.insert(publicResource);
        // 记录操作日志
        resourceVO = dictionaryTransfer(resourceVO,resourceVO.getArea(),resourceVO.getType());
        recordService.insert(OperationEnum.PUBLIC_MANAGE,resourceVO, CustomContext.getTokenInfo().getUser().getId());
        return publicResource.getId();
    }

    private ResourceVO dictionaryTransfer(ResourceVO resourceVO,String areaCode,String typeCode){
        String[] codeParam = {areaCode,typeCode};
        Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
        resourceVO.setArea(map.get(areaCode).getName());
        resourceVO.setType(map.get(typeCode).getName());
        return resourceVO;
    }

    @Override
    public void update(ResourceVO resourceVO) {
        PublicResource extPublicResource = resourceMapper.selectById(resourceVO.getId());
        PublicResource publicResource = new PublicResource();
        BeanUtils.copyProperties(resourceVO,publicResource);
        resourceMapper.updateById(publicResource);
        // 记录操作日志
        ResourceVO extResourceVO = new ResourceVO();
        BeanUtils.copyProperties(extPublicResource,extResourceVO);
        extResourceVO = dictionaryTransfer(extResourceVO,extPublicResource.getArea(),extPublicResource.getType());
        resourceVO = dictionaryTransfer(resourceVO,resourceVO.getArea(),resourceVO.getType());
        recordService.update(OperationEnum.PUBLIC_MANAGE,extResourceVO,resourceVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public void delete(Long id) {
        PublicResource publicResource = resourceMapper.selectById(id);
        ResourceVO resourceVO = new ResourceVO();
        BeanUtils.copyProperties(publicResource,resourceVO);
        resourceMapper.deleteById(id);
        // 记录操作日志
        recordService.delete(OperationEnum.PUBLIC_MANAGE,resourceVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public PublicResource findById(Long id) {
        return resourceMapper.selectById(id);
    }

    @Override
    public Page<PublicResource> list(PageRequest<ResourceCondition> pageRequest) {
        Page<PublicResource> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        ResourceCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new ResourceCondition();
        }
        // 批量导入数据，造成updateTime是一致的，因此再增加一个ID字段，保证排序分页准确性
        QueryWrapper<PublicResource> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(condition.getName()),PublicResource::getName,condition.getName())
                .eq(StringUtils.isNotEmpty(condition.getType()),PublicResource::getType,condition.getType())
                .orderByDesc(PublicResource::getUpdateTime).orderByDesc(PublicResource::getId);
        IPage<PublicResource> pageResource =  resourceMapper.selectPage(page,wrapper);
        Long count = pageResource.getTotal();
        List<PublicResource> resourceList = pageResource.getRecords();
        List<PublicResource> collect = resourceList.stream().peek(publicResource -> {
            // 通过数据字典进行编码转换
            String[] codeParam = {publicResource.getArea(),publicResource.getType()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            publicResource.setArea(map.get(publicResource.getArea())!=null?map.get(publicResource.getArea()).getName():null);
            publicResource.setType(map.get(publicResource.getType())!=null?map.get(publicResource.getType()).getName():null);
        }).collect(Collectors.toList());
        page.setTotal(count);
        page.setRecords(collect);
        return page;
    }
}
