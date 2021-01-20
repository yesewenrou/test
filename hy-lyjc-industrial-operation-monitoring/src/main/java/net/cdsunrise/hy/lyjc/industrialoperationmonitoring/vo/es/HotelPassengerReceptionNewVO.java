package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LHY
 * @date 2020/3/24 14:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPassengerReceptionNewVO {
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
     * 期间累计接待
     * */
    private Long cumulativeReception;
}
