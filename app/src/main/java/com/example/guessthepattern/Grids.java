package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.prefsName;
import static com.example.guessthepattern.MainActivity.sfxVolKey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;

public class Grids extends AppCompatActivity {
    private static boolean shouldPlay;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grids);
        MyGlobals gob = new MyGlobals(this);
        shouldPlay = false;
        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        Handler resetHandler = new Handler();

        int modeSelected = getIntent().getIntExtra("gamemodeSelected", 0);

        ConstraintLayout easyBtn = findViewById(R.id.diffEasy);
        ConstraintLayout mediumBtn = findViewById(R.id.diffMedium);
        ConstraintLayout hardBtn = findViewById(R.id.diffHard);

        final float[] sfxVol = {prefs.getInt(sfxVolKey, 100) * 0.01f};
        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        levelEnter.setVolume(sfxVol[0], sfxVol[0]);

        int buttonPressDelay = 100; // Delay in MS

        easyBtn.setOnClickListener(view -> {
            gob.clickEffectResize(easyBtn, this);
            if (levelEnter != null){
                levelEnter.seekTo(0);
                levelEnter.start();
            }
            Intent intent;
            if (modeSelected == 1){
                intent = new Intent(this, Easy.class);
            }else{
                intent = new Intent(this, EasyTimed.class);
            }
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                startActivity(intent, options.toBundle());
            }, buttonPressDelay);
        });

        mediumBtn.setOnClickListener(view -> {
            gob.clickEffectResize(mediumBtn, this);
            if (levelEnter != null){
                levelEnter.seekTo(0);
                levelEnter.start();
            }
            Intent intent;
            if (modeSelected == 1){
                intent = new Intent(this, Medium.class);
            }else{
                intent = new Intent(this, MediumTimed.class);
            }
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
            if (levelEnter != null){
                levelEnter.seekTo(0);
                levelEnter.start();
            }
            Intent intent;
            if (modeSelected == 1){
                intent = new Intent(this, Hard.class);
            }else{
                intent = new Intent(this, HardTimed.class);
            }
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                startActivity(intent, options.toBundle());
            }, buttonPressDelay);
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            resetHandler.postDelayed(this::finish, 100 );
        });

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