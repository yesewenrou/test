package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.IndustryManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.TourismResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismResourceService;
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
public class TourismResourceServiceImpl implements TourismResourceService {

    @Autowired
    private TourismResourceMapper resourceMapper;
    @Autowired
    private DataDictionaryFeignClient feignClient;
    @Autowired
    private RecordService recordService;

    @Override
    public Long add(ResourceVO resourceVO) {
        TourismResource tourismResource = new TourismResource();
        BeanUtils.copyProperties(resourceVO,tourismResource);
        resourceMapper.insert(tourismResource);
        // 记录操作日志
        resourceVO = dictionaryTransfer(resourceVO,resourceVO.getArea(),resourceVO.getType());
        recordService.insert(OperationEnum.TOURISM_MANAGE,resourceVO, CustomContext.getTokenInfo().getUser().getId());
        return tourismResource.getId();
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
        TourismResource extTourismResource = resourceMapper.selectById(resourceVO.getId());
        TourismResource tourismResource = new TourismResource();
        BeanUtils.copyProperties(resourceVO,tourismResource);
        resourceMapper.updateById(tourismResource);
        // 记录操作日志
        ResourceVO extResourceVO = new ResourceVO();
        BeanUtils.copyProperties(extTourismResource,extResourceVO);
        extResourceVO = dictionaryTransfer(extResourceVO,extTourismResource.getArea(),extTourismResource.getType());
        resourceVO = dictionaryTransfer(resourceVO,resourceVO.getArea(),resourceVO.getType());
        recordService.update(OperationEnum.TOURISM_MANAGE,extResourceVO,resourceVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public void delete(Long id) {
        TourismResource tourismResource = resourceMapper.selectById(id);
        ResourceVO resourceVO = new ResourceVO();
        BeanUtils.copyProperties(tourismResource,resourceVO);
        resourceMapper.deleteById(id);
        // 记录操作日志
        recordService.delete(OperationEnum.TOURISM_MANAGE,resourceVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public TourismResource findById(Long id) {
        return resourceMapper.selectById(id);
    }

    @Override
    public Page<TourismResource> list(PageRequest<ResourceCondition> pageRequest) {
        Page<TourismResource> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        ResourceCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new ResourceCondition();
        }
        // 批量导入数据，造成updateTime是一致的，因此再增加一个ID字段，保证排序分页准确性
        QueryWrapper<TourismResource> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(condition.getName()),TourismResource::getName,condition.getName())
                .eq(StringUtils.isNotEmpty(condition.getType()),TourismResource::getType,condition.getType())
                .orderByDesc(TourismResource::getUpdateTime).orderByDesc(TourismResource::getId);
        IPage<TourismResource> pageResource =  resourceMapper.selectPage(page,wrapper);
        Long count = pageResource.getTotal();
        List<TourismResource> resourceList = pageResource.getRecords();
        List<TourismResource> collect = resourceList.stream().peek(tourismResource -> {
            // 通过数据字典进行编码转换
            String[] codeParam = {tourismResource.getArea(),tourismResource.getType()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            tourismResource.setArea(map.get(tourismResource.getArea())!=null?map.get(tourismResource.getArea()).getName():null);
            tourismResource.setType(map.get(tourismResource.getType())!=null?map.get(tourismResource.getType()).getName():null);
        }).collect(Collectors.toList());
        page.setTotal(count);
        page.setRecords(collect);
        return page;
    }
}