package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

/**
 * MaterialTermPageRequest
 *
 * @author LiuYin
 * @date 2021/1/18 18:59
 */
@Data
public class MaterialTermPageRequest {

    /** 当前页*/
    private Integer current;
    /** 每页条数*/
    private Integer size;
    /** 物资类型*/
    private Integer type;
    /** 物资名称*/
    private String name;
}
