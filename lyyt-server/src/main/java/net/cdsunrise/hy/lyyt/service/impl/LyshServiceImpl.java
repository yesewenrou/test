package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.vo.CreditTrendVO;
import net.cdsunrise.hy.lyyt.entity.vo.InspectManageNewestVo;
import net.cdsunrise.hy.lyyt.entity.vo.KeyCountVO;
import net.cdsunrise.hy.lyyt.entity.vo.PatrolStatisticVO;
import net.cdsunrise.hy.lyyt.service.LyshService;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YQ on 2020/1/16.
 */
@Service
public class LyshServiceImpl implements LyshService {
    private static final String LYSH_ALARM_TYPE_STATISTICS = "LYSH_ALARM_TYPE_STATISTICS";
    private static final String LYSH_ALARM_NEWEST_STATISTICS = "LYSH_ALARM_NEWEST_STATISTICS";
    private static final String LYSH_CREDIT_TREND_LIST = "LYSH_CREDIT_TREND_LIST";
    private static final String LYSH_PATROL_STATISTIC = "LYSH_PATROL_STATISTIC";

    @Override
    public List<InspectManageNewestVo> alarmStatisticsNewest() {
        String redisData = RedisUtil.valueOperations().get(LYSH_ALARM_NEWEST_STATISTICS);
        return JsonUtils.toObject(redisData, new TypeReference<List<InspectManageNewestVo>>() {});
    }

    @Override
    public List<KeyCountVO> alarmStatisticsType() {
        String redisData = RedisUtil.valueOperations().get(LYSH_ALARM_TYPE_STATISTICS);
        return JsonUtils.toObject(redisData, new TypeReference<List<KeyCountVO>>() {});
    }

    @Override
    public List<CreditTrendVO> creditTrendList() {
        String redisData = RedisUtil.valueOperations().get(LYSH_CREDIT_TREND_LIST);
        return JsonUtils.toObject(redisData, new TypeReference<List<CreditTrendVO>>() {});
    }

    @Override
    public PatrolStatisticVO patrolStatistic() {
        String redisData = RedisUtil.valueOperations().get(LYSH_PATROL_STATISTIC);
        return JsonUtils.toObject(redisData, new TypeReference<PatrolStatisticVO>() {});
    }
}
