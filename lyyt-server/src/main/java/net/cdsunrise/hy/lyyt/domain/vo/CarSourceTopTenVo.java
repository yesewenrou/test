package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * 车辆来源TOP10
 * @author YQ on 2019/11/5.
 */
@Data
public class CarSourceTopTenVo {
    /**
     * 总车辆
     */
    private Integer totalCount;
    /**
     * top10柱状图
     */
    private Map<String,String> carSourceHistogram;

    public static CarSourceTopTenVo build(Integer count, Map<String, String> map) {
        CarSourceTopTenVo vo = new CarSourceTopTenVo();
        vo.setTotalCount(count);
        vo.setCarSourceHistogram(map);
        return vo;
    }
}
