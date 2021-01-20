package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LHY
 * @date 2019/11/6 11:57
 */
@Data
public class HolidayRequest {
    private Date startDate;
    private Date endDate;
    // 去年
    private Date lastStartDate;
    private Date lastEndDate;
}
