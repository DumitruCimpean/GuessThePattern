package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer themeSong;
    private MediaPlayer levelEnter;
    private boolean isPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        MyGlobals gob = new MyGlobals(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        themeSong = MediaPlayer.create(this, R.raw.main_theme);
        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;

        final ImageView logo = findViewById(R.id.gtpLogo);
        logo.getLayoutParams().height = (int) (displayHeight * 0.25f);

        Button easyBtn = findViewById(R.id.diffEasy);
        easyBtn.getLayoutParams().width = (int) (displayWidth * 0.7);

        Button mediumBtn = findViewById(R.id.diffMedium);
        mediumBtn.getLayoutParams().width = (int) (displayWidth * 0.7);

        Button hardBtn = findViewById(R.id.diffHard);
        hardBtn.getLayoutParams().width = (int) (displayWidth * 0.7);

        TextView welcome = findViewById(R.id.welcomeTitle);
        welcome.getLayoutParams().height = (int) (displayWidth * 0.20f);

        TextView difficulty = findViewById(R.id.difficultyTitle);
        difficulty.getLayoutParams().height = (int) (displayWidth * 0.15f);

        easyBtn.setOnClickListener(view -> {
            easyBtn.setAlpha(0.5f);
            levelEnter.start();
            Intent intent = new Intent(this, Easy.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    easyBtn.setAlpha(1.0F); // Reset alpha to its original value
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                            view,  // The View you want to zoom from (e.g., a button or an ImageView)
                            0, 0,  // Start position (left, top)
                            view.getWidth(), view.getHeight()  // Final size after zoom
                    );
                    startActivity(intent, options.toBundle());
                }
            }, 200);
        });

        mediumBtn.setOnClickListener(view -> {
            mediumBtn.setAlpha(0.5f);
            levelEnter.start();
            Intent intent = new Intent(this, Medium.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediumBtn.setAlpha(1.0F); // Reset alpha to its original value
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                            view,  // The View you want to zoom from (e.g., a button or an ImageView)
                            0, 0,  // Start position (left, top)
                            view.getWidth(), view.getHeight()  // Final size after zoom
                    );
                    startActivity(intent, options.toBundle());
                }
            }, 200);
        });
        hardBtn.setOnClickListener(view -> {
            hardBtn.setAlpha(0.5f);
            levelEnter.start();
            Intent intent = new Intent(this, Hard.class);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hardBtn.setAlpha(1.0F); // Reset alpha to its original value
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                            view,  // The View you want to zoom from (e.g., a button or an ImageView)
                            0, 0,  // Start position (left, top)
                            view.getWidth(), view.getHeight()  // Final size after zoom
                    );
                    startActivity(intent, options.toBundle());
                }
            }, 200);
        });

        final int[] logoResources = {R.drawable.gtp_phase2, R.drawable.gtp_phase3, R.drawable.gtp_phase4, R.drawable.gtp_logo};
        final int delayMS = 1000; // Delay in milliseconds

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentIndex = 0;
            @Override
            public void run() {
                logo.setImageResource(logoResources[currentIndex]);
                currentIndex = (currentIndex + 1) % logoResources.length;
                if (themeSong != null & !isPlaying){
                    themeSong.start();
                    themeSong.setLooping(true);
                    isPlaying = true;
                }
                handler.postDelayed(this, delayMS);
            }
        };
        handler.postDelayed(runnable, delayMS);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (themeSong != null) {
            themeSong.stop();
            themeSong.release();
            themeSong = null;
        }
        if (levelEnter != null){
            levelEnter.stop();
            levelEnter.release();
            levelEnter = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (themeSong != null && themeSong.isPlaying()) {
            themeSong.stop();
            try {
                themeSong.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            isPlaying = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (themeSong != null && isPlaying) {
            themeSong.start();
            isPlaying = false;
        }
    }

}