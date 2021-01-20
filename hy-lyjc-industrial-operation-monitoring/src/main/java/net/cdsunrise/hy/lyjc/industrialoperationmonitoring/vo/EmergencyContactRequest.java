package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fang yun long
 * on 2021/1/19
 */
public class EmergencyContactRequest {

    @Data
    public static class Add {
        /** "联系人 */
        @NotBlank(message = "联系人不能为空")
        private String name;
        /** 联系电话 */
        @NotBlank(message = "联系电话不能为空")
        private String phone;
        /** 单位名称 */
        @NotBlank(message = "单位名称不能为空")
        private String org;
        /** 职务名称 */
        @NotBlank(message = "职务名称不能为空")
        private String position;
    }

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Update extends Add {
        /** id */
        @NotNull(message = "id不能为空")
        private Long id;
    }

    @Data
    public static class Page {
        @NotNull(message = "页数不能为空")
        private Long current;
        @NotNull(message = "记录数不能为空")
        private Long size;
        private String name;
    }
}
