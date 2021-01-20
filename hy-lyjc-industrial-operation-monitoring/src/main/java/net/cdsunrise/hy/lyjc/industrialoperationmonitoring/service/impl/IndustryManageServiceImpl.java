package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.IndustryManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.IndustryManageMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IndustryManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.IndustryManageVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;
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
public class IndustryManageServiceImpl implements IndustryManageService{

    @Autowired
    private IndustryManageMapper manageMapper;
    @Autowired
    private DataDictionaryFeignClient feignClient;
    @Autowired
    private RecordService recordService;

    @Override
    public Long add(IndustryManageVO industryManageVO) {
        IndustryManage industryManage = new IndustryManage();
        BeanUtils.copyProperties(industryManageVO,industryManage);
        manageMapper.insert(industryManage);
        // 记录操作日志
        industryManageVO = dictionaryTransfer(industryManageVO,industryManageVO.getArea(),industryManageVO.getType());
        recordService.insert(OperationEnum.INDUSTRY_MANAGE,industryManageVO, CustomContext.getTokenInfo().getUser().getId());
        return industryManage.getId();
    }

    private IndustryManageVO dictionaryTransfer(IndustryManageVO industryManageVO,String areaCode,String typeCode){
        String[] codeParam = {areaCode,typeCode};
        Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
        industryManageVO.setArea(map.get(areaCode).getName());
        industryManageVO.setType(map.get(typeCode).getName());
        return industryManageVO;
    }

    @Override
    public void update(IndustryManageVO industryManageVO) {
        IndustryManage extIndustryManage = manageMapper.selectById(industryManageVO.getId());
        IndustryManage industryManage = new IndustryManage();
        BeanUtils.copyProperties(industryManageVO,industryManage);
        manageMapper.updateById(industryManage);
        // 记录操作日志
        IndustryManageVO extIndustryManageVO = new IndustryManageVO();
        BeanUtils.copyProperties(extIndustryManage,extIndustryManageVO);
        extIndustryManageVO = dictionaryTransfer(extIndustryManageVO,extIndustryManage.getArea(),extIndustryManage.getType());
        industryManageVO = dictionaryTransfer(industryManageVO,industryManageVO.getArea(),industryManageVO.getType());
        recordService.update(OperationEnum.INDUSTRY_MANAGE,extIndustryManageVO,industryManageVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public void delete(Long id) {
        IndustryManage industryManage = manageMapper.selectById(id);
        IndustryManageVO industryManageVO = new IndustryManageVO();
        BeanUtils.copyProperties(industryManage,industryManageVO);
        manageMapper.deleteById(id);
        // 记录操作日志
        recordService.delete(OperationEnum.INDUSTRY_MANAGE,industryManageVO,CustomContext.getTokenInfo().getUser().getId());
    }

    @Override
    public IndustryManage findById(Long id) {
        return manageMapper.selectById(id);
    }

    @Override
    public Page<IndustryManage> list(PageRequest<ResourceCondition> pageRequest) {
        Page<IndustryManage> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        ResourceCondition condition = pageRequest.getCondition();
        if (condition == null){
            condition = new ResourceCondition();
        }
        // 批量导入数据，造成updateTime是一致的，因此再增加一个ID字段，保证排序分页准确性
        QueryWrapper<IndustryManage> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(condition.getName()),IndustryManage::getName,condition.getName())
                .eq(StringUtils.isNotEmpty(condition.getType()),IndustryManage::getType,condition.getType())
                .orderByDesc(IndustryManage::getUpdateTime).orderByDesc(IndustryManage::getId);
        IPage<IndustryManage> pageResource =  manageMapper.selectPage(page,wrapper);
        Long count = pageResource.getTotal();
        List<IndustryManage> resourceList = pageResource.getRecords();
        List<IndustryManage> collect = resourceList.stream().peek(industryManage -> {
            // 通过数据字典进行编码转换
            String[] codeParam = {industryManage.getArea(),industryManage.getType()};
            Map<String, DataDictionaryVO> map = feignClient.getByCodes(codeParam).getData();
            industryManage.setArea(map.get(industryManage.getArea())!=null?map.get(industryManage.getArea()).getName():null);
            industryManage.setType(map.get(industryManage.getType())!=null?map.get(industryManage.getType()).getName():null);
        }).collect(Collectors.toList());
        page.setTotal(count);
        page.setRecords(collect);
        return page;
    }
}
