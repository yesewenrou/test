package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.WarningUnconfirmedVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/2/14 22:17
 */
public interface ILyjcService {

    /**
     * 值班表
     *
     * @return 结果
     */
    Object dutyRoster();

    /**
     * 未确认预警
     * @return  Object
     */
    List<WarningUnconfirmedVO> warningUnconfirmed();
}
