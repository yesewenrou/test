package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.DataCenterCodeFeignClient;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.FeaturedFeignClient;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeaturesResourceDirectoryService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.INewPublicResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITalentResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITravelRelatedResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.NewPublicResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TalentResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TravelRelatedResourceVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/1/15 16:16
 */
@RestController
@RequestMapping("resource-directory")
public class ResourceDirectoryController {

    private DataCenterCodeFeignClient dataCenterCodeFeignClient;
    private ITravelRelatedResourceService travelRelatedResourceService;
    private INewPublicResourceService newPublicResourceService;
    private ITalentResourceService talentResourceService;
    private IFeaturesResourceDirectoryService featuresResourceDirectoryService;
    private FeaturedFeignClient featuredFeignClient;

    public ResourceDirectoryController(DataCenterCodeFeignClient dataCenterCodeFeignClient, ITravelRelatedResourceService travelRelatedResourceService, INewPublicResourceService newPublicResourceService, ITalentResourceService talentResourceService, IFeaturesResourceDirectoryService featuresResourceDirectoryService, FeaturedFeignClient featuredFeignClient) {
        this.dataCenterCodeFeignClient = dataCenterCodeFeignClient;
        this.travelRelatedResourceService = travelRelatedResourceService;
        this.newPublicResourceService = newPublicResourceService;
        this.talentResourceService = talentResourceService;
        this.featuresResourceDirectoryService = featuresResourceDirectoryService;
        this.featuredFeignClient = featuredFeignClient;
    }

    /**
     * 获取菜单数据
     *
     * @return 菜单数据
     */
    @Auth("resource-directory")
    @GetMapping("menu")
    public Result<List<DataMenuVO>> getMenu() {
        return featuresResourceDirectoryService.getMenu();
    }

    /**
     * 获取所有数据
     *
     * @return 数据
     */
    @Auth("resource-directory")
    @GetMapping("/all")
    public Result<MapAllListVO> getAllList() {
        return featuresResourceDirectoryService.getAllList();
    }

    /**
     * 获取精品景区
     *
     * @return 精品景区数据
     */
    @Auth("resource-directory")
    @GetMapping("menu/boutiqueScenic")
    public Result<List<BoutiqueScenicDetailVO>> getBoutiqueScenic() {
        return dataCenterCodeFeignClient.getBoutiqueScenic();
    }

    /**
     * 获取文物保护点
     *
     * @return 文物保护点
     */
    @Auth("resource-directory")
    @GetMapping("menu/culturalProtectionPoint")
    public Result<List<CulturalProtectionPointDetailVO>> getCulturalProtectionPoint() {
        return dataCenterCodeFeignClient.getCulturalProtectionPoint();
    }

    /**
     * 获取娱乐场所
     *
     * @return 娱乐场所
     */
    @Auth("resource-directory")
    @GetMapping("menu/entertainment")
    public Result<List<MerchantInfoVoExt>> getEntertainment() {
        return featuresResourceDirectoryService.getEntertainment();
    }

    /**
     * 获取特色商品
     *
     * @return 特色商品
     */
    @Auth("resource-directory")
    @GetMapping("menu/featuredGoods")
    public Result<List<FeaturedGoodsDetailVO>> getFeaturedGoods() {
        return featuredFeignClient.getFeaturedAll();
    }

    /**
     * 获取购物场所
     *
     * @return 购物场所
     */
    @Auth("resource-directory")
    @GetMapping("menu/shop")
    public Result<List<MerchantInfoVoExt>> getShop() {
        return featuresResourceDirectoryService.getShop();
    }

    /**
     * 获取餐饮行业
     *
     * @return 餐饮行业
     */
    @Auth("resource-directory")
    @GetMapping("menu/catering")
    public Result<List<MerchantInfoVoExt>> getCatering() {
        return featuresResourceDirectoryService.getCatering();
    }

    /**
     * 获取酒店住宿
     *
     * @return 酒店住宿
     */
    @Auth("resource-directory")
    @GetMapping("menu/accommodation")
    public Result<List<MerchantInfoVoExt>> getAccommodation() {
        return featuresResourceDirectoryService.getAccommodation();
    }

    /**
     * 获取旅行社
     *
     * @return 旅行社
     */
    @Auth("resource-directory")
    @GetMapping("menu/travelAgency")
    public Result<List<TravelAgencyDetailVO>> getTravelAgency() {
        return dataCenterCodeFeignClient.getTravelAgency();
    }

    /**
     * 获取涉旅企业
     *
     * @return 涉旅企业
     */
    @Auth("resource-directory")
    @GetMapping("menu/travelRelatedEnterprises")
    public Result<List<TravelRelatedEnterprisesDetailVO>> getTravelRelatedEnterprises() {
        return dataCenterCodeFeignClient.getTravelRelatedEnterprises();
    }

