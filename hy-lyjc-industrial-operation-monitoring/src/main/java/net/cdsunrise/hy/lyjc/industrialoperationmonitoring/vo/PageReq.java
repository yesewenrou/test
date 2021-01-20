package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lijiafeng 2021/01/18 15:46
 */
@Data
public class PageReq {

    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private Long current;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Long size;
}
