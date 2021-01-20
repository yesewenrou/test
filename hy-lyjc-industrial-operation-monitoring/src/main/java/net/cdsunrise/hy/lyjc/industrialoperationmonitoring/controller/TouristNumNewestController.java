package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristNumNewestService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristNumNewestVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/9/30 10:27
 */
@RestController
@RequestMapping("tourist/newest")
public class TouristNumNewestController {

    private ITouristNumNewestService touristNumNewestService;

    public TouristNumNewestController(ITouristNumNewestService touristNumNewestService) {
        this.touristNumNewestService = touristNumNewestService;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result listTouristNumNewest() {
        List<TouristNumNewestVO> touristNumNewestVoList = touristNumNewestService.listTouristNumNewest();
        return ResultUtil.buildSuccessResultWithData(touristNumNewestVoList);
    }
}
