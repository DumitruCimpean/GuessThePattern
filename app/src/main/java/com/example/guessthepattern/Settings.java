package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private static boolean shouldPlay;
    private static final String bcgKey = "bcgKey";
    private static final String prefsName = "MyPrefs";
    private static final String sqNum = "sqNum";
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MyGlobals gob = new MyGlobals(this);
        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            finish();
        });

        ImageButton sqBlue = findViewById(R.id.sq1);
        ImageButton sqRed = findViewById(R.id.sq2);
        ImageButton sqYellow = findViewById(R.id.sq3);
        ImageButton sqPurple = findViewById(R.id.sq4);

        int sqBlueID = R.drawable.sq_bcg_blue;
        int sqRedID = R.drawable.sq_bcg_red;
        int sqYellowID = R.drawable.sq_bcg_yellow;
        int sqPurpleID = R.drawable.sq_bcg_purple;

        Drawable checkmark = getResources().getDrawable(R.drawable.checkmark);
        Drawable checkmarkDark = getResources().getDrawable(R.drawable.checkmark_darkgrey);

        int selectedBcg = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue);
        if (selectedBcg == sqBlueID){
            sqBlue.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqBlue);
        }
        if (selectedBcg == sqRedID){
            sqRed.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqRed);
        }
        if (selectedBcg == sqYellowID){
            sqYellow.setImageDrawable(checkmarkDark);
            clearOtherSquaresExcept(sqYellow);
        }
        if (selectedBcg == sqPurpleID){
            sqPurple.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqPurple);
        }

        ImageButton numberedBtn = findViewById(R.id.numberedCheckbox);
        final boolean[] numberedBool = {prefs.getBoolean(sqNum, false)};
        if (numberedBool[0]){
            numberedBtn.setImageDrawable(checkmark);
        }else{
            numberedBtn.setImageDrawable(null);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayHeight = metrics.heightPixels;
        int numLayoutHeight = displayHeight / 10;

        numberedBtn.getLayoutParams().width = numLayoutHeight / 2;
        numberedBtn.getLayoutParams().height = numLayoutHeight / 2;

        // TODO: seekbar size and functionality

        sqBlue.setOnClickListener(v -> {
            gob.clickEffectResize(sqBlue, this);
            sqBlue.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqBlue);
            editor.putInt(bcgKey, sqBlueID);
            editor.apply();
        });
        sqRed.setOnClickListener(v -> {
            gob.clickEffectResize(sqRed, this);
            sqRed.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqRed);
            editor.putInt(bcgKey, sqRedID);
            editor.apply();
        });
        sqYellow.setOnClickListener(v -> {
            gob.clickEffectResize(sqYellow, this);
            sqYellow.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqYellow);
            editor.putInt(bcgKey, sqYellowID);
            editor.apply();
        });
        sqPurple.setOnClickListener(v -> {
            gob.clickEffectResize(sqPurple, this);
            sqPurple.setImageDrawable(checkmark);
            clearOtherSquaresExcept(sqPurple);
            editor.putInt(bcgKey, sqPurpleID);
            editor.apply();
        });

        numberedBtn.setOnClickListener(v ->{
            gob.clickEffectResize(numberedBtn, this);
            if (numberedBool[0]){
                numberedBool[0] = false;
                editor.putBoolean(sqNum, false);
                numberedBtn.setImageDrawable(null);
            }else{
                numberedBool[0] = true;
                editor.putBoolean(sqNum, true);
                numberedBtn.setImageDrawable(checkmark);
            }
            editor.apply();
        });


    }

    public void clearOtherSquaresExcept(ImageButton sqToExclude){
        ImageButton sqBlue = findViewById(R.id.sq1);
        ImageButton sqRed = findViewById(R.id.sq2);
        ImageButton sqYellow = findViewById(R.id.sq3);
        ImageButton sqPurple = findViewById(R.id.sq4);
        Drawable checkmark = getResources().getDrawable(R.drawable.checkmark);
        Drawable checkmarkDark = getResources().getDrawable(R.drawable.checkmark_darkgrey);

        ImageButton[] squares = {sqBlue, sqRed, sqYellow, sqPurple};
        for (ImageButton square : squares){
            square.setImageDrawable(null);
        }
        if (sqToExclude == sqYellow ||  sqToExclude == sqRed){
            sqToExclude.setImageDrawable(checkmarkDark);
        }else{
            sqToExclude.setImageDrawable(checkmark);
        }

    }

    @Override
    public void onBackPressed(){
        shouldPlay = true;
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!shouldPlay){
            if (themeSong != null){
                themeSong.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldPlay = false;
        if (themeSong != null){
            themeSong.start();
        }
    }

}