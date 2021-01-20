package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IAutoWarningService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ILyytCacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lijiafeng
 * @date 2020/2/14 14:02
 */
@Slf4j
@Component
public class LyytCacheScheduler {

    private ILyytCacheService lyytCacheService;
    private IAutoWarningService iAutoWarningService;

    public LyytCacheScheduler(ILyytCacheService lyytCacheService, IAutoWarningService iAutoWarningService) {
        this.lyytCacheService = lyytCacheService;
        this.iAutoWarningService = iAutoWarningService;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void dutyRoster() {
        lyytCacheService.dutyRoster();
        log.info("缓存刷新完成：DUTY_ROSTER");
    }

    /**
     * 根据预警类型统计未确认状态的数量
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void warningStatistics() {
        iAutoWarningService.countUnconfirmedWarningByType();
        log.info("缓存刷新完成：warningStatistics");
    }
}
