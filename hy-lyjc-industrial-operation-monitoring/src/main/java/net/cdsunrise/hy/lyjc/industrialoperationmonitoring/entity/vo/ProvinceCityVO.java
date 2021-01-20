package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/3/24 13:37 @Description:
 */
@Data
public class ProvinceCityVO {
    /**
     * 车流量统计
     */
    private Integer flowCount;
    /**
     * 数据来源
     */
    private String dataSource;
    /**
     * 国家车流量
     */
    private List<CountryFlow> countryFlowList;

    @Data
    public static class CountryFlow {
        /**
         * 国家
         */
        private String countryName;
        /**
         * 国家流量数据
         */
        private Integer flowCount;
        /**
         * 省份数据
         */
        private List<ProvinceFlow> provinceFlowList;
    }

    @Data
    public static class ProvinceFlow {
        /**
         * 省份名称
         */
        private String provinceName;
        /**
         * 省份的车流量数据
         */
        private Integer flowCount;
        /**
         * 城市车流量数据
         */
        private List<CityFlow> cityFlowList;
    }

    @Data
    public static class CityFlow {
        /**
         * 城市名称
         */
        private String cityName;
        /**
         * 城市车流量数据
         */
        private Integer flowCount;
    }
}
