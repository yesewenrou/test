package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2020/1/13 15:28
 */
@Data
public class KeyTicketVO {

    /**
     * 线上销售票数
     */
    private Integer onlineTicketCount;

    /**
     * 线下销售票数
     */
    private Integer offlineTicketCount;

    /**
     * 线上索道总票数（通过 线上门票销售数+线上门票销售占比，倒推出索道和观光车）
     */
    private Long onlineCablewayCount;

    /**
     * 线上观光车总票数
     */
    private Long onlineSightseeingCarCount;

    /**
     * 采集时间
     */
    private String responseTime;
}
