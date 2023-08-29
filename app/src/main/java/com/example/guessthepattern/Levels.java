package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

public class Levels extends AppCompatActivity {

    // TODO: make dialog quit to main menu not levels screen
    private MediaPlayer themeSong;
    private MediaPlayer levelEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        Button easyBtn = findViewById(R.id.diffEasy);

        Button mediumBtn = findViewById(R.id.diffMedium);

        Button hardBtn = findViewById(R.id.diffHard);

        themeSong = MediaPlayer.create(this, R.raw.main_theme);
        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        int currentThemeSongTime = prefs.getInt("themeSongTime", 0);
        themeSong.seekTo(currentThemeSongTime);
        themeSong.start();

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
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (themeSong != null) {
            themeSong.pause();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("themeSongTime", themeSong.getCurrentPosition());
            editor.apply();
            themeSong.setLooping(true);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (themeSong != null) {
            themeSong.start();
        }
    }

}