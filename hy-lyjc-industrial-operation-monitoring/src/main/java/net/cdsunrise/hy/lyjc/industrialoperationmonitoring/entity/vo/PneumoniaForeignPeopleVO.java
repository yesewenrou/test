package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fang yun long
 * @date 2020-03-03 15:39
 */
@Data
public class PneumoniaForeignPeopleVO {
    /** 武汉返乡人数 **/
    private Long whCount;
    /** 湖北返乡人数 **/
    private Long hbCount;
    /** 折线图 **/
    List<LineChartVO> lineCharts;

    /**
     * 调整属性顺序慎重 PneumaticServiceImpl 中使用了他的全参构造方法
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineChartVO{
        /** 日期 **/
        private String date;
        /** 今年人数 **/
        private Long currentYear;
        /** 去年人数 **/
        private Long lastYear;
        /** 较去年同期 **/
        private Double compareLastYear;
        /** 较昨日丶上月环比 **/
        private Double compareLast;
    }

    /**
     * 查询外来车辆时  feign返回VO
     */
    @Data
    public static class FeignCarVO {
        /** 省名 **/
        private String provinceName;
        /**
         * 市名
         */
        private String cityName;
        /** 湖北总数 **/
        private Long provinceCount;
        /** 武汉总数 **/
        private Long cityCount;
        /** 折线图 **/
        private List<FeignCarLineChartVO> epidemicListList;
    }

    @Data
    public static class FeignCarLineChartVO {
        private Long time;
        private Long nowCount;
        private Long lastYearCount;
        private Double lastYearPeriod;
        private Double yesterdaySequential;
    }
}
