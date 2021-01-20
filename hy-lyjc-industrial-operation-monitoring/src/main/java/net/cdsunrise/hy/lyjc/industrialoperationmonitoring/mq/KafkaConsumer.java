package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mq;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ITouristNumNewestService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author LHY
 * @date 2019/10/18 14:43
 */
@Slf4j
//@Component
public class KafkaConsumer {

    private ITouristNumNewestService touristNumNewestService;

    public KafkaConsumer(ITouristNumNewestService touristNumNewestService) {
        this.touristNumNewestService = touristNumNewestService;
    }

    @KafkaListener(topics = {"pbu-yidong-tourist_minuteLocal-operated"})
    public void receiveTouristMinuteLocalData(String data) {
        log.info("receiveTouristMinuteLocalData: {}", data);
        TouristLocalData touristLocalData = JsonUtil.parse(data, TouristLocalData.class);
        touristNumNewestService.dealTouristLocalOperated(touristLocalData);
    }

    @KafkaListener(topics = "pbu-ticket_data-tourist_tickets-operated")
    public void receiveTouristTicketsData(String data) {
        TouristPassengerTicket ticket = JsonUtil.parse(data, TouristPassengerTicket.class);
        touristNumNewestService.dealTouristTicketOperated(ticket);
    }
}
