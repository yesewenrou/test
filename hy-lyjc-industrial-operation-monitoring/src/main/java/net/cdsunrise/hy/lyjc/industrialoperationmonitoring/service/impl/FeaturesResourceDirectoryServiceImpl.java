package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.DataCenterCodeFeignClient;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.DataMenuVO;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MapAllListVO;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MerchantInfoVoExt;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.TravelRelatedResourceTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeaturesResourceDirectoryService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.MerchantLabelFeignClient;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.FeatureTourismResourceVO;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.MerchantInfo;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.MerchantLabelCountVo;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.vo.req.MerchantLabelReq;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2020/1/18 10:03
 */
@Service
public class FeaturesResourceDirectoryServiceImpl implements IFeaturesResourceDirectoryService {

    private DataCenterCodeFeignClient dataCenterCodeFeignClient;
    private MerchantLabelFeignClient merchantLabelFeignClient;

    public FeaturesResourceDirectoryServiceImpl(DataCenterCodeFeignClient dataCenterCodeFeignClient, MerchantLabelFeignClient merchantLabelFeignClient) {
        this.dataCenterCodeFeignClient = dataCenterCodeFeignClient;
        this.merchantLabelFeignClient = merchantLabelFeignClient;
    }

    @Override
    public Result<List<DataMenuVO>> getMenu() {
        Result<List<DataMenuVO>> menu = dataCenterCodeFeignClient.getMenu();
        String tourismResource = "tourismResource";
        DataMenuVO tourismResourceDataMenuVO = menu.getData().stream()
                .filter(dataMenuVO -> tourismResource.equals(dataMenuVO.getCode()))
                .findFirst()
                .orElse(buildTourismResourceDataMenuVO());
        Result<List<MerchantLabelCountVo>> merchantCount = merchantLabelFeignClient.count();
        List<MerchantLabelCountVo> data = merchantCount.getData();
        Map<String, Integer> collect = data.stream().collect(Collectors.toMap(MerchantLabelCountVo::getMerchantTypeCode, MerchantLabelCountVo::getCount));
        List<DataMenuVO.SubMenu> subMenuList = tourismResourceDataMenuVO.getSubMenuList();
        // 餐饮
        int cateringCount = collect.getOrDefault(TravelRelatedResourceTypeEnum.CATERING.getMerchantType(), 0);
        subMenuList.add(buildDataMenuVoSubMenu("catering", "特色餐饮", cateringCount));
        // 住宿
        int accommodationCount = collect.getOrDefault(TravelRelatedResourceTypeEnum.ACCOMMODATION.getMerchantType(), 0);
        subMenuList.add(buildDataMenuVoSubMenu("accommodation", "特色住宿", accommodationCount));
        // 娱乐
        int entertainmentCount = collect.getOrDefault(TravelRelatedResourceTypeEnum.ENTERTAINMENT.getMerchantType(), 0);
        subMenuList.add(buildDataMenuVoSubMenu("entertainment", "娱乐场所", entertainmentCount));
        // 购物
        int shopCount = collect.getOrDefault(TravelRelatedResourceTypeEnum.SHOP.getMerchantType(), 0);
        subMenuList.add(buildDataMenuVoSubMenu("shop", "购物场所", shopCount));

        return menu;
    }

    private DataMenuVO buildTourismResourceDataMenuVO() {
        DataMenuVO dataMenuVO = new DataMenuVO();
        dataMenuVO.setCode("tourismResource");
        dataMenuVO.setName("涉旅资源");
        dataMenuVO.setSubMenuList(new ArrayList<>());
        return dataMenuVO;
    }

    private DataMenuVO.SubMenu buildDataMenuVoSubMenu(String code, String name, Integer count) {
        DataMenuVO.SubMenu subMenu = new DataMenuVO.SubMenu();
        subMenu.setCode(code);
        subMenu.setName(name);
        subMenu.setCount(count);
        return subMenu;
    }

