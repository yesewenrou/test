package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2020/5/6 13:46
 *
 * 酒店接待量统计升级版（基于凌晨校准数据 + 当日入住数据），由于字段相同，也可与之前逻辑公用
 */
@Data
public class HotelReceptionCommon {

    private String id;

    private String stationId;

    /**
     * 当日累计接待
     * */
    private Integer cumulativeReception;

    /**
     * 统计日期
     * */
   private String statisticalDate;

    /** 记录插入系统时间，方便追溯数据 **/
    private String createTime;

}
