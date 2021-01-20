package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import java.util.Objects;

/**
 * TimeDurationUtil
 * 时间区间工具
 * @author LiuYin
 * @date 2020/5/12 10:10
 */
public final class TimeDurationUtil {


    /**
     * 得到时间描述
     * @param minute 分钟数
     * @return
     */
    public static String getTimeMessage(Long minute){
        if(Objects.isNull(minute)){
            return "0分";
        }
        minute = Math.abs(minute);

        final TimeDuration timeDuration = new TimeDuration();
        timeDuration.minute = minute % 60;
        timeDuration.hour = minute / 60;

        if(timeDuration.hour > 23){

            timeDuration.day = timeDuration.hour / 24;
            timeDuration.hour = timeDuration.hour % 24;
        }

        return timeDuration.getMessage();

    }


    /**
     * 时间区间描述对象
     */
    private static class TimeDuration{
        /** 分钟数*/
        private long minute;
        /** 小时数*/
        private long hour;
        /** 天数*/
        private long day;

        /**
         * 得到对分、小时、天的描述
         * @return string
         */
        private String getMessage(){
            String minuteMessage = minute + "分";
            String hourMessage = hour == 0 ? "" : hour + "时";
            String dayMessage = day == 0 ? "" : day + "天";
            return dayMessage + hourMessage + minuteMessage;
        }
    }

}
