package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String prefsName = "MyPrefs"; // Name for the preferences file
    private MediaPlayer clickSound;
    public static boolean shouldPlay;
    public static final String bcgImgUriKey = "backgroundImageUri";
    public static final String bcgImgPresetKey = "backgroundImagePreset";
    public static final String isPresetKey = "isPreset";
    public static final  String coinsKey = "coinsKey";
    private static final  String firstTimerKey = "firstTimer";
    public static final String coinsPoolKey = "coinsPoolKey";
    public static final String revealsKey = "revealsKey";
    public static final String revivesKey = "revivesKey";
    public static final String scoreKey = "scoreKey";
    public static final String paceKey = "paceKey";
    public static final String sqBcgKey = "bcgKey";
    public static final String sqNum = "sqNum";
    public static final String timerMsKey = "timerMsKey";
    public static final String musicVolKey = "musicVolKey";
    public static final String sfxVolKey = "sfxVolKey";

    public static final String isColorFromPicker = "isColorFromPickerKey";
    public static final String sqColorPickedKey = "sqColorPickedKey";
    public static final String sqColorPickedKey2 = "sqColorPickedKey2";
    public static final String hueKey = "hueKey";
    public static final String saturationKey = "saturationKey";
    public static final String valueKey = "valueKey";
    public static final String hexInputKey = "hexInputKey";

    // the difference between delay1 and delay2 is the amount of time (ms) the square is highlighted
    // delay3 is the delay between the finishing of a square highlight and the start of another one

    public static final String delay1 = "delay1";
    public static final String delay2 = "delay2";
    public static final String delay3 = "delay3";
    public static final String delay1ratio = "delay1ratio";
    public static final String delay2ratio = "delay2ratio";
    public static final String delay3ratio = "delay3ratio";
    public static int defaultDelay1 = 1000;
    public static int defaultDelay2 = 1800;
    public static int defaultDelay3 = 1500;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private int firstTimeGiftAmount;
    private ImageView logo;
    private int cheatActivationCount;
    private MyGlobals gob;
    private MediaPlayer coinSfx;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    public static int defaultSqColor;

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

        prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt(paceKey, 3);
        editor.putInt(timerMsKey, 10000); // 10 seconds
        editor.putInt(delay1, defaultDelay1);
        editor.putInt(delay2, defaultDelay2);
        editor.putInt(delay3, defaultDelay3);
        editor.putFloat(delay1ratio, 1.05f);
        editor.putFloat(delay2ratio, 1.06f);
        editor.putFloat(delay3ratio, 1.05f);
        editor.apply();
        defaultSqColor = getResources().getColor(R.color.medium_blue, getTheme());

        int musicVol = prefs.getInt(musicVolKey, 100);


        try {
            int resId = getResources().getIdentifier("main_theme", "raw", getPackageName());

            if (resId != 0) {
                themeSong.reset();
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
        clickSound = MediaPlayer.create(this, R.raw.sq_clicked);
        clickSound.setVolume(sfxVol[0] * 0.01f,  sfxVol[0] * 0.01f);
        coinSfx = MediaPlayer.create(this, R.raw.spent_coins);
        coinSfx.setVolume(sfxVol[0], sfxVol[0]);

        logo = findViewById(R.id.gtpLogo);

        cheatActivationCount = prefs.getInt("coinsCheat", 0);
        logo.setOnClickListener(v -> {
            cheatActivationCount++;
            int totalCoins = prefs.getInt(coinsKey, 0);
            if (cheatActivationCount == 10){
                gob.showToast("Cheat money added");
                totalCoins += 999;
                editor.putInt(coinsKey, totalCoins);
                editor.putInt("coinsCheat", cheatActivationCount + 5);
                editor.apply();
            }
        });

        ConstraintLayout chooseBtn = findViewById(R.id.diffChoose);
        chooseBtn.setOnClickListener(view -> {
            gob.clickEffectResize(chooseBtn, this);
            clickSound.seekTo(0);
            clickSound.start();
            shouldPlay = true;
            resetHandler.postDelayed(() -> gob.openActivity(Gamemodes.class), 200);
        });

        ConstraintLayout shop = findViewById(R.id.shopBtn);
        shop.setOnClickListener(view -> {
            gob.clickEffectResize(shop, this);
            clickSound.seekTo(0);
            clickSound.start();
            shouldPlay = true;
            resetHandler.postDelayed(() -> gob.openActivity(Shop.class), 200);
        });

        ConstraintLayout settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(view -> {
            gob.clickEffectResize(settingsBtn, this);
            clickSound.seekTo(0);
            clickSound.start();
            shouldPlay = true;
            resetHandler.postDelayed(()-> gob.openActivity(Settings.class), 200);
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
        final int[] isFirstTimer = {prefs.getInt(firstTimerKey, 1)};

        firstTimeGiftAmount = 50;
        if(isFirstTimer[0] == 1){
            showFirstTimePresent();
            editor.putInt(coinsKey, firstTimeGiftAmount);
            editor.putInt(firstTimerKey, 0);
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
        TextView ftRewardText = dialogView.findViewById(R.id.ftRewardSum);
        ftRewardText.setText("+" + firstTimeGiftAmount);
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

        if (clickSound != null){
            clickSound.stop();
            clickSound.release();
            clickSound = null;
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
        if (!themeSong.isPlaying()){
            themeSong.start();
        }

        if (clickSound != null){
            final float[] sfxVol = {prefs.getInt(sfxVolKey, 100) * 0.01f};
            clickSound.setVolume(sfxVol[0],  sfxVol[0]);
        }

    }

}