package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author lijiafeng
 * @date 2020/2/4 11:34
 */
@Data
public class TouristStatisticsRequest {

    /**
     * 按日统计
     */
    public static final String TYPE_DAY = "DAY";

    /**
     * 按月统计
     */
    public static final String TYPE_MONTH = "MONTH";

    /**
     * 正则
     */
    private static final String PATTERN = TYPE_DAY + "|" + TYPE_MONTH;

    /**
     * 景区名称
     */
    @NotBlank(message = "统计类型不能为空")
    @Pattern(regexp = PATTERN, message = "统计类型只能为MONTH或DAY")
    private String type;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private LocalDateTime beginDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    private LocalDateTime endDate;
}
