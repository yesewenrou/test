package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.ImportantProjectVO;
import net.cdsunrise.hy.lyyt.service.TravelEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.cdsunrise.hy.lyyt.enums.DataTypeEnum.LYXMSJ;
import static net.cdsunrise.hy.lyyt.enums.DataTypeEnum.SLQYJCSJ;

/**
 * TravelEnterpriseController
 * 涉旅企业控制器
 * @author LiuYin
 * @date 2020/1/19 14:14
 */
@RestController
@RequestMapping("/travel/enterprise")
public class TravelEnterpriseController {

    @Autowired
    private TravelEnterpriseService travelEnterpriseService;

    @GetMapping("/important/project")
    @DataType({SLQYJCSJ, LYXMSJ})
    public List<ImportantProjectVO> getAll(){
        return travelEnterpriseService.getAllImportantProject();
    }


}
