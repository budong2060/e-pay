package util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2017/8/24.
 */
public class DateUtils extends DateFormatUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daySub(Date date1, Date date2) {
        long second = (date1.getTime() - date2.getTime()) / 1000L;
        return (int)(second / 24L / 60L / 60L);
    }

    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int minuteSub(Date date1, Date date2) {
        long second = (date1.getTime() - date2.getTime()) / 1000L;
        return (int)(second / 60L);
    }

    /**
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, days);
        return c.getTime();
    }

    /**
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addMonth(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, days);
        return c.getTime();
    }

    /**
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYear(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(1, years);
        return c.getTime();
    }

    /**
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(11, hour);
        return c.getTime();
    }

    /**
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(12, minute);
        return c.getTime();
    }

    /**
     *
     * @param date
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(13, seconds);
        return c.getTime();
    }

    public static int isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            boolean r = false;
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date1);
            c2.setTime(date2);
            c1.set(11, 0);
            c1.set(12, 0);
            c1.set(13, 0);
            c1.set(14, 0);
            c2.set(11, 0);
            c2.set(12, 0);
            c2.set(13, 0);
            c2.set(14, 0);
            int r1 = c1.compareTo(c2);
            if(r1 != 0) {
                r1 = (int)((c1.getTimeInMillis() - c2.getTimeInMillis()) / 24L / 3600L / 1000L);
            }

            return r1;
        } else {
            return 0;
        }
    }

    public static Timestamp truncDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static Timestamp getCurrentDay() {
        Calendar c = Calendar.getInstance();
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        Timestamp time = new Timestamp(c.getTimeInMillis());
        return time;
    }

    /**
     * 获取当前月份
     * @return
     */
    public static Timestamp getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        Timestamp time = new Timestamp(c.getTimeInMillis());
        return time;
    }

    public static long timeDiff(String startDate, String endDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date end = df.parse(endDate);
            java.util.Date start = df.parse(startDate);
            long timeDiff = end.getTime() - start.getTime();
            long day=timeDiff/(24*60*60*1000);
            long hour=(timeDiff/(60*60*1000)-day*24);
            long min=((timeDiff/(60*1000))-day*24*60-hour*60);
            long s=(timeDiff/1000-day*24*60*60-hour*60*60-min*60);
            return min;
        } catch (ParseException e) {
            logger.error(">>时间计算失败, 原因:{}", e);
        }
        return 0;
    }

    public static long timeDiff(Date startDate, Date endDate) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        long day=timeDiff/(24*60*60*1000);
        long hour=(timeDiff/(60*60*1000)-day*24);
        long min=((timeDiff/(60*1000))-day*24*60-hour*60);
        long s=(timeDiff/1000-day*24*60*60-hour*60*60-min*60);
        return min;
    }

    /**
     * 日期比较
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(Date date1, Date date2) {
        return date1 == null && date2 == null?0:(date1 == null?-1:(date2 == null?1:(date1.getTime() - date2.getTime() == 0L?0:((int)(date1.getTime() - date2.getTime()) > 0?1:-1))));
    }

    public static Date parse(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(date);
        } catch (ParseException e) {
            logger.error(">>时间[{}]解析失败, 原因:{}", date, e);
        }
        return null;
    }

}
