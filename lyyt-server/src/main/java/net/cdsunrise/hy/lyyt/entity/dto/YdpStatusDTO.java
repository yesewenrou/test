package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;

/**
 * @ClassName YdpStatusDTO
 * @Description 诱导屏状态转换对象
 * @Author LiuYin
 * @Date 2019/12/20 17:27
 */
@Data
public class YdpStatusDTO {

    /** 设备id*/
    private String deviceId;
    /** 在线状态（通常是在线或者离线）*/
    private String netStatus;
    /** 发送时间（时间戳）*/
    private Long happenTime;
    /** 唯一标识*/
    private Long id;
}
