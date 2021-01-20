package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.SharedDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.MenuVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign.DataCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/3/7 14:36 @Description:
 */
@RestController
@RequestMapping("/dataCenter")
public class DataCenterController {

    @Autowired
    private DataCenterService dataCenterService;

    @GetMapping("menu")
    public Result<List<MenuVO>> getMenu() {
        return dataCenterService.getMenu();
    }

    @GetMapping("/shared")
    public Result<List<SharedDTO>> getSharedData(){ return  dataCenterService.getSharedData();}
}