    /**
     * 获取重点项目建设
     *
     * @return 重点项目建设
     */
    @Auth("resource-directory")
    @GetMapping("menu/projectBuild")
    public Result<List<EmphasisProjectBuildDetailVO>> getProjectBuild() {
        return dataCenterCodeFeignClient.getProjectBuild();
    }

    /**
     * 旅游厕所
     *
     * @return 旅游厕所
     */
    @Auth("resource-directory")
    @GetMapping("menu/touristToilet")
    public Result<List<TouristToiletDetailVO>> getTouristToilet() {
        return dataCenterCodeFeignClient.getTouristToilet();
    }

    /**
     * 汽车修理厂
     *
     * @return 汽车修理厂
     */
    @Auth("resource-directory")
    @GetMapping("menu/autoRepairShop")
    public Result<List<AutoRepairShopVO>> getRepairShop() {
        return dataCenterCodeFeignClient.getRepairShop();
    }

    /**
     * 加油站
     *
     * @return 加油站
     */
    @Auth("resource-directory")
    @GetMapping("menu/gasStation")
    public Result<List<GasStationVO>> getGasStation() {
        return dataCenterCodeFeignClient.getGasStation();
    }

    /**
     * 药房药店
     *
     * @return 药房药店
     */
    @Auth("resource-directory")
    @GetMapping("menu/pharmacy")
    public Result<List<PharmacyVO>> getPharmacy() {
        return dataCenterCodeFeignClient.getPharmacy();
    }

    /**
     * 医院
     *
     * @return 医院
     */
    @Auth("resource-directory")
    @GetMapping("menu/hospital")
    public Result<List<HospitalVO>> getHospital() {
        return dataCenterCodeFeignClient.getHospital();
    }

    /**
     * 银行/ATM
     *
     * @return 银行/ATM
     */
    @Auth("resource-directory")
    @GetMapping("menu/bank")
    public Result<List<BankVO>> getBank() {
        return dataCenterCodeFeignClient.getBank();
    }

    /**
     * 派出所
     *
     * @return 派出所
     */
    @Auth("resource-directory")
    @GetMapping("menu/policeStation")
    public Result<List<PoliceStationVO>> getPoliceStation() {
        return dataCenterCodeFeignClient.getPoliceStation();
    }

    /**
     * 卡口监控
     *
     * @return 卡口监控
     */
    @Auth("resource-directory")
    @GetMapping("menu/junction")
    public Result<List<JunctionVO>> getJunction() {
        return dataCenterCodeFeignClient.getJunction();
    }

    /**
     * LED信息屏
     *
     * @return LED信息屏
     */
    @Auth("resource-directory")
    @GetMapping("menu/ydp")
    public Result<List<YdpVO>> getYdp() {
        return dataCenterCodeFeignClient.getYdp();
    }

    /**
     * 停车场
     *
     * @return 停车场
     */
    @Auth("resource-directory")
    @GetMapping("menu/parkingLotResources")
    public Result<List<ParkingLotResourcesVO>> getParkingLotResources() {
        return dataCenterCodeFeignClient.getParkingLotResources();
    }

    /**
     * 旅游干道
     *
     * @return 旅游干道
     */
    @Auth("resource-directory")
    @GetMapping("menu/touristRoadResources")
    public Result<List<TouristRoadResourcesVO>> getTouristRoadResources() {
        return dataCenterCodeFeignClient.getTouristRoadResources();
    }

    @Auth("resource-directory:travelRelatedResources")
    @GetMapping("travelRelatedResources")
    public Result<PageResult<TravelRelatedResourceVO>> listTravelRelatedResources(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region
    ) {
        return travelRelatedResourceService.listTravelRelatedResources(page, size, type, name, region);
    }

    @Auth("resource-directory:travelRelatedResources:get")
    @GetMapping("travelRelatedResources/get")
    public Result<?> getTravelRelatedResource(
            @RequestParam String type,
            @RequestParam Long id
    ) {
        return travelRelatedResourceService.getTravelRelatedResource(type, id);
    }

    @Auth("resource-directory:publicResources")
    @GetMapping("publicResources")
    public Result<PageResult<NewPublicResourceVO>> listNewPublicResources(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region
    ) {
        return newPublicResourceService.listNewPublicResources(page, size, type, name, region);
    }

    @Auth("resource-directory:publicResources:get")
    @GetMapping("publicResources/get")
    public Result<?> getNewPublicResource(
            @RequestParam String type,
            @RequestParam Long id
    ) {
        return newPublicResourceService.getNewPublicResource(type, id);
    }

    @Auth("resource-directory:talentResources")
    @GetMapping("talentResources")
    public Result<PageResult<TalentResourceVO>> listTalentResources(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam String type,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String unitAndPosition
    ) {
        return talentResourceService.listTalentResources(page, size, type, name, unitAndPosition);
    }
}
