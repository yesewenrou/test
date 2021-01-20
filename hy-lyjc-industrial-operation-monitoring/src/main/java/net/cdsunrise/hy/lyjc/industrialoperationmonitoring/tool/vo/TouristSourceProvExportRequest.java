package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2020/1/19 13:01
 */
@Data
public class TouristSourceProvExportRequest {

    /**
     * 开始时间
     */
    private Date startDate = new Date();

    /**
     * 结束时间
     */
    private Date endDate = new Date();

    public void setStartDate(Long startDate) {
        this.startDate = new Date(startDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = new Date(endDate);
    }
}
