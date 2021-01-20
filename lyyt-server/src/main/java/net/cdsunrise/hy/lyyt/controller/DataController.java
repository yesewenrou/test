package net.cdsunrise.hy.lyyt.controller;


import net.cdsunrise.hy.lyyt.entity.resp.GatherResponse;
import net.cdsunrise.hy.lyyt.entity.resp.SourceShareResponse;
import net.cdsunrise.hy.lyyt.entity.resp.VisualResponse;
import net.cdsunrise.hy.lyyt.entity.vo.DataGatherStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.ShareStatisticsVO;
import net.cdsunrise.hy.lyyt.entity.vo.TotalStatisticsVO;
import net.cdsunrise.hy.lyyt.service.GatherViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DataController
 * @Description
 * @Author LiuYin
 * @Date 2019/7/22 16:28
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private GatherViewService gatherViewService;

    /**
     * 按照数据分类获取数据描述
     * @param index
     * @return
     */
    @GetMapping("/visualization/{index}")
    public VisualResponse getByTypeIndex(@PathVariable ("index") Integer index){
        return gatherViewService.getByTypeIndex(index);
    }

    /**
     * 获取整体数据统计
     * @return
     */
    @GetMapping("/total/statistics")
    public TotalStatisticsVO totalStatistics(){
        return gatherViewService.getTotalStatisticsVO();
    }



    /**
     * 获取采集数据列表
     * @param number 需要生成的列表中的记录数，可以不传递，如果不传递，默认记录数从0到24条随机生成
     * @param random 是否根据number的值作为范围来随机生成。可以不传递，若不传递，默认为false。 true：是， false：否。
     * @return
     */
    @GetMapping("/gather")
    public GatherResponse gatherResponse(@RequestParam (value = "number",required = false)Integer number,
                                         @RequestParam (value = "random",required =false, defaultValue = "false")Boolean random){
        return gatherViewService.getGatherResponse(-1);
    }

    /**
     * 获取采集数据统计列表
     * @param number 需要生成的列表中的记录数，可以不传递，如果不传递，默认记录数从0到24条
     * @param random 是否根据number的值作为范围来随机生成。可以不传递，若不传递，默认为false。 true：是， false：否。
     * @return
     */
    @GetMapping("/gather/statistics")
    public List<DataGatherStatisticsVO> gatherStatistics(@RequestParam(value = "number",required = false)Integer number,
                                                         @RequestParam (value = "random",required =false, defaultValue = "false")Boolean random){
        return gatherViewService.getDataGatherStatisticsList().stream().limit(4).collect(Collectors.toList());
    }


    /**
     * 按照数据共享分类获取数据统计
     * @param index 数据共享分类编号
     * @return
     */
    @GetMapping("/share/{index}")
    public SourceShareResponse getBySourceIndex(@PathVariable("index") Integer index){
        return gatherViewService.getShareResponse(index);
    }


    /**
     * 获取数据共享统计数据
     * @return
     */
    @GetMapping("/share/statistics")
    public ShareStatisticsVO sourceStatistics(){
        return gatherViewService.getShareResponse(1).getShareStatistics();
    }



}
