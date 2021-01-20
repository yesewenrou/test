package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/29 16:30
 */
@Data
public class CountyConsumptionCityVO {

    /**
     * 省份（此处存储省份code，方便统一查询）
     * */
    private String sourceProvince;

    /**
     * 城市（此处存储城市code，方便统一查询）
     * */
    private String sourceCity;

    /**
     * 日期，格式：2019-11-28
     * */
    private String dealDay;

    /**
     * 消费金额
     * */
    private Double transAt;

    /**
     * 消费笔数
     * */
    private Integer transNum;

    /**
     * 消费人次
     * */
    private Integer acctNum;

    /**
     * 人均消费金额
     * */
    private Double perConsumption;

    /**
     * 人均消费笔数
     * */
    private Double perConsumptionPens;

    public CountyConsumptionCityVO() {
    }

    public CountyConsumptionCityVO(String sourceProvince, Double transAt, Integer transNum, Integer acctNum, Double perConsumption, Double perConsumptionPens) {
        this.sourceProvince = sourceProvince;
        this.transAt = transAt;
        this.transNum = transNum;
        this.acctNum = acctNum;
        this.perConsumption = perConsumption;
        this.perConsumptionPens = perConsumptionPens;
    }
}