    @Override
    public Result<MapAllListVO> getAllList() {
        Result<MapAllListVO> allListResult = dataCenterCodeFeignClient.getAllList();
        MyMapAllListVO myMapAllListVO = new MyMapAllListVO();
        BeanUtils.copyProperties(allListResult.getData(), myMapAllListVO);
        Result<Page<FeatureTourismResourceVO>> result = merchantLabelFeignClient.list(buildMerchantLabelReq(null));
        List<FeatureTourismResourceVO> records = result.getData().getRecords();
        Map<String, List<FeatureTourismResourceVO>> merchantMap = records.stream().collect(Collectors.groupingBy(MerchantInfo::getMerchantTypeCode));
        // 餐饮
        List<FeatureTourismResourceVO> cateringList = merchantMap.getOrDefault(TravelRelatedResourceTypeEnum.CATERING.getMerchantType(), new ArrayList<>());
        myMapAllListVO.setCatering(convertFromMerchant(cateringList, TravelRelatedResourceTypeEnum.CATERING));
        // 住宿
        List<FeatureTourismResourceVO> accommodationList = merchantMap.getOrDefault(TravelRelatedResourceTypeEnum.ACCOMMODATION.getMerchantType(), new ArrayList<>());
        myMapAllListVO.setAccommodation(convertFromMerchant(accommodationList, TravelRelatedResourceTypeEnum.ACCOMMODATION));
        // 娱乐
        List<FeatureTourismResourceVO> entertainmentList = merchantMap.getOrDefault(TravelRelatedResourceTypeEnum.ENTERTAINMENT.getMerchantType(), new ArrayList<>());
        myMapAllListVO.setEntertainment(convertFromMerchant(entertainmentList, TravelRelatedResourceTypeEnum.ENTERTAINMENT));
        // 购物
        List<FeatureTourismResourceVO> shopList = merchantMap.getOrDefault(TravelRelatedResourceTypeEnum.SHOP.getMerchantType(), new ArrayList<>());
        myMapAllListVO.setShop(convertFromMerchant(shopList, TravelRelatedResourceTypeEnum.SHOP));

        allListResult.setData(myMapAllListVO);
        return allListResult;
    }

    @Override
    public Result<List<MerchantInfoVoExt>> getCatering() {
        Result<Page<FeatureTourismResourceVO>> result = merchantLabelFeignClient.list(buildMerchantLabelReq(TravelRelatedResourceTypeEnum.CATERING.getMerchantType()));
        return convertFromMerchantToResult(result, TravelRelatedResourceTypeEnum.CATERING);
    }

    @Override
    public Result<List<MerchantInfoVoExt>> getAccommodation() {
        Result<Page<FeatureTourismResourceVO>> result = merchantLabelFeignClient.list(buildMerchantLabelReq(TravelRelatedResourceTypeEnum.ACCOMMODATION.getMerchantType()));
        return convertFromMerchantToResult(result, TravelRelatedResourceTypeEnum.ACCOMMODATION);
    }

    @Override
    public Result<List<MerchantInfoVoExt>> getEntertainment() {
        Result<Page<FeatureTourismResourceVO>> result = merchantLabelFeignClient.list(buildMerchantLabelReq(TravelRelatedResourceTypeEnum.ENTERTAINMENT.getMerchantType()));
        return convertFromMerchantToResult(result, TravelRelatedResourceTypeEnum.ENTERTAINMENT);
    }

    @Override
    public Result<List<MerchantInfoVoExt>> getShop() {
        Result<Page<FeatureTourismResourceVO>> result = merchantLabelFeignClient.list(buildMerchantLabelReq(TravelRelatedResourceTypeEnum.SHOP.getMerchantType()));
        return convertFromMerchantToResult(result, TravelRelatedResourceTypeEnum.SHOP);
    }

    private List<MerchantInfoVoExt> convertFromMerchant(List<FeatureTourismResourceVO> list, TravelRelatedResourceTypeEnum typeEnum) {
        return list.stream().map(featureTourismResourceVO -> {
            MerchantInfoVoExt merchantInfoVoExt = new MerchantInfoVoExt();
            BeanUtils.copyProperties(featureTourismResourceVO, merchantInfoVoExt);
            merchantInfoVoExt.setLicensePhoto(convertUrlToList(featureTourismResourceVO.getLicensePhoto()));
            merchantInfoVoExt.setStorePhoto(convertUrlToList(featureTourismResourceVO.getStorePhoto()));
            merchantInfoVoExt.setMerchantTypeCode(typeEnum.getCode());
            merchantInfoVoExt.setMerchantTypeName(typeEnum.getName());
            return merchantInfoVoExt;
        }).collect(Collectors.toList());
    }

    private Result<List<MerchantInfoVoExt>> convertFromMerchantToResult(Result<Page<FeatureTourismResourceVO>> result, TravelRelatedResourceTypeEnum typeEnum) {
        List<FeatureTourismResourceVO> list = result.getData().getRecords();
        return ResultUtil.buildSuccessResultWithData(convertFromMerchant(list, typeEnum));
    }

    private MerchantLabelReq buildMerchantLabelReq(String type) {
        MerchantLabelReq req = new MerchantLabelReq();
        req.setPage(1L);
        req.setSize(-1L);
        req.setResourceTypeCode(type);
        return req;
    }

    private List<String> convertUrlToList(String urls) {
        if (StringUtils.hasText(urls)) {
            return Arrays.asList(urls.split(","));
        }
        return new ArrayList<>();
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MyMapAllListVO extends MapAllListVO {

        /**
         * 娱乐场所
         */
        private List<MerchantInfoVoExt> entertainment;
        /**
         * 购物场所
         */
        private List<MerchantInfoVoExt> shop;
        /**
         * 餐饮行业
         */
        private List<MerchantInfoVoExt> catering;
        /**
         * 酒店住宿
         */
        private List<MerchantInfoVoExt> accommodation;
    }
}
