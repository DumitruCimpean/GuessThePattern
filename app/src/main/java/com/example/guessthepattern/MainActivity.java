package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyGlobals gob = new MyGlobals(this);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;
        double screenPercent = 0.60F;

        final ImageView logo = findViewById(R.id.gtpLogo);
        logo.getLayoutParams().width = (int) (displayWidth * screenPercent);

        Button easyBtn = findViewById(R.id.diffEasy);
        easyBtn.getLayoutParams().width = (int) (displayWidth * screenPercent);
        easyBtn.setTextSize(displayHeight * 0.013F);

        Button mediumBtn = findViewById(R.id.diffMedium);
        mediumBtn.getLayoutParams().width = (int) (displayWidth * screenPercent);
        mediumBtn.setTextSize(displayHeight * 0.013F);

        Button hardBtn = findViewById(R.id.diffHard);
        hardBtn.getLayoutParams().width = (int) (displayWidth * screenPercent);
        hardBtn.setTextSize(displayHeight * 0.013F);

        TextView welcome = findViewById(R.id.welcomeTitle);
        welcome.setTextSize(displayHeight * 0.02F);

        TextView difficulty = findViewById(R.id.difficultyTitle);
        difficulty.setTextSize(displayHeight * 0.02F);

        easyBtn.setOnClickListener(view -> {
            gob.openActivity(Easy.class);
            easyBtn.setAlpha(0.5f);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    easyBtn.setAlpha(1.0F); // Reset alpha to its original value
                }
            }, 200);
        });

        mediumBtn.setOnClickListener(view -> {
            gob.openActivity(Medium.class);
            mediumBtn.setAlpha(0.5f);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediumBtn.setAlpha(1.0F); // Reset alpha to its original value
                }
            }, 200);
        });
        hardBtn.setOnClickListener(view -> {
            gob.openActivity(Hard.class);
            hardBtn.setAlpha(0.5f);
            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hardBtn.setAlpha(1.0F); // Reset alpha to its original value
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
                handler.postDelayed(this, delayMS);
            }
        };
        handler.postDelayed(runnable, delayMS);

    }
}