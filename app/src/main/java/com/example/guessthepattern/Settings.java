package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private static boolean shouldPlay;
    private static final String bcgKey = "bcgKey";
    private static final String prefsName = "MyPrefs";
    private static final String sqNum = "sqNum";
    private static final String musicVolKey = "musicVolKey";
    private static final String sfxVolKey = "sfxVolKey";

    private Drawable checkmark;
    private Drawable checkmarkDark;
    private boolean numberedBool;
    private int musicVolumeChosen;
    private int sfxVolumeChosen;
    private ImageButton sqBlue;
    private ImageButton sqWhite;
    private ImageButton sqYellow;
    private ImageButton sqPurple;
    private ImageButton sqPeach;
    private ImageButton sqBrown;
    private ImageButton sqTurquoise;
    private HorizontalScrollView sqScrollView;
    private int sqBlueID;
    private int sqWhiteID;
    private int sqYellowID;
    private int sqPurpleID;
    private int sqPeachID;
    private int sqBrownID;
    private int sqTurquoiseID;
    private ImageButton[] squares;
    private Resources resources;
    private MyGlobals gob;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Handler handler;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // ------------------------ Initializations ----------------------------------------------- //

        resources = getResources();
        gob = new MyGlobals(this);
        prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        editor = prefs.edit();
        handler = new Handler();

        ImageButton back = findViewById(R.id.backButton);
        ImageButton numberedBtn = findViewById(R.id.numberedCheckbox);
        TextView musicText = findViewById(R.id.musicVolumeText);
        SeekBar musicSeek = findViewById(R.id.musicSeekBar);
        TextView sfxText = findViewById(R.id.sfxVolumeText);
        SeekBar sfxSeek = findViewById(R.id.sfxSeekBar);

        sqScrollView = findViewById(R.id.sqColorScrollView);
        sqBlue = findViewById(R.id.sq1);
        sqWhite = findViewById(R.id.sq2);
        sqYellow = findViewById(R.id.sq3);
        sqPurple = findViewById(R.id.sq4);
        sqPeach = findViewById(R.id.sq5);
        sqBrown = findViewById(R.id.sq6);
        sqTurquoise = findViewById(R.id.sq7);
        squares = new ImageButton[]{sqBlue, sqWhite, sqYellow, sqPurple, sqPeach, sqBrown, sqTurquoise};

        sqBlueID = R.drawable.sq_bcg_blue_lc;
        sqWhiteID = R.drawable.sq_bcg_white_dc;
        sqYellowID = R.drawable.sq_bcg_yellow_dc;
        sqPurpleID = R.drawable.sq_bcg_purple_lc;
        sqPeachID = R.drawable.sq_bcg_peach;
        sqBrownID = R.drawable.sq_bcg_brown;
        sqTurquoiseID = R.drawable.sq_bcg_turquoise;

        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        checkmark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark, getTheme());
        checkmarkDark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark_darkgrey, getTheme());

        // Numbered checkbox resize so it stays square
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayHeight = metrics.heightPixels;
        int numLayoutHeight = displayHeight / 10;

        numberedBtn.getLayoutParams().width = numLayoutHeight / 2;
        numberedBtn.getLayoutParams().height = numLayoutHeight / 2;

        // -------------------------------- Restoring settings ------------------------------------ //

        int selectedBcg = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue_lc);
        if (selectedBcg == sqBlueID){
            sqBlue.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqBlue);
        }
        if (selectedBcg == sqWhiteID){
            sqWhite.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqWhite);
        }
        if (selectedBcg == sqYellowID){
            sqYellow.setImageDrawable(checkmarkDark);
            checkAndScrollToSquare(sqYellow);
        }
        if (selectedBcg == sqPurpleID){
            sqPurple.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqPurple);
        }
        if (selectedBcg == sqPeachID){
            sqPeach.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqPeach);
        }
        if (selectedBcg == sqBrownID){
            sqBrown.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqBrown);
        }
        if (selectedBcg == sqTurquoiseID){
            sqTurquoise.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqTurquoise);
        }

        numberedBool = prefs.getBoolean(sqNum, false);
        if (numberedBool){
            numberedBtn.setImageDrawable(checkmark);
        }else{
            numberedBtn.setImageDrawable(null);
        }

        // ---------------------------------- Buttons --------------------------------------------- //

        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            handler.postDelayed(this::finish, 100);
        });


        // Squares color ---------------------------------------------------------------------------

        sqBlue.setOnClickListener(v -> {
            sqCheckRoutine(sqBlue, sqBlueID);
        });

        sqWhite.setOnClickListener(v -> {
            sqCheckRoutine(sqWhite, sqWhiteID);
        });

        sqYellow.setOnClickListener(v -> {
           sqCheckRoutine(sqYellow, sqYellowID);
        });

        sqPurple.setOnClickListener(v -> {
            sqCheckRoutine(sqPurple, sqPurpleID);
        });

        sqPeach.setOnClickListener(v -> {
            sqCheckRoutine(sqPeach, sqPeachID);
        });

        sqBrown.setOnClickListener(v -> {
            sqCheckRoutine(sqBrown, sqBrownID);
        });

        sqTurquoise.setOnClickListener(v -> {
            sqCheckRoutine(sqTurquoise, sqTurquoiseID);
        });

        // Numbered squares check ------------------------------------------------------------------

        numberedBtn.setOnClickListener(v ->{
            gob.clickEffectResize(numberedBtn, this);
            if (numberedBool){
                numberedBool = false;
                editor.putBoolean(sqNum, false);
                numberedBtn.setImageDrawable(null);
            }else{
                numberedBool = true;
                editor.putBoolean(sqNum, true);
                numberedBtn.setImageDrawable(checkmark);
            }
            editor.apply();
        });

        // Sound settings --------------------------------------------------------------------------

        musicVolumeChosen = prefs.getInt(musicVolKey, 100);
        musicSeek.setProgress(musicVolumeChosen);
        musicSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                themeSong.setVolume(i * 0.01f , i * 0.01f);
                musicVolumeChosen = i;
                editor.putInt(musicVolKey, musicVolumeChosen);
                editor.apply();
                musicText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicText.setText("Music");
            }
        });

        sfxVolumeChosen = prefs.getInt(sfxVolKey, 100);
        sfxSeek.setProgress(sfxVolumeChosen);

        sfxSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                levelEnter.setVolume(i * 0.01f, i * 0.01f);
                sfxVolumeChosen = i;
                editor.putInt(sfxVolKey, sfxVolumeChosen);
                editor.apply();
                sfxText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                levelEnter.seekTo(0);
                levelEnter.start();
                sfxText.setText("SFX");
            }
        });

        // ---------------------------------------------------------------------------------------- //

    }

    public void sqCheckRoutine(ImageButton sqColor, int sqColorID){
        gob.clickEffectResize(sqColor, this);
        checkAndScrollToSquare(sqColor);
        editor.putInt(bcgKey, sqColorID);
        editor.apply();
    }

    public void checkAndScrollToSquare(ImageButton sqToExclude){
        for (ImageButton square : squares){
            square.setImageDrawable(null);
        }
        sqToExclude.setImageDrawable(checkmarkDark);

        sqScrollView.post(() -> {
            int scrollX = sqToExclude.getLeft() - (sqScrollView.getWidth() - sqBlue.getWidth()) / 2;
            sqScrollView.smoothScrollTo(scrollX, 0);
        });

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