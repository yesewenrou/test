package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity;

import lombok.Data;

/**
 * @Author: suzhouhe @Date: 2020/2/28 13:45 @Description: 共享数据
 */
@Data
public class SharedDTO {
    /**
     * code码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 统计数量
     */
    private String count;
}
