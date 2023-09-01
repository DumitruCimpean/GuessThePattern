package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

public class Levels extends AppCompatActivity {

    private static boolean shouldPlay;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        MyGlobals gob = new MyGlobals(this);
        shouldPlay = false;

        Button easyBtn = findViewById(R.id.diffEasy);

        Button mediumBtn = findViewById(R.id.diffMedium);

        Button hardBtn = findViewById(R.id.diffHard);

        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        int buttonPressDelay = 100; // Delay in MS

        easyBtn.setOnClickListener(view -> {
            gob.clickEffectResize(easyBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Easy.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                            view,
                            0, 0,
                            view.getWidth(), view.getHeight()
                    );
                    startActivity(intent, options.toBundle());
                }
            }, buttonPressDelay);
        });

        mediumBtn.setOnClickListener(view -> {
            gob.clickEffectResize(mediumBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Medium.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                startActivity(intent, options.toBundle());
            }, buttonPressDelay);
        });
        hardBtn.setOnClickListener(view -> {
            gob.clickEffectResize(hardBtn,this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Hard.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                startActivity(intent, options.toBundle());
            }, buttonPressDelay);
        });

        Button classicBtn = findViewById(R.id.classicGames);
        Button timedBtn = findViewById(R.id.timedGamemod);
        ConstraintLayout classicGrids = findViewById(R.id.classicGrids);
        final Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        final boolean[] isGridsExpanded = {false};
        Handler handler = new Handler();
        classicBtn.setOnClickListener(v -> {

            if (!isGridsExpanded[0]){
                classicGrids.setVisibility(View.VISIBLE);
                classicGrids.startAnimation(slideDown);
                timedBtn.startAnimation(slideDown);
                setButtonsUnclickable();
                handler.postDelayed(this::setButtonsClickable, 500);
                isGridsExpanded[0] = true;
            } else{
                classicGrids.startAnimation(slideUp);
                timedBtn.startAnimation(slideUp);
                setButtonsUnclickable();
                handler.postDelayed(() -> {
                    classicGrids.setVisibility(View.GONE);
                    setButtonsClickable();
                }, 500);
                isGridsExpanded[0] = false;
            }
        });

        timedBtn.setOnClickListener(v -> {
            gob.clickEffectResize(timedBtn, this);
            gob.showWIP();
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            shouldPlay = false;
            finish();
        });

    }

    public void setButtonsUnclickable(){
        Button easyBtn = findViewById(R.id.diffEasy);
        Button mediumBtn = findViewById(R.id.diffMedium);
        Button hardBtn = findViewById(R.id.diffHard);
        Button classicBtn = findViewById(R.id.classicGames);
        Button timedBtn = findViewById(R.id.timedGamemod);
        easyBtn.setClickable(false);
        mediumBtn.setClickable(false);
        hardBtn.setClickable(false);
        classicBtn.setClickable(false);
        timedBtn.setClickable(false);

    }

    public void setButtonsClickable(){
        Button easyBtn = findViewById(R.id.diffEasy);
        Button mediumBtn = findViewById(R.id.diffMedium);
        Button hardBtn = findViewById(R.id.diffHard);
        Button classicBtn = findViewById(R.id.classicGames);
        Button timedBtn = findViewById(R.id.timedGamemod);
        easyBtn.setClickable(true);
        mediumBtn.setClickable(true);
        hardBtn.setClickable(true);
        classicBtn.setClickable(true);
        timedBtn.setClickable(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (levelEnter != null){
            levelEnter.stop();
            levelEnter.release();
            levelEnter = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!shouldPlay){
            if (themeSong != null){
                themeSong.pause();
            }
        }
        finish();

    }

}