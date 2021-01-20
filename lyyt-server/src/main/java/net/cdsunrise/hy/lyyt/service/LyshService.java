package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.CreditTrendVO;
import net.cdsunrise.hy.lyyt.entity.vo.InspectManageNewestVo;
import net.cdsunrise.hy.lyyt.entity.vo.KeyCountVO;
import net.cdsunrise.hy.lyyt.entity.vo.PatrolStatisticVO;

import java.util.List;

/**
 * @author YQ on 2020/1/16.
 */
public interface LyshService {
    /**
     * 最新预警统计
     * @return vo
     */
    List<InspectManageNewestVo> alarmStatisticsNewest();

    /**
     * 预警类型统计
     * @return vo
     */
    List<KeyCountVO> alarmStatisticsType();

    /**
     * 商户红黑榜
     * @return
     */
    List<CreditTrendVO> creditTrendList();

    /**
     * 商户巡查合格率
     * @return
     */
    PatrolStatisticVO patrolStatistic();
}
