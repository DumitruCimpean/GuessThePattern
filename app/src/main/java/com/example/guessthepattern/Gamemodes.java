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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class Gamemodes extends AppCompatActivity {

    private static boolean shouldPlay;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemodes);
        MyGlobals gob = new MyGlobals(this);
        shouldPlay = false;

        Button classicBtn = findViewById(R.id.classicGames);
        Button timedBtn = findViewById(R.id.timedGamemod);
        classicBtn.setOnClickListener(v -> {
            gob.clickEffectResize(classicBtn, this);
            gob.openActivityWithExtraInt(Grids.class, "gamemodeSelected", 1);
            shouldPlay = true;
        });

        timedBtn.setOnClickListener(v -> {
            gob.clickEffectResize(timedBtn, this);
            gob.openActivityWithExtraInt(Grids.class, "gamemodeSelected", 2);
            shouldPlay = true;
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            finish();
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