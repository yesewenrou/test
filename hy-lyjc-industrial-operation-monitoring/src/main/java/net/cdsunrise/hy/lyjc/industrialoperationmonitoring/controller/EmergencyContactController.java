package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.EmergencyContactService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactDetailVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang yun long
 * on 2021/1/19
 */
@RestController
@RequestMapping("emergency_contact")
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    @Auth("emergency_contact:add")
    public Result<Long> addEmergencyContact(@RequestBody @Validated EmergencyContactRequest.Add request) {
        return ResultUtil.buildSuccessResultWithData(emergencyContactService.saveEmergencyContact(request));
    }

    @PutMapping
    @Auth("emergency_contact:update")
    public Result<Long> updateEmergencyContact(@RequestBody @Validated EmergencyContactRequest.Update request) {
        return ResultUtil.buildSuccessResultWithData(emergencyContactService.updateEmergencyContact(request));
    }

    @DeleteMapping("{id}")
    @Auth("emergency_contact:delete")
    public Result<Long> deleteEmergencyContact(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyContactService.deleteEmergencyContact(id));
    }

    @GetMapping("list")
    public Result<PageResult<EmergencyContactVO>> pageEmergencyContact(@Validated EmergencyContactRequest.Page request) {
        return ResultUtil.buildSuccessResultWithData(emergencyContactService.pageEmergencyContact(request));
    }

    @GetMapping("{id}")
    public Result<EmergencyContactDetailVO> detailEmergencyContact(@PathVariable Long id) {
        return ResultUtil.buildSuccessResultWithData(emergencyContactService.detailEmergencyContact(id));
    }

}
