package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.AdminAreaLevelEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/3/31 17:19
 */
@Data
public class ConsumptionSourceResp {

    /**
     * 累计消费金额
     */
    private BigDecimal totalTransAt;

    /**
     * 各国消费
     */
    private List<Detail> list;

    @Data
    public static class Detail {

        public static Detail getEmpty(String sourceId, AdminAreaLevelEnum level) {
            Detail empty = new Detail();
            empty.setSourceId(sourceId);
            empty.setLevel(level);
            empty.setTransAt(BigDecimal.ZERO);
            empty.setTransNum(0);
            empty.setAcctNum(0);
            empty.setAvgTransAt(BigDecimal.ZERO);
            empty.setAvgTransNum(BigDecimal.ZERO);
            if (AdminAreaLevelEnum.CITY.equals(level)) {
                empty.setChildren(null);
            } else {
                empty.setChildren(new ArrayList<>(0));
            }

            return empty;
        }

        /**
         * 来源地ID
         */
        private String sourceId;

        /**
         * 来源地名称
         */
        private String sourceName;

        /**
         * 来源地级别 国、省、市
         */
        private AdminAreaLevelEnum level;

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
         * 人均消费金额（元）
         */
        private BigDecimal avgTransAt;

        /**
         * 人均消费笔数
         */
        private BigDecimal avgTransNum;

        /**
         * 下级来源地
         */
        private List<Detail> children;
    }
}
