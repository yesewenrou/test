package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/12/5 10:00
 *
 * 用于假日和历史统计中的旅游收入分析
 */
@Data
public class HolidayConsumptionVO {

    /**
     * 所属区域-商圈名称
     * */
    private String cbdName;

    /**
     * 收入金额
     * */
    private Double value;

    /**
     * 日期
     * */
    private String dealDay;

    public HolidayConsumptionVO() {
    }

    public HolidayConsumptionVO(String cbdName, Double value, String dealDay) {
        this.cbdName = cbdName;
        this.value = value;
        this.dealDay = dealDay;
    }
}
