package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeSerializer;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.serializer.DoubleSerializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消费趋势
 *
 * @author YQ on 2020/3/26.
 */
@Data
public class ConsumptionTrendResp {
    private List<Detail> thisYearList;
    private List<Detail> lastYearList;

    @Data
    @AllArgsConstructor
    public static class Detail{
        /**
         * 时间
         */
        @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
        private LocalDateTime time;

        /**
         * 今年消费金额
         * */
        @JsonSerialize(using = DoubleSerializer.class)
        private Double transAtThisYear;


        /**
         * 去年消费金额
         */
        @JsonSerialize(using = DoubleSerializer.class)
        private Double transAtLastYear;

        /**
         * 较去年同期(百分比)
         */
        private Integer compareLastYear;
        /**
         * 较昨日环比(百分比)
         */
        private Integer compareYesterday;
    }
}
