package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author funnylog
 */
public class SmsContactCondition {
    private static final String PHONE_REGEXP = "^1[3-9]\\d{9}$";

    @Data
    public static class Edit {
        /** 姓名 **/
        @NotBlank(message = "姓名不能为空")
        @Length(max = 11, message = "姓名长度不能超过11位")
        private String name;

        /** 手机号 **/
        @NotBlank(message = "手机号不能为空")
        @Length(max = 11, message = "手机号长度不能超过11位")
        @Pattern(regexp = PHONE_REGEXP, message = "手机号格式不正确")
        private String phone;

        /** 是否自动发送客流预警短信 **/
        private Boolean scenicAuto = false;
        /** 是否自动发送交通预警短信 **/
        private Boolean trafficAuto = false;
        /** 是否自动发送停车预警短信 **/
        private Boolean carAuto = false;
    }
}
