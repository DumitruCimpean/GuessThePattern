package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Gamemodes extends AppCompatActivity {

    private static boolean shouldPlay;
    private MyGlobals gob;
    private Handler handler;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemodes);
        gob = new MyGlobals(this);
        shouldPlay = false;
        handler = new Handler();

        ConstraintLayout classicBtn = findViewById(R.id.classicGamemode);
        ConstraintLayout timedBtn = findViewById(R.id.timedGamemode);
        ConstraintLayout reflexBtn = findViewById(R.id.reflexGamemode);
        ImageButton back = findViewById(R.id.backButton);
        ImageButton info = findViewById(R.id.info);

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

        // TODO: make Reflex looks less empty

        reflexBtn.setOnClickListener(v -> {
            gob.clickEffectResize(reflexBtn, this);
            handler.postDelayed(() -> gob.openActivityWithExtraInt(Grids.class, "gamemodeSelected", 3), 200);
            shouldPlay = true;
        });

        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            handler.postDelayed(this::finish, 100 );
        });

        info.setOnClickListener(v -> {
            gob.clickEffectResize(info, this);
            showInfoDialog();
        });

    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.gamemodes_info_dialog, null);
        builder.setView(dialogView);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            gob.clickEffectResize(positiveButton, this);
            handler.postDelayed(dialog::dismiss, 200);
        });

        dialog.show();
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