package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cdsunrise.hy.lyxxfb.iip.autoconfigure.feign.req.PublishRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author funnylog
 */
public class AutoWarningCondition {

    /**
     * 告警-待确认列表
     **/
    @Data
    public static class Query {
        private String type = "035001";
        private String object;
        private List<String> status;
        @NotNull(message = "开始时间不能为空")
        private Date begin;
        @NotNull(message = "结束时间不能为空")
        private Date end;
        @NotNull(message = "查询页数不能为空")
        private Long page;
        @NotNull(message = "查询记录数不能为空")
        private Long size;

        private String[] orderBy;
        /** 是否是升序 **/
        private Boolean asc = false;
    }

    /**
     * 告警-待确认-申请发布
     */
    @Data
    public static class RequestPublic {
        @NotNull(message = "预警id不能为空")
        private Long id;
        @Valid
        @NotNull(message = "请选择接收者")
        private Set<Receiver> receivers;

        @NotBlank(message = "内容不能为空")
        private String content;

        /**
         * 处理类型 1 申请发布 2 发送短信
         **/
        @NotNull(message = "处理类型不能为空")
        private Integer handleType;

        /**
         * 附件列表。 发布短信是可不传
         */
        @Valid
        private Set<Attach> attaches;
    }

    @Data
    public static class Receiver {
        @NotNull(message = "接收者id不能为空")
        private Long id;
        @NotBlank(message = "接收者名称不能为空")
        private String name;
    }

    @Data
    public static class Attach {
        /**
         * 附件名称
         */
        @NotBlank(message = "附件名称不能为空")
        private String name;

        /**
         * 附件地址
         */
        @NotBlank(message = "附件地址不能为空")
        private String url;
    }

    /**
     * 即时信息发布请求类
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class PublishReq extends PublishRequest.InsertProgramReq {
        @NotNull(message = "预警ID不能为空")
        private Long id;
    }

    /**
     * 节目发布请求类
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class PublishProgramReq extends PublishRequest.AddReq {

    }
}
