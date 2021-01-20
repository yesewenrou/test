package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/19 15:21
 */
@Data
public class HotelPassengerReceptionVO {

    private String name;

    private String area;

    private String address;

    /**
     * 统计日期
     * */
    private String statisticalDate;

    /**
     * 当日累计接待
     * */
    private Double cumulativeReception;

    /**
     * 当日估算入住率
     * */
    private String estimateOccupancy;

    /** 当日酒店状态，0：未满，1：已满客 **/
    private Integer status = 0;
}
