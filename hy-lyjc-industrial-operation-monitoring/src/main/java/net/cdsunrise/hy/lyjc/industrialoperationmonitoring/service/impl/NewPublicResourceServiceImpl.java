package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.feign.PublicResourceClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.PublicResourceTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.INewPublicResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.NewPublicResourceVO;
import org.springframework.stereotype.Service;

/**
 * @author lijiafeng
 * @date 2020/1/16 21:06
 */
@Service
public class NewPublicResourceServiceImpl implements INewPublicResourceService {

    private PublicResourceClient publicResourceClient;

    public NewPublicResourceServiceImpl(PublicResourceClient publicResourceClient) {
        this.publicResourceClient = publicResourceClient;
    }

    @Override
    public Result<PageResult<NewPublicResourceVO>> listNewPublicResources(int page, int size, String type, String name, String region) {
        PublicResourceTypeEnum typeEnum = checkResourceType(type);
        switch (typeEnum) {
            // 旅游厕所
            case TOILET:
                return PageUtil.convertResult(
                        publicResourceClient.getTouristToilets(page, size, name, null, region),
                        touristToiletVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(touristToiletVO.getId());
                            newPublicResourceVO.setName(touristToiletVO.getProjectName());
                            newPublicResourceVO.setRegion(touristToiletVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(touristToiletVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(null);
                            return newPublicResourceVO;
                        }
                );
            // 汽车修理厂
            case AUTO_REPAIR_SHOP:
                return PageUtil.convertResult(
                        publicResourceClient.queryRepairShops(page, size, name, region),
                        autoRepairShopVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(autoRepairShopVO.getId());
                            newPublicResourceVO.setName(autoRepairShopVO.getName());
                            newPublicResourceVO.setRegion(autoRepairShopVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(autoRepairShopVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(autoRepairShopVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            // 药房药店
            case PHARMACY:
                return PageUtil.convertResult(
                        publicResourceClient.queryPharmacy(page, size, name, region),
                        pharmacyVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(pharmacyVO.getId());
                            newPublicResourceVO.setName(pharmacyVO.getName());
                            newPublicResourceVO.setRegion(pharmacyVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(pharmacyVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(pharmacyVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            // 加油站
            case GAS_STATION:
                return PageUtil.convertResult(
                        publicResourceClient.queryGasStation(page, size, name, region),
                        gasStationVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(gasStationVO.getId());
                            newPublicResourceVO.setName(gasStationVO.getName());
                            newPublicResourceVO.setRegion(gasStationVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(gasStationVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(gasStationVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            // 医院
            case HOSPITAL:
                return PageUtil.convertResult(
                        publicResourceClient.queryHospital(page, size, name, region),
                        hospitalVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(hospitalVO.getId());
                            newPublicResourceVO.setName(hospitalVO.getName());
                            newPublicResourceVO.setRegion(hospitalVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(hospitalVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(hospitalVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            // 银行/ATM
            case BANK:
                return PageUtil.convertResult(
                        publicResourceClient.queryBank(page, size, name, null, region),
                        bankVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(bankVO.getId());
                            newPublicResourceVO.setName(bankVO.getName());
                            newPublicResourceVO.setRegion(bankVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(bankVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(bankVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            // 派出所
            case POLICE_STATION:
                return PageUtil.convertResult(
                        publicResourceClient.queryPoliceStation(page, size, name, region),
                        policeStationVO -> {
                            NewPublicResourceVO newPublicResourceVO = new NewPublicResourceVO();
                            newPublicResourceVO.setId(policeStationVO.getId());
                            newPublicResourceVO.setName(policeStationVO.getName());
                            newPublicResourceVO.setRegion(policeStationVO.getBelongArea());
                            newPublicResourceVO.setType(typeEnum.getName());
                            newPublicResourceVO.setAddress(policeStationVO.getDetailAddress());
                            newPublicResourceVO.setPhoneNumber(policeStationVO.getContactPhone());
                            return newPublicResourceVO;
                        }
                );
            default:
                throw new ParamErrorException("资源类型不正确");
        }
    }

    @Override
    public Result<?> getNewPublicResource(String type, Long id) {
        PublicResourceTypeEnum typeEnum = checkResourceType(type);
        switch (typeEnum) {
            // 旅游厕所
            case TOILET:
                return publicResourceClient.getTouristDetailById(id);
            // 汽车修理厂
            case AUTO_REPAIR_SHOP:
                return publicResourceClient.getRepairShopById(id);
            // 药房药店
            case PHARMACY:
                return publicResourceClient.getPharmacyById(id);
            // 加油站
            case GAS_STATION:
                return publicResourceClient.getGasStationById(id);
            // 医院
            case HOSPITAL:
                return publicResourceClient.getHospitalById(id);
            // 银行/ATM
            case BANK:
                return publicResourceClient.getBankById(id);
            // 派出所
            case POLICE_STATION:
                return publicResourceClient.getPoliceStationById(id);
            default:
                throw new ParamErrorException("资源类型不正确");
        }
    }

    private PublicResourceTypeEnum checkResourceType(String type) {
        PublicResourceTypeEnum typeEnum = PublicResourceTypeEnum.getByCode(type);
        if (typeEnum == null) {
            throw new ParamErrorException("资源类型不正确");
        }
        return typeEnum;
    }
}
