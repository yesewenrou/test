package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterVO;

import java.util.List;

/**
 * 旅游云图会用到的缓存
 *
 * @author lijiafeng
 * @date 2020/2/11 16:50
 */
public interface ILyytCacheService {

    /**
     * 值班表
     *
     * @return 结果
     */
    List<DutyRosterVO> dutyRoster();
}
