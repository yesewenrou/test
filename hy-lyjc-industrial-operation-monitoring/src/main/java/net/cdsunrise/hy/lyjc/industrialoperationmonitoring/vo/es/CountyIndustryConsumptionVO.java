package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/12/2 9:57
 */
@Data
public class CountyIndustryConsumptionVO {

    /**
     * 行业
     * */
    private String type;

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

    /**
     * 行业总消费金额
     * */
    private Double transAtTotal;

    /**
     * 游客消费额金额贡献度
     * */
    private Double transAtRatio;
}
