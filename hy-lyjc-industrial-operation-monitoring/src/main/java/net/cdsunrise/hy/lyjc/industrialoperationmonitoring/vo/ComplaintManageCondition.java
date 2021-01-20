package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author LHY
 * 用于分页搜索条件
 */
@Data
public class ComplaintManageCondition {

    private String complaintNumber;
    private String type;
    private Integer status;

}
