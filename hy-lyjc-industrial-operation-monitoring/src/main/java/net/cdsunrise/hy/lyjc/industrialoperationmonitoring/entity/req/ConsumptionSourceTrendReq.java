package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;
import net.cdsunrise.hy.lydsjdatacenter.starter.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.AdminAreaLevelEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author lijiafeng on 2020/4/1.
 */
@Data
public class ConsumptionSourceTrendReq {

    /**
     * 来源地ID
     */
    @NotBlank(message = "来源地ID不能为空")
    private String sourceId;

    /**
     * 来源地级别
     */
    @NotNull(message = "来源地级别不能为空")
    private AdminAreaLevelEnum level;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDate endDate;

    public void setBeginDate(Long beginDate) {
        this.beginDate = DateUtil.longToLocalDate(beginDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = DateUtil.longToLocalDate(endDate);
    }
}
