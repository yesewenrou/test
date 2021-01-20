package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.RescueTeamService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamPageReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTypeVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/**
 * @author lijiafeng 2021/01/18 15:03
 */
@RestController
@RequestMapping("rescue-team")
public class RescueTeamController {

    private final RescueTeamService rescueTeamService;

    public RescueTeamController(RescueTeamService rescueTeamService) {
        this.rescueTeamService = rescueTeamService;
    }

    /**
     * 获取救援队伍类型列表
     *
     * @return 结果
     */
    @GetMapping("type/list")
    public Result<List<RescueTypeVO>> listType() {
        return ResultUtil.buildSuccessResultWithData(RescueTypeVO.getList());
    }

    /**
     * 新增救援队伍
     *
     * @param req 请求
     * @return 结果
     */
    @PostMapping
    public Result<Object> add(@Validated @RequestBody RescueTeamVO req) {
        return ResultUtil.buildSuccessResultWithData(rescueTeamService.add(req));
    }

    /**
     * 删除救援队伍
     *
     * @param id 主键ID
     * @return 结果
     */
    @DeleteMapping("{id}")
    public Result<Object> delete(@PathVariable("id") Long id) {
        rescueTeamService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 更新救援队伍
     *
     * @param req 请求
     * @return 结果
     */
    @PutMapping
    public Result<Object> update(@Validated({Default.class, UpdateCheckGroup.class}) @RequestBody RescueTeamVO req) {
        rescueTeamService.update(req);
        return ResultUtil.success();
    }

    /**
     * 查询救援队伍详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("{id}")
    public Result<RescueTeamVO> get(@PathVariable("id") Long id) {
        return ResultUtil.buildSuccessResultWithData(rescueTeamService.get(id));
    }

    /**
     * 分页查询救援队伍列表
     *
     * @param req 请求
     * @return 结果
     */
    @GetMapping("page")
    public Result<IPage<RescueTeamVO>> page(@Validated RescueTeamPageReq req) {
        return ResultUtil.buildSuccessResultWithData(rescueTeamService.page(req));
    }
}
