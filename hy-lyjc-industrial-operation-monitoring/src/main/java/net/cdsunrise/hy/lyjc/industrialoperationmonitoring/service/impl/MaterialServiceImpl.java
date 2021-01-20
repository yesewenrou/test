package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.BusinessTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MaterialTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.MaterialService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.AttachmentUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.MaterialCheckUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.MaterialConvertUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MaterialServiceImpl
 *
 * @author LiuYin
 * @date 2021/1/17 16:06
 */
@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    /** 附件*/
    private final AttachmentMapper attachmentMapper;
    /** 物资报损*/
    private final MaterialLossReportMapper materialLossReportMapper;
    /** 物资租赁*/
    private final MaterialLeaseMapper materialLeaseMapper;
    /** 物资核算*/
    private final MaterialAccountingMapper materialAccountingMapper;
    /** 物资采购*/
    private final MaterialPurchaseMapper materialPurchaseMapper;
    /** 物资条目*/
    private final MaterialTermMapper materialTermMapper;


    public MaterialServiceImpl(AttachmentMapper attachmentMapper,
                               MaterialLossReportMapper materialLossReportMapper,
                               MaterialLeaseMapper materialLeaseMapper,
                               MaterialAccountingMapper materialAccountingMapper,
                               MaterialPurchaseMapper materialPurchaseMapper,
                               MaterialTermMapper materialTermMapper) {
        this.attachmentMapper = attachmentMapper;
        this.materialLossReportMapper = materialLossReportMapper;
        this.materialLeaseMapper = materialLeaseMapper;
        this.materialAccountingMapper = materialAccountingMapper;
        this.materialPurchaseMapper = materialPurchaseMapper;
        this.materialTermMapper = materialTermMapper;
    }


    /**
     * 得到所有物资类型
     *
     * @return list
     */
    @Override
    public List<MaterialTypeVO> getAllMaterialType() {
        final List<MaterialTypeVO> list = Arrays.stream(MaterialTypeEnum.values()).map(e -> {
            final MaterialTypeVO vo = new MaterialTypeVO();
            vo.setType(e.getType());
            vo.setName(e.getName());
            return vo;
        }).collect(Collectors.toList());

        return list;
    }

    /**
     * 新增物资条目
     *
     * @param request 请求
     * @return 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Long saveTerm(MaterialTermRequest request) {
        MaterialCheckUtil.checkSaveTerm(request);

        // 名称不能重复
        final String name = request.getName();
        final MaterialTermDO termByName = getTermByName(name);
        AssertUtil.isNull(termByName, BusinessException.toParamError(BusinessExceptionEnum.MATERIAL_NAME_EXISTS));

        final MaterialTermDO entity = MaterialConvertUtil.termRequestToDO(request);
        final int affectedRows = materialTermMapper.insert(entity);
        final Long id = entity.getId();

        // 如果有附件（图片），就保存附件
        final List<String> pics = request.getPics();
        if(CollectionUtils.isNotEmpty(pics) && affectedRows > 0){
            final List<AttachmentDO> attachmentList = pics.stream().map(url -> AttachmentUtil.create(url, id, BusinessTypeEnum.MATERIAL_TERM)).collect(Collectors.toList());
            attachmentList.forEach(this::saveAttachment);
        }

        // 返回新增数据的主键
        return id;
    }

    /**
     * 更新物资条目
     *
     * @param request 请求
     * @return 受影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer updateTerm(MaterialTermRequest request) {
        MaterialCheckUtil.checkUpdateTerm(request);
        // 记录必须存在
        final Long id = request.getId();
        final MaterialTermDO termById = getTermById(id);
        AssertUtil.notNull(termById, BusinessException.toParamError(BusinessExceptionEnum.NOT_FOUND_RECORD));

        // 如果发生了名字变更，则要判断名称是否已经存在
        final String name = request.getName();
        if(!termById.getName().equals(name)){
            final MaterialTermDO termByName = getTermByName(name);
            AssertUtil.isNull(termByName, BusinessException.toParamError(BusinessExceptionEnum.MATERIAL_TERM_NAME_EXISTS));
        }

        final MaterialTermDO newEntity = MaterialConvertUtil.termRequestToDO(request);
        final int affectedRows = materialTermMapper.updateById(newEntity);

        final List<AttachmentDO> attachmentDOS = queryAttachmentsByBusinessId(BusinessTypeEnum.MATERIAL_TERM, id);
        final List<String> oldPics = attachmentDOS.stream().sorted(Comparator.comparingLong(AttachmentDO::getId)).map(AttachmentDO::getUrl).collect(Collectors.toList());

        // 如果受影响行数大于1，而且附件有改动（数量、顺序、地址）
        if(affectedRows > 0 && !isExactlySameList(request.getPics(), oldPics)){
            // 删除旧的附件地址
            batchDeleteAttachments(BusinessTypeEnum.MATERIAL_TERM, id);

            // 如果有附件（图片），就保存附件
            final List<String> pics = request.getPics();
            if(CollectionUtils.isNotEmpty(pics)){
                final List<AttachmentDO> attachmentList = pics.stream().map(url -> AttachmentUtil.create(url, id, BusinessTypeEnum.MATERIAL_TERM)).collect(Collectors.toList());
                attachmentList.forEach(this::saveAttachment);
            }
        }

        return affectedRows;
    }


    /**
     * 删除条目
     *
     * @param id 条目id
     * @return 受影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer deleteTerm(Long id) {
        AssertUtil.notNull(id, () -> new ParamErrorException("id为空"));

        final MaterialTermDO termById = getTermById(id);
        if(Objects.isNull(termById)){
            return 0;
        }

        final AtomicInteger adder = new AtomicInteger();
        final int affectedRows = materialTermMapper.deleteById(id);
        adder.addAndGet(affectedRows);

        if(affectedRows > 0){
            // 删除采购
            adder.addAndGet(deletePurchaseByTermId(id));
            // 删除报损
            adder.addAndGet(deleteLossReportByTermId(id));
            // 删除核算
            adder.addAndGet(deleteAccountingByTermId(id));
            // 删除租赁
            adder.addAndGet(deleteLeaseByTermId(id));
            // 删除附件
            adder.addAndGet(batchDeleteAttachments(BusinessTypeEnum.MATERIAL_TERM, id));
        }
        return adder.get();
    }


    /**
     * 分页查询物资条目（带统计）
     *
     * @param pageRequest 物资条目分页查询请求
     * @return IPage
     */
    @Override
    public PageResult<MaterialTermSummaryVO> pageTerm(MaterialTermPageRequest pageRequest) {
        MaterialCheckUtil.checkPageTerm(pageRequest);

        final IPage<MaterialTermDO> recordPage = materialTermMapper.selectPage(createTermPage(pageRequest), createTermPageWrapper(pageRequest));
        final List<MaterialTermDO> records = recordPage.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return PageUtil.emptyPage(recordPage);
        }

        // 转换成物资条目统计概要对象
        final PageResult<MaterialTermSummaryVO> summaryPageResult = PageUtil.page(recordPage, MaterialConvertUtil::termSummaryFromEntity);

        final List<MaterialTermSummaryVO> summaryList = summaryPageResult.getList();

        final CompletableFuture<Void> fu = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> setPurchaseTotal(summaryList)),
                CompletableFuture.runAsync(() -> setLossReportTotal(summaryList)),
                CompletableFuture.runAsync(() -> setAccountingTotal(summaryList)),
                CompletableFuture.runAsync(() -> setLeaseTotal(summaryList)),
                CompletableFuture.runAsync(() -> setPics(summaryList))
        ).thenRun(() -> {
            // 计算存货库存
            summaryList.forEach(s -> {
                s.setInventory(s.getBeginningInventory() + s.getPurchaseTotal() + s.getLossReportTotal() + s.getAccountingTotal() + s.getLeaseTotal());
            });
        });



        try {
            fu.get(10, TimeUnit.SECONDS);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return summaryPageResult;
    }


    private void setPics(List<MaterialTermSummaryVO> list){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        final List<Long> ids = list.stream().map(MaterialTermSummaryVO::getId).collect(Collectors.toList());
        final List<AttachmentDO> attachmentList = queryAttachmentsByBusinessIds(BusinessTypeEnum.MATERIAL_TERM, ids);
        if(CollectionUtils.isEmpty(attachmentList)){
            return ;
        }
        final Map<Long, List<AttachmentDO>> map = attachmentList.stream().collect(Collectors.groupingBy(AttachmentDO::getBusinessId));
        list.forEach(vo -> {
            final List<AttachmentDO> lst = map.get(vo.getId());
            if(CollectionUtils.isNotEmpty(lst)){
                vo.setPics(lst.stream().map(AttachmentDO::getUrl).collect(Collectors.toList()));
            }
        });

    }

    /**
     * 设置采购总数
     * @param list 物资条目统计摘要
     */
    private void setPurchaseTotal(List<MaterialTermSummaryVO> list){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        final List<Long> ids = list.stream().map(MaterialTermSummaryVO::getId).collect(Collectors.toList());
        final List<MaterialPurchaseDO> purchaseList = queryPurchaseListByTermIds(ids);
        final Map<Long, Long> map = purchaseSum(purchaseList);
        if(CollectionUtils.isNotEmpty(map)){
            list.forEach(v -> {
                final Long total = map.get(v.getId());
                if(Objects.nonNull(total)){
                    v.setPurchaseTotal(total);
                }
            });
        }
    }

    /**
     * 设置报损总数
     * @param list 物资条目统计摘要
     */
    private void setLossReportTotal(List<MaterialTermSummaryVO> list){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        final List<Long> ids = list.stream().map(MaterialTermSummaryVO::getId).collect(Collectors.toList());
        final List<MaterialLossReportDO> lossReportList = queryLossReportByTermIds(ids);
        final Map<Long, Long> map = lossReportSum(lossReportList);
        if(CollectionUtils.isNotEmpty(map)){
            list.forEach(v -> {
                final Long total = map.get(v.getId());
                if(Objects.nonNull(total)){
                    v.setLossReportTotal(total);
                }
            });
        }
    }

    /**
     * 设置核算总数
     * @param list 物资条目统计摘要
     */
    private void setAccountingTotal(List<MaterialTermSummaryVO> list){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        final List<Long> ids = list.stream().map(MaterialTermSummaryVO::getId).collect(Collectors.toList());
        final List<MaterialAccountingDO> accountingList = queryAccountingListByTermIds(ids);
        final Map<Long, Long> map = accountingSum(accountingList);
        if(CollectionUtils.isNotEmpty(map)){
            list.forEach(v -> {
                final Long total = map.get(v.getId());
                if(Objects.nonNull(total)){
                    v.setAccountingTotal(total);
                }
            });
        }
    }

    /**
     * 设置租赁总数
     * @param list 物资条目统计摘要
     */
    private void setLeaseTotal(List<MaterialTermSummaryVO> list){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }
        final List<Long> ids = list.stream().map(MaterialTermSummaryVO::getId).collect(Collectors.toList());
        final List<MaterialLeaseDO> leaseList = queryLeaseListByTermIds(ids);
        final Map<Long, Long> map = leaseSum(leaseList);
        if(CollectionUtils.isNotEmpty(map)){
            list.forEach(v -> {
                final Long total = map.get(v.getId());
                if(Objects.nonNull(total)){
                    v.setLeaseTotal(total);
                }
            });
        }
    }




    private static IPage<MaterialTermDO> createTermPage(MaterialTermPageRequest pageRequest){

        return new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
    }

    private static LambdaQueryWrapper<MaterialTermDO> createTermPageWrapper(MaterialTermPageRequest pageRequest){
        return Wrappers.<MaterialTermDO>lambdaQuery()
                .eq(Objects.nonNull(pageRequest.getType()), MaterialTermDO::getType, pageRequest.getType())
                .like(!StringUtils.isEmpty(pageRequest.getName()), MaterialTermDO::getName, pageRequest.getName())
                .orderByDesc(MaterialTermDO::getId);
    }


    /**
     * 新增物资采购
     *
     * @param request 请求
     * @return 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long savePurchase(MaterialPurchaseRequest request) {
        MaterialCheckUtil.checkSavePurchase(request);
        final Long termId = request.getTermId();
        // 校验物资条目必须存在
        mustHasTerm(termId);

        final MaterialPurchaseDO entity = MaterialConvertUtil.purchaseRequestToDO(request);
        final int affectedRows = materialPurchaseMapper.insert(entity);
        final Long id = entity.getId();

        // 保存附件
        final String voucherUrl = request.getVoucherUrl();
        if(affectedRows > 0 && !StringUtils.isEmpty(voucherUrl)){
            final AttachmentDO attachmentDO = AttachmentUtil.create(voucherUrl, id, BusinessTypeEnum.MATERIAL_PURCHASE);
            saveAttachment(attachmentDO);
        }

        return id;
    }

    /**
     * 获取某个条目的所有采购列表
     *
     * @param termId 条目id
     * @return list
     */
    @Override
    public List<MaterialPurchaseVO> queryAllPurchaseByTermId(Long termId) {
        AssertUtil.notNull(termId, () -> new ParamErrorException("条目id为空"));

        final List<MaterialPurchaseDO> list = queryPurchaseListByTermId(termId);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>(0);
        }

        final List<MaterialPurchaseVO> voList = list.stream().map(MaterialConvertUtil::purchaseDOToVO).collect(Collectors.toList());
        // 获取附件
        final Set<Long> ids = voList.stream().map(MaterialPurchaseVO::getId).collect(Collectors.toSet());
        final List<AttachmentDO> attachmentDOS = queryAttachmentsByBusinessIds(BusinessTypeEnum.MATERIAL_PURCHASE, ids);

        // 设置附件地址
        if(CollectionUtils.isNotEmpty(attachmentDOS)){
            final Map<Long, List<AttachmentDO>> map = attachmentDOS.stream().collect(Collectors.groupingBy(AttachmentDO::getBusinessId));
            voList.forEach(v -> {
                // 目前的业务，这个lst只会有一个元素
                final List<AttachmentDO> lst = map.get(v.getId());
                if(CollectionUtils.isNotEmpty(lst)){
                    v.setVoucherUrl(lst.get(0).getUrl());
                }
            });
        }
        return voList;
    }

    /**
     * 删除一个采购
     *
     * @param id 主键id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deletePurchase(Long id) {
        AssertUtil.notNull(id, () -> new ParamErrorException("采购id为空"));
        final int affectedRows = materialPurchaseMapper.deleteById(id);
        final AtomicInteger adder = new AtomicInteger(affectedRows);
        if(affectedRows > 0){
            adder.addAndGet(batchDeleteAttachments(BusinessTypeEnum.MATERIAL_PURCHASE, id));
        }
        return adder.get();
    }


    /**
     * 新增物资报损
     *
     * @param request 请求
     * @return 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveLossReport(MaterialLossReportRequest request){
        MaterialCheckUtil.checkSaveLossReport(request);
        final Long termId = request.getTermId();

        // 校验物资条目必须存在
        mustHasTerm(termId);

        final MaterialLossReportDO entity = MaterialConvertUtil.lossReportRequestToDO(request);
        final int affectedRows = materialLossReportMapper.insert(entity);
        final Long id = entity.getId();

        // 保存附件
        final String voucherUrl = request.getVoucherUrl();
        if(affectedRows > 0 && !StringUtils.isEmpty(voucherUrl)){
            final AttachmentDO attachmentDO = AttachmentUtil.create(voucherUrl, id, BusinessTypeEnum.MATERIAL_LOSS_REPORT);
            saveAttachment(attachmentDO);
        }

        return id;
    }

    /**
     * 获取某个条目的所有报损列表
     *
     * @param termId 条目id
     * @return list
     */
    @Override
    public List<MaterialLossReportVO> queryAllLossReportByTermId(Long termId) {
        AssertUtil.notNull(termId, () -> new ParamErrorException("条目id为空"));

        final List<MaterialLossReportDO> list = queryLossReportListByTermId(termId);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>(0);
        }

        final List<MaterialLossReportVO> voList = list.stream().map(MaterialConvertUtil::lossReportDOToVO).collect(Collectors.toList());
        // 获取附件
        final Set<Long> ids = voList.stream().map(MaterialLossReportVO::getId).collect(Collectors.toSet());
        final List<AttachmentDO> attachmentDOS = queryAttachmentsByBusinessIds(BusinessTypeEnum.MATERIAL_LOSS_REPORT, ids);

        // 设置附件地址
        if(CollectionUtils.isNotEmpty(attachmentDOS)){
            final Map<Long, List<AttachmentDO>> map = attachmentDOS.stream().collect(Collectors.groupingBy(AttachmentDO::getBusinessId));
            voList.forEach(v -> {
                // 目前的业务，这个lst只会有一个元素
                final List<AttachmentDO> lst = map.get(v.getId());
                if(CollectionUtils.isNotEmpty(lst)){
                    v.setVoucherUrl(lst.get(0).getUrl());
                }
            });
        }
        return voList;
    }

    /**
     * 删除一个报损
     *
     * @param id 主键id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteLossReport(Long id) {
        AssertUtil.notNull(id, () -> new ParamErrorException("报损id为空"));
        final int affectedRows = materialLossReportMapper.deleteById(id);
        final AtomicInteger adder = new AtomicInteger(affectedRows);
        if(affectedRows > 0){
            adder.addAndGet(batchDeleteAttachments(BusinessTypeEnum.MATERIAL_LOSS_REPORT, id));
        }
        return adder.get();
    }

    /**
     * 新增物资核算
     *
     * @param request 请求
     * @return 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveAccounting(MaterialAccountingRequest request) {
        MaterialCheckUtil.checkSaveAccounting(request);
        final Long termId = request.getTermId();

        // 校验物资条目必须存在
        mustHasTerm(termId);

        final MaterialAccountingDO entity = MaterialConvertUtil.accountingRequestToDO(request);
        final int affectedRows = materialAccountingMapper.insert(entity);
        final Long id = entity.getId();

        // 保存附件
        final String voucherUrl = request.getVoucherUrl();
        if(affectedRows > 0 && !StringUtils.isEmpty(voucherUrl)){
            final AttachmentDO attachmentDO = AttachmentUtil.create(voucherUrl, id, BusinessTypeEnum.MATERIAL_ACCOUNTING);
            saveAttachment(attachmentDO);
        }

        return id;
    }

    /**
     * 获取某个条目的所有核算列表
     *
     * @param termId 条目id
     * @return list
     */
    @Override
    public List<MaterialAccountingVO> queryAllAccountingByTermId(Long termId) {
        AssertUtil.notNull(termId, () -> new ParamErrorException("条目id为空"));

        final List<MaterialAccountingDO> list = queryAccountingListByTermId(termId);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>(0);
        }

        final List<MaterialAccountingVO> voList = list.stream().map(MaterialConvertUtil::accountingDOToVO).collect(Collectors.toList());
        // 获取附件
        final Set<Long> ids = voList.stream().map(MaterialAccountingVO::getId).collect(Collectors.toSet());
        final List<AttachmentDO> attachmentDOS = queryAttachmentsByBusinessIds(BusinessTypeEnum.MATERIAL_ACCOUNTING, ids);

        // 设置附件地址
        if(CollectionUtils.isNotEmpty(attachmentDOS)){
            final Map<Long, List<AttachmentDO>> map = attachmentDOS.stream().collect(Collectors.groupingBy(AttachmentDO::getBusinessId));
            voList.forEach(v -> {
                // 目前的业务，这个lst只会有一个元素
                final List<AttachmentDO> lst = map.get(v.getId());
                if(CollectionUtils.isNotEmpty(lst)){
                    v.setVoucherUrl(lst.get(0).getUrl());
                }
            });
        }
        return voList;
    }

    /**
     * 删除一个核算
     *
     * @param id 主键id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAccounting(Long id) {
        AssertUtil.notNull(id, () -> new ParamErrorException("核算id为空"));
        final int affectedRows = materialAccountingMapper.deleteById(id);
        final AtomicInteger adder = new AtomicInteger(affectedRows);
        if(affectedRows > 0){
            adder.addAndGet(batchDeleteAttachments(BusinessTypeEnum.MATERIAL_ACCOUNTING, id));
        }
        return adder.get();
    }


    /**
     * 新增物资租赁
     *
     * @param request 请求
     * @return 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveLease(MaterialLeaseRequest request) {
        MaterialCheckUtil.checkSaveLease(request);
        final Long termId = request.getTermId();

        // 校验物资条目必须存在
        mustHasTerm(termId);

        final MaterialLeaseDO entity = MaterialConvertUtil.leaseRequestToDO(request);
        final int affectedRows = materialLeaseMapper.insert(entity);
        final Long id = entity.getId();

        // 保存附件
        final String voucherUrl = request.getVoucherUrl();
        if(affectedRows > 0 && !StringUtils.isEmpty(voucherUrl)){
            final AttachmentDO attachmentDO = AttachmentUtil.create(voucherUrl, id, BusinessTypeEnum.MATERIAL_LEASE);
            saveAttachment(attachmentDO);
        }

        return id;
    }

    /**
     * 获取某个条目的所有租赁列表
     *
     * @param termId 租赁id
     * @return list
     */
    @Override
    public List<MaterialLeaseVO> queryAllLeaseByTermId(Long termId) {
        AssertUtil.notNull(termId, () -> new ParamErrorException("条目id为空"));

        final List<MaterialLeaseDO> list = queryLeaseListByTermId(termId);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>(0);
        }

        final List<MaterialLeaseVO> voList = list.stream().map(MaterialConvertUtil::leaseDOToVO).collect(Collectors.toList());
        // 获取附件
        final Set<Long> ids = voList.stream().map(MaterialLeaseVO::getId).collect(Collectors.toSet());
        final List<AttachmentDO> attachmentDOS = queryAttachmentsByBusinessIds(BusinessTypeEnum.MATERIAL_LEASE, ids);

        // 设置附件地址
        if(CollectionUtils.isNotEmpty(attachmentDOS)){
            final Map<Long, List<AttachmentDO>> map = attachmentDOS.stream().collect(Collectors.groupingBy(AttachmentDO::getBusinessId));
            voList.forEach(v -> {
                // 目前的业务，这个lst只会有一个元素
                final List<AttachmentDO> lst = map.get(v.getId());
                if(CollectionUtils.isNotEmpty(lst)){
                    v.setVoucherUrl(lst.get(0).getUrl());
                }
            });
        }
        return voList;
    }

    /**
     * 删除一个租赁
     *
     * @param id 主键id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteLease(Long id) {
        AssertUtil.notNull(id, () -> new ParamErrorException("租赁id为空"));
        final int affectedRows = materialLeaseMapper.deleteById(id);
        final AtomicInteger adder = new AtomicInteger(affectedRows);
        if(affectedRows > 0){
            adder.addAndGet(batchDeleteAttachments(BusinessTypeEnum.MATERIAL_LEASE, id));
        }
        return adder.get();
    }


    /**
     * 根据名称的条目
     * @param name 名称
     * @return entity
     */
    private MaterialTermDO getTermByName(String name){
        return materialTermMapper.selectOne(Wrappers.<MaterialTermDO>lambdaQuery().eq(MaterialTermDO::getName, name).last("limit 1"));
    }

    /**
     * 根据id得到条目
     * @param id id
     * @return entity
     */
    private MaterialTermDO getTermById(Long id){
        return materialTermMapper.selectById(id);
    }


    /**
     * 必须有物资条目
     * @param termId 物资条目id
     * @return 物资条目
     */
    private MaterialTermDO mustHasTerm(Long termId){
        AssertUtil.notNull(termId, () -> new ParamErrorException("条目id为空"));
        final MaterialTermDO term = getTermById(termId);
        AssertUtil.notNull(term, BusinessException.toParamError(BusinessExceptionEnum.MATERIAL_TERM_NOT_FOUND));
        return term;
    }


    /**
     * 通过条目id删除报损
     * @param termId 条目id
     * @return 受影响行数
     */
    private Integer deleteLossReportByTermId(Long termId){
        final List<MaterialLossReportDO> lossReportDOList = queryLossReportListByTermId(termId);
        if(CollectionUtils.isEmpty(lossReportDOList)){
            return 0;
        }
        final Set<Long> purchaseIds = lossReportDOList.stream().map(MaterialLossReportDO::getId).collect(Collectors.toSet());
        final int deleteRows = materialLossReportMapper.deleteBatchIds(purchaseIds);

        final AtomicInteger adder = new AtomicInteger();
        adder.addAndGet(deleteRows);

        // 删除附件
        final int deleteAttachmentRows = batchDeleteAttachments(BusinessTypeEnum.MATERIAL_LOSS_REPORT, purchaseIds);
        return adder.addAndGet(deleteAttachmentRows);
    }

    /**
     * 通过条目id查询报损列表
     * @param termId 条目id
     * @return list
     */
    private List<MaterialLossReportDO> queryLossReportListByTermId(Long termId){
        if(Objects.isNull(termId)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialLossReportDO> wrapper =
                Wrappers.<MaterialLossReportDO>lambdaQuery().eq(MaterialLossReportDO::getTermId, termId).orderByAsc(MaterialLossReportDO::getId);
        return materialLossReportMapper.selectList(wrapper);
    }

    /**
     * 通过多个条目id查询报损列表
     * @param termIds 条目id集合
     * @return list
     */
    private List<MaterialLossReportDO> queryLossReportByTermIds(Collection<Long> termIds){
        if(CollectionUtils.isEmpty(termIds)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialLossReportDO> wrapper =
                Wrappers.<MaterialLossReportDO>lambdaQuery().in(MaterialLossReportDO::getTermId, termIds).orderByAsc(MaterialLossReportDO::getId);
        return materialLossReportMapper.selectList(wrapper);
    }

    /**
     * 按条目分组的报损累加
     * @param list 报损列表
     * @return map
     */
    private Map<Long, Long> lossReportSum(List<MaterialLossReportDO> list){
        if(CollectionUtils.isEmpty(list)){
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(MaterialLossReportDO::getTermId, Collectors.reducing(0L, MaterialLossReportDO::getQuantity, Long::sum)));
    }



    /**
     * 通过条目id删除租赁
     * @param termId 条目id
     * @return 受影响行数
     */
    private Integer deleteLeaseByTermId(Long termId){
        final List<MaterialLeaseDO> leaseDOList = queryLeaseListByTermId(termId);
        if(CollectionUtils.isEmpty(leaseDOList)){
            return 0;
        }
        final Set<Long> purchaseIds = leaseDOList.stream().map(MaterialLeaseDO::getId).collect(Collectors.toSet());
        final int deleteRows = materialLeaseMapper.deleteBatchIds(purchaseIds);

        final AtomicInteger adder = new AtomicInteger();
        adder.addAndGet(deleteRows);

        // 删除附件
        final int deleteAttachmentRows = batchDeleteAttachments(BusinessTypeEnum.MATERIAL_LEASE, purchaseIds);
        return adder.addAndGet(deleteAttachmentRows);
    }


    /**
     * 通过条目id查询租赁列表
     * @param termId 条目id
     * @return list
     */
    private List<MaterialLeaseDO> queryLeaseListByTermId(Long termId){
        if(Objects.isNull(termId)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialLeaseDO> wrapper =
                Wrappers.<MaterialLeaseDO>lambdaQuery().eq(MaterialLeaseDO::getTermId, termId).orderByAsc(MaterialLeaseDO::getId);
        return materialLeaseMapper.selectList(wrapper);
    }

    /**
     * 通过多个条目id查询租赁列表
     * @param termIds 条目id集合
     * @return list
     */
    private List<MaterialLeaseDO> queryLeaseListByTermIds(Collection<Long> termIds){
        if(CollectionUtils.isEmpty(termIds)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialLeaseDO> wrapper =
                Wrappers.<MaterialLeaseDO>lambdaQuery().in(MaterialLeaseDO::getTermId, termIds).orderByAsc(MaterialLeaseDO::getId);
        return materialLeaseMapper.selectList(wrapper);
    }

    /**
     * 按条目分组的租赁累加
     * @param list 租赁列表
     * @return map
     */
    private Map<Long, Long> leaseSum(List<MaterialLeaseDO> list){
        if(CollectionUtils.isEmpty(list)){
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(MaterialLeaseDO::getTermId, Collectors.reducing(0L, MaterialLeaseDO::getQuantity, Long::sum)));
    }



    /**
     * 通过条目id删除核算
     * @param termId 条目id
     * @return 受影响行数
     */
    private Integer deleteAccountingByTermId(Long termId){

        final List<MaterialAccountingDO> accountingDOList = queryAccountingListByTermId(termId);
        if(CollectionUtils.isEmpty(accountingDOList)){
            return 0;
        }
        final Set<Long> purchaseIds = accountingDOList.stream().map(MaterialAccountingDO::getId).collect(Collectors.toSet());
        final int deleteRows = materialAccountingMapper.deleteBatchIds(purchaseIds);

        final AtomicInteger adder = new AtomicInteger();
        adder.addAndGet(deleteRows);

        // 删除附件
        final int deleteAttachmentRows = batchDeleteAttachments(BusinessTypeEnum.MATERIAL_ACCOUNTING, purchaseIds);
        return adder.addAndGet(deleteAttachmentRows);
    }

    /**
     * 通过条目id查询核算列表
     * @param termId 条目id
     * @return list
     */
    private List<MaterialAccountingDO> queryAccountingListByTermId(Long termId){
        if(Objects.isNull(termId)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialAccountingDO> wrapper =
                Wrappers.<MaterialAccountingDO>lambdaQuery().eq(MaterialAccountingDO::getTermId, termId).orderByAsc(MaterialAccountingDO::getId);
        return materialAccountingMapper.selectList(wrapper);
    }

    /**
     * 通过多个条目id查询核算列表
     * @param termIds 条目id集合
     * @return list
     */
    private List<MaterialAccountingDO> queryAccountingListByTermIds(Collection<Long> termIds){
        if(CollectionUtils.isEmpty(termIds)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialAccountingDO> wrapper =
                Wrappers.<MaterialAccountingDO>lambdaQuery().in(MaterialAccountingDO::getTermId, termIds).orderByAsc(MaterialAccountingDO::getId);
        return materialAccountingMapper.selectList(wrapper);
    }


    /**
     * 按条目分组的核算累加
     * @param list 核算列表
     * @return map
     */
    private Map<Long, Long> accountingSum(List<MaterialAccountingDO> list){
        if(CollectionUtils.isEmpty(list)){
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(MaterialAccountingDO::getTermId, Collectors.reducing(0L, MaterialAccountingDO::getQuantity, Long::sum)));
    }




    /**
     * 通过条目id删除采购
     * @param termId 条目id
     * @return 受影响行数
     */
    private Integer deletePurchaseByTermId(Long termId){
        final List<MaterialPurchaseDO> purchaseList = queryPurchaseListByTermId(termId);
        if(CollectionUtils.isEmpty(purchaseList)){
            return 0;
        }
        final Set<Long> purchaseIds = purchaseList.stream().map(MaterialPurchaseDO::getId).collect(Collectors.toSet());
        final int deleteRows = materialPurchaseMapper.deleteBatchIds(purchaseIds);

        final AtomicInteger adder = new AtomicInteger();
        adder.addAndGet(deleteRows);

        // 删除附件
        final int deleteAttachmentRows = batchDeleteAttachments(BusinessTypeEnum.MATERIAL_PURCHASE, purchaseIds);
        return adder.addAndGet(deleteAttachmentRows);

    }


    /**
     * 通过条目id查询采购列表
     * @param termId 条目id
     * @return list
     */
    private List<MaterialPurchaseDO> queryPurchaseListByTermId(Long termId){
        if(Objects.isNull(termId)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialPurchaseDO> wrapper = Wrappers.<MaterialPurchaseDO>lambdaQuery().eq(MaterialPurchaseDO::getTermId, termId).orderByAsc(MaterialPurchaseDO::getId);
        return materialPurchaseMapper.selectList(wrapper);
    }

    /**
     * 通过多个条目id查询采购列表
     * @param termIds 条目id集合
     * @return list
     */
    private List<MaterialPurchaseDO> queryPurchaseListByTermIds(Collection<Long> termIds){
        if(CollectionUtils.isEmpty(termIds)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<MaterialPurchaseDO> wrapper = Wrappers.<MaterialPurchaseDO>lambdaQuery().in(MaterialPurchaseDO::getTermId, termIds).orderByAsc(MaterialPurchaseDO::getId);
        return materialPurchaseMapper.selectList(wrapper);
    }

    /**
     * 按条目分组的采购累加
     * @param list 采购列表
     * @return map
     */
    private Map<Long, Long> purchaseSum(List<MaterialPurchaseDO> list){
        if(CollectionUtils.isEmpty(list)){
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(MaterialPurchaseDO::getTermId, Collectors.reducing(0L, MaterialPurchaseDO::getQuantity, Long::sum)));
    }



    /**
     * 保存一个附件
     * @param entity 附件实体
     * @return 主键id
     */
    private Long saveAttachment(AttachmentDO entity){
        final int insert = attachmentMapper.insert(entity);
        AssertUtil.isTrue(insert > 0, BusinessException.toParamError(BusinessExceptionEnum.ATTACHMENT_SAVE_FAILED));
        return entity.getId();
    }

    /**
     * 根据业务查询附件
     * @param type 业务类型
     * @param businessId 该业务类型的主键id
     * @return list
     */
    private List<AttachmentDO> queryAttachmentsByBusinessId(BusinessTypeEnum type, Long businessId){
        final LambdaQueryWrapper<AttachmentDO> wrapper =
                Wrappers.<AttachmentDO>lambdaQuery().eq(AttachmentDO::getBusinessType, type.name().toUpperCase()).eq(AttachmentDO::getBusinessId, businessId);
        return attachmentMapper.selectList(wrapper);
    }


    /**
     * 根据多个业务id查询附件
     * @param type 业务类型
     * @param businessIds 该业务类型下多个主键
     * @return list
     */
    private List<AttachmentDO> queryAttachmentsByBusinessIds(BusinessTypeEnum type, Collection<Long> businessIds){
        if(Objects.isNull(type) || CollectionUtils.isEmpty(businessIds)){
            return new ArrayList<>(0);
        }

        final LambdaQueryWrapper<AttachmentDO> wrapper =
                Wrappers.<AttachmentDO>lambdaQuery().eq(AttachmentDO::getBusinessType, type.name().toUpperCase()).in(AttachmentDO::getBusinessId, businessIds);
        return attachmentMapper.selectList(wrapper);
    }



    /**
     * 根据主键删除附件
     * @param ids 主键id列表
     * @return 受影响行数
     */
    private int batchDeleteAttachments(List<Long> ids){
        return attachmentMapper.deleteBatchIds(ids);
    }

    /**
     * 根据业务id删除附件
     * @param type 业务类型
     * @param businessId 该业务类型的主键id
     * @return 受影响行数
     */
    private int batchDeleteAttachments(BusinessTypeEnum type, Long businessId){
        final LambdaQueryWrapper<AttachmentDO> deleteWrapper
                = Wrappers.<AttachmentDO>lambdaQuery().eq(AttachmentDO::getBusinessType, type.name().toUpperCase()).eq(AttachmentDO::getBusinessId, businessId);
        return attachmentMapper.delete(deleteWrapper);
    }

    /**
     * 根据业务id集合删除附件
     * @param type 业务类型
     * @param businessIds 改业务类型的注解id集合
     * @return 受影响行数
     */
    private int batchDeleteAttachments(BusinessTypeEnum type, Collection<Long> businessIds){
        if(CollectionUtils.isEmpty(businessIds) || Objects.isNull(type)){
            return 0;
        }
        final LambdaQueryWrapper<AttachmentDO> deleteWrapper
                = Wrappers.<AttachmentDO>lambdaQuery().eq(AttachmentDO::getBusinessType, type.name().toUpperCase()).in(AttachmentDO::getBusinessId, businessIds);
        return attachmentMapper.delete(deleteWrapper);
    }


    /**
     * 比较两个集合是否完全一致
     * @param list1 集合1
     * @param list2 集合2
     * @return bool
     */
    private static <T> boolean isExactlySameList(List<T> list1, List<T> list2){

        final boolean firstIsNull = Objects.isNull(list1);
        final boolean secondIsNull = Objects.isNull(list2);

        if(firstIsNull || secondIsNull){
            return firstIsNull && secondIsNull;
        }

        final int size1 = list1.size();
        final int size2 = list2.size();

        if(size1 != size2){
            return false;
        }

        for(int i = 0; i < size1; i ++){
            if(!list1.get(i).equals(list2.get(i))){
                return false;
            }
        }
        return true;
    }
}
