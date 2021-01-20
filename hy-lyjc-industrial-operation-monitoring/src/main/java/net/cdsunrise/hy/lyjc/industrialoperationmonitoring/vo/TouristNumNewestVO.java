package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/30 10:30
 */
@Data
public class TouristNumNewestVO {

    /**
     * 主键ID
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
}
