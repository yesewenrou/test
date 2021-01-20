package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2020/1/16 21:01
 */
@Data
public class NewPublicResourceVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域名称
     */
    private String region;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phoneNumber;
}
