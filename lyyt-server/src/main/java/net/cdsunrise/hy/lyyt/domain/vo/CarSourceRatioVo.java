package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * 车来源占比分析
 * @author YQ on 2019/11/6.
 */
@Data
public class CarSourceRatioVo {
    /**
     * 省内车辆来源统计
     */
    private Integer provinceCount;

    /**
     * 省外车辆来源统计
     */
    private Integer outProvinceCount;
    /**
     * 本周省内车辆来源柱状图
     */
    private Map<String,String> provinceHistogram;
    /**
     * 本周省外车辆来源柱状图
     */
    private Map<String,String> outProvinceHistogram;

    public static CarSourceRatioVo build(Map<String, String> provinceHistogram, Map<String, String> outProvinceHistogram) {
        Integer provinceCount = provinceHistogram.entrySet().stream().map(e -> Integer.parseInt(e.getValue())).reduce(0, (e1, e2) -> e1 + e2);
        Integer outProvinceCount = outProvinceHistogram.entrySet().stream().map(e -> Integer.parseInt(e.getValue())).reduce(0, (e1, e2) -> e1 + e2);
        CarSourceRatioVo vo = new CarSourceRatioVo();
        vo.setProvinceHistogram(provinceHistogram);
        vo.setOutProvinceHistogram(outProvinceHistogram);
        vo.setProvinceCount(provinceCount);
        vo.setOutProvinceCount(outProvinceCount);
        return vo;
    }
}
