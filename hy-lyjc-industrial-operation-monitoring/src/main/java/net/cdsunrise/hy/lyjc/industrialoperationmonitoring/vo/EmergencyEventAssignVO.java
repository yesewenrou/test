package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2019/11/27 16:35
 */
@Data
public class EmergencyEventAssignVO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;

    /**
     * 被指派人员Id
     */
    @NotNull(message = "被指派人员Id不能为空")
    private Long assignedUserId;
}
