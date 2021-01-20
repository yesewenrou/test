package net.cdsunrise.hy.lyyt.controller;

import com.alibaba.fastjson.JSON;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.WarningUnconfirmedVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.ILyjcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author lijiafeng
 * @date 2020/2/14 22:44
 */
@RestController
@RequestMapping("lyjc")
public class LyjcController {

    private ILyjcService lyjcService;

    public LyjcController(ILyjcService lyjcService) {
        this.lyjcService = lyjcService;
    }

    /**
     * 值班表
     *
     * @return 结果
     */
    @GetMapping("dutyRoster")
    @DataType({DataTypeEnum.HYJGSJ})
    public Object dutyRoster() {
        return lyjcService.dutyRoster();
    }

    /**
     * 待确认预警
     * @return
     */
    @GetMapping("unconfirmedWarning")
    @DataType({DataTypeEnum.HYJGSJ})
    public List<WarningUnconfirmedVO> unconfirmedWarning() {
        return lyjcService.warningUnconfirmed();
    }
}
