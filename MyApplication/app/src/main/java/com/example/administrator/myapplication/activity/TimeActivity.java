package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/7/2.
 */
public class TimeActivity extends Activity {

    private TextView tvTime;
    private static long[] timeStemp = new long[]{1467244800L, 1467158400L, 1466726400L};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        tvTime = (TextView) findViewById(R.id.time);


        tvTime.setText(getTimeCheck());

    }

    private String getTimeCheck() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < timeStemp.length; i++) {
            buffer.append("TimeStemp = ").append(timeStemp[i]).append("\n");
            buffer.append("timeZone = ").append(getCurrentTimeZone());
//            buffer.append("  Date == ").append(unixTimeToBeijingTime(timeStemp[i] - getCurrentTimeZone(timeStemp[i]))).append("\n");
//            buffer.append("not change Date == ").append(unixTimeToBeijingTime(timeStemp[i] - getCurrentTimeZone()));
//            buffer.append("change 1970 Date ==").append(unixTimeToBeijingTime(timeStemp[i] + changeStrDateToLongDate("1970-01-01 00:00:00")));
            buffer.append("  Date == ").append(unixTimeToBeijingTime(timeStemp[i])).append("\n");
            buffer.append("not change Date == ").append(unixTimeToBeijingTime(timeStemp[i]));
            buffer.append("change 1970 Date ==").append(unixTimeToBeijingTime(timeStemp[i]));

            buffer.append("\n\n");
        }
        buffer.append(Locale.getDefault().getCountry());
        return buffer.toString();
    }

    private String isTodayCheck() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TimeStemp = ").append("2016-07-04").append("\n");
        buffer.append("isToday = ").append(isToday("2016-07-04"));
        return buffer.toString();
    }


    public static String unixTimeToBeijingTime(long secondTime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date(secondTime * 1000L));
    }

    public static long changeStrDateToLongDate(String strDate) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = f.parse(strDate);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static int getCurrentTimeZone() {
//        int offset = TimeZone.getTimeZone(Locale..getCountry()).getOffset(0) / 1000;
        int offset = TimeZone.getDefault().getOffset(0) / 1000;
        return offset;
    }

    public static int getCurrentTimeZone(long timeStemp) {
        Date date = new Date();
        date.setTime(timeStemp * 1000L);
        int offset = 0;
        if (TimeZone.getDefault().inDaylightTime(date)) {
            offset = TimeZone.getDefault().getOffset(0) / 1000 - 3600;
        } else {
            offset = TimeZone.getDefault().getOffset(0) / 1000;
        }
        return offset;
    }

    public static boolean isToday(String date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        try {
            date2 = sdFormat.parse(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        long when = date2.getTime();
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;
        Log.i("Time", "then == " + thenYear + "-" + thenMonth + "-" + thenMonthDay);

        time.set(System.currentTimeMillis());
        Log.i("Time", "time == " + time.year + "-" + time.month + "-" + time.monthDay);
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

}
