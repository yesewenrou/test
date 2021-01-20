package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.DataMenuVO;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MapAllListVO;
import net.cdsunrise.hy.lydsjdatacenter.starter.vo.MerchantInfoVoExt;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/1/18 9:39
 */
public interface IFeaturesResourceDirectoryService {

    /**
     * 全部菜单
     *
     * @return 结果
     */
    Result<List<DataMenuVO>> getMenu();

    /**
     * 全部数据
     *
     * @return 结果
     */
    Result<MapAllListVO> getAllList();

    /**
     * 餐饮
     *
     * @return 结果
     */
    Result<List<MerchantInfoVoExt>> getCatering();

    /**
     * 住宿
     *
     * @return 结果
     */
    Result<List<MerchantInfoVoExt>> getAccommodation();

    /**
     * 娱乐
     *
     * @return 结果
     */
    Result<List<MerchantInfoVoExt>> getEntertainment();

    /**
     * 购物
     *
     * @return 结果
     */
    Result<List<MerchantInfoVoExt>> getShop();
}
