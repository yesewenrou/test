package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具
 *
 * @author LHY
 */
public class DateUtil {
    public static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter LOCAL_DATE_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter LOCAL_DATE_MONTH_DAY = DateTimeFormatter.ofPattern("MM-dd");

    public static final String PATTERN_YYYY_MM = "yyyy-MM";
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MOBILE_PATTERN_YYYY_MM_DD = "yyyyMMdd";
    public static final String PATTERN_YYYYMMDDHH = "yyyyMMddHH";
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.S";

    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String getToday() {
        return format(new Date(),PATTERN_YYYY_MM_DD);
    }

    public static String format(LocalDateTime dateTime) {
        return LOCAL_DATE_TIME.format(dateTime);
    }

    public static Date parse(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime convert(String dateTime) {
        return LocalDateTime.parse(dateTime, LOCAL_DATE_TIME);
    }

    /**
     * 获取过去或将来几小时，几天，几个月，几年时间
     **/
    public static Date getTime(String type, Date date, int number) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch (type) {
                case "hour":
                    cal.add(Calendar.HOUR_OF_DAY, number);
                    break;
                case "day":
                    cal.add(Calendar.DATE, number);
                    break;
                case "month":
                    cal.add(Calendar.MONTH, number);
                    break;
                case "year":
                    cal.add(Calendar.YEAR, number);
                    break;
                default:
                    break;
            }
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getYesterday() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -1);
        return today.getTime();
    }

    public static String getYesterdayString(String pattern) {
        return format(getYesterday(), pattern);
    }

    public static String getTodayString(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取两个时间段内的所有日期
     **/
    public static List<String> getBetweenDate(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> betweenList = new ArrayList<String>();
        try {
            Calendar startDay = Calendar.getInstance();
            startDay.setTime(format.parse(begin));
            startDay.add(Calendar.DATE, -1);

            while (true) {
                startDay.add(Calendar.DATE, 1);
                Date newDate = startDay.getTime();
                String newend = format.format(newDate);
                betweenList.add(newend);
                if (end.equals(newend)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return betweenList;
    }

    /**
     * 获取两个时间段内的所有月份
     **/
    public static List<String> getBetweenMonth(String beginDate, String endDate) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            min.setTime(sdf.parse(beginDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(endDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static LocalDateTime longToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static String localDateToString(LocalDate begin) {
        return LOCAL_DATE.format(begin);
    }

    public static long dateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static LocalDate stringToLocalDate(String str) {
        return LocalDate.parse(str, LOCAL_DATE);
    }

    public static Long localDateToLong(LocalDate localDate){
        return dateTimeToLong(localDate.atStartOfDay());
    }

    /**
     * 计算两个时间的间隔天数
     * @param startTime ： 开始时间
     * @param endTime  ： 结束时间
     * @return
     */
    public static int calculateTotalTime(String startTime,String endTime) {
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd");
        Long diff = 0L;
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            diff = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff.intValue();
    }

    /**
     * 获取该月第一天
     * @param dateLong 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime getFirstDayOfMonth(Long dateLong) {
        LocalDateTime localDateTime = longToLocalDateTime(dateLong);
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1), getBeginLocalTime());
    }

    /**
     * 获取该月最后一天
     * @param dateLong 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime getLastDayOfMonth(Long dateLong) {
        LocalDateTime localDateTime = longToLocalDateTime(dateLong);
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.lengthOfMonth()), getEndLocalTime());
    }

    public static LocalTime getBeginLocalTime() {
        return LocalTime.of(0, 0, 0, 0);
    }

    public static LocalTime getEndLocalTime() {
        return LocalTime.of(23, 59, 59, 999999999);
    }
}
