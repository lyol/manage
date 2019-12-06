package com.lyl.layuiadmin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Slf4j
public class DateUtils {

    /**
     * yyyy-MM-dd
     * 例如:2018-12-28
     */
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     * 例如:2018-12-28 10:00:00
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * HHmmss
     * 例如:10:00:00
     */
    public static final String PATTERN_HHMMSS = "HHmmss";
    /**
     * HH:mm
     * 例如:10:00
     */
    public static final String PATTERN_HH_MM = "HH:mm";

    /**
     * yyyy-MM-dd HH:mm
     * 例如:2018-12-28 10:00
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * yyyyMMddHHmmss
     */
    public static final String	PATTERN_YYYYMMDDHHMMSS			= "yyyyMMddHHmmss";

    /**
     *  yyyyMMdd
     */
    public static final String	PATTERN_YYYYMMDD				= "yyyyMMdd";


    /**
     * 获取当前日期年
     *
     * @return 年
     */
    public static int getYear() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.YEAR);
    }

    /**
     * 获取当前日期月份
     *
     * @return 月份
     */
    public static int getMonth() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.MONTH_OF_YEAR);
    }

    /**
     * TODO 获取某月的第几天
     *
     * @return 几号
     */
    public static int getMonthOfDay() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.DAY_OF_MONTH);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date date
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern){

        Instant instant = date.toInstant();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String dateStr, String pattern) {

        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 为Date增加分钟,减传负数
     *
     * @param date        日期
     * @param plusMinutes 要增加的分钟数
     * @return 新的日期
     */
    public static Date addMinutes(Date date, Long plusMinutes) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime newDateTime = dateTime.plusMinutes(plusMinutes);
        return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 增加时间
     *
     * @param date date
     * @param hour 要增加的小时数
     * @return new date
     */
    public static Date addHour(Date date, Long hour) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime = dateTime.plusHours(hour);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取今天的00:00:00
     *
     * @return
     */
    public static String getDayStart() {
        return getDayStart(LocalDateTime.now());
    }

    /**
     * 获取今天的23:59:59
     *
     * @return
     */
    public static String getDayEnd() {
        return getDayEnd(LocalDateTime.now());
    }

    /**
     * 获取某天的00:00:00
     *
     * @param dateTime
     * @return
     */
    public static String getDayStart(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MIN), PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取某天的23:59:59
     *
     * @param dateTime
     * @return
     */
    public static String getDayEnd(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(LocalTime.MAX), PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取本月第一天的00:00:00
     *
     * @return
     */
    public static String getFirstDayOfMonth() {
        return getFirstDayOfMonth(LocalDateTime.now());
    }

    /**
     * 获取本月最后一天的23:59:59
     *
     * @return
     */
    public static String getLastDayOfMonth() {
        return getLastDayOfMonth(LocalDateTime.now());
    }

    /**
     * 获取某月第一天的00:00:00
     *
     * @param dateTime
     *            LocalDateTime对象
     * @return
     */
    public static String getFirstDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN), PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取某月最后一天的23:59:59
     *
     * @param dateTime
     *            LocalDateTime对象
     * @return
     */
    public static String getLastDayOfMonth(LocalDateTime dateTime) {
        return formatDateTime(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX), PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 按pattern格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime
     *            LocalDateTime对象
     * @param pattern
     *            要格式化的字符串
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = PATTERN_YYYY_MM_DD_HH_MM_SS;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 减月份
     *
     * @param monthsToSubtract 月份
     * @return Date
     */
    public static Date minusMonths(long monthsToSubtract){
        LocalDate localDate = LocalDate.now().minusMonths(monthsToSubtract);

        return localDate2Date(localDate);
    }

    /**
     * LocalDate类型转为Date
     *
     * @param localDate LocalDate object
     * @return Date object
     */
    public static Date localDate2Date(LocalDate localDate) {

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());

        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * LocalDateTime类型转为Date
     *
     * @param localDateTime LocalDateTime object
     * @return Date object
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 查询当前年的第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getFirstDayOfCurrentYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().withMonth(1).withDayOfMonth(1);

        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyyMMdd";
        }

        return format(localDateTime2Date(localDateTime), pattern);
    }

    /**
     * 查询前一年最后一个月第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getLastMonthFirstDayOfPreviousYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).withMonth(12).withDayOfMonth(1);

        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyyMMdd";
        }

        return format(localDateTime2Date(localDateTime), pattern);
    }

    /**
     * 查询前一年最后一个月第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getLastMonthLastDayOfPreviousYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).with(TemporalAdjusters.lastDayOfYear());

        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyyMMdd";
        }

        return format(localDateTime2Date(localDateTime), pattern);
    }

    /**
     * 获取当前日期
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getCurrentDay(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now();

        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN_YYYYMMDD;
        }

        return format(localDateTime2Date(localDateTime), pattern);
    }

    /**
     * 比较2个日期的先后，如果secondDay 晚于 firstDay 返回整数1，否则返回负数-1
     */
    public static int compareTwoDateOrder(Date firstDay, Date secondDay) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        try {
            firstDay = sdf.parse(sdf.format(firstDay));
            secondDay = sdf.parse(sdf.format(secondDay));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (secondDay.getTime() >= firstDay.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 计算两个日期相隔的天数 :secondDay晚于firstDay 返回正整数，否则返回负整数，同一天返回0
     */
    public static int countDaysBetweenTwoDate(Date firstDay, Date secondDay) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD);
        try {
            firstDay = sdf.parse(sdf.format(firstDay));// 丢掉时间信息，变成00:00:00
            secondDay = sdf.parse(sdf.format(secondDay));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int nDay = (int) ((secondDay.getTime() - firstDay.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    public static void main(String[] args) {
        Date i = localDate2Date(LocalDate.now());
        System.out.println("**"+i);
    }

}
