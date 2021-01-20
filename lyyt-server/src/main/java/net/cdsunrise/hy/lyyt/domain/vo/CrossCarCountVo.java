package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * 进城车辆
 *
 * @author YQ on 2019/11/6.
 */
@Data
public class CrossCarCountVo {
    /**
     * 当前进城车辆
     */
    private Integer currentCarCount;
    /**
     * 当月进城车辆
     */
    private Integer monthCarCount;

    public static CrossCarCountVo build(Integer currentCarCount, Integer monthCarCount) {
        CrossCarCountVo vo = new CrossCarCountVo();
        vo.setCurrentCarCount(currentCarCount);
        vo.setMonthCarCount(monthCarCount);
        return vo;
    }
}
