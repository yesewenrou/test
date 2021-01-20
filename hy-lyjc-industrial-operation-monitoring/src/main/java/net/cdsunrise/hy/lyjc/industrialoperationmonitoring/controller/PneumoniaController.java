package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IPneumaticService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang yun long
 * @date 2020-03-03 09:36
 */
@RestController
@RequestMapping("pneumonia")
public class PneumoniaController {

    private final IPneumaticService iPneumaticService;

    public PneumoniaController(IPneumaticService iPneumaticService) {
        this.iPneumaticService = iPneumaticService;
    }


    @GetMapping("peopleFromForeign")
    @Auth("pneumonia:peopleFromForeign")
    public Result peopleFromForeign(@RequestParam("city") String city,
                                    @RequestParam("dateType") String dateType,
                                    @RequestParam("begin") Long begin,
                                    @RequestParam("end") Long end) {
        return iPneumaticService.peopleFromForeign(city, dateType, begin, end);
    }


    @GetMapping("carFromForeign")
    @Auth("pneumonia:carFromForeign")
    public Result carFromForeign(@RequestParam("province") String province,
                                @RequestParam("city") String city,
                                @RequestParam("dateType") String dateType,
                                @RequestParam("begin") Long begin,
                                @RequestParam("end") Long end) {
        return iPneumaticService.carFromForeign(province, city, dateType, begin, end);
    }

    @GetMapping("carCities")
    @Auth("pneumonia:carCities")
    public Result carCities(){
        return iPneumaticService.carCities();
    }


}
