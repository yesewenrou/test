package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentVO;
import java.util.Map;

/**
 * @Author: LHY
 * @Date: 2019/9/25 15:37
 */
public interface PublicSentimentService {

    /**
     * 舆情列表
     *
     */
    PageResult<PublicSentimentVO> list(Integer page, Integer size, PublicSentimentCondition condition);

    /**
     * 舆情最近走势（7日舆情和30日舆情）
     *
     */
    Map<String,Object> publicSentimentTrend(String startTime,String endTime);

    /**
     * 舆情最近走势（自定义筛选时间）
     *
     */
    Map<String,Object> publicSentimentTrend(Long beginDate,Long endDate);
}
