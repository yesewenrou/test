package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IKeyScenicService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyScenicVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LHY
 * @date 2020/1/13 15:56
 *
 * 重点景区数据（包含票务数据）
 */
@RestController
@RequestMapping("/keyScenic")
@Slf4j
public class KeyScenicController {

    private IKeyScenicService keyScenicService;

    public KeyScenicController(IKeyScenicService keyScenicService) {
        this.keyScenicService = keyScenicService;
    }

    @Auth(value = "keyScenic:touristList")
    @PostMapping("touristList")
    public Result touristList(@RequestBody PageRequest<KeyScenicVO> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(keyScenicService.touristList(pageRequest));
    }

    @Auth(value = "keyScenic:ticketList")
    @PostMapping("ticketList")
    public Result ticketList(@RequestBody PageRequest<KeyScenicVO> pageRequest) {
        return ResultUtil.buildSuccessResultWithData(keyScenicService.ticketList(pageRequest));
    }
}
