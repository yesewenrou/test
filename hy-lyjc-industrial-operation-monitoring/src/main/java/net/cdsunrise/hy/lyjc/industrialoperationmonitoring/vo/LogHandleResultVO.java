package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.common.utility.annotations.FieldInfo;

/**
 * @author LHY
 * @date 2019/11/20 15:03
 */
@Data
public class LogHandleResultVO {

    @FieldInfo(value = "投诉编号",order = 1)
    private String complaintNumber;

    @FieldInfo(value = "被投诉方全称",order = 2)
    private String complaintObjectFullname;

    @FieldInfo(value = "投诉行业分类",order = 3)
    private String industryType;
}
