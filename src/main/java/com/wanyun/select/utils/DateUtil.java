package com.wanyun.select.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    //当前时间减去多少小时
    public static String subtractTime(int hour, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.HOUR, -hour);
        Date da = rightNow.getTime();
        return sdf.format(da);
    }

    public static String getCurrentTime(String format) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(now.getTime());
    }

    public static String subtractMinute(int minute, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, -minute);
        Date da = rightNow.getTime();
        return sdf.format(da);
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
            String strTime = sdf.format(date.getTime() - ((amount * 1000) * 60));
            Date time = sdf.parse(strTime);
            return sdf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static String tranTime(String time, String fromt, String fromt1) throws ParseException {
        //进行转化时区
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromt, Locale.US);
        Date myDate = dateFormat.parse(time);
        //转换为年月日时分秒
        DateFormat df = new SimpleDateFormat(fromt1);
        String format = df.format(myDate);
        return df.format(myDate);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime   当前时间
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    //根据传递的时间格式和要转换的时间格式把时间转换成想要的时间格式
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

    public static String addhour(String datetime, int addMinutes, String format, String outfrom) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        SimpleDateFormat outformat = new SimpleDateFormat(outfrom);
        Date originalDate = formatter.parse(datetime);
        Calendar newTime = Calendar.getInstance();
        newTime.setTime(originalDate);
        newTime.add(Calendar.HOUR, addMinutes);//日期加n分

        Date newDate = newTime.getTime();
        String retval = outformat.format(newDate);
        return retval;
    }

}
