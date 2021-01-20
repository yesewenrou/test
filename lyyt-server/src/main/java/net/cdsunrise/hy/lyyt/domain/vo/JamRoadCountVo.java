package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * 当日拥堵路段条数VO
 *
 * @author YQ on 2019/12/21.
 */
@Data
public class JamRoadCountVo {
    /**
     * 路段总条数,目前固定58条
     */
    private Integer roadTotal = 60;
    /**
     * 当日路段拥堵条数
     */
    private Integer roadJamCount;
}
