package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LHY
 * @date 2020/1/13 15:35
 */
@Data
public class KeyScenicVO {

    /**
     * 景区名称
     */
    private String scenicName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
