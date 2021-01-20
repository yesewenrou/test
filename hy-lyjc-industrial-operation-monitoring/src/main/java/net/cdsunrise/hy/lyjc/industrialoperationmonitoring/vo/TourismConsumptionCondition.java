package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;

import javax.validation.constraints.NotNull;

/**
 * @author LHY
 * @date 2019/11/28 14:29
 *
 * 用于旅游消费条件搜索
 */
@Data
public class TourismConsumptionCondition {

    /**
     * 商圈名称
     * */
    private String cbdName;

    /**
     * 游客类型，省内:1, 省外:2
     * */
    private String travellerType;

    /**
     * 省份（此处存储省份code，方便统一查询）
     * */
    private String sourceProvince;

    /**
     * 城市（此处存储城市code，方便统一查询）
     * */
    private String sourceCity;

    /**
     * 行业
     * */
    private String type;

    @NotNull(message = ParamConst.START_TIME_ERROR)
    private String startTime;

    @NotNull(message = ParamConst.END_TIME_ERROR)
    private String endTime;
}
