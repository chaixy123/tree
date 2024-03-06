package com.wanyun.select.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author lengon
 * ClassName: DateUtils
 * Description: 时间类操作
 * date 2016年1月18日 上午11:49:39
 */
public class DateUtils {


    public static final String DAY_FORMAT = "yyyyMMdd";
    public static final String DAY_FORMAT1 = "yyyy-MM-dd";
    public static final String HOUR_FORMAT = "yyyyMMddHH";
    public static final String HOUR_FORMAT2 = "yyyy-MM-dd HH";
    public static final String MINUTE_FORMAT = "yyyyMMddHHmm";
    public static final String MINUTE_FORMAT1 = "yyyy-MM-dd HH:mm";
    public static final String SECOND_FORMAT = "yyyyMMddHHmmss";
    public static final String SECOND_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static final String HOUR_FORMAT1 = "MM月dd日 HH时";

    public static final int DAY_IN_MILLSECOND = 1000 * 3600 * 24;
    public static final int HOUR_IN_MILLSECOND = 1000 * 3600;
    public static final int MINUTE_IN_MILLSECOND = 1000 * 60;
    public static final int MINUTE_IN_SECOND = 60;
    public static final int DAY_IN_SECOND = 3600 * 24;
    public static final int HOUR_IN_SECOND = 3600;

