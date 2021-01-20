package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/12/11 15:11
 */
@Data
public class HolidayEsRequest {

    private String startTime;
    private String endTime;
    private String lastStartTime;
    private String lastEndTime;
}
