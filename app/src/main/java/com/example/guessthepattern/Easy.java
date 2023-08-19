package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Easy extends AppCompatActivity {


// TODO: increasing levels, game loop, reset button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> showExitConfirmationDialog());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;
        double screenPercent = 0.30;

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);

        sq1.getLayoutParams().width = (int) (displayWidth * screenPercent);
        sq1.getLayoutParams().height = (int) (displayWidth * screenPercent);
        sq2.getLayoutParams().width = (int) (displayWidth * screenPercent);
        sq2.getLayoutParams().height = (int) (displayWidth * screenPercent);
        sq3.getLayoutParams().width = (int) (displayWidth * screenPercent);
        sq3.getLayoutParams().height = (int) (displayWidth * screenPercent);
        sq4.getLayoutParams().width = (int) (displayWidth * screenPercent);
        sq4.getLayoutParams().height = (int) (displayWidth * screenPercent);

        Button start = findViewById(R.id.startBtn);
        start.getLayoutParams().width = (int) (displayWidth * screenPercent);
        start.getLayoutParams().height = (int) (displayWidth * 0.20);
        double startTextSize = displayWidth * 0.025;
        start.setTextSize((int)startTextSize);

        double titleTextSize = displayWidth * 0.04;
        TextView title = findViewById(R.id.title);
        title.setTextSize((int)titleTextSize);

        double levelTextSize = displayWidth * 0.02;
        TextView level = findViewById(R.id.level);
        level.setTextSize((int)levelTextSize);
        final int[] levelTurns = {4};
        final int[] turns = {levelTurns[0]};
        ArrayList<Button> correctSeq = new ArrayList<>();

        start.setOnClickListener(view -> {

            start.setAlpha(0);
            level.setVisibility(View.VISIBLE);
            title.setText("Watch the pattern");
            Handler handler = new Handler();
            Runnable game = new Runnable() {
                @Override
                public void run() {
                    if (turns[0] > 0){
                        int delayStartSeq = 1500; // Delay in ms
                        int delayBetweenSeq = 2000;

                        Button sq1 = findViewById(R.id.sq1);
                        Button sq2 = findViewById(R.id.sq2);
                        Button sq3 = findViewById(R.id.sq3);
                        Button sq4 = findViewById(R.id.sq4);

                        Button[] squares = {sq1, sq2, sq3, sq4};

                        Handler handler = new Handler();
                        Random random = new Random();
                        int randomIndex = random.nextInt(squares.length);
                        Button randomSq = squares[randomIndex];

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                randomSq.setAlpha(1);
                            }
                        };
                        handler.postDelayed(runnable, delayBetweenSeq);

                        Runnable runnable2 = new Runnable() {
                            @Override
                            public void run() {
                                randomSq.setAlpha(0.5F);
                                correctSeq.add(randomSq);
                            }

                        };
                        handler.postDelayed(runnable2, delayStartSeq);
                        turns[0]--;
                        if (turns[0] == 0) {
                            Handler titleHandler = new Handler();
                            titleHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    title.setText("Repeat the pattern");
                                    sq1.setClickable(true);
                                    sq2.setClickable(true);
                                    sq3.setClickable(true);
                                    sq4.setClickable(true);
                                }
                            }, 3000);
                        }
                        handler.postDelayed(this, delayStartSeq);
                    }
                }

            };
            handler.post(game);

        });

        int sqPressedDelay = 200;
        final int[] userIndex = {0};

        ArrayList<Button> userSeq = new ArrayList<>();

        sq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sq1.setAlpha(0.5F);
                userSeq.add(userIndex[0], sq1);
                if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
                    userIndex[0]++;
                    level.setText(userIndex[0] + "/" + levelTurns[0]);
                }else{
                    title.setText("Game Over!");
                    makeSquaresUnclickable();
                }
                if (userIndex[0] == levelTurns[0]){
                    title.setText("Congratulations!");
                    makeSquaresUnclickable();
                }
                Handler resetHandler = new Handler();
                resetHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sq1.setAlpha(1.0F); // Reset alpha to its original value
                    }
                }, sqPressedDelay);
            }
        });

        sq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sq2.setAlpha(0.5F);
                userSeq.add(userIndex[0], sq2);
                if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
                    userIndex[0]++;
                    level.setText(userIndex[0] + "/" + levelTurns[0]);
                }else{
                    title.setText("Game Over!");
                    makeSquaresUnclickable();
                }
                if (userIndex[0] == levelTurns[0]){
                    title.setText("Congratulations!");
                    makeSquaresUnclickable();
                }
                Handler resetHandler = new Handler();
                resetHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sq2.setAlpha(1.0F); // Reset alpha to its original value
                    }
                }, sqPressedDelay);
            }
        });

        sq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sq3.setAlpha(0.5F);
                userSeq.add(userIndex[0], sq3);
                if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
                    userIndex[0]++;
                    level.setText(userIndex[0] + "/" + levelTurns[0]);
                }else{
                    title.setText("Game Over!");
                    makeSquaresUnclickable();
                }
                if (userIndex[0] == levelTurns[0]){
                    title.setText("Congratulations!");
                    makeSquaresUnclickable();
                }
                Handler resetHandler = new Handler();
                resetHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sq3.setAlpha(1.0F); // Reset alpha to its original value
                    }
                }, sqPressedDelay);
            }
        });

        sq4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sq4.setAlpha(0.5F);
                userSeq.add(userIndex[0], sq4);
                if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
                    userIndex[0]++;
                    level.setText(userIndex[0] + "/" + levelTurns[0]);
                }else{
                    title.setText("Game Over!");
                    makeSquaresUnclickable();
                }
                if (userIndex[0] == levelTurns[0]){
                    title.setText("Congratulations!");
                    makeSquaresUnclickable();
                }

                Handler resetHandler = new Handler();
                resetHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sq4.setAlpha(1.0F); // Reset alpha to its original value
                    }
                }, sqPressedDelay);
            }
        });

        makeSquaresUnclickable();

    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        TextView message = dialogView.findViewById(R.id.dialog_message);
        message.setText("Quit to main menu?");

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            finish();
        });

        negativeButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void makeSquaresUnclickable(){
        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);

        sq1.setClickable(false);
        sq2.setClickable(false);
        sq3.setClickable(false);
        sq4.setClickable(false);
    }
}