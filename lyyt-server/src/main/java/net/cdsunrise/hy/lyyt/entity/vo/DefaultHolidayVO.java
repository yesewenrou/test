package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LHY
 * @date 2020/2/17 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultHolidayVO {

    private String key;
    private String value;
    /**标记当前节假日*/
    private Boolean current;
}
