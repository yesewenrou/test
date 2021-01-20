package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ComplaintManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HandleResultService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.lysh.merchantmanage.autoconfigure.feign.MerchantInfoFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/complaintManage")
@Slf4j
public class ComplaintManageController {

    @Autowired
    private ComplaintManageService manageService;
    @Autowired
    private HandleResultService resultService;
    @Autowired
    private DataDictionaryFeignClient feignClient;
    @Autowired
    private MerchantInfoFeignClient merchantInfoFeignClient;

    /**
     * 产业管理 -> 投诉管理 -> 新增一条投诉
     *
     * */
    @PostMapping("add")
    public Result add(@Validated @RequestBody ComplaintManageVO manageVO){
        ComplaintManage complaintManage = new ComplaintManage();
        BeanUtils.copyProperties(manageVO,complaintManage);
        complaintManage.setComplaintNumber(manageService.generateComplaintNumber());
        try{
            return ResultUtil.buildSuccessResultWithData(manageService.add(complaintManage));
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.COMPLAINT_NUMBER_REPEAT_ERROR);
        }
    }

    /**
     * 产业管理 -> 投诉管理 -> 查看投诉详情
     *
     * */
    @Auth(value = "complaintManage:findById")
    @GetMapping("findById/{id}")
    @SuppressWarnings("all")
    public Result findById(@PathVariable Long id) {
        Map resultMap = new HashMap();
        resultMap.put("complaintManage",manageService.findById(id));
        resultMap.put("handleResult",resultService.findById(id));
        return ResultUtil.buildSuccessResultWithData(resultMap);
    }

    /**
     * 产业管理 -> 投诉管理 -> 获取投诉列表
     *
     * */
    @Auth(value = "complaintManage:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest<ComplaintManageCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(manageService.list(pageRequest));
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 总数统计
     *
     * */
    @Auth(value = "complaintManage:statisticsComplaint")
    @GetMapping("statisticsComplaint")
    public Result statisticsComplaint(){
        return ResultUtil.buildSuccessResultWithData(manageService.statisticsComplaint());
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 四个扇形图
     *
     * */
    @Auth(value = "complaintManage:chartComplaint")
    @GetMapping("chartComplaint")
    public Result chartComplaint(){
        return ResultUtil.buildSuccessResultWithData(manageService.chartComplaint());
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 按条件搜索投诉统计
     *
     * */
    @Auth(value = "complaintManage:conditionStatisticsComplaint")
    @PostMapping("conditionStatisticsComplaint")
    public Result conditionStatisticsComplaint(@Validated @RequestBody PageRequest<StatisticsComplaintCondition> pageRequest){
        return ResultUtil.buildSuccessResultWithData(manageService.conditionStatisticsComplaint(pageRequest));
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 投诉分类
     *
     * */
    @Auth(value = "complaintManage:typeList")
    @GetMapping("typeList")
    public Result typeList(){
        String codeParam[] = {ParamConst.COMPLAINT_TYPE};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.COMPLAINT_TYPE).getChildren());
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 投诉重要性
     *
     * */
    @GetMapping("importanceList")
    public Result importanceList(){
        return ResultUtil.buildSuccessResultWithData(ComplaintImportanceVO.getList());
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 投诉渠道
     *
     * */
    @Auth(value = "complaintManage:channelList")
    @GetMapping("channelList")
    public Result channelList(){
        String codeParam[] = {ParamConst.COMPLAINT_CHANNEL};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.COMPLAINT_CHANNEL).getChildren());
    }

    /**
     * 产业管理 -> 投诉管理 -> 投诉统计分析 -> 导出报表
     *
     * */
    @Auth(value = "complaintManage:export")
    @PostMapping("export")
    public ResponseEntity<byte[]> export(@Validated @RequestBody PageRequest<StatisticsComplaintCondition> pageRequest) {
        return manageService.export(pageRequest);
    }

    /**
     * 根据商家名，获取投诉列表（提供给商户系统使用）
     *
     * */
    @GetMapping("findByComplaintObject/{complaintObject}")
    public Result findByComplaintObject(@PathVariable String complaintObject) {
        return ResultUtil.buildSuccessResultWithData(manageService.findByComplaintObject(complaintObject));
    }

    /**
     * 产业管理 -> 投诉管理 -> 模糊搜索返回商户列表
     *
     * */
    @Auth(value = "complaintManage:queryMerchant")
    @GetMapping("queryMerchant")
    public Result queryMerchant(@RequestParam String merchantName,
                                @RequestParam(required = false,defaultValue = "5") Long size){
        return merchantInfoFeignClient.listByName(merchantName, size);
    }

    /**
     * 营销中心 -> 投诉统计分析 -> 总数统计
     *
     * */
    @GetMapping("statisticsComplaintByTime")
    public Result statisticsComplaintByTime(@RequestParam(value = "beginTime")String beginTime,
                                            @RequestParam(value = "endTime")String endTime){
        return ResultUtil.buildSuccessResultWithData(manageService.statisticsComplaintByTime(beginTime, endTime));
    }

    /**
     * 营销中心 -> 投诉统计分析 -> 图表统计
     *
     * */
    @GetMapping("chartComplaintByTime")
    public Result chartComplaintByTime(@RequestParam(value = "beginTime")String beginTime,
                                       @RequestParam(value = "endTime")String endTime){
        return ResultUtil.buildSuccessResultWithData(manageService.chartComplaintByTime(beginTime, endTime));
    }

    /**
     * 营销中心 -> 投诉统计分析 -> 投诉趋势分析
     *
     * */
    @GetMapping("complaintTrend")
    public Result complaintTrend(@RequestParam(value = "beginTime")String beginTime,
                                 @RequestParam(value = "endTime")String endTime){
        return ResultUtil.buildSuccessResultWithData(manageService.complaintTrend(beginTime, endTime));
    }
}
