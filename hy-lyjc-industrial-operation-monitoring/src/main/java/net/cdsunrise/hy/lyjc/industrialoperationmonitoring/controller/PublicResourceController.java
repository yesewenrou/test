package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.PublicResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.PublicResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceVO;
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
@RequestMapping("/publicResource")
@Slf4j
public class PublicResourceController {

    @Autowired
    private PublicResourceService resourceService;
    @Autowired
    private DataDictionaryFeignClient feignClient;

    /**
     * 资源管理 -> 公共资源 -> 新增
     *
     * */
    @Auth(value = "publicResource:add")
    @PostMapping("add")
    public Result add(@Validated @RequestBody ResourceVO resourceVO){
        try{
            return ResultUtil.buildSuccessResultWithData(resourceService.add(resourceVO));
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.NAME_REPEAT_ERROR);
        }
    }

    /**
     * 资源管理 -> 公共资源 -> 编辑
     *
     * */
    @Auth(value = "publicResource:update")
    @PostMapping("update")
    public Result update(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody ResourceVO resourceVO){
        try {
            resourceService.update(resourceVO);
            return ResultUtil.buildResult(true,ParamConst.OPERATE_SUCCESS);
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.NAME_REPEAT_ERROR);
        }
    }

    /**
     * 资源管理 -> 公共资源 -> 删除
     *
     * */
    @Auth(value = "publicResource:delete")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable long id) {
        resourceService.delete(id);
        return ResultUtil.buildResult(true,ParamConst.OPERATE_SUCCESS);
    }

    /**
     * 资源管理 -> 公共资源 -> 查看详情
     *
     * */
    @Auth(value = "publicResource:findById")
    @GetMapping("findById/{id}")
    public Result findById(@PathVariable long id) {
        return ResultUtil.buildSuccessResultWithData(resourceService.findById(id));
    }

    /**
     * 资源管理 -> 公共资源 -> 列表
     *
     * */
    @Auth(value = "publicResource:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest<ResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(resourceService.list(pageRequest));
    }

    /**
     * 资源管理 -> 公共资源 -> 资源类型
     *
     * */
    @Auth(value = "publicResource:resourceTypeList")
    @GetMapping("resourceTypeList")
    public Result resourceTypeList(){
        String codeParam[] = {ParamConst.PUBLIC_RESOURCE_TYPE};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.PUBLIC_RESOURCE_TYPE).getChildren());
    }
}
