package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author YQ on 2020/3/26.
 */
@Data
public class IndustryConsumptionResp {

    /**
     * 累计消费金额
     */
    private BigDecimal totalTransAt;

    /**
     * 各行业消费
     */
    private List<Detail> list;

    @Data
    public static class Detail {

        /**
         * 行业
         */
        private String type;

        /**
         * 游客消费金额
         */
        private BigDecimal transAt;

        /**
         * 游客消费笔数
         */
        private Integer transNum;

        /**
         * 游客消费人次
         */
        private Integer acctNum;

        /**
         * 游客消费额金额贡献度
         */
        private BigDecimal transAtRatio;

        /**
         * 人均消费金额（元）
         */
        private BigDecimal avgTransAt;

        /**
         * 人均消费笔数
         */
        private BigDecimal avgTransNum;
    }
}
