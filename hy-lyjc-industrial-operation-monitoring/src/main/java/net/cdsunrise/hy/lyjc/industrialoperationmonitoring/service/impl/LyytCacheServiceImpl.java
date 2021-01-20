package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyRosterService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ILyytCacheService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterVO;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/2/14 21:44
 */
@Service
public class LyytCacheServiceImpl implements ILyytCacheService {

    private IDutyRosterService dutyRosterService;

    public LyytCacheServiceImpl(IDutyRosterService dutyRosterService) {
        this.dutyRosterService = dutyRosterService;
    }

    @Override
    @CachePut(cacheNames = "LYJC", key = "'DUTY_ROSTER'")
    public List<DutyRosterVO> dutyRoster() {
        return dutyRosterService.listDutyRoster();
    }
}
