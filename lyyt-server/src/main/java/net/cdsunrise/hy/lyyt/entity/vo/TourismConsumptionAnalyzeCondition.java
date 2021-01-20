package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author lijiafeng
 * @date 2020/3/3 17:32
 */
@Data
public class TourismConsumptionAnalyzeCondition {

    /**
     * 查询类型
     */
    @NotNull(message = "查询类型不能为空")
    private TypeEnum type;

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

    public enum TypeEnum {

        /**
         * 日
         */
        DAY,

        /**
         * 月
         */
        MONTH
    }
}
