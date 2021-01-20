package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;


import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.*;

import java.util.List;

/**
 * MaterialService
 *
 * @author LiuYin
 * @date 2021/1/17 15:59
 */
public interface MaterialService {

    /**
     * 得到所有物资类型
     * @return list
     */
    List<MaterialTypeVO> getAllMaterialType();



    /**
     * 新增物资条目
     * @param request 请求
     * @return 主键id
     */
    Long saveTerm(MaterialTermRequest request);


    /**
     * 更新物资条目
     * @param request 请求
     * @return 受影响行数
     */
    Integer updateTerm(MaterialTermRequest request);


    /**
     * 删除条目
     * @param id 条目id
     * @return 受影响行数
     */
    Integer deleteTerm(Long id);


    /**
     * 分页查询物资条目（带统计）
     * @param pageRequest 物资条目分页查询请求
     * @return IPage
     */
    PageResult<MaterialTermSummaryVO> pageTerm(MaterialTermPageRequest pageRequest);


    /**
     * 新增物资采购
     * @param request 请求
     * @return 主键id
     */
    Long savePurchase(MaterialPurchaseRequest request);


    /**
     * 获取某个条目的所有采购列表
     * @param termId 条目id
     * @return list
     */
    List<MaterialPurchaseVO> queryAllPurchaseByTermId(Long termId);


    /**
     * 删除一个采购
     * @param id 主键id
     * @return
     */
    Integer deletePurchase(Long id);


    /**
     * 新增物资报损
     * @param request 请求
     * @return 主键id
     */
    Long saveLossReport(MaterialLossReportRequest request);

    /**
     * 获取某个条目的所有报损列表
     * @param termId 条目id
     * @return list
     */
    List<MaterialLossReportVO> queryAllLossReportByTermId(Long termId);


    /**
     * 删除一个报损
     * @param id 主键id
     * @return
     */
    Integer deleteLossReport(Long id);


    /**
     * 新增物资核算
     * @param request 请求
     * @return 主键id
     */
    Long saveAccounting(MaterialAccountingRequest request);


    /**
     * 获取某个条目的所有核算列表
     * @param termId 条目id
     * @return list
     */
    List<MaterialAccountingVO> queryAllAccountingByTermId(Long termId);

    /**
     * 删除一个核算
     * @param id 主键id
     * @return
     */
    Integer deleteAccounting(Long id);



    /**
     * 新增物资租赁
     * @param request 请求
     * @return 主键id
     */
    Long saveLease(MaterialLeaseRequest request);


    /**
     * 获取某个条目的所有租赁列表
     * @param termId 租赁id
     * @return list
     */
    List<MaterialLeaseVO> queryAllLeaseByTermId(Long termId);

    /**
     * 删除一个租赁
     * @param id 主键id
     * @return
     */
    Integer deleteLease(Long id);
}
