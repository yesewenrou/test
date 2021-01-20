package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.PublicSentimentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.aop.Auth;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.PublicSentimentCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author: LHY
 * @Date: 2019/9/25 15:27
 */
@RestController
@RequestMapping("/publicSentiment")
@Slf4j
public class PublicSentimentController {

    @Autowired
    private PublicSentimentService publicSentimentService;

    /**
     * 产业管理 -> 舆情管理 -> 列表
     *
     * */
    @Auth(value = "publicSentiment:list")
    @PostMapping("list")
    public Result list(@Validated @RequestBody PageRequest<PublicSentimentCondition> pageRequest){
        return ResultUtil.buildSuccessResultWithData(publicSentimentService.list(Integer.parseInt(String.valueOf(pageRequest.getCurrent())),
                Integer.parseInt(String.valueOf(pageRequest.getSize())),pageRequest.getCondition()));
    }

    /**
     * 产业管理 -> 舆情管理 -> 最近7天舆情分析
     *
     * */
    @Auth(value = "publicSentiment:sevenPublicSentimentTrend")
    @GetMapping("sevenPublicSentimentTrend")
    public Result sevenPublicSentimentTrend(){
        String today = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD);
        String lastWeek = DateUtil.format(DateUtil.getTime("day",new Date(),-6),DateUtil.PATTERN_YYYY_MM_DD);
        return ResultUtil.buildSuccessResultWithData(publicSentimentService.publicSentimentTrend(lastWeek,today));
    }

    /**
     * 产业管理 -> 舆情管理 -> 最近30天舆情分析
     *
     * */
    @Auth(value = "publicSentiment:thirtyPublicSentimentTrend")
    @GetMapping("thirtyPublicSentimentTrend")
    public Result thirtyPublicSentimentTrend(){
        String today = DateUtil.getTodayString(DateUtil.PATTERN_YYYY_MM_DD);
        String lastMonth = DateUtil.format(DateUtil.getTime("day",new Date(),-29),DateUtil.PATTERN_YYYY_MM_DD);
        return ResultUtil.buildSuccessResultWithData(publicSentimentService.publicSentimentTrend(lastMonth,today));
    }

    /**
     * 产业管理 -> 舆情管理 -> 自定义筛选时间舆情分析
     *
     * */
    @GetMapping("publicSentimentTrend")
    public Result thirtyPublicSentimentTrend(@RequestParam(value = "beginDate")Long beginDate,
                                             @RequestParam(value = "endDate")Long endDate){
        return ResultUtil.buildSuccessResultWithData(publicSentimentService.publicSentimentTrend(beginDate, endDate));
    }
}
