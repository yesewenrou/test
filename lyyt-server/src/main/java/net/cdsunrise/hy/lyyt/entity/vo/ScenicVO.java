package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fang yun long
 * @date 2020-01-16 14:38
 */
public class ScenicVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statistics {
        /**
         * 景区名称
         **/
        private String scenicName;
        /**
         * 本周门票数
         **/
        private Integer currentWeekTickets;

        /**
         *  当前日期减1天后  统计那周的门票数
         *  如果当前是星期一 则统计上周的完整数据
         *  如果当前是 星期二 则统计本周一的数据，   以此类推
         **/
        private Integer currentSub1Day;

        /**
         * 当前日期减8天后  统计那周的门票数
         */
        private Integer currentSub8Day;

        public Statistics(String scenicName) {
            this.scenicName = scenicName;
            this.currentWeekTickets = 0;
            this.currentSub1Day = 0;
            this.currentSub8Day = 0;
        }

    }
}
