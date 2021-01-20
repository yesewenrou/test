package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 * @author LHY
 * @date 2019/12/2 14:33
 */
@Data
public class ProvinceConsumptionCondition {

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
}
