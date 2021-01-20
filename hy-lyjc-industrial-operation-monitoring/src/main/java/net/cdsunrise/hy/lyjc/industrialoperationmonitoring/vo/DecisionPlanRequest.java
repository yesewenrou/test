package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author fang yun long
 * on 2021/1/18
 */
public class DecisionPlanRequest {

    @Data
    public static class Add {
        @NotBlank(message = "名称不能为空")
        private String name;
        @NotBlank(message = "事件类型不能为空")
        private String eventType;
        @NotBlank(message = "事件等级不能为空")
        private String eventLevel;

        @Valid
        private List<AttachmentRequest> attaches;
    }

    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Update extends Add {
        @NotNull(message = "ID不能为空")
        private Long id;
    }
}
