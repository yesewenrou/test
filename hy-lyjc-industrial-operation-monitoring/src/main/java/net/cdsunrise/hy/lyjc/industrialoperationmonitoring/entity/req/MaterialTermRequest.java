package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req;

import lombok.Data;

import java.util.List;

/**
 * MaterialTermRequest
 * 物资条目请求
 * @author LiuYin
 * @date 2021/1/17 11:59
 */
@Data
public class MaterialTermRequest {

    /** id*/
    private Long id;

    /** 名称*/
    private String name;

    /** 用途*/
    private String purpose;

    /** 单位*/
    private String unit;

    /** 物资类型*/
    private Integer type;

    /** 期初库存*/
    private Long beginningInventory;

    /** 物资图片*/
    private List<String> pics;

}
