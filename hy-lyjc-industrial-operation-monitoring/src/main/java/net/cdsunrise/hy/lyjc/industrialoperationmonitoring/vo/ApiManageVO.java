package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

/**
 * API管理
 *
 * @author lijiafeng
 * @date 2020/05/14 14:44
 */
@Data
public class ApiManageVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口描述
     */
    private String interfaceDesc;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 接口地址
     */
    private String interfaceAddress;
}
