package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.AllArgsConstructor;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.TouristInformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 旅游资讯
 * @author YQ on 2020/3/24.
 */
@RestController
@RequestMapping("/tourist/information")
@AllArgsConstructor
public class TouristInformationController {
    private final TouristInformationService touristInformationService;

    /**
     * 资讯统计-月
     */
    @GetMapping("/statistics/month")
    public Object statisticsMonth() {
        return touristInformationService.statisticsMonth();
    }

    /**
     * 资讯统计-年
     */
    @GetMapping("/statistics/year")
    public Object statisticsYear() {
        return touristInformationService.statisticsYear();
    }
}
