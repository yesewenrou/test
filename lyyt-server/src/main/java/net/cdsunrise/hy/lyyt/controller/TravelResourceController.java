package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.resp.TravelResourceResponse;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.TravelResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 涉旅资源控制器
 * @author LiuYin 2020/2/5
 */
@RestController
@RequestMapping("/travel/resource")
public class TravelResourceController {

    @Autowired
    private TravelResourceService travelResourceService;

    /**
     * 涉旅资源基础数据
     * @return 涉旅资源响应
     */
    @GetMapping("/type/count")
    @DataType({DataTypeEnum.LYSHZYJCSJ})
    public TravelResourceResponse typeCount(){
        return travelResourceService.getResourceTypeCount();
    }

    /**
     * 涉旅行业数据分析
     * @return 涉旅资源响应
     */
    @GetMapping("/industry/analysis")
    @DataType({DataTypeEnum.LYSHZYJCSJ})
    public TravelResourceResponse industryCount(){
        return travelResourceService.getIndustryAnalysis();
    }
}
