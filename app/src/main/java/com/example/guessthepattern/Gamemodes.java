package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
        Handler handler = new Handler();

        ConstraintLayout classicBtn = findViewById(R.id.classicGamemode);
        ConstraintLayout timedBtn = findViewById(R.id.timedGamemode);
        ConstraintLayout reflexBtn = findViewById(R.id.reflexGamemode);

        classicBtn.setOnClickListener(v -> {
            gob.clickEffectResize(classicBtn, this);
            handler.postDelayed(() -> gob.openActivityWithExtraInt(Grids.class, "gamemodeSelected", 1), 200);
            shouldPlay = true;
        });

        timedBtn.setOnClickListener(v -> {
            gob.clickEffectResize(timedBtn, this);
            handler.postDelayed(() -> gob.openActivityWithExtraInt(Grids.class, "gamemodeSelected", 2), 200);
            shouldPlay = true;
        });

        reflexBtn.setOnClickListener(v -> {
            gob.clickEffectDarken(reflexBtn);
            gob.showWIP();
        });

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            handler.postDelayed(this::finish, 100 );
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

    }

}