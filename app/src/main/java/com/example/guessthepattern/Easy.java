package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Easy extends AppCompatActivity {
    private static final String prefsName = "MyPrefs"; // Name for the preferences file
    private static final String highscoreKey = "highscoreKey"; // Key for saving the value



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> showExitConfirmationDialog());
        ImageButton reset = findViewById(R.id.redoButton);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;
        int displayAvg = (displayHeight + displayWidth) / 2;
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
        start.getLayoutParams().width = (int) (displayWidth * 0.30);
        start.getLayoutParams().height = (int) (displayWidth * 0.20);

        TextView title = findViewById(R.id.title);
        title.getLayoutParams().height = (int) (displayAvg * 0.1f);

        TextView level = findViewById(R.id.level);
        level.getLayoutParams().height = (int) (displayAvg * 0.06f);

        final int[] currentLevel = {1};
        final int[] currentScore = {currentLevel[0] - 1};
        final int[] overallHighscore = {prefs.getInt("highscoreKey", 0)};

        TextView highscoreText = findViewById(R.id.highscore);
        highscoreText.setText("Highscore: " + overallHighscore[0]);
        highscoreText.getLayoutParams().height = (int) (displayHeight * 0.04f);
        highscoreText.getLayoutParams().width = (int) (displayWidth * 0.3f);

        TextView scoreText = findViewById(R.id.score);
        scoreText.getLayoutParams().height = (int) (displayHeight * 0.04f);
        scoreText.getLayoutParams().width = (int) (displayWidth * 0.3f);

        ArrayList<Button> correctSeq = new ArrayList<>();

        final int[] levelTurns = {4};
        final int[] turns = {levelTurns[0]};
        final int[] levelTurnsPace = {2};

        int delayStartSeq = 2000;       // Time in ms
        int delayBetweenSeq = 3000;

        start.setOnClickListener(view -> {

            start.setAlpha(0.5f);
            level.setVisibility(View.VISIBLE);
            scoreText.setText("Score: "+ (currentLevel[0] - 1));
            scoreText.setVisibility(View.VISIBLE);

            startGameRun(levelTurns, correctSeq, delayStartSeq, delayBetweenSeq);

            Handler resetHandler = new Handler();
            resetHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start.setVisibility(View.INVISIBLE);
                }
            }, 100);
        });

        int sqPressedDelay = 200;
        final int[] userIndex = {0};
        ArrayList<Button> userSeq = new ArrayList<>();

        sq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sq1.setAlpha(0.5F);
                checkSequence(sq1,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
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
                checkSequence(sq2, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
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
                checkSequence(sq3, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
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
                checkSequence(sq4, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);

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

        reset.setOnClickListener(view -> {
            reset.setVisibility(View.INVISIBLE);
            levelTurns[0] = 4;
            currentLevel[0] = 1;
            level.setText("Level: " + currentLevel[0]);
            currentScore[0] = 0;
            scoreText.setText("Score: " + currentScore[0]);
            turns[0] = levelTurns[0];
            startGameRun(levelTurns, correctSeq, delayStartSeq, delayBetweenSeq);
        });

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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;
        int displayAvg = (displayHeight + displayWidth) / 2;

        TextView message = dialogView.findViewById(R.id.dialog_message);
        message.getLayoutParams().height = (int) (displayAvg * 0.08f);
        message.setText("Quit to main menu?");

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        positiveButton.getLayoutParams().height = (int) (displayHeight * 0.07f);
        positiveButton.getLayoutParams().width = (int) (displayWidth * 0.2f);

        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        negativeButton.getLayoutParams().height = (int) (displayHeight * 0.07f);
        negativeButton.getLayoutParams().width = (int) (displayWidth * 0.2f);

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

    public void startGameRun(int[] levelTurns, ArrayList<Button> correctSeq, int delayStartSeq, int delayBetweenSeq){

        final int[] turns = {levelTurns[0]};
        correctSeq.clear();
        TextView title = findViewById(R.id.title);
        TextView level = findViewById(R.id.level);

        title.setText("Watch the pattern");
        makeSquaresUnclickable();
        Handler handler = new Handler();
        Runnable game = new Runnable() {
            @Override
            public void run() {
                if (turns[0] > 0){

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
                                level.setText(0 + "/" + levelTurns[0]);
                                sq1.setClickable(true);
                                sq2.setClickable(true);
                                sq3.setClickable(true);
                                sq4.setClickable(true);
                            }
                        }, delayBetweenSeq);
                    }
                    handler.postDelayed(this, delayBetweenSeq);
                }
            }

        };
        handler.post(game);
    }


    public void checkSequence(Button sqAdded ,int[] userIndex, ArrayList<Button> userSeq, ArrayList<Button> correctSeq, int[] currentScore, int[] currentLevel, int[] levelTurns, int[] levelTurnsPace){


        int delayStartSeq = 2000;
        int delayBetweenSeq = 3000;
        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        final int[] turns = {levelTurns[0]};
        final int[] overallHighscore = {0};
        overallHighscore[0] = prefs.getInt("highscoreKey", 0);

        TextView title = findViewById(R.id.title);
        TextView level = findViewById(R.id.level);
        TextView scoreText = findViewById(R.id.score);
        TextView highscoreText = findViewById(R.id.highscore);
        ImageButton reset = findViewById(R.id.redoButton);

        userSeq.add(userIndex[0], sqAdded);

        if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
            userIndex[0]++;
            level.setText(userIndex[0] + "/" + levelTurns[0]);
        }else{
            title.setText("Game Over!");
            level.setText("Try again");
            if (currentScore[0] >= overallHighscore[0]){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("highscoreKey", currentScore[0]);
                editor.apply();
                overallHighscore[0] = prefs.getInt("highscoreKey", 0);
            }
            highscoreText.setText("Highscore: " + overallHighscore[0]);
            makeSquaresUnclickable();
            userIndex[0] = 0;
            userSeq.clear();
            reset.setVisibility(View.VISIBLE);
        }
        if (userIndex[0] == levelTurns[0]){
            title.setText("Congratulations!");
            makeSquaresUnclickable();
            currentLevel[0]++;
            currentScore[0]++;
            level.setText("Level " + currentLevel[0]);
            scoreText.setText("Score: " + currentScore[0]);
            userIndex[0] = 0;
            userSeq.clear();
            correctSeq.clear();
            delayStartSeq -= 1000;
            delayBetweenSeq -= 1000;
            levelTurnsPace[0]--;
            if(levelTurnsPace[0] == 0){
                levelTurns[0]++;
                levelTurnsPace[0] = 2;
            }
            turns[0] = levelTurns[0];
            Handler handler = new Handler();
            int finalDelayStartSeq = delayStartSeq;
            int finalDelayBetweenSeq = delayBetweenSeq;
            Runnable afterCongrats = new Runnable() {
                @Override
                public void run() {
                    startGameRun(levelTurns, correctSeq, finalDelayStartSeq, finalDelayBetweenSeq);
                }
            };
            handler.postDelayed(afterCongrats, 2000);
        }
    }

}