package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2020/05/14 14:51
 */
@Data
public class ApiManagePageRequest {

    /**
     * 页数
     */
    @NotNull(message = "页数不能为空")
    private Long page;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Long size;

    /**
     * 数据来源
     */
    private String dataSource;
}
