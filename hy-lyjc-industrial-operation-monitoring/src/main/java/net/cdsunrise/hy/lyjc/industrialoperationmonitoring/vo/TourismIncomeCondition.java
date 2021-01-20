package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/9/17 17:41
 */
@Data
public class TourismIncomeCondition {
    private String incomeSource;
    private String scenicName;
    /**今年开始时间**/
    @NotNull(message = ParamConst.START_TIME_ERROR)
    private Timestamp startTime;
    /**今年结束时间**/
    @NotNull(message = ParamConst.END_TIME_ERROR)
    private Timestamp endTime;
    /**去年开始时间**/
    @NotNull(message = ParamConst.START_TIME_ERROR,groups = {UpdateCheckGroup.class})
    private Timestamp lastStartTime;
    /**去年结束时间**/
    @NotNull(message = ParamConst.END_TIME_ERROR,groups = {UpdateCheckGroup.class})
    private Timestamp lastEndTime;
    /**标记是按天/月/年统计**/
    private String flag = "day";
}
