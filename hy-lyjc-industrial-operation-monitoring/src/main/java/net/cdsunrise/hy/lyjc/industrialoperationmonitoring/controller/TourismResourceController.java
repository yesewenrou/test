package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TourismResourceService;
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
@RequestMapping("/tourismResource")
@Slf4j
public class TourismResourceController {

    @Autowired
    private TourismResourceService resourceService;
    @Autowired
    private DataDictionaryFeignClient feignClient;

    /**
     * 资源管理 -> 旅游管理 -> 新增
     *
     * */
    @Auth(value = "tourismResource:add")
    @PostMapping("add")
    public Result add(@Validated @RequestBody ResourceVO resourceVO){
        try{
            return ResultUtil.buildSuccessResultWithData(resourceService.add(resourceVO));
        }catch (DuplicateKeyException e){
            return ResultUtil.buildResult(false, ParamConst.NAME_REPEAT_ERROR);
        }
    }

    /**
     * 资源管理 -> 旅游管理 -> 编辑
     *
     * */
    @Auth(value = "tourismResource:update")
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
     * 资源管理 -> 旅游管理 -> 删除
     *
     * */
    @Auth(value = "tourismResource:delete")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResultUtil.buildResult(true,ParamConst.OPERATE_SUCCESS);
    }

    /**
     * 资源管理 -> 旅游管理 -> 查看详情
     *
     * */
    @Auth(value = "tourismResource:findById")
    @GetMapping("findById/{id}")
    public Result findById(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(resourceService.findById(id));
    }

    /**
     * 资源管理 -> 旅游管理 -> 列表
     *
     * */
    @Auth(value = "tourismResource:list")
    @PostMapping("list")
    public Result list(@RequestBody PageRequest<ResourceCondition> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(resourceService.list(pageRequest));
    }

    /**
     * 资源管理 -> 旅游管理 -> 区域列表
     *
     * */
    @Auth(value = "tourismResource:areaList")
    @GetMapping("areaList")
    public Result areaList(){
        String codeParam[] = {ParamConst.REGION};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.REGION).getChildren());
    }

    /**
     * 资源管理 -> 旅游管理 -> 资源类型列表
     *
     * */
    @Auth(value = "tourismResource:resourceTypeList")
    @GetMapping("resourceTypeList")
    public Result resourceTypeList(){
        String codeParam[] = {ParamConst.TOURISM_RESOURCE_TYPE};
        return ResultUtil.buildSuccessResultWithData(feignClient.getByCodes(codeParam).getData()
                .get(ParamConst.TOURISM_RESOURCE_TYPE).getChildren());
    }

}
