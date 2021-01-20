package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.controller;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.service.ITouristSourceProvService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.vo.TouristSourceProvExportRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiafeng
 * @date 2020/1/19 13:21
 */
@RestController
@RequestMapping("tourist-source-prov")
public class TouristSourceProvController {

    private ITouristSourceProvService touristSourceProvService;

    public TouristSourceProvController(ITouristSourceProvService touristSourceProvService) {
        this.touristSourceProvService = touristSourceProvService;
    }

    @GetMapping("day/export")
    public String exportDay(TouristSourceProvExportRequest request) {
        return touristSourceProvService.exportDay(request);
    }
}
