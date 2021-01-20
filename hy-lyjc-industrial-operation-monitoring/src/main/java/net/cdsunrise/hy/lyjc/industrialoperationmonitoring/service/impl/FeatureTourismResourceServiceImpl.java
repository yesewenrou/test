package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.FeaturedFeignClient;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.FeaturedGoodsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureTourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.TravelRelatedResourceTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.FeatureTourismResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeatureLabelService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeatureTourismResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.MerchantLabelFeignClient;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.req.MerchantLabelReq;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binke Zhang
 * @date 2019/11/13 16:14
 */
@Service
@Slf4j
public class FeatureTourismResourceServiceImpl extends ServiceImpl<FeatureTourismResourceMapper, FeatureTourismResource> implements IFeatureTourismResourceService {

    private final FeatureTourismResourceMapper featureTourismResourceMapper;
    private final IFeatureLabelService featureLabelService;
    private final RecordService recordService;
    private MerchantLabelFeignClient merchantLabelFeignClient;
    private FeaturedFeignClient featuredFeignClient;

    @Autowired
    public FeatureTourismResourceServiceImpl(FeatureTourismResourceMapper featureTourismResourceMapper,
                                             IFeatureLabelService featureLabelService,
                                             RecordService recordService, MerchantLabelFeignClient merchantLabelFeignClient, FeaturedFeignClient featuredFeignClient) {
        this.featureTourismResourceMapper = featureTourismResourceMapper;
        this.featureLabelService = featureLabelService;
        this.recordService = recordService;
        this.merchantLabelFeignClient = merchantLabelFeignClient;
        this.featuredFeignClient = featuredFeignClient;
    }

