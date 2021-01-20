package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author YQ on 2019/11/15.
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/normal")
    public Long normal(@RequestParam DataTypeEnum dataType, Date date) {
        return transferService.normal(dataType, date);
    }
    @GetMapping("/normal/total")
    public Long normalTotal(@RequestParam DataTypeEnum dataType) {
        return transferService.normalTotal(dataType);
    }
    @GetMapping("/error")
    public Long error(Date date) {
        return transferService.error(date);
    }
    @GetMapping("/error/total")
    public Long errorTotal() {
        return transferService.errorTotal();
    }
}
