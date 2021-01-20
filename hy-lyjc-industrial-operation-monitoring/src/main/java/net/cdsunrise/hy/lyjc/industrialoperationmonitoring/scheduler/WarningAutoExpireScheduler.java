package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 预警自动过期
 * @author funnylog
 */
@Slf4j
@Component
public class WarningAutoExpireScheduler {
    private final IAutoWarningService iAutoWarningService;

    public WarningAutoExpireScheduler(IAutoWarningService iAutoWarningService) {
        this.iAutoWarningService = iAutoWarningService;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void dutyRoster() {
        iAutoWarningService.warningAutoExpire();
        log.info("旅游监测景区丶停车丶交通预警自动过期完成");
    }
}
