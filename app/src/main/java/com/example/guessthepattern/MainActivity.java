package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String prefsName = "MyPrefs"; // Name for the preferences file
    private MediaPlayer levelEnter;
    public static boolean shouldPlay;
    public static final String bcgImgUriKey = "backgroundImageUri";
    public static final  String coinsKey = "coinsKey";
    public static final String coinsPoolKey = "coinsPoolKey";
    public static final String revealsKey = "revealsKey";
    public static final String revivesKey = "revivesKey";
    public static final String scoreKey = "scoreKey";
    public static final String paceKey = "paceKey";
    public static final String bcgKey = "bcgKey";
    public static final String sqNum = "sqNum";
    public static final String timerMsKey = "timerMsKey";
    public static final String musicVolKey = "musicVolKey";
    public static final String sfxVolKey = "sfxVolKey";

    // the difference between delay1 and delay2 is the amount of time (ms) the square is highlighted
    // delay3 is the delay between the finishing of a square highlight and the start of another one

    public static final String delay1 = "delay1";
    public static final String delay2 = "delay2";
    public static final String delay3 = "delay3";

    private MyGlobals gob;
    private MediaPlayer coinSfx;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();

    private Handler resetHandler;

    /*
     1. TODO: More personalization,ui tweaks for tablets and maybe landscape mode, unlock levels on certain highscores?, global leaderboard
     2. TODO: include some documentation somewhere
    */

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gob = new MyGlobals(this);
        shouldPlay = false;
        resetHandler = new Handler();

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(paceKey, 2);
        editor.putInt(timerMsKey, 10000); // 10 seconds
        editor.putInt(delay1, 1000);
        editor.putInt(delay2, 1800);
        editor.putInt(delay3, 1500);
        editor.apply();

        int musicVol = prefs.getInt(musicVolKey, 100);


        try {
            int resId = getResources().getIdentifier("main_theme", "raw", getPackageName());

            if (resId != 0) {
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
        coinSfx = MediaPlayer.create(this, R.raw.spent_coins);
        coinSfx.setVolume(sfxVol[0], sfxVol[0]);

        final ImageView logo = findViewById(R.id.gtpLogo);

        ConstraintLayout chooseBtn = findViewById(R.id.diffChoose);
        chooseBtn.setOnClickListener(view -> {
            gob.clickEffectResize(chooseBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Gamemodes.class);
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

        ConstraintLayout shop = findViewById(R.id.shopBtn);
        shop.setOnClickListener(view -> {
            gob.clickEffectResize(shop, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Shop.class);
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

        ConstraintLayout settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(view -> {
            gob.clickEffectResize(settingsBtn, this);
            levelEnter.seekTo(0);
            levelEnter.start();
            Intent intent = new Intent(this, Settings.class);
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
            editor.putInt(coinsKey, 100);
            editor.putInt("firstTimer", 0);
            editor.apply();
        }
        handler.postDelayed(runnable, delayMS);

    }

    private void showFirstTimePresent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_time_dialog_layout, null);
        builder.setView(dialogView);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            gob.clickEffectResize(positiveButton, this);
            coinSfx.start();
            resetHandler.postDelayed(dialog::dismiss, 200);
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
        if (coinSfx != null){
            coinSfx.stop();
            coinSfx.release();
            coinSfx = null;
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