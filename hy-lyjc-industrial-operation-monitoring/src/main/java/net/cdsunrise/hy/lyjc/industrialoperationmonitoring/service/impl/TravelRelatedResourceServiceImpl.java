package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.TravelRelatedResourcesFeignClient;
import net.cdsunrise.hy.lydsjdatacenter.starter.reqeust.PageRequest;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MerchantInfoReq;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MerchantInfoVoExt;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.TravelRelatedResourceTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITravelRelatedResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TravelRelatedResourceVO;
import org.springframework.stereotype.Service;

/**
 * @author lijiafeng
 * @date 2020/1/16 12:58
 */
@Service
public class TravelRelatedResourceServiceImpl implements ITravelRelatedResourceService {

    private TravelRelatedResourcesFeignClient travelRelatedResourcesFeignClient;

    public TravelRelatedResourceServiceImpl(TravelRelatedResourcesFeignClient travelRelatedResourcesFeignClient) {
        this.travelRelatedResourcesFeignClient = travelRelatedResourcesFeignClient;
    }

    @Override
    public Result<PageResult<TravelRelatedResourceVO>> listTravelRelatedResources(int page, int size, String type, String name, String region) {
        PageRequest<MerchantInfoReq> req = buildMerchantInfoPageRequest(page, size, name, region);
        TravelRelatedResourceTypeEnum typeEnum = checkResourceType(type);
        switch (typeEnum) {
            // 精品景区
            case SCENIC:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getBoutiqueScenic(page, size, name, region),
                        boutiqueScenicVO -> {
                            TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
                            travelRelatedResourceVO.setId(boutiqueScenicVO.getId());
                            travelRelatedResourceVO.setName(boutiqueScenicVO.getScenicName());
                            travelRelatedResourceVO.setRegion(boutiqueScenicVO.getBelongArea());
                            travelRelatedResourceVO.setType(typeEnum.getName());
                            travelRelatedResourceVO.setAddress(boutiqueScenicVO.getDetailAddress());
                            travelRelatedResourceVO.setPhoneNumber(boutiqueScenicVO.getServerPhone());
                            return travelRelatedResourceVO;
                        }
                );
            // 文物保护点
            case CULTURAL:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getCulturalProtectionPoints(page, size, name, null, region),
                        culturalProtectionPointVO -> {
                            TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
                            travelRelatedResourceVO.setId(culturalProtectionPointVO.getId());
                            travelRelatedResourceVO.setName(culturalProtectionPointVO.getCulturalProtectionUnitName());
                            travelRelatedResourceVO.setRegion(culturalProtectionPointVO.getBelongArea());
                            travelRelatedResourceVO.setType(typeEnum.getName());
                            travelRelatedResourceVO.setAddress(culturalProtectionPointVO.getAddress());
                            travelRelatedResourceVO.setPhoneNumber(null);
                            return travelRelatedResourceVO;
                        }
                );
            // 购物场所
            case SHOP:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getShopList(req),
                        merchantInfoVoExt -> convertMerchant(typeEnum.getName(), merchantInfoVoExt)
                );
            // 特色商品
            case GOODS:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getFeatureGoods(page, size, name),
                        featuredGoodsVO -> {
                            TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
                            travelRelatedResourceVO.setId(featuredGoodsVO.getId());
                            travelRelatedResourceVO.setName(featuredGoodsVO.getCompanyName());
                            travelRelatedResourceVO.setRegion(null);
                            travelRelatedResourceVO.setType(typeEnum.getName());
                            travelRelatedResourceVO.setAddress(featuredGoodsVO.getAddress());
                            travelRelatedResourceVO.setPhoneNumber(featuredGoodsVO.getContactPhone());
                            return travelRelatedResourceVO;
                        }
                );
            // 娱乐场所
            case ENTERTAINMENT:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getEntertainmentList(req),
                        merchantInfoVoExt -> convertMerchant(typeEnum.getName(), merchantInfoVoExt)
                );
            // 餐饮行业
            case CATERING:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getCateringList(req),
                        merchantInfoVoExt -> convertMerchant(typeEnum.getName(), merchantInfoVoExt)
                );
            // 酒店住宿
            case ACCOMMODATION:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getAccommodationList(req),
                        merchantInfoVoExt -> convertMerchant(typeEnum.getName(), merchantInfoVoExt)
                );
            // 旅行社
            case AGENCY:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getTravelAgencies(page, size, name, region),
                        travelAgencyVO -> {
                            TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
                            travelRelatedResourceVO.setId(travelAgencyVO.getId());
                            travelRelatedResourceVO.setName(travelAgencyVO.getTravelAgencyName());
                            travelRelatedResourceVO.setRegion(travelAgencyVO.getBelongArea());
                            travelRelatedResourceVO.setType(typeEnum.getName());
                            travelRelatedResourceVO.setAddress(travelAgencyVO.getAddress());
                            travelRelatedResourceVO.setPhoneNumber(travelAgencyVO.getContactPhone());
                            return travelRelatedResourceVO;
                        }
                );
            // 涉旅企业
            case ENTERPRISES:
                return PageUtil.convertResult(
                        travelRelatedResourcesFeignClient.getTravelRelatedEnterprises(page, size, name, region, null),
                        travelRelatedEnterprisesVO -> {
                            TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
                            travelRelatedResourceVO.setId(travelRelatedEnterprisesVO.getId());
                            travelRelatedResourceVO.setName(travelRelatedEnterprisesVO.getEnterpriseName());
                            travelRelatedResourceVO.setRegion(travelRelatedEnterprisesVO.getBelongArea());
                            travelRelatedResourceVO.setType(typeEnum.getName());
                            travelRelatedResourceVO.setAddress(travelRelatedEnterprisesVO.getRunAddress());
                            travelRelatedResourceVO.setPhoneNumber(travelRelatedEnterprisesVO.getContactPhone());
                            return travelRelatedResourceVO;
                        }
                );
            default:
                throw new ParamErrorException("资源类型不正确");
        }
    }

    private PageRequest<MerchantInfoReq> buildMerchantInfoPageRequest(int page, int size, String name, String region) {
        PageRequest<MerchantInfoReq> req = new PageRequest<>();
        req.setCurrent((long) page);
        req.setSize((long) size);
        MerchantInfoReq merchantInfoReq = new MerchantInfoReq();
        merchantInfoReq.setMerchantName(name);
        merchantInfoReq.setRegionalCode(region);
        req.setCondition(merchantInfoReq);
        return req;
    }

    private TravelRelatedResourceVO convertMerchant(String type, MerchantInfoVoExt merchantInfoVoExt) {
        TravelRelatedResourceVO travelRelatedResourceVO = new TravelRelatedResourceVO();
        travelRelatedResourceVO.setId(merchantInfoVoExt.getId());
        travelRelatedResourceVO.setName(merchantInfoVoExt.getMerchantName());
        travelRelatedResourceVO.setRegion(merchantInfoVoExt.getRegionalName());
        travelRelatedResourceVO.setType(type);
        travelRelatedResourceVO.setAddress(merchantInfoVoExt.getBusinessAddress());
        travelRelatedResourceVO.setPhoneNumber(merchantInfoVoExt.getPhoneNumber());
        return travelRelatedResourceVO;
    }

    @Override
    public Result<?> getTravelRelatedResource(String type, Long id) {
        TravelRelatedResourceTypeEnum typeEnum = checkResourceType(type);
        switch (typeEnum) {
            // 精品景区
            case SCENIC:
                return travelRelatedResourcesFeignClient.getScenicById(id);
            // 文物保护点
            case CULTURAL:
                return travelRelatedResourcesFeignClient.getCulturalById(id);
            // 购物场所
            case SHOP:
                return travelRelatedResourcesFeignClient.getShopById(id);
            // 特色商品
            case GOODS:
                return travelRelatedResourcesFeignClient.getGoodsById(id);
            // 娱乐场所
            case ENTERTAINMENT:
                return travelRelatedResourcesFeignClient.getEntertainmentById(id);
            // 餐饮行业
            case CATERING:
                return travelRelatedResourcesFeignClient.getCateringById(id);
            // 酒店住宿
            case ACCOMMODATION:
                return travelRelatedResourcesFeignClient.getAccommodationById(id);
            // 旅行社
            case AGENCY:
                return travelRelatedResourcesFeignClient.getAgencyById(id);
            // 涉旅企业
            case ENTERPRISES:
                return travelRelatedResourcesFeignClient.getEnterprisesById(id);
            default:
                throw new ParamErrorException("资源类型不正确");
        }
    }

    private TravelRelatedResourceTypeEnum checkResourceType(String type) {
        TravelRelatedResourceTypeEnum typeEnum = TravelRelatedResourceTypeEnum.getByCode(type);
        if (typeEnum == null) {
            throw new ParamErrorException("资源类型不正确");
        }
        return typeEnum;
    }
}
