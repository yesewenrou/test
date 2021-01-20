package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lijiafeng
 * @date 2020/1/17 15:08
 */
@Data
public class SpecialReportPageRequest {

    /**
     * 页数
     */
    @NotNull(message = "页数不能为空")
    private Long page = 1L;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Long size = 10L;

    /**
     * 报告名称
     */
    private String name;

    /**
     * 报告类型
     */
    private String type;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public void setStartTime(Long startTime) {
        this.startTime = new Date(startTime);
    }

    public void setEndTime(Long endTime) {
        this.endTime = new Date(endTime);
    }
}
