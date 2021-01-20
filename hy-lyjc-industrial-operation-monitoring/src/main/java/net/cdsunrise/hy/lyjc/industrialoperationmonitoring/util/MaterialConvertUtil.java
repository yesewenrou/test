package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MaterialConvertUtil
 * 物资转换工具类
 * @author LiuYin
 * @date 2021/1/17 17:21
 */
public class MaterialConvertUtil {


    public static MaterialTermDO termRequestToDO(MaterialTermRequest request) {
        Objects.requireNonNull(request);

        final MaterialTermDO entity = new MaterialTermDO();
        BeanUtils.copyProperties(request, entity);

        final LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        return entity;
    }

    public static MaterialPurchaseDO purchaseRequestToDO(MaterialPurchaseRequest request) {
        Objects.requireNonNull(request);

        final MaterialPurchaseDO entity = new MaterialPurchaseDO();
        BeanUtils.copyProperties(request, entity);

        entity.setPurchaseDate(DateUtil.longToLocalDateTime(request.getPurchaseDate()));

        final LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        return entity;
    }

    public static MaterialPurchaseVO purchaseDOToVO(MaterialPurchaseDO entity){
        Objects.requireNonNull(entity);

        final MaterialPurchaseVO vo = new MaterialPurchaseVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setCreateTime(DateUtil.dateTimeToLong(entity.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateTimeToLong(entity.getUpdateTime()));
        vo.setPurchaseDate(DateUtil.dateTimeToLong(entity.getPurchaseDate()));
        return vo;
    }



    public static MaterialAccountingDO accountingRequestToDO(MaterialAccountingRequest request) {
        Objects.requireNonNull(request);

        final MaterialAccountingDO entity = new MaterialAccountingDO();
        BeanUtils.copyProperties(request, entity);

        entity.setAccountingDate(DateUtil.longToLocalDateTime(request.getAccountingDate()));

        final LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        return entity;
    }


    public static MaterialAccountingVO accountingDOToVO(MaterialAccountingDO entity){
        Objects.requireNonNull(entity);

        final MaterialAccountingVO vo = new MaterialAccountingVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setCreateTime(DateUtil.dateTimeToLong(entity.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateTimeToLong(entity.getUpdateTime()));
        vo.setAccountingDate(DateUtil.dateTimeToLong(entity.getAccountingDate()));
        return vo;


    }






    public static MaterialLossReportDO lossReportRequestToDO(MaterialLossReportRequest request) {
        Objects.requireNonNull(request);

        final MaterialLossReportDO entity = new MaterialLossReportDO();
        BeanUtils.copyProperties(request, entity);

        entity.setReportDate(DateUtil.longToLocalDateTime(request.getReportDate()));

        final LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        return entity;
    }

    public static MaterialLossReportVO lossReportDOToVO(MaterialLossReportDO entity){
        Objects.requireNonNull(entity);

        final MaterialLossReportVO vo = new MaterialLossReportVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setCreateTime(DateUtil.dateTimeToLong(entity.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateTimeToLong(entity.getUpdateTime()));
        vo.setReportDate(DateUtil.dateTimeToLong(entity.getReportDate()));
        return vo;
    }

    public static MaterialLeaseDO leaseRequestToDO(MaterialLeaseRequest request) {
        Objects.requireNonNull(request);

        final MaterialLeaseDO entity = new MaterialLeaseDO();
        BeanUtils.copyProperties(request, entity);

        entity.setLeaseDate(DateUtil.longToLocalDateTime(request.getLeaseDate()));

        final LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        return entity;
    }


    public static MaterialLeaseVO leaseDOToVO(MaterialLeaseDO entity){
        Objects.requireNonNull(entity);

        final MaterialLeaseVO vo = new MaterialLeaseVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setCreateTime(DateUtil.dateTimeToLong(entity.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateTimeToLong(entity.getUpdateTime()));
        vo.setLeaseDate(DateUtil.dateTimeToLong(entity.getLeaseDate()));
        return vo;
    }


    public static MaterialTermSummaryVO termSummaryFromEntity(MaterialTermDO entity){
        Objects.requireNonNull(entity);

        final MaterialTermSummaryVO vo = new MaterialTermSummaryVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setCreateTime(DateUtil.dateTimeToLong(entity.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateTimeToLong(entity.getUpdateTime()));

        // 默认设置统计值：0
        vo.setPurchaseTotal(0L);
        vo.setAccountingTotal(0L);
        vo.setLeaseTotal(0L);
        vo.setLossReportTotal(0L);
        vo.setInventory(0L);
        return vo;

    }
}
