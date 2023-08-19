package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyGlobals gob = new MyGlobals(this);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;
        double screenPercent = 0.50;

        final ImageView logo = findViewById(R.id.gtpLogo);
        logo.getLayoutParams().width = (int) (displayWidth * screenPercent);


        Button easyBtn = findViewById(R.id.diffEasy);

        Button mediumBtn = findViewById(R.id.diffMedium);

        Button hardBtn = findViewById(R.id.diffHard);

        easyBtn.setOnClickListener(view -> {gob.openActivity(Easy.class);});
        mediumBtn.setOnClickListener(view -> {gob.showToast("Chose Medium");});
        hardBtn.setOnClickListener(view -> {gob.showToast("Chose Hard");});

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