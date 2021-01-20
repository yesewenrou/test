package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/28 14:14
 */
@Data
public class CountyBusinessCircleConsumptionVO {

    /**
     * 商圈名称
     * */
    private String cbdName;

    /**
     * 游客类型，省内:1, 省外:2
     * */
    private String travellerType;

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
}
