package net.cdsunrise.hy.lyyt.service.impl;

import net.cdsunrise.hy.lyyt.cache.LyjcCache;
import net.cdsunrise.hy.lyyt.entity.vo.WarningUnconfirmedVO;
import net.cdsunrise.hy.lyyt.service.ILyjcService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/2/14 22:20
 */
@Service
public class LyjcServiceImpl implements ILyjcService {

    private LyjcCache lyjcCache;

    public LyjcServiceImpl(LyjcCache lyjcCache) {
        this.lyjcCache = lyjcCache;
    }

    @Override
    public Object dutyRoster() {
        return lyjcCache.dutyRoster();
    }

    /**
     * 未确认预警
     *
     * @return Object
     */
    @Override
    public List<WarningUnconfirmedVO> warningUnconfirmed() {
        return lyjcCache.warningUnconfirmed();
    }
}
