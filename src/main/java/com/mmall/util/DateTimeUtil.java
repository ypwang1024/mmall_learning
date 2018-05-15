package com.mmall.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @program: mmall
 * @description:日期处理工具类
 * @author: ypwang
 * @create: 2018-05-15 20:36
 **/
public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转日期型
     *
     * @param dateTimeStr 毫秒数
     * @param formatStr   格式字符串
     * @return
     */
    public static Date stringToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = formatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * 日期转字符串
     *
     * @param date      日期
     * @param formatStr 格式字符串
     * @return
     */
    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
     * 字符串转日期型（标准格式）
     *
     * @param dateTimeStr
     * @return
     */
    public static Date stringToDate(String dateTimeStr) {
        return stringToDate(dateTimeStr, STANDARD_FORMAT);
    }

    /**
     * 日期转字符串(标准格式)
     *
     * @param date 日期
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, STANDARD_FORMAT);
    }

//    public static void main(String[] args) {
//        System.out.println(dateToString(new Date(), "yyyy年MM月dd日 HH:mm:ss"));
//        System.out.println(stringToDate("2016-04-23 18:59:59", "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(dateToString(new Date()));
//        System.out.println(stringToDate("2016-04-23 18:59:59"));
//
//    }
}
