package net.cdsunrise.hy.lyyt.entity.vo.reids;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RoadSectionForecastVO
 * 路段预测视图对象
 * @author LiuYin
 * @date 2020/4/30 14:29
 */
@Data
public class RoadSectionForecastVO {

    private String key;
    private List<TimeValueForecastDTO> forecastList;


}
