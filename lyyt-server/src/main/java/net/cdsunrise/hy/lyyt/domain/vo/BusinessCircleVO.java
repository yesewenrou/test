package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/12/19 17:48
 */
@Data
public class BusinessCircleVO {

    /**
     * 名称
     * */
    private String name;

    /**
     * 消费金额
     * */
    private Double transAt;

    /**
     * 消费人次
     * */
    private Double acctNum;

    /**
     * 人均消费金额
     * */
    private Double perConsumption;

    public BusinessCircleVO() {
    }

    public BusinessCircleVO(String name, Double transAt, Double acctNum, Double perConsumption) {
        this.name = name;
        this.transAt = transAt;
        this.acctNum = acctNum;
        this.perConsumption = perConsumption;
    }
}
