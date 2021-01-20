package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.JsonLocalDateTimeDeserializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author zbk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCenterMonthlyReportReq {

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime beginDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;
}
