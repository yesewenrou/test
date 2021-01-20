package net.cdsunrise.hy.lyyt.task;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * TranferTask
 *
 * @author LiuYin
 * @date 2020/8/20 15:17
 */
@Component
@Slf4j
public class TransferTask {

    @Autowired
    private TransferService transferService;

    @PostConstruct
    private void init(){
        final LocalDateTime now = LocalDateTime.now();
        if(now.getYear() == 2020 && now.getMonthValue() == 8 && now.getDayOfMonth()  == 20){
            transferService.initDayCount(now.toLocalDate());
            transferService.initDayCount(now.plusDays(1).toLocalDate());
        }
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 60000)
    public void task(){
        LocalDateTime now = LocalDateTime.now();
        try{
            if(now.getHour() == 23){
                transferService.initDayCount(now.toLocalDate());
            }
        }catch (Exception e){
            log.error("transfer task error,cause ", e);
        }
    }


}
