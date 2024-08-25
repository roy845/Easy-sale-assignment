package com.example.myapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
    private static final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

    public static String formatMonth(String month) {
        try {
            Date date = monthFormat.parse(month);
            return displayFormat.format(date);
        } catch (ParseException e) {
            return displayFormat.format(new Date());
        }
    }


    public static Date[] getWeekRange(int weeksBefore) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.WEEK_OF_YEAR, -weeksBefore);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date end = calendar.getTime();

        return new Date[]{start, end};
    }

    public static String formatWeekRange(Date start, Date end) {
        return dateFormat.format(start) + " - " + dateFormat.format(end);
    }

    public static Date normalizeToDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateMonthsAgo(int monthsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthsAgo);
        return calendar.getTime();
    }

    public static Date getMonthStartDate(int monthsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthsAgo);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setToStartOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getMonthEndDate(int monthsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -monthsAgo);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setToEndOfDay(calendar);
        return calendar.getTime();
    }

    private static void setToStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setToEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static String formatMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(date);
    }
}

