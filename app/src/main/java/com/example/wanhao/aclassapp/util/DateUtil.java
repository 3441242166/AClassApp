package com.example.wanhao.aclassapp.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by wanhao on 2017/8/10.
 */

public class DateUtil {
    private static final String TAG = "DateUtil";

    public static String getNowDateTimeString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        Log.i("date","getNowDateTimeString    "+dateString);
        return dateString;
    }

    public static String getNowDateString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        Log.i("date",dateString);
        return dateString;
    }

//    public static String getStringByDateTime(Date date){
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        Log.i(TAG, "getStringByDateTime: dateString " +formatter.format(date));
//        return formatter.format(date);
//    }

    public static String getNowTimeString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(currentTime);
        Log.i("date",dateString);
        return dateString;
    }

    public static String getTimeString(String dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date= new Date();
        try {
            date = formatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatter.format(date);
        Log.i("date","getNowTimeString    "+dateString);
        return dateString;
    }

    public static String getDateString(String dateTime)  {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date= new Date();
        try {
            date = formatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatter.format(date);
        Log.i("date","getNowDateString    "+dateString);
        return dateString;
    }

    public static String getDateTimeString(String date, String time) {
        Date dateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = formatter.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatter.format(dateTime);
        Log.i("date","getNowDateTimeString    "+dateString);
        return dateString;
    }

    public static Date getDateByDateTimeString(String date){
        Date mDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static Date getDateByDateString(String date){
        Date mDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static Date getTimeByDateString(String date){
        Date mDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    /*    date_1 大 返回 1 --- date_1 小 返回 -1 ---else 返回 0 */
    public static int compareStringByDate(String date_1, String date_2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date_1);
            Date dt2 = df.parse(date_2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int differentDay(String bdate, String smdate){

        DateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d1 = sdf.parse(bdate);
            Date d2 = sdf.parse(smdate);

            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return (int)days;
        }catch (Exception e){
            return -1;
        }

    }

    public static String getAddDayString(String date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDateByDateString(date).getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DATE, day);

        return sdf.format(calendar.getTime());

    }

    public static long getTimeLongByString(String time){
        Log.i(TAG, "getTimeLongByString: time = "+time);
        int x = 0;
        long sum =0;
        StringTokenizer st = new StringTokenizer(time, ":");
        while(st.hasMoreElements()){
            String temp =st.nextToken();
            if(x==0){
                sum += Integer.valueOf(temp) *1000*3600;
            }else if(x==1){
                sum += Integer.valueOf(temp) *1000*60;
            }else{
                sum += Integer.valueOf(temp) *1000;
            }
            x++;
        }
        return sum;
    }

    public static String getTimeByLong(long num){
        Date date = new Date(num);
        String h = ""+num /1000 /60/60;
        if(h.length()<2){
            h ="0"+h;
        }
        String m = ""+num /1000 /60 %60;
        if(m.length()<2){
            m ="0"+m;
        }
        String s = ""+num /1000 %60;
        if(s.length()<2){
            s ="0"+s;
        }
        return h+":"+m+":"+s;
    }

}
