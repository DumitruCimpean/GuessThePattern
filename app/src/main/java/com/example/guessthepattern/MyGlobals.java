package com.example.guessthepattern;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.renderscript.Sampler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
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

    public void openActivityWithExtraInt(Class className, String stringKey, int value) {
        Intent intent = new Intent(mContext, className);
        intent.putExtra(stringKey, value);
        mContext.startActivity(intent);
    }

    public void openActivityNoAnim(Class className) {
        Intent intent = new Intent(mContext, className).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent);
    }

    public void clickEffectDarken(final View button){
        button.setAlpha(0.5f);
        Handler resetHandler = new Handler();
        resetHandler.postDelayed(() -> button.setAlpha(1.0F), 200);
    }

    public void clickEffectResize(final View button , Context context){
        Handler resetHandler = new Handler();
        final Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.button_down);
        final Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.button_up);
        button.startAnimation(scaleDown);
        resetHandler.postDelayed(() -> button.startAnimation(scaleUp), 200);
    }

    public void makeSqUnclickable(Button[] squaresList){
        for (Button square:squaresList){
            square.setClickable(false);
        }
    }

    public void makeSqClickable(Button[] squaresList){
        for (Button square:squaresList){
            square.setClickable(true);
        }
    }

    public void changeSqAlpha(Button[] squares, float alphaValue){
        for (Button square : squares) {
            square.setAlpha(alphaValue);
        }
    }

    public void setAppBackground(Uri imageUri, ImageView layout) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(imageUri);
            Drawable drawable = Drawable.createFromStream(inputStream, imageUri.toString());
            layout.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}