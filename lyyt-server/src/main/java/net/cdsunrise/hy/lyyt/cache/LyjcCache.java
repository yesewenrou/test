package net.cdsunrise.hy.lyyt.cache;

import net.cdsunrise.hy.lyyt.entity.vo.WarningUnconfirmedVO;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/2/13 11:14
 */
@Service
public class LyjcCache {

    private static final String CACHE_NAME = "LYJC";

    @Cacheable(cacheNames = CACHE_NAME, key = "'DUTY_ROSTER'")
    public Object dutyRoster() {
        throw BusinessException.fail(BusinessExceptionEnum.NO_DATA).get();
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'WARNING_UNCONFIRMED'")
    public List<WarningUnconfirmedVO> warningUnconfirmed() {
        List<WarningUnconfirmedVO> list = new ArrayList<>();
        WarningUnconfirmedVO scenic = new WarningUnconfirmedVO("035001", 0);
        WarningUnconfirmedVO parking = new WarningUnconfirmedVO("035002", 0);
        WarningUnconfirmedVO traffic = new WarningUnconfirmedVO("035003", 0);
        list.add(scenic);
        list.add(parking);
        list.add(traffic);
        return list;
    }
}
