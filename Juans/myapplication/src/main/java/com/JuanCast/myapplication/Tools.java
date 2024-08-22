package com.JuanCast.myapplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tools {
    public static String dateToString(Date date,String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return  sdf.format(date);
    }

    public static String dateToString(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/y");

        return  sdf.format(date);
    }

    public static Date StringToDate(String dateString)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/y");
            return sdf.parse(dateString);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToDate(String dateString,String format)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateString);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToTime(String timeString)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.parse(timeString);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToTime(String timeString, String format)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(timeString);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeToString(Date time)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(time);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeToString(Date time, String format)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean dateTimeEnd(String dateTo, String timeEnd)
    {
        try{
            Date dateNow = Calendar.getInstance().getTime();
            SimpleDateFormat sdfTimeEnd = new SimpleDateFormat("M/d/y-hh:mm a");
            Date dateToCompare = sdfTimeEnd.parse(dateTo+"-"+timeEnd);
            return dateToCompare.before(dateNow);
        }catch (Exception e)
        {

        }
        return false;
    }
}
