package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author LHY
 * 用于带搜索条件查询统计结果
 */
@Data
public class StatisticsComplaintCondition {

    /**参数接收时间戳**/
    @NotNull(message = ParamConst.START_TIME_ERROR)
    private Timestamp startTime;
    @NotNull(message = ParamConst.END_TIME_ERROR)
    private Timestamp endTime;
    /**用于导出Excel显示投诉时间**/
    private String statisticsTime;
    /**投诉行业分类**/
    private String industryType;
    /**投诉分类**/
    private String type;
    /**投诉渠道**/
    private String channel;
    /**标记是按天/月/年统计**/
    @NotBlank(message = ParamConst.FLAG_ERROR)
    private String flag;
}
