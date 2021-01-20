package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:33
 */
@Data
public class Pageable {

    /**
     * 页数
     */
    @NotNull(message = "页码不能为空")
    private Integer page;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Integer size;
}
