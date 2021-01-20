package net.cdsunrise.hy.lyyt.entity.vo.reids;

import lombok.Data;

/**
 * @ClassName RoadSectionJamStatisticsDo
 * @Description 交通拥堵统计对象
 * @Author LiuYin
 * @Date 2020/1/6 11:49
 */
@Data
public class RoadSectionJamStatisticsDo {

    /**
     * 路段id
     */
    private String sectionId;

    /**
     * 拥堵次数
     */
    private Integer jamCount;
    /**
     * 连续拥堵次数
     */
    private Integer continuousJamCount;

    public Integer getContinuousJamCount() {
        return continuousJamCount == null ? 0 : continuousJamCount;
    }
}
