package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristRegionCompareVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 游客区域来源比较响应
 * @author LiuYin 2020/3/28
 */
@Data
public class TouristRegionCompareResponse {

    /** 游客区域来源比较对象列表*/
    private List<TouristRegionCompareVO> list;
    /** 时间列表，从来源比较对象列表中提取，通常作为x轴返回给前端*/
    private List<Long> timeList;

    public static TouristRegionCompareResponse createDefault(){
        final TouristRegionCompareResponse resp = new TouristRegionCompareResponse();
        resp.setList(new ArrayList<>(0));
        resp.setTimeList(new ArrayList<>(0));
        return resp;
    }

}
