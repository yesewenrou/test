package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


/**
 * @author funnylog
 */
@ToString
public class AutoWarningDTO {

    @Data
    public static class Common {
        /** 预警对象 **/
        private String object;
        /** 预警类型 AutoWarningTypeEnum **/
        private String type;
        /** 预警时间 **/
        private Date warningTime;
    }

    /**
     * 景区客流量预警 AutoWarningTypeEnum : 035001
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class ScenicWarning extends Common{
        /** 预警人数 **/
        private Integer peopleNum;
    }
}