    @Override
    public boolean save(FeatureTourismResourceVO vo) {
        FeatureTourismResource featureTourismResource = new FeatureTourismResource();
        BeanUtils.copyProperties(vo, featureTourismResource);
        // 设置标签名称
        String[] strings = vo.getFeatureLabelIds().split(",");
        List<Long> ids = Arrays.stream(strings).map(e -> Long.valueOf(e)).collect(Collectors.toList());
        featureTourismResource.setFeatureLabelNames(featureLabelService.getLabelNames(ids));
        boolean success = featureTourismResourceMapper.insert(featureTourismResource) > 0;
        if (success) {
            try {
                recordService.insert(OperationEnum.FEATURE_TOURISM_RESOURCE, featureTourismResource, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {
            }
        }
        return success;
    }

    @Override
    public boolean update(FeatureTourismResourceUpdateVO vo) {
        FeatureTourismResource old = featureTourismResourceMapper.selectById(vo.getId());
        FeatureTourismResource present = new FeatureTourismResource();
        BeanUtils.copyProperties(old, present);
        BeanUtils.copyProperties(vo, present);
        boolean success = featureTourismResourceMapper.updateById(present) > 0;
        if (success) {
            try {
                recordService.update(OperationEnum.FEATURE_TOURISM_RESOURCE, old, present, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {
            }
        }
        return success;
    }

    @Override
    public boolean delete(Long id) {
        FeatureTourismResource old = featureTourismResourceMapper.selectById(id);
        boolean success = featureTourismResourceMapper.deleteById(id) > 0;
        if (success) {
            try {
                recordService.delete(OperationEnum.FEATURE_TOURISM_RESOURCE, old, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {
            }
        }
        return success;
    }

    @Override
    public IPage<FeatureTourismResource> list(PageRequest<FeatureTourismResourceCondition> pageRequest) {
        IPage<FeatureTourismResource> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        FeatureTourismResourceCondition condition = pageRequest.getCondition();
        if (condition == null) {
            condition = new FeatureTourismResourceCondition();
        }
        QueryWrapper<FeatureTourismResource> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotEmpty(condition.getRegionalCode()), FeatureTourismResource::getRegionalCode, condition.getRegionalCode())
                .eq(StringUtils.isNotEmpty(condition.getResourceTypeCode()), FeatureTourismResource::getResourceTypeCode, condition.getResourceTypeCode())
                .like(StringUtils.isNotEmpty(condition.getResourceName()), FeatureTourismResource::getResourceName, condition.getResourceName())
                .like(condition.getFeatureLabel() != null, FeatureTourismResource::getFeatureLabelIds, condition.getFeatureLabel());
        return featureTourismResourceMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<FeatureTourismResource> page(PageRequest<FeatureTourismResourceCondition> pageRequest) {
        FeatureTourismResourceCondition condition = pageRequest.getCondition();
        String resourceTypeCode = condition.getResourceTypeCode();
        TravelRelatedResourceTypeEnum typeEnum = TravelRelatedResourceTypeEnum.getByCode(resourceTypeCode);
        if (typeEnum == null) {
            throw new ParamErrorException("资源类型不正确");
        }

        switch (typeEnum) {
            // 特色商品
            case GOODS:
                return listGoods(pageRequest);
            // 餐饮
            // 住宿
            // 娱乐
            // 购物
            case CATERING:
            case ACCOMMODATION:
            case ENTERTAINMENT:
            case SHOP:
                return listMerchant(pageRequest, typeEnum);
            default:
                Page<FeatureTourismResource> pageResult = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
                pageResult.setRecords(new ArrayList<>());
                pageResult.setTotal(0);
                pageResult.setPages(0);
                return pageResult;
        }
    }

    private IPage<FeatureTourismResource> listMerchant(PageRequest<FeatureTourismResourceCondition> pageRequest, TravelRelatedResourceTypeEnum typeEnum) {
        MerchantLabelReq merchantLabelReq = new MerchantLabelReq();
        merchantLabelReq.setPage(pageRequest.getCurrent());
        merchantLabelReq.setSize(pageRequest.getSize());
        merchantLabelReq.setResourceTypeCode(typeEnum.getMerchantType());
        FeatureTourismResourceCondition condition = pageRequest.getCondition();
        String featureLabel = condition.getFeatureLabel();
        if (StringUtils.isNotEmpty(featureLabel)) {
            merchantLabelReq.setFeatureLabel(Long.valueOf(featureLabel));
        }
        merchantLabelReq.setRegionalCode(condition.getRegionalCode());
        merchantLabelReq.setResourceName(condition.getResourceName());
        Result<Page<net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.FeatureTourismResourceVO>> result
                = merchantLabelFeignClient.list(merchantLabelReq);
        return result.getData().convert(featureTourismResourceVO -> {
            FeatureTourismResource featureTourismResource = new FeatureTourismResource();
            featureTourismResource.setId(featureTourismResourceVO.getId());
            featureTourismResource.setResourceName(featureTourismResourceVO.getMerchantName());
            featureTourismResource.setResourceTypeCode(typeEnum.getCode());
            featureTourismResource.setResourceTypeName(typeEnum.getName());
            featureTourismResource.setRegionalCode(featureTourismResourceVO.getRegionalCode());
            featureTourismResource.setRegionalName(featureTourismResourceVO.getRegionalName());
            featureTourismResource.setAddressDetail(featureTourismResourceVO.getBusinessAddress());
            featureTourismResource.setFeatureLabelIds(featureTourismResourceVO.getFeatureLabelIds());
            featureTourismResource.setFeatureLabelNames(featureTourismResourceVO.getFeatureLabelNames());
            featureTourismResource.setUpdateTime(featureTourismResourceVO.getModifyTime());
            featureTourismResource.setLongitude(featureTourismResourceVO.getLongitude());
            featureTourismResource.setLatitude(featureTourismResourceVO.getLatitude());
            return featureTourismResource;
        });
    }

    private IPage<FeatureTourismResource> listGoods(PageRequest<FeatureTourismResourceCondition> pageRequest) {
        FeatureTourismResourceCondition condition = pageRequest.getCondition();
        Result<PageResult<FeaturedGoodsVO>> featuredPage = featuredFeignClient.getFeaturedPage(
                pageRequest.getCurrent().intValue(),
                pageRequest.getSize().intValue(),
                null,
                condition.getRegionalCode()
        );
        PageResult<FeaturedGoodsVO> data = featuredPage.getData();
        List<FeaturedGoodsVO> list = data.getList();
        List<FeatureTourismResource> collect = list.stream().map(featuredGoodsVO -> {
            FeatureTourismResource featureTourismResource = new FeatureTourismResource();
            featureTourismResource.setId(featuredGoodsVO.getId());
            featureTourismResource.setResourceName(featuredGoodsVO.getCompanyName());
            featureTourismResource.setResourceTypeCode(TravelRelatedResourceTypeEnum.GOODS.getCode());
            featureTourismResource.setResourceTypeName(TravelRelatedResourceTypeEnum.GOODS.getName());
            featureTourismResource.setRegionalCode(featuredGoodsVO.getBelongArea());
            featureTourismResource.setRegionalName(featuredGoodsVO.getBelongArea());
            featureTourismResource.setAddressDetail(featuredGoodsVO.getAddress());
            featureTourismResource.setFeatureLabelIds(null);
            List<String> tagNames = featuredGoodsVO.getTagNames();
            featureTourismResource.setFeatureLabelNames(String.join(",", tagNames));
            featureTourismResource.setLongitude(featuredGoodsVO.getLongitude());
            featureTourismResource.setLatitude(featuredGoodsVO.getLatitude());
            return featureTourismResource;
        }).collect(Collectors.toList());
        Page<FeatureTourismResource> pageResult = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        pageResult.setRecords(collect);
        pageResult.setTotal(data.getTotalCount());
        pageResult.setPages(data.getTotalPages());
        return pageResult;
    }
}
