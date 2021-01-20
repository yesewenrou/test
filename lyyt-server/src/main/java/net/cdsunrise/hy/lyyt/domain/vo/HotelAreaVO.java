package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author LHY
 * @date 2020/2/7 10:07
 *
 * 用于展示 “产业运行监测 -> 住宿接待 -> 柱状图”
 */
@Data
public class HotelAreaVO {

    /**
     * 区域名称
     * */
    private String name;

    /**
     * 床位总数
     * */
    private Integer bedNum;

    /**
     * 入住人数
     * */
    private Integer checkInNum;
}
