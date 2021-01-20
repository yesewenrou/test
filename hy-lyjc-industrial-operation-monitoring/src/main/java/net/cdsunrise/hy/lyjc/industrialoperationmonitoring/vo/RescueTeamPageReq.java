package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author lijiafeng 2021/01/18 15:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RescueTeamPageReq extends PageReq {

    /**
     * 救援队伍类型
     */
    private Integer type;

    /**
     * 救援队伍名称
     */
    private String name;
}
