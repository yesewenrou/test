package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author YQ on 2020/3/25.
 */
@Data
public class ConsumptionStatisticsReq {

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endDate;
}
