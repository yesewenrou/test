package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author LHY
 * 用于构建图表数据
 */
@Data
public class ChartVO {

    private String name;
    private Integer value;

    public ChartVO() {
    }

    public ChartVO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
