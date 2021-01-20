package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/04/22 16:14
 */
@Data
public class TouristPredictionVO<T extends TouristPredictionVO.ScenicVO> {

    /**
     * 是否是节假日
     */
    private Boolean isHoliday;

    /**
     * 预测结果
     */
    private List<T> predictions;


    @Data
    public static class ScenicVO {

        /**
         * 景区名称
         */
        private String scenicName;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class NormalScenicVO extends ScenicVO {

        /**
         * 今日游客数
         */
        private Integer todayPeopleNum;

        /**
         * 明日游客数
         */
        private Integer tomorrowPeopleNum;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class HolidayScenicVO extends ScenicVO {

        /**
         * 节假日名称
         */
        private String holidayName;

        /**
         * 今日游客数
         */
        private Integer peopleNum;
    }
}
