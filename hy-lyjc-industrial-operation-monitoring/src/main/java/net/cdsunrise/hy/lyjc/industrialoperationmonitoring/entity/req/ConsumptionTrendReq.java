package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeDeserializer;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;

import java.time.LocalDateTime;

/**
 * @author YQ on 2020/3/25.
 */
@Data
public class ConsumptionTrendReq {

    /**
     * 范围
     */
    private String cbdName;

    /**
     * 开始时间
     */
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime beginDate;

    /**
     * 结束时间
     */
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    /**
     * @param offsetYear 偏移量(年)
     */
    public String fetchOffsetBeginDate(int offsetYear) {
        LocalDateTime time = beginDate.minusDays(1).minusYears(offsetYear);
        return time.format(DateUtil.LOCAL_DATE);
    }

    /**
     * @param offsetYear 偏移量(年)
     */
    public String fetchOffsetEndDate(int offsetYear) {
        LocalDateTime time = endDate.minusYears(offsetYear);
        return time.format(DateUtil.LOCAL_DATE);
    }
}
