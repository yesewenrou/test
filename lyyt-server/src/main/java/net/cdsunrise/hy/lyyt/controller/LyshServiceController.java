package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.vo.CreditTrendVO;
import net.cdsunrise.hy.lyyt.entity.vo.InspectManageNewestVo;
import net.cdsunrise.hy.lyyt.entity.vo.KeyCountVO;
import net.cdsunrise.hy.lyyt.entity.vo.PatrolStatisticVO;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.LyshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YQ on 2020/1/16.
 */
@RestController
@RequestMapping("/lysh")
public class LyshServiceController {
    private final LyshService lyshService;

    @Autowired
    public LyshServiceController(LyshService lyshService) {
        this.lyshService = lyshService;
    }

    /**
     * 预警类型统计
     * @return vo
     */
    @GetMapping("/alarm/statistics/type")
    @DataType({DataTypeEnum.HYJGSJ})
    public List<KeyCountVO> alarmStatisticsType() {
        return lyshService.alarmStatisticsType();
    }

    /**
     * 最新预警统计
     * @return vo
     */
    @GetMapping("/alarm/statistics/newest")
    @DataType({DataTypeEnum.HYJGSJ})
    public List<InspectManageNewestVo> alarmStatisticsNewest() {
        return lyshService.alarmStatisticsNewest();
    }

    /**
     * 商户红黑榜
     * @return vo
     */
    @GetMapping("/credit/trend/list")
    @DataType({DataTypeEnum.HYJGSJ})
    public List<CreditTrendVO> creditTrendList() {
        return lyshService.creditTrendList();
    }
    /**
     * 商户巡查合格率
     * @return vo
     */
    @GetMapping("/patrol/statistic")
    @DataType({DataTypeEnum.HYJGSJ})
    public PatrolStatisticVO patrolStatistic() {
        return lyshService.patrolStatistic();
    }
}
