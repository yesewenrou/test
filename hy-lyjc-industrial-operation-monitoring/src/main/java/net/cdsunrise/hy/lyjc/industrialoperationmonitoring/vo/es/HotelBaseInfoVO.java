package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/13 16:17
 */
@Data
public class HotelBaseInfoVO {

    /**
    * 公安编码
    * */
    private String stationId;

    /**
     * 酒店名称
     * */
    private String name;

    /**
     * 所属区域
     * */
    private String area;

    /**
     * 所属商圈
     * */
    private String businessCircle;

    /**
     * 酒店地址
     * */
    private String address;

    /**
     * 床位总数
     * */
    private Integer bedNum;

    /**
     * 联系方式
     * */
    private String phoneNum;
}
