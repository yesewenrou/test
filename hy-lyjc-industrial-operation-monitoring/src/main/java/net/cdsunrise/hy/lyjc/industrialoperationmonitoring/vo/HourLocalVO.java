package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lijiafeng
 * @date 2019/9/30 14:03
 */
@Data
public class HourLocalVO {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 景区ID
     */
    private String scenicId;

    /**
     * 景区名称
     */
    private String scenicName;

    /**
     * 人数
     */
    private Integer peopleNum;

    /**
     * 人员类型:0为游客 1 为居住 2为上班
     */
    private Integer memberType;

    /**
     * 统计时间
     */
    private Date time;
}
