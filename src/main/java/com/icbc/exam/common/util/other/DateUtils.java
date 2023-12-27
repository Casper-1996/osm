package com.icbc.exam.common.util.other;

import com.icbc.exam.common.constant.DailyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 2018/8/17.
 */

@Component
@Slf4j
public class DateUtils {

    /**
     * 　* @description: 将时间戳转为 时间字符格式
     *
     * @param timeStamp
     * @param format    　* @return java.lang.String
     *                  　* @throws
     *                  　* @author cyt
     *                  　* @date 2019/5/6 15:50
     */
    public static String getFormatTime(Long timeStamp, String format) {
        if (timeStamp != null && timeStamp.toString().length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(timeStamp);
        String formatDate = simpleDateFormat.format(date);
        return formatDate;
    }


    /**
     * 　* @description: 将字符串时间格式 转为 时间戳
     *
     * @param dataStr
     * @param format  　* @return java.lang.Long
     *                　* @throws
     *                　* @author cyt
     */
    public static Long getLongTime(String dataStr, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(dataStr);
            long time = date.getTime();
            return time;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }


    /**
     * 转换时间格式
     * 时间格式为下来类型时   inputFormatStr可以传null， 如果不是需要传入对应的时间格式
     * yyyy-MM-dd HH:mm:ss
     * yyyy-MM-dd
     * localDateTime.tostring()格式
     *
     * @param date            时间
     * @param inputFormatStr  输入格式
     *                        *默认为 yyyy-MM-dd HH:mm:ss
     * @param outputFormatStr 输出格式
     **/
    public static String conversionFormat(String date, String inputFormatStr, String outputFormatStr) {
        if (StringUtils.isEmpty(inputFormatStr)) {
            inputFormatStr = "yyyy-MM-dd HH:mm:ss";
        }
        if (date.contains("T")) {
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            return localDateTime.format(DateTimeFormatter.ofPattern(outputFormatStr));
        } else if (date.length() < 10 && !StringUtils.isEmpty(inputFormatStr)) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(inputFormatStr));
            return localDate.format(DateTimeFormatter.ofPattern(outputFormatStr));
        } else if (date.length() == 10) {
            date = date + " 00:00:00";
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(inputFormatStr);
        LocalDateTime localDateTime = null;

        try {
            localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        } catch (Exception e) {
            log.error("当前传入时间有误{}", e.getMessage());
            return null;
        }

        return localDateTime.format(DateTimeFormatter.ofPattern(outputFormatStr));
    }


    /**
     * LocalDate日期调整仅支持调整
     * yyyy-MM-dd格式日期
     *
     * @param date            需要调整日期
     * @param type            DailyConstant.YEAR/DailyConstant.MONTH/DailyConstant.DAY
     * @param num             负数表示前推， 正数表示后退
     * @param inputFormatStr  指定输入格式 默认DailyConstant.STAND_DATE_FORMAT(yyyy-MM-dd)  无指定可以为null
     * @param outputFormatStr 指定输出格式 默认DailyConstant.STAND_DATE_FORMAT(yyyy-MM-dd)  无指定可以为null
     **/
    public static String dateModify(String date, String type, int num, String inputFormatStr, String outputFormatStr) {
        SimpleDateFormat outdf;
        SimpleDateFormat indf;
        if (StringUtils.isEmpty(outputFormatStr)) {
            outdf = new SimpleDateFormat(DailyConstant.STAND_DATE_FORMAT);
        } else {
            outdf = new SimpleDateFormat(outputFormatStr);
        }
        if (StringUtils.isEmpty(inputFormatStr)) {
            indf = new SimpleDateFormat(DailyConstant.STAND_DATE_FORMAT);
        } else {
            indf = new SimpleDateFormat(inputFormatStr);
        }

        Calendar ca = Calendar.getInstance();
        Date changeDate = null;
        try {
            changeDate = indf.parse(date);
            ca.setTime(changeDate);
        } catch (ParseException e) {
            log.error("当前输入时间有误请检查时间格式是否规范，date={}", date);
            return date;
        }
        switch (type) {
            case DailyConstant.YEAR:
                ca.add(Calendar.YEAR, num);
                return outdf.format(ca.getTime());
            case DailyConstant.MONTH:
                ca.add(Calendar.MONTH, num);
                return outdf.format(ca.getTime());
            case DailyConstant.DAY:
                ca.add(Calendar.DATE, num);
                return outdf.format(ca.getTime());
            default:
                return date;
        }
    }

