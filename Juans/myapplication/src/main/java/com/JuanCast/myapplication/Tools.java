package com.JuanCast.myapplication;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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



    public static boolean dateTimeEnd(Date dateNow,String dateTo, String timeEnd)
    {
        try{
            ;
            SimpleDateFormat sdfTimeEnd = new SimpleDateFormat("M/d/y-hh:mm a");
            Date dateToCompare = sdfTimeEnd.parse(dateTo+"-"+timeEnd);
            return dateToCompare.before(dateNow);
        }catch (Exception e)
        {

        }
        return false;
    }
    public static boolean dateTimeEnd(Date dateNow,Date dateToCompare)
    {
        try{
            return dateToCompare.before(dateNow);
        }catch (Exception e)
        {

        }
        return false;
    }

    public static long getMinutesFromDateTimeInterval(Date from, Date to)
    {
        long difference = to.getTime() - from.getTime();

        return TimeUnit.MILLISECONDS.toMinutes(difference);
    }

    public static String getPowerupNameFromProductID(String productID) {
        String powerupName = "";
        switch (productID) {
            case "agimat_ni_juan":
                powerupName = "Agimat Ni Juan";
                return powerupName;
            case "apolaki":
                powerupName = "Apolaki";
                return powerupName;
        }
        return "";
    }

    public static String getTransactionTypeFormatted(String transactionType)
    {
        switch (transactionType){
            case "star_purchase":
                return "Star Purchase";
            case "powerup_purchase":
                return "Power Up Purchase";
        }
        return "";
    }

}
