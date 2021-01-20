package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningContactConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ISmsContactService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SmsContactCondition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author funnylog
 */
@Slf4j
@RestController
@RequestMapping("smsContact")
public class AutoWarningContactConfigController {

    private final ISmsContactService iSmsContactService;

    public AutoWarningContactConfigController(ISmsContactService iSmsContactService) {
        this.iSmsContactService = iSmsContactService;
    }


    @PostMapping("edit")
    @Auth("smsContact:edit")
    public Long editContact(@RequestBody @Validated SmsContactCondition.Edit edit) {
        return iSmsContactService.editContact(edit);
    }

    @GetMapping("list")
    @Auth("smsContact:list")
    public IPage<AutoWarningContactConfig> list(@RequestParam("page") Long page, @RequestParam("size") Long size) {
        return iSmsContactService.pageList(page, size);
    }

    @DeleteMapping
    @Auth("smsContact:delete")
    public void delete(@RequestParam("id") Long id) {
         log.info("预警-联系人-删除:{}", id);
         iSmsContactService.removeById(id);
    }

}
