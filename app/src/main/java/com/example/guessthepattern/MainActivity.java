package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String prefsName = "MyPrefs"; // Name for the preferences file
    private MediaPlayer levelEnter;
    private static boolean shouldPlay;
    private static final String musicVolKey = "musicVolKey";
    private static final String sfxVolKey = "sfxVolKey";
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();

    // TODO: More personalization,ui tweaks for tablets and maybe landscape mode, unlock levels on certain highscores?, global leaderboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        MyGlobals gob = new MyGlobals(this);
        shouldPlay = false;

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("paceKey", 2);
        editor.apply();

        int musicVol = prefs.getInt(musicVolKey, 100);


        try {
            // Get the resource identifier for the raw resource
            int resId = getResources().getIdentifier("main_theme", "raw", getPackageName());

            if (resId != 0) {
                // Set the data source using the resource identifier
                themeSong.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + resId));
                themeSong.prepare();
                themeSong.start();
                themeSong.setVolume(musicVol * 0.01f, musicVol * 0.01f);
                themeSong.setLooping(true);

            } else {
                Log.d("Error", "theme song audio file is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        final int[] sfxVol = {prefs.getInt(sfxVolKey, 100)};
        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        levelEnter.setVolume(sfxVol[0] * 0.01f,  sfxVol[0] * 0.01f);

        final ImageView logo = findViewById(R.id.gtpLogo);

        Button chooseBtn = findViewById(R.id.diffChoose);
        chooseBtn.setOnClickListener(view -> {
            gob.clickEffectResize(chooseBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Levels.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                shouldPlay = true;
                startActivity(intent, options.toBundle());
            }, 100);
        });

        Button shop = findViewById(R.id.shopBtn);
        shop.setOnClickListener(view -> {
            gob.clickEffectResize(shop, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Shop.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                shouldPlay = true;
                startActivity(intent, options.toBundle());
            }, 100);
        });

        ImageButton settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(view -> {
            gob.clickEffectResize(settingsBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Settings.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> {
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view,
                        0, 0,
                        view.getWidth(), view.getHeight()
                );
                shouldPlay = true;
                startActivity(intent, options.toBundle());
            }, 100);
        });


        final int[] logoResources = {R.drawable.gtp_phase2, R.drawable.gtp_phase3, R.drawable.gtp_phase4, R.drawable.gtp_logo};
        final int delayMS = 1000;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentIndex = 0;
            @Override
            public void run() {
                logo.setImageResource(logoResources[currentIndex]);
                currentIndex = (currentIndex + 1) % logoResources.length;
                handler.postDelayed(this, delayMS);
            }

        };
        final int[] isFirstTimer = {prefs.getInt("firstTimer", 1)};
        if(isFirstTimer[0] == 1){
            showFirstTimePresent();
        }
        handler.postDelayed(runnable, delayMS);

    }

    private void showFirstTimePresent() {

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_time_dialog_layout, null);
        builder.setView(dialogView);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            editor.putInt("coinsKey", 100);
            editor.putInt("firstTimer", 0);
            editor.apply();
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
            themeSong.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldPlay = false;
        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        if (!themeSong.isPlaying()){
            themeSong.start();
        }
        if (levelEnter != null){
            final float[] sfxVol = {prefs.getInt(sfxVolKey, 100) * 0.01f};
            levelEnter.setVolume(sfxVol[0],  sfxVol[0]);
        }
    }

}