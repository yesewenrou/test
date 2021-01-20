package net.cdsunrise.hy.lyyt.utils;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author : suzhouhe  @date : 2019/8/5 15:04  @description : 时间处理工具类
 */
@Slf4j
public class DateUtil {

    public static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter LOCAL_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final String PATTERN_YYYY_MM = "yyyy-MM";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 完全时间格式
     */
    private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TO_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    /**
     * CST时间格式
     */
    private static final DateTimeFormatter CST_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    public static LocalDateTime stringToLocalDateTime(String str) {
        return LocalDateTime.parse(str, LOCAL_DATE_TIME);
    }


    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static Date parse(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 本地日期对象转换成字符串
     *
     * @param localDate 本地日期对象
     * @return yyyy-MM-dd
     */
    public static String localDateToString(LocalDate localDate) {
        return DF.format(localDate);
    }

    /**
     * 将完全的时间格式数据转换为时间戳
     *
     * @param localDateTime 时间
     * @return 时间戳
     */
    public static Long convert(String localDateTime) {
        try {
            LocalDateTime parse = LocalDateTime.parse(localDateTime, LOCAL_DATE_TIME);
            return localDateTimeToLong(parse);
        } catch (Exception e) {
            log.error("convert error", e);
        }
        return null;
    }

    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 将时间差转换为LocalDateTime
     *
     * @param time 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime convert(Long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8));
    }

    /**
     * long时间转string
     *
     * @param time 时间
     * @return 字符串
     */
    public static String convertLong2String(Long time) {
        return LOCAL_DATE_TIME.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8)));
    }

    /**
     * 将CST时间格式的时间转换为时间戳
     *
     * @param date CST时间格式
     * @return 时间戳
     */
    public static Long convertByCST(Date date) {
        String instant = Instant.ofEpochMilli(date.getTime()).toString();
        LocalDateTime localDateTime = LocalDateTime.parse(instant, CST_TIME_FORMATTER);
        return DateUtil.convert(localDateTime.format(LOCAL_DATE_TIME));
    }

    /**
     * 获取offset天的最开始时间
     *
     * @param offset 距离当天的偏差
     * @return 时间
     */
    public static Long getCurrentStartTime(Integer offset) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(new Date());
        dateStart.add(Calendar.DAY_OF_MONTH, offset);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTimeInMillis();
    }

    /**
     * 获取offset月的最开始时间
     *
     * @param offset 距离当月的偏差
     * @return 时间
     */
    public static Long getCurrentMonthStartTime(Integer offset) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(new Date());
        dateStart.add(Calendar.MONTH, offset);
        dateStart.set(Calendar.DAY_OF_MONTH, 1);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTimeInMillis();
    }

    /**
     * 获取本周的最开始时间
     *
     * @return 时间
     */
    public static Long getCurrentWeekStartTime() {
        LocalDateTime localDate = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        return localDate.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 通过时间戳获取当天的起始时间
     *
     * @param time 时间戳
     * @return 起始时间
     */
    public static Long getStartByLong(Long time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        LocalDateTime of = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
        return of.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 通过时间戳获取当天的结束时间
     *
     * @param time 时间戳
     * @return 结束时间
     */
    public static Long getEndByLong(Long time) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        LocalDateTime of = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
        return of.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static String getToday() {
        return format(new Date(), PATTERN_YYYY_MM_DD);
    }

    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return format(cal.getTime(), PATTERN_YYYY_MM_DD);
    }

    // 获取某年某月第一天
    public static String getFirstDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return format(cal.getTime(), PATTERN_YYYY_MM_DD);
    }

    // 获取某年某月最后一天
    public static String getEndDay(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format(cal.getTime(), PATTERN_YYYY_MM_DD);
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
     * 时间字符串转换为时间
     *
     * @param localDateTime 时间字符串
     * @return 时间
     */
    public static Date stringToDate(String localDateTime) {
        LocalDateTime parse = LocalDateTime.parse(localDateTime, LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = parse.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 五分钟整点时间
     *
     * @param time 时间戳
     * @return 对应的五分钟整点时间戳
     */
    public static long fiveMinutes(Long time) {
        LocalDateTime convert = convert(time);
        int minute = convert.getMinute();
        minute = minute - minute % 5;
        LocalDateTime of = LocalDateTime.of(convert.toLocalDate(), LocalTime.of(convert.getHour(), minute, 0));
        return of.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * localDateTime转为时间戳
     *
     * @param localDateTime 时间
     * @return 时间戳
     */
    public static long DateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 日期转换成数字格式的字符串
     *
     * @param localDate 日期
     * @return string with number format: 20191212
     */
    public static String dateToNumberFormat(LocalDate localDate) {

        return DATE_TO_NUMBER_FORMATTER.format(Objects.requireNonNull(localDate));
    }


    public static List<LocalDate> rangeLocalDateList(LocalDate startTime, LocalDate endTime) {
        List<LocalDate> list = new ArrayList<>();
        while (DateUtil.localDateTimeToLong(startTime.atStartOfDay()) <= DateUtil.localDateTimeToLong(endTime.atStartOfDay())) {
            list.add(startTime);
            startTime = startTime.plusDays(1);
        }
        return list;

    }

    /**
     * 获取某周的开始日期
     *
     * @param offset 0本周，1下周，-1上周，依次类推
     * @return LocalDate
     */
    public static LocalDate weekStart(int offset) {
        LocalDate localDate = LocalDate.now().plusWeeks(offset);
        return localDate.with(DayOfWeek.MONDAY);
    }

    public static String localDateTimeToString(LocalDateTime localDate) {
        if (localDate != null) {
            return LOCAL_DATE_TIME.format(localDate);
        }
        return null;

    }

    public static LocalDateTime longToLocalDateTime(Long time) {
        AssertUtil.notNull(time, () -> new RuntimeException("时间格式错误"));

        try {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.ofHours(8));
        } catch (Exception var2) {
            throw new RuntimeException("时间格式错误");
        }
    }

    public static LocalDate longToLocalDate(Long time) {
        return longToLocalDateTime(time).toLocalDate();
    }

}
