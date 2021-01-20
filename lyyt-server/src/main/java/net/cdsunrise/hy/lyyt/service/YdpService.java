package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.YdpInfoVO;

import java.util.List;

/**
 * @ClassName YdpService
 * @Description 诱导屏服务接口
 * @Author LiuYin
 * @Date 2019/12/21 10:25
 */
public interface YdpService {

    /**
     * 获取所有诱导屏
     * @return 诱导屏列表
     */
    List<YdpInfoVO> getAll();
}
