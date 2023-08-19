package com.example.guessthepattern;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyGlobals extends Activity {
    Context mContext;


    public MyGlobals(Context context) {
        this.mContext = context;

    }

    public void showWIP(){
        Toast wip = Toast.makeText(mContext, "Work in progress", Toast.LENGTH_SHORT);
        wip.show();
    }

    public void showToast(String text) {
        Toast mToast = Toast.makeText(mContext , text , Toast.LENGTH_SHORT);
        if (mToast != null) mToast.cancel();
        mToast.show();
    }

    public void openActivity(Class className) {
        Intent intent = new Intent(mContext, className);
        mContext.startActivity(intent);
    }

    public void openActivityNoAnim(Class className) {
        Intent intent = new Intent(mContext, className).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent);
    }

    public void setDateMinusDays(TextView textID, Integer days){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, - days);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String Date = dateFormat.format(cal.getTime());
        textID.setText(Date.toLowerCase().replace('j','i').replace('y', 'i').replace('v', 'i'));

    }

    public static String getDateMinus(int days, int hours, int minutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, - days);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd MMM. yyyy");
        String Date = dateFormat.format(cal.getTime());
        String DateFormated = Date.toLowerCase().replace('j','i').replace('y', 'i').replace('v', 'i');

        Calendar calH = Calendar.getInstance();
        calH.add(Calendar.MINUTE, minutes);
        calH.add(Calendar.HOUR_OF_DAY, - hours);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormatH = new SimpleDateFormat("HH:mm");
        String Hour = dateFormatH.format(calH.getTime());
        return DateFormated + " " + Hour;
    }

    public static String getRandomMoney(){
        Random random = new Random();
        int digit1 = random.nextInt(9 - 3) + 1;
        int digit2 = random.nextInt(999 - 100) + 100;
        int floating = random.nextInt(99 - 10) + 10;
        String digit1String = Integer.toString(digit1);
        String digit2String = Integer.toString(digit2);
        String floatingString = Integer.toString(floating);
        return digit1String + "," + digit2String +  "." + floatingString;
    }



}