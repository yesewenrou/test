package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.CulturalActivityAddReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.CulturalActivityService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: suzhouhe @Date: 2020/3/26 10:25 @Description: 文化活动
 */
@RestController
@RequestMapping("/cultural")
public class CulturalActivityController {

    @Autowired
    private CulturalActivityService culturalActivityService;

    @PostMapping("/add")
    @Auth("cultural:add")
    public Result add(@RequestBody CulturalActivityAddReq culturalActivityAddReq) {
        return culturalActivityService.add(culturalActivityAddReq);
    }

    @DeleteMapping("/{id}")
    @Auth("cultural:delete")
    public Result delete(@PathVariable Long id) {
        return culturalActivityService.delete(id);
    }

    @GetMapping("/list")
    @Auth("cultural:list")
    public Result list(@RequestParam(required = false) String culturalName,
                       @RequestParam(required = false) Long beginTime,
                       @RequestParam(required = false) Long endTime,
                       @RequestParam(required = false, defaultValue = "1") int page,
                       @RequestParam(required = false, defaultValue = "10") int size) {
        return culturalActivityService.list(culturalName, beginTime, endTime, page, size);
    }

}