    public static String buildTodayStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
        Date date = new Date();
        try {
            String time = sdf.format(date);
            return time;
        } catch (Exception ex) {
        }
        return null;
    }

    public static String buildNowStr(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            String time = sdf.format(date);
            return time;
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * @param start
     * @param day   (负号表示过去几天)
     * @return
     * @Title: futureDay
     * @Description: 未来的第几天的string
     */
    public static String futureDay(String start, String format, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long startTime;
        try {
            startTime = sdf.parse(start).getTime();
            long endTime = startTime + ((long) DAY_IN_MILLSECOND * day);
            return sdf.format(new Date(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> buildHourTimes(String startTime, String endTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long startTimeMillus = sdf.parse(startTime).getTime();
            long endTimeMillus = sdf.parse(endTime).getTime();
            if (endTimeMillus < startTimeMillus) {
                return null;
            }

            List<String> hours = new ArrayList<String>();

            while (endTimeMillus >= startTimeMillus) {
                hours.add(sdf.format(new Date(startTimeMillus)));
                startTimeMillus += HOUR_IN_MILLSECOND;
            }
            return hours;
        } catch (Exception ex) {
        }
        return null;

    }

    public static List<String> buildTimes(String startTime, String endTime, String format, long timeGap) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long startTimeMillus = sdf.parse(startTime).getTime();
            long endTimeMillus = sdf.parse(endTime).getTime();
            if (endTimeMillus < startTimeMillus) {
                return null;
            }

            List<String> hours = new ArrayList<String>();

            while (endTimeMillus >= startTimeMillus) {
                hours.add(sdf.format(new Date(startTimeMillus)));
                startTimeMillus += timeGap;
            }
            return hours;
        } catch (Exception ex) {
        }
        return null;

    }

    public static List<String> buildDayTimes(String startTime, String endTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long startTimeMillus = sdf.parse(startTime).getTime();
            long endTimeMillus = sdf.parse(endTime).getTime();
            if (endTimeMillus < startTimeMillus) {
                return null;
            }

            List<String> hours = new ArrayList<String>();

            while (endTimeMillus >= startTimeMillus) {
                hours.add(sdf.format(new Date(startTimeMillus)));
                startTimeMillus += DAY_IN_MILLSECOND;
            }
            return hours;
        } catch (Exception ex) {
        }
        return null;

    }

    /**
     * @param start (负号表示过去几小时)
     * @return
     * @Title: futureHour
     * @Description: 未来的第几小时的string
     */
    public static String futureHour(String start, String format, int hour) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long startTime;
        try {
            startTime = sdf.parse(start).getTime();
            long endTime = startTime + ((long) HOUR_IN_MILLSECOND * hour);
            return sdf.format(new Date(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param start
     * @param format
     * @param minute
     * @return
     * @Title: futureHour
     * @Description: 未来的第几分钟的string
     */
    public static String futureMinute(String start, String format, int minute) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long startTime;
        try {
            startTime = sdf.parse(start).getTime();
            long endTime = startTime + ((long) MINUTE_IN_MILLSECOND * minute);
            return sdf.format(new Date(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dayGap(String start, String end, String startFormat, String endFormat) {
        SimpleDateFormat startSdf = new SimpleDateFormat(startFormat);
        SimpleDateFormat endSdf = new SimpleDateFormat(endFormat);
        long startTime;
        try {
            startTime = startSdf.parse(start).getTime();
            long endTime = endSdf.parse(end).getTime();
            return (int) ((endTime - startTime) / DAY_IN_MILLSECOND);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int hourGap(String start, String end, String startFormat, String endFormat) {
        SimpleDateFormat startSdf = new SimpleDateFormat(startFormat);
        SimpleDateFormat endSdf = new SimpleDateFormat(endFormat);
        long startTime;
        try {
            startTime = startSdf.parse(start).getTime();
            long endTime = endSdf.parse(end).getTime();
            return (int) ((endTime - startTime) / HOUR_IN_MILLSECOND);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int minuteGap(String start, String end, String startFormat, String endFormat) {
        SimpleDateFormat startSdf = new SimpleDateFormat(startFormat);
        SimpleDateFormat endSdf = new SimpleDateFormat(endFormat);
        long startTime;
        try {
            startTime = startSdf.parse(start).getTime();
            long endTime = endSdf.parse(end).getTime();
            return (int) ((endTime - startTime) / MINUTE_IN_MILLSECOND);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDataFromString(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(sdf.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDataFromString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串转换成指定的时间格式的字符串
     *
     * @param time   yyyyMMddHHmmss
     * @param format
     * @return String
     * @Title: getTimeString
     * @Description: TODO
     */
    public static String getTimeString(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat sdfTemp = new SimpleDateFormat(SECOND_FORMAT);
        try {
            return sdf.format(sdfTemp.parse(time));
        } catch (Exception e) {
//			LogUtils.error("convert date to String error:" + e);
        }
        return null;
    }

    /**
     * 将字符串转换成指定的时间格式的字符串
     *
     * @param time yyyyMMddHHmmss
     * @param
     * @return String
     * @Title: getTimeString
     * @Description: TODO
     */
    public static String getTimeString(String time, String inFormat, String outFormat) {
        SimpleDateFormat inSdf = new SimpleDateFormat(inFormat);
        SimpleDateFormat outSdf = new SimpleDateFormat(outFormat);
        try {
            return outSdf.format(inSdf.parse(time));
        } catch (Exception e) {
//			LogUtils.error("convert date to String error:" + e);
        }
        return null;
    }

    public static Date formatDate(String dateStr) {
        Date date = null;

        try {
            SimpleDateFormat e = new SimpleDateFormat(MINUTE_FORMAT);
            date = e.parse(dateStr);
        } catch (Exception e) {
//			LogUtils.error("format date to String error:" + e);
        }

        return date;
    }

    public static String parseDate(Date date) {
        String str = "";

        try {
            SimpleDateFormat e = new SimpleDateFormat(MINUTE_FORMAT);
            str = e.format(date);
        } catch (Exception e) {
//			LogUtils.error("parse date to String error:" + e);
        }

        return str;
    }

    public static Date parseToDate(String timeStr, String formatStr) {
        Date date = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(formatStr);
            date = sf.parse(timeStr);
        } catch (Exception e) {
//			LogUtils.error("format date to String error:" + e);
        }
        return date;
    }

    public static int getDayIndex(String timeStr) {
        long time = DateUtils.getTime(timeStr, DateUtils.DAY_FORMAT);
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(time);
        return startCal.get(Calendar.DAY_OF_YEAR);
    }

    public static String getDayStr(int index, String year) {
        long time = DateUtils.getTime(year, "yyyy");
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(time);
        timeCal.add(Calendar.DAY_OF_YEAR, index - 1);
        return DateUtils.getTime(timeCal.getTimeInMillis(), DateUtils.DAY_FORMAT);
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getNewFormat(String data) {
        String result = "";

        try {
            Date date = new SimpleDateFormat("yyyyMMddHHmm").parse(data);
            result = new SimpleDateFormat("yyyy年MM月dd日HH:mm").format(date);
            return result;
        } catch (Exception e) {
            return data;
        }
    }

    //字符串减去分钟
    public static String strMinute(String time, int num, String format) throws ParseException {
        String result = "";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(time);
        date.setTime(date.getTime() - (num * 60 * 1000));
        return sdf.format(date);
    }

    //当前时间减去多少分钟
    public static String subtractMinute(int hour, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, -hour);
        Date da = rightNow.getTime();
        return sdf.format(da);
    }


    //当前时间减去多少小时
    public static String subtractTime(int hour, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.HOUR, -hour);
        Date da = rightNow.getTime();
        return sdf.format(da);
    }

    //当前时间减去多少天
    public static String subtractDay(int day, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, -day);
        Date da = rightNow.getTime();
        return sdf.format(da);
    }

    public static void main(String[] args) {
        String a = subtractDay(60, "yyyy-MM-dd HH:mm:ss");
        System.out.println(a);
    }

    //当前时间减去多少月
    public static String subtractMonth(int month, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MONTH, -month);
        Date da = rightNow.getTime();
        return sdf.format(da);
    }

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    /**
     * 根据要求的格式，格式化时间，返回String
     *
     * @param format 默认：yyyy-MM-dd HH:mm:ss
     * @param time   要格式化的时间
     * @return 时间字符串
     */
    public static String toStr(String format, Date time) {
        SimpleDateFormat df = null;
        if (null == format) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        } else {
            df = new SimpleDateFormat(format);
        }
        try {
            return df.format(time);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 字符串转时间
     *
     * @param source yyyy-MM-dd HH:mm:ss.SSS 格式的字符串
     * @return
     */
    public static Date toDate(String source) {
        String formatString = "yyyy-MM-dd hh:mm:ss";
        if (source == null || "".equals(source.trim())) {
            return null;
        }
        source = source.trim();
        if (source.matches("^\\d{4}$")) {
            formatString = "yyyy";
        } else if (source.matches("^\\d{4}-\\d{1,2}$")) {
            formatString = "yyyy-MM";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            formatString = "yyyy-MM-dd";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh:mm";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            formatString = "yyyy-MM-dd hh:mm:ss";
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$")) {
            formatString = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatString);
            Date date = sdf.parse(source);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得本小时的开始时间
     *
     * @return
     */
    public static Date getHourStartTime(Date date) {
        Date dt = null;
        try {
            dt = longHourSdf.parse(longHourSdf.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本小时的结束时间
     *
     * @return
     */
    public static Date getHourEndTime(Date date) {
        Date dt = null;
        try {
            dt = longSdf.parse(longHourSdf.format(date) + ":59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本天的开始时间
     *
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Date dt = null;
        try {
            dt = shortSdf.parse(shortSdf.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本天的结束时间
     *
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Date dt = null;
        try {
            dt = longSdf.parse(shortSdf.format(date) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getWeekStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getWeekEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getMonthStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            dt = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 本月的结束时间
     *
     * @return
     */
    public static Date getMonthEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            dt = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的结束时间
     *
     * @return
     */
    public static Date getYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的开始时间
     *
     * @return
     */
    public static Date getQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return
     */
    public static Date getHalfYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;

    }

    /**
     * 获取前/后半年的结束时间
     *
     * @return
     */
    public static Date getHalfYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取月旬 三旬: 上旬1-10日 中旬11-20日 下旬21-31日
     *
     * @param date
     * @return
     */
    public static int getTenDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11)
            return 1;
        else if (i < 21)
            return 2;
        else
            return 3;
    }


    /**
     * 获取所属旬开始时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayStartTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                return getMonthStartTime(date);
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-11");
                return shortSdf.parse(df.format(date));
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-21");
                return shortSdf.parse(df.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * 获取所属旬结束时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayEndTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-10 23:59:59.999");
                return longSdf.parse(df.format(date));
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-20 23:59:59.999");
                return longSdf.parse(df.format(date));
            } else {
                return getMonthEndTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }


    /**
     * 属于本年第几天
     *
     * @return
     */
    public static int getYearDayIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 属于本年第几周
     *
     * @return
     */
    public static int getYearWeekIndex(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 属于本年第几月
     *
     * @return
     */
    public static int getYearMonthIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 当前属于本年第几个季度
     *
     * @return
     */
    public static int getYeartQuarterIndex(Date date) {
        int month = getYearMonthIndex(date);
        if (month <= 3)
            return 1;
        else if (month <= 6)
            return 2;
        else if (month <= 9)
            return 3;
        else
            return 4;
    }

    /**
     * 获取date所属年的所有天列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {
            Date st = getDayStartTime(calendar.getTime());
            Date et = getDayEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有星期列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearWeekList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        while (calendar.getTime().before(endtm)) {
            Date st = getWeekStartTime(calendar.getTime());
            Date et = getWeekEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有月列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearMonthList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date tm = calendar.getTime();
            Date st = getMonthStartTime(tm);
            Date et = getMonthEndTime(tm);
            result.add(new Date[]{st, et});
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取date所属年的所有季度列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearQuarterList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date st = getQuarterStartTime(calendar.getTime());
            Date et = getQuarterEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.MONTH, 3);
        }
        return result;
    }

    /**
     * 获取date所属月份的所有旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> monthTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getMonthStartTime(date);
        Date endtm = getMonthEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date st = getTenDayStartTime(calendar.getTime());
            Date et = getTenDayEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.DAY_OF_MONTH, 11);
        }
        return result;
    }


    /**
     * 获取date所属年的所有月旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {//
            result.addAll(monthTenDayList(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }


    /**
     * 根据开始时间和结束时间把时间集合输出 yyyy-MM-dd
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static List<String> getDateListBySETime(String startTime, String endTime) {
        try {
            // 格式转换
            String format = "";
            if (startTime.length() == 4)
                format = "yyyy";
            if (startTime.length() == 7)
                format = "yyyy-MM";
            if (startTime.length() == 10)
                format = "yyyy-MM-dd";
            SimpleDateFormat sf = new SimpleDateFormat(format);
            // 开始日期
            Date sDate = sf.parse(startTime);
            // 结束日期
            Date eDate = sf.parse(endTime);

            List<String> list = new ArrayList<String>();
            // 首先加入第一项
            list.add(startTime);
            // 当开始时间小于等于结束时间的时候
            while (sDate.getTime() < eDate.getTime()) {
                // 开始时间+1天
                Calendar sc = Calendar.getInstance();
                sc.setTime(sDate);
                sc.add(sc.DAY_OF_MONTH, +1);
                // 赋给开始时间
                sDate = sc.getTime();
                // 赋给集合
                list.add(sf.format(sDate));
            }
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(j).equals(list.get(i))) {
                        list.remove(j);
                    }
                }
            }
            ;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //时间转换成指定格式
    public static String getFrom(String time, String formt, String formt1) {
        try {
            Date d1 = new SimpleDateFormat(formt).parse(time);
            SimpleDateFormat sdf0 = new SimpleDateFormat(formt1);
            return sdf0.format(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //计算一个时间是否在某个时间段内
    public static boolean belongCalendar(String nowTime, String beginTime, String endTime) throws ParseException {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowdate = sdf.parse(nowTime);
        Date startate = sdf.parse(beginTime);
        Date enddate = sdf.parse(endTime);

        long now = nowdate.getTime();
        long start = startate.getTime();
        long ends = enddate.getTime();
        if (start <= now && ends >= now) {
            result = true;
        }
        return result;
    }

    public static boolean belongCalendars(String nowTime, String beginTime, String endTime) throws ParseException {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowdate = sdf.parse(nowTime);
        Date startate = sdf.parse(beginTime);
        Date enddate = sdf.parse(endTime);

        long now = nowdate.getTime();
        long start = startate.getTime();
        long ends = enddate.getTime();
        if (start <= now && ends >= now) {
            result = true;
        }
        return result;
    }

    //获取当前时间
    public static String getCurrentTime(String format) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(now.getTime());
    }


    public static Date StrToDateTime(String vStrDate) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(vStrDate);
        } catch (ParseException e) {

        }
        return date;
    }

    public static Date getTimeDate(String time, String fromt) {
        try {
            Date d1 = new SimpleDateFormat(fromt).parse(time);
            return d1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加减对应时间后的日期
     *
     * @param date   需要加减时间的日期
     * @param amount 加减的时间(毫秒)
     * @return 加减对应时间后的日期
     */
    public static String subtractTimea(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() - (amount * 1000));
            Date time = sdf.parse(strTime);
            return sdf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加减对应时间后的日期
     *
     * @param date   需要加减时间的日期
     * @param amount 加减的时间(毫秒)
     * @return 加减对应时间后的日期
     */
    public static String subtractTimeas(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() + ((amount * 1000) * 60));
            Date time = sdf.parse(strTime);
            return sdf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date getTimeDates(String time, String fromt) {
        try {
            Date d1 = new SimpleDateFormat(fromt).parse(time);
            return d1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