    /**
     * 调整日期时间
     *
     * @param date            需要调整日期
     * @param type            DailyConstant.YEAR/DailyConstant.MONTH/DailyConstant.DAY
     *                        /DailyConstant.HOUR/DailyConstant.MINUTE/DailyConstant.SECOND
     * @param num             负数表示前推， 正数表示后退
     * @param outputFormatStr 指定输入格式 默认DailyConstant.STAND_TIME_FORMAT(yyyy-MM-dd HH:mm:ss) 无指定可以为null
     * @param inputFormatStr  指定输出格式 默认DailyConstant.STAND_TIME_FORMAT(yyyy-MM-dd HH:mm:ss) 无指定可以为null
     **/
    public static String dateTimeModify(String date, String type, int num, String outputFormatStr, String inputFormatStr) {
        if (StringUtils.isEmpty(outputFormatStr)) {
            outputFormatStr = DailyConstant.STAND_TIME_FORMAT;
        }
        if (StringUtils.isEmpty(inputFormatStr)) {
            inputFormatStr = DailyConstant.STAND_TIME_FORMAT;
        }
        LocalDateTime localDateTime = null;
        try {
            if (date.contains("T")) {
                localDateTime = LocalDateTime.parse(date);
            } else {
                localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(inputFormatStr));
            }

        } catch (Exception e) {
            log.error("当前输入时间有误请检查时间格式是否规范，date={}", date);
            return date;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(outputFormatStr);

        switch (type) {
            case DailyConstant.YEAR:
                localDateTime = localDateTime.minusYears(-num);
                return localDateTime.format(dateTimeFormatter);
            case DailyConstant.MONTH:
                localDateTime = localDateTime.minusMonths(-num);
                return localDateTime.format(dateTimeFormatter);
            case DailyConstant.DAY:
                localDateTime = localDateTime.minusDays(-num);
                return localDateTime.format(dateTimeFormatter);
            case DailyConstant.HOUR:
                localDateTime = localDateTime.minusHours(-num);
                return localDateTime.format(dateTimeFormatter);
            case DailyConstant.MINUTE:
                localDateTime = localDateTime.minusMinutes(-num);
                return localDateTime.format(dateTimeFormatter);
            case DailyConstant.SECOND:
                localDateTime = localDateTime.minusSeconds(-num);
                return localDateTime.format(dateTimeFormatter);
            default:
                return null;
        }
    }

    /**
     * 修改时间
     *
     * @param outputFormatStr 需要的时间格式
     * @param dateTypeStr     需要改变的时间
     * @param vartiation      改变量
     **/
    public static String getChangeDate(String outputFormatStr, String dateTypeStr, int vartiation) {
        String re = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(outputFormatStr);
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            if ("DAY".equals(dateTypeStr)) {
                calendar.add(Calendar.DAY_OF_MONTH, vartiation);
                date = calendar.getTime();
            }
            if ("MONTH".equals(dateTypeStr)) {
                calendar.add(Calendar.MONTH, vartiation);
                date = calendar.getTime();
            }
            if ("YEAR".equals(dateTypeStr)) {
                calendar.add(Calendar.YEAR, vartiation);
                date = calendar.getTime();
            }
            re = sdf.format(date);
        } catch (Exception e) {
            log.error("Get Appoint DateTime Fail!", e);
        }

        return re;
    }

    /**
     * 时间规划
     *
     * @param inputDateStr    需要转换的时间
     * @param inputFormatStr  你的时间的格式
     * @param outputFormatStr 你需要的格式
     * @return
     */
    public static String getFormatDate(String inputDateStr, String inputFormatStr, String outputFormatStr) {

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatStr);
            Date date = inputFormat.parse(inputDateStr);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatStr);
            return outputFormat.format(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    //    public static void main(String[] args) {
//        //不能识别YYYYMM格式的时间
////        System.out.println(DateUtils.conversionFormat("2020-05", "yyyy-MM", "yyyymmdd"));
////        System.out.println(DateUtils.conversionFormat("2020-05-05 12:50:30", null, "yyyymmdd--HHmmss"));
//
//        DateTimeFormatter df = new DateTimeFormatterBuilder()
//                .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
//                .appendLiteral('.')
//                .appendValue(ChronoField.MONTH_OF_YEAR, 2)
//                .toFormatter();
//
//        TemporalAccessor parse = df.parse("2020.12");
//        LocalDate of = LocalDate.of(parse.get(ChronoField.YEAR), parse.get(ChronoField.MONTH_OF_YEAR), 1);
//        String format = of.format(DateTimeFormatter.ofPattern("yyyy-_MM"));
//
//        System.out.println(format);
//
//    }


}