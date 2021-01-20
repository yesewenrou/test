package net.cdsunrise.hy.lyyt.entity.vo.reids;

import lombok.Data;

/**
 * @ClassName RoadSectionTpiDto
 * @Description 路段拥堵指数转换对象
 * @Author LiuYin
 * @Date 2020/1/6 11:18
 */
@Data
public class RoadSectionTpiDto {

    /**
     * 路段id
     */
    private String sectionId;
    /**
     * 拥堵指数
     */
    private Double tpi;

    /**
     * 平均速度
     */
    private Double avgSpeed;

    /**
     * 数据时间
     */
    private String rangTime;



}
