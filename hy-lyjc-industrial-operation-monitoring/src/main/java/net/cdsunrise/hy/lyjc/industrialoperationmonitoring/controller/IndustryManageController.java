package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.IndustryManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IndustryManageService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.IndustryManageVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author LHY
 */
@RestController
@RequestMapping("/industryManage")
@Slf4j
public class IndustryManageController {

    @Autowired
    private IndustryManageService manageService;
    @Autowired
    private DataDictionaryFeignClient feignClient;

    /**
     * 产业管理 -> 行业管理 -> 新增
     *
     * */
    @Auth(value = "industryManage:add")
    @PostMapping("add")
    public Result add(@Validated @RequestBody IndustryManageVO industryManageVO){
        try{
            return ResultUtil.buildSuccessResultWithData(manageService.add(industryManageVO));
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.NAME_REPEAT_ERROR);
        }
    }

    /**
     * 产业管理 -> 行业管理 -> 编辑
     *
     * */
    @Auth(value = "industryManage:update")
    @PostMapping("update")
    public Result update(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody IndustryManageVO industryManageVO){
        try {
            manageService.update(industryManageVO);
            return ResultUtil.buildResult(true,ParamConst.OPERATE_SUCCESS);
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.NAME_REPEAT_ERROR);
        }
    }

    /**
     * 产业管理 -> 行业管理 -> 删除
     *
     * */
    @Auth(value = "industryManage:delete")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        manageService.delete(id);
        return ResultUtil.buildResult(true,ParamConst.OPERATE_SUCCESS);
    }

    /**
     * 产业管理 -> 行业管理 -> 查看
     *
     * */
    @Auth(value = "industryManage:findById")
    @GetMapping("findById/{id}")
    public Result findById(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(manageService.findById(id));
    }

    /**
     * 产业管理 -> 行业管理 -> 列表
     *
     * */
    @Auth(value = "industryManage:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest<ResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(manageService.list(pageRequest));
    }

    /**
     * 产业管理 -> 行业管理 -> 行业类型列表
     *
     * */
    @Auth(value = "industryManage:industryTypeList")
    @GetMapping("industryTypeList")
    public Result resourceTypeList(){
        String codeParam[] = {ParamConst.INDUSTRY_TYPE};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.INDUSTRY_TYPE).getChildren());
    }
}
