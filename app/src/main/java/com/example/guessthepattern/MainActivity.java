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
    private MediaPlayer levelEnter;
    private static boolean shouldPlay;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();

    // TODO: More personalization, settings menu, ui tweaks for tablets and landscape mode, unlock levels on certain highscores?, global leaderboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        MyGlobals gob = new MyGlobals(this);
        shouldPlay = false;

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("paceKey", 2);
        editor.apply();


        try {
            // Get the resource identifier for the raw resource
            int resId = getResources().getIdentifier("main_theme", "raw", getPackageName());

            if (resId != 0) {
                // Set the data source using the resource identifier
                themeSong.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + resId));
                themeSong.prepare();
                themeSong.start();
                themeSong.setLooping(true);
            } else {
                Log.d("Error", "theme song audio file is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        final ImageView logo = findViewById(R.id.gtpLogo);
        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);

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
        if (!themeSong.isPlaying()){
            themeSong.start();
        }
    }

}