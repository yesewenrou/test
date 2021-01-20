package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/28 16:28
 */
@Data
public class ConsumptionVO {

    private String name;

    private Double value;

    public ConsumptionVO() {
    }

    public ConsumptionVO(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
