package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 * @date 2019/12/10 11:06
 */
@Data
public class IndustryCarSourceVO {

    /**
     * 省份车流量图表数据
     */
    private List<ProvinceGraph> provinceGraphList;
    /**
     * 省内车流量图表数据
     */
    private List<CityGraph> cityGraphList;

    @Data
    public static class CityGraph {
        public CityGraph(String cityName, Integer flowNumber) {
            this.cityName = cityName;
            this.flowNumber = flowNumber;
        }

        public CityGraph() {
        }

        /**
         * 省份名称
         */
        private String cityName;
        /**
         * 省份流量
         */
        private Integer flowNumber;
    }

    @Data
    public static class ProvinceGraph {
        public ProvinceGraph(String provinceName, Integer flowNumber) {
            this.provinceName = provinceName;
            this.flowNumber = flowNumber;
        }

        public ProvinceGraph() {
        }

        /**
         * 省份名称
         */
        private String provinceName;
        /**
         * 省份流量
         */
        private Integer flowNumber;
    }
}
