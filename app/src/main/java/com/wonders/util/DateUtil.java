package com.wonders.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 1229 on 2016/5/31.
 */
public class DateUtil {

    public static String format(Long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String formate2(Long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static Date formate3(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date= sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date formate4(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static int getThisYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
