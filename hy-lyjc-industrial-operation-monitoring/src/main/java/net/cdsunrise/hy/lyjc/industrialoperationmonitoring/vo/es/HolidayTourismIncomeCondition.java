package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.checkgroup.UpdateCheckGroup;

import javax.validation.constraints.NotNull;

/**
 * @author LHY
 * @date 2019/12/4 13:47
 *
 * 用于假日统计分析-旅游收入条件搜索
 */
@Data
public class HolidayTourismIncomeCondition {

    /**
     * 所属区域-商圈名称
     * */
    private String cbdName;
    /**今年开始时间**/
    @NotNull(message = ParamConst.START_TIME_ERROR)
    private String startTime;
    /**今年结束时间**/
    @NotNull(message = ParamConst.END_TIME_ERROR)
    private String endTime;
    /**去年开始时间**/
    @NotNull(message = ParamConst.START_TIME_ERROR,groups = {UpdateCheckGroup.class})
    private String lastStartTime;
    /**去年结束时间**/
    @NotNull(message = ParamConst.END_TIME_ERROR,groups = {UpdateCheckGroup.class})
    private String lastEndTime;
    /**标记是按天/月/年统计**/
    private String flag = "day";
}
