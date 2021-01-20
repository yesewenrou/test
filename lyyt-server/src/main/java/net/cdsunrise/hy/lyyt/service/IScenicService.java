package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyyt.entity.vo.TouristCountVO;

/**
 *
 * @author fang yun long
 * @date 2020-01-16
 */
public interface IScenicService {

    /**
     * 景区统计本周和上周的销售票数
     * @return Result
     */
    Result statisticScenicTickets();

    /**
     * 统计本月和上月客流人数
     * @return result
     */
    Result<TouristCountVO> statisticCurrentAndLastMonthPassenger();
}
