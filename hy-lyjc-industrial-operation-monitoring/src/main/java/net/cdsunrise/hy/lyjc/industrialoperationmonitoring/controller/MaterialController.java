package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.*;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.MaterialService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MaterialController
 *
 * @author LiuYin
 * @date 2021/1/19 9:50
 */
@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 得到所有的物资分类
     * @return result
     */
    @GetMapping("/all_type")
    public Result<List<MaterialTypeVO>> getAllType(){
        return success(materialService.getAllMaterialType());
    }

    /**
     * 新增一个物资条目
     * @param request 物资条目请求
     * @return 主键id
     */
    @PostMapping("/term")
    public Result<Long> saveTerm(@RequestBody MaterialTermRequest request){
        return success(materialService.saveTerm(request));
    }

    /**
     * 更新一个物资条目
     * @param request 物资条目请求
     * @return 受影响行数
     */
    @PutMapping("/term")
    public Result<Integer> updateTerm(@RequestBody MaterialTermRequest request){
        return success(materialService.updateTerm(request));
    }

    /**
     * 删除一个物资条目
     * @param id 物资条目id
     * @return 受影响函数
     */
    @DeleteMapping("/term/{id}")
    public Result<Integer> deleteTerm(@PathVariable Long id){
        return success(materialService.deleteTerm(id));
    }


    /**
     * 分页查询物资条目（含统计摘要）
     * @param request 物资条目查询请求
     * @return 分页结果
     */
    @GetMapping("/term/summary/page")
    public Result<PageResult<MaterialTermSummaryVO>> pageSummary(MaterialTermPageRequest request){
        return success(materialService.pageTerm(request));
    }

    /**
     * 保存一个采购
     * @param request 物资采购请求
     * @return 主键id
     */
    @PostMapping("/purchase")
    public Result<Long> savePurchase(@RequestBody MaterialPurchaseRequest request){
        return success(materialService.savePurchase(request));
    }



    /**
     * 获取某个物资条目下的所有采购
     * @param termId 物资条目id
     * @return 采购列表
     */
    @GetMapping("/purchase/all_by_term/{termId}")
    public Result<List<MaterialPurchaseVO>> queryTermAllPurchase(@PathVariable Long termId){
        return success(materialService.queryAllPurchaseByTermId(termId));
    }


    /**
     * 删除一个采购
     * @param id 采购id
     * @return 受影响行数
     */
    @DeleteMapping("/purchase/{id}")
    public Result<Integer> deletePurchase(@PathVariable Long id){
        return success(materialService.deletePurchase(id));
    }


    /**
     * 保存一个报损
     * @param request 物资报损请求
     * @return 主键id
     */
    @PostMapping("/loss_report")
    public Result<Long> saveLossReport(@RequestBody MaterialLossReportRequest request){
        return success(materialService.saveLossReport(request));
    }



    /**
     * 获取某个物资条目下的所有报损
     * @param termId 物资条目id
     * @return 报损列表
     */
    @GetMapping("/loss_report/all_by_term/{termId}")
    public Result<List<MaterialLossReportVO>> queryTermAllLossReport(@PathVariable Long termId){
        return success(materialService.queryAllLossReportByTermId(termId));
    }


    /**
     * 删除一个报损
     * @param id 报损id
     * @return 受影响行数
     */
    @DeleteMapping("/loss_report/{id}")
    public Result<Integer> deleteLossReport(@PathVariable Long id){
        return success(materialService.deleteLossReport(id));
    }


    /**
     * 保存一个核算
     * @param request 物资核算请求
     * @return 主键id
     */
    @PostMapping("/accounting")
    public Result<Long> saveAccounting(@RequestBody MaterialAccountingRequest request){
        return success(materialService.saveAccounting(request));
    }



    /**
     * 获取某个物资条目下的所有核算
     * @param termId 物资条目id
     * @return 核算列表
     */
    @GetMapping("/accounting/all_by_term/{termId}")
    public Result<List<MaterialAccountingVO>> queryTermAllAccounting(@PathVariable Long termId){
        return success(materialService.queryAllAccountingByTermId(termId));
    }


    /**
     * 删除一个核算
     * @param id 核算id
     * @return 受影响行数
     */
    @DeleteMapping("/accounting/{id}")
    public Result<Integer> deleteAccounting(@PathVariable Long id){
        return success(materialService.deleteAccounting(id));
    }

    /**
     * 保存一个租赁
     * @param request 物资租赁请求
     * @return 主键id
     */
    @PostMapping("/lease")
    public Result<Long> saveLease(@RequestBody MaterialLeaseRequest request){
        return success(materialService.saveLease(request));
    }


    /**
     * 获取某个物资条目下的所有租赁
     * @param termId 物资条目id
     * @return 租赁列表
     */
    @GetMapping("/lease/all_by_term/{termId}")
    public Result<List<MaterialLeaseVO>> queryTermAllLease(@PathVariable Long termId){
        return success(materialService.queryAllLeaseByTermId(termId));
    }


    /**
     * 删除一个核算
     * @param id 核算id
     * @return 受影响行数
     */
    @DeleteMapping("/lease/{id}")
    public Result<Integer> deleteLease(@PathVariable Long id){
        return success(materialService.deleteLease(id));
    }





    private static  <T> Result<T> success(T data){
        return ResultUtil.buildSuccessResultWithData(data);
    }


}
