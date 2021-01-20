package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ParamConst;

import javax.validation.constraints.NotNull;

/**
 * @Author: LHY
 * @Date: 2019/9/28 13:50
 */
@Data
public class SourceCityTopVO {

    @NotNull(message = ParamConst.YEAR_ERROR)
    private Integer year;

    /**范围，0：不限，1：省内，2：省外**/
    private Integer scope;

}
