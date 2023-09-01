package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Hard extends AppCompatActivity {
    
    private static final String prefsName = "MyPrefs"; // Name for the preferences file
    private static final String coinsKey = "coinsKey";
    private static final String coinsPoolKey = "coinsPoolKey";
    private static final String revealsKey = "revealsKey";
    private static final String revivesKey = "revivesKey";
    private static final String highscoreKey = "highscoreKeyHard";
    private static final String scoreKey = "scoreKey";
    private static final String paceKey = "paceKey";
    private MediaPlayer startSound;
    private MediaPlayer sqSound;
    private MediaPlayer gameOnSound;
    private MediaPlayer repeatSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hard);
        MyGlobals gob = new MyGlobals(this);

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        sqSound = MediaPlayer.create(this, R.raw.sq_clicked);
        sqSound.setLooping(false);
        startSound = MediaPlayer.create(this, R.raw.start_sound);
        startSound.setLooping(false);
        gameOnSound = MediaPlayer.create(this, R.raw.gameon_sound);
        gameOnSound.setLooping(true);
        gameOnSound.start();
        repeatSound = MediaPlayer.create(this, R.raw.repeat_sound);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> showExitConfirmation());
        ImageButton reset = findViewById(R.id.redoButton);


        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);
        Button sq5 = findViewById(R.id.sq5);
        Button sq6 = findViewById(R.id.sq6);
        Button sq7 = findViewById(R.id.sq7);
        Button sq8 = findViewById(R.id.sq8);
        Button sq9 = findViewById(R.id.sq9);
        Button sq10 = findViewById(R.id.sq10);
        Button sq11 = findViewById(R.id.sq11);
        Button sq12 = findViewById(R.id.sq12);
        Button sq13 = findViewById(R.id.sq13);
        Button sq14 = findViewById(R.id.sq14);
        Button sq15 = findViewById(R.id.sq15);
        Button sq16 = findViewById(R.id.sq16);

        Button start = findViewById(R.id.startBtn);
        TextView level = findViewById(R.id.level);
        TextView newScore = findViewById(R.id.newHScore);
        RelativeLayout itemBar = findViewById(R.id.itemBar);

        final int[] currentLevel = {1};
        final int[] currentScore = {currentLevel[0] - 1};
        final int[] overallHighscore = {prefs.getInt(highscoreKey, 0)};
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(scoreKey, currentScore[0]);
        editor.putInt(coinsPoolKey, 1);
        editor.apply();

        TextView highscoreText = findViewById(R.id.highscore);
        highscoreText.setText("Highscore: " + overallHighscore[0]);
        TextView scoreText = findViewById(R.id.score);

        final int[] revealersCount = {prefs.getInt(revealsKey, 0)};
        TextView revelearsCountText = findViewById(R.id.revelearsCount);
        revelearsCountText.setText("x" + revealersCount[0]);

        ArrayList<Button> correctSeq = new ArrayList<>();

        final int[] levelTurns = {4};
        final int[] turns = {levelTurns[0]};
        final int[] levelTurnsPace = {prefs.getInt(paceKey, 0)};

        start.setOnClickListener(view -> {

            start.setAlpha(0.5f);
            startSound.start();
            level.setVisibility(View.VISIBLE);
            scoreText.setText("Score: "+ (currentLevel[0] - 1));
            scoreText.setVisibility(View.VISIBLE);
            itemBar.setVisibility(View.VISIBLE);
            startGameRun(levelTurns, correctSeq, currentLevel);

            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> start.setVisibility(View.INVISIBLE), 100);
        });

        final int[] userIndex = {0};
        ArrayList<Button> userSeq = new ArrayList<>();

        sq1.setOnClickListener(view -> {
            sq1.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq1);
            checkSequence(sq1,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });


        sq2.setOnClickListener(view -> {
            sq2.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq2);
            checkSequence(sq2, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq3.setOnClickListener(view -> {
            sq3.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq3);
            checkSequence(sq3, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq4.setOnClickListener(view -> {
            sq4.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq4);
            checkSequence(sq4, userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq5.setOnClickListener(view -> {
            sq5.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq5);
            checkSequence(sq5,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq6.setOnClickListener(view -> {
            sq6.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq6);
            checkSequence(sq6,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq7.setOnClickListener(view -> {
            sq7.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq7);
            checkSequence(sq7,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq8.setOnClickListener(view -> {
            sq8.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq8);
            checkSequence(sq8,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq9.setOnClickListener(view -> {
            sq9.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq9);
            checkSequence(sq9,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq10.setOnClickListener(view -> {
            sq10.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq10);
            checkSequence(sq10,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq11.setOnClickListener(view -> {
            sq11.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq11);
            checkSequence(sq11,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq12.setOnClickListener(view -> {
            sq12.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq12);
            checkSequence(sq12,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq13.setOnClickListener(view -> {
            sq13.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq13);
            checkSequence(sq13,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq14.setOnClickListener(view -> {
            sq14.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq14);
            checkSequence(sq14,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq15.setOnClickListener(view -> {
            sq15.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq15);
            checkSequence(sq15,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        sq16.setOnClickListener(view -> {
            sq16.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq16);
            checkSequence(sq16,userIndex, userSeq, correctSeq, currentScore, currentLevel, levelTurns, levelTurnsPace);
        });

        makeSqUnclickable();

        reset.setOnClickListener(view -> {
            reset.setVisibility(View.INVISIBLE);
            startSound.start();
            gameOnSound.start();
            levelTurns[0] = 4;
            currentLevel[0] = 1;
            levelTurnsPace[0] = prefs.getInt(paceKey, 0);
            level.setText("Level: " + currentLevel[0]);
            currentScore[0] = 0;
            scoreText.setText("Score: " + currentScore[0]);
            turns[0] = levelTurns[0];
            newScore.setVisibility(View.INVISIBLE);
            startGameRun(levelTurns, correctSeq, currentLevel);
        });

        ImageButton revealBtn = findViewById(R.id.revelearBtn);
        ConstraintLayout revealBox = findViewById(R.id.revealerBox);
        TextView title = findViewById(R.id.title);
        revealBtn.setOnClickListener(view -> {
            gob.clickEffectResize(revealBox, this);
            if (revealersCount[0] > 0){
                revealersCount[0]--;
                editor.putInt(revealsKey, revealersCount[0]);
                editor.apply();
                revelearsCountText.setText("x" + revealersCount[0]);
                title.setText("Revealing!");
                makeSqUnclickable();
                revealBtn.setClickable(false);
                Handler handler = new Handler();
                final int[] userIndexAux = {userIndex[0]};

                Runnable revealRun = new Runnable() {
                    @Override
                    public void run() {

                        if (userIndexAux[0] >= 0 && userIndexAux[0] < correctSeq.size()){
                            Button square = correctSeq.get(userIndexAux[0]);
                            int delayStartSeq = 1000; // Delay in ms
                            int delayBetweenSeq = 1800;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    square.setBackgroundResource(R.drawable.sq_bcg);
                                }
                            };
                            handler.postDelayed(runnable, delayBetweenSeq);

                            Runnable runnable2 = new Runnable() {
                                @Override
                                public void run() {
                                    square.setBackgroundResource(R.drawable.start_rectangle);
                                    userIndexAux[0]++;
                                }

                            };
                            handler.postDelayed(runnable2, delayStartSeq);
                            if (userIndexAux[0] == correctSeq.size() - 1) {
                                Handler titleHandler = new Handler();
                                titleHandler.postDelayed(() -> {
                                    title.setText("Repeat the pattern");
                                    makeSqClickable();
                                    revealBtn.setClickable(true);
                                }, delayBetweenSeq);
                            }
                            handler.postDelayed(this, 1500);
                        }
                    }

                };
                handler.post(revealRun);
            }

        });

    }


    public void startGameRun(int[] levelTurns, ArrayList<Button> correctSeq, int[] currentLevel){

        final int[] turns = {levelTurns[0]};
        correctSeq.clear();
        TextView title = findViewById(R.id.title);
        TextView level = findViewById(R.id.level);
        ImageView coinPlus = findViewById(R.id.coinPlus);
        coinPlus.setVisibility(View.INVISIBLE);

        title.setText("Watch the pattern");
        level.setText("Level " + currentLevel[0]);
        ImageButton revealBtn = findViewById(R.id.revelearBtn);
        revealBtn.setClickable(false);
        makeSqUnclickable();

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);
        Button sq5 = findViewById(R.id.sq5);
        Button sq6 = findViewById(R.id.sq6);
        Button sq7 = findViewById(R.id.sq7);
        Button sq8 = findViewById(R.id.sq8);
        Button sq9 = findViewById(R.id.sq9);
        Button sq10 = findViewById(R.id.sq10);
        Button sq11 = findViewById(R.id.sq11);
        Button sq12 = findViewById(R.id.sq12);
        Button sq13 = findViewById(R.id.sq13);
        Button sq14 = findViewById(R.id.sq14);
        Button sq15 = findViewById(R.id.sq15);
        Button sq16 = findViewById(R.id.sq16);

        changeSqAlpha(1.0f);

        Handler handler = new Handler();
        Runnable game = new Runnable() {
            @Override
            public void run() {
                if (turns[0] > 0){
                    int delayStartSeq = 1000; // Delay in ms
                    int delayBetweenSeq = 1800;

                    Button[] squares = {sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9, sq10, sq11, sq12, sq13, sq14, sq15, sq16};

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
                        titleHandler.postDelayed(() -> {
                            title.setText("Repeat the pattern");
                            level.setText(0 + "/" + levelTurns[0]);
                            if(repeatSound != null){
                                repeatSound.start();
                            }
                            ImageButton revealBtn = findViewById(R.id.revelearBtn);
                            revealBtn.setClickable(true);
                            makeSqClickable();

                        }, delayBetweenSeq);
                    }
                    handler.postDelayed(this, 1500);
                }
            }

        };
        handler.post(game);
    }

    public void checkSequence(Button sqAdded ,int[] userIndex, ArrayList<Button> userSeq, ArrayList<Button> correctSeq, int[] currentScore, int[] currentLevel, int[] levelTurns, int[] levelTurnsPace){

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        final int[] turns = {levelTurns[0]};
        final int[] overallHighscore = {0};
        overallHighscore[0] = prefs.getInt(highscoreKey, 0);
        final int[] revivesOwned = {prefs.getInt(revivesKey, 0)};
        final int[] totalCoins = {prefs.getInt(coinsKey, 0)};
        final int[] coinPool = {prefs.getInt(coinsPoolKey, 0)};

        TextView title = findViewById(R.id.title);
        TextView level = findViewById(R.id.level);
        TextView scoreText = findViewById(R.id.score);
        TextView highscoreText = findViewById(R.id.highscore);
        TextView newScore = findViewById(R.id.newHScore);
        ImageButton reset = findViewById(R.id.redoButton);
        Button nextLevel = findViewById(R.id.nextLevel);
        ImageView coinPlus = findViewById(R.id.coinPlus);
        ImageButton revealBtn = findViewById(R.id.revelearBtn);

        userSeq.add(userIndex[0], sqAdded);

        if (userSeq.get(userIndex[0]) == correctSeq.get(userIndex[0])){
            userIndex[0]++;
            level.setText(userIndex[0] + "/" + levelTurns[0]);
        }else{
            title.setText("Game Over!");
            level.setText("Try again");
            revealBtn.setClickable(false);
            gameOnSound.stop();
            gameOnSound.prepareAsync();
            MediaPlayer gameOverSound = MediaPlayer.create(this, R.raw.game_over);
            gameOverSound.start();
            if (revivesOwned[0] > 0){
                showReviveConfirmation(levelTurns, correctSeq, userIndex, userSeq, currentScore, currentLevel);
            }else{
                if (currentScore[0] > overallHighscore[0]){
                    editor.putInt(highscoreKey, currentScore[0]);
                    editor.apply();
                    overallHighscore[0] = prefs.getInt(highscoreKey, 0);
                    newScore.setVisibility(View.VISIBLE);
                }
                highscoreText.setText("Highscore: " + overallHighscore[0]);
                makeSqUnclickable();
                userIndex[0] = 0;
                userSeq.clear();
                Handler handler = new Handler();
                Runnable afterGameOver = () -> {
                    changeSqAlpha(0.5f);
                    reset.setVisibility(View.VISIBLE);
                };handler.postDelayed(afterGameOver, 300);
            }
        }
        if (userIndex[0] == levelTurns[0]){
            title.setText("Correct!");
            MediaPlayer correctSound = MediaPlayer.create(this, R.raw.correct_sound);
            correctSound.start();
            makeSqUnclickable();
            revealBtn.setClickable(false);
            currentScore[0]++;
            totalCoins[0]+= coinPool[0];
            level.setText("+" + coinPool[0] + " ");
            coinPlus.setVisibility(View.VISIBLE);
            editor.putInt(scoreKey, currentScore[0]);
            editor.putInt(coinsKey, totalCoins[0]);
            editor.apply();
            currentLevel[0]++;
            scoreText.setText("Score: " + currentScore[0]);
            userIndex[0] = 0;
            userSeq.clear();
            correctSeq.clear();
            levelTurnsPace[0]--;
            if(levelTurnsPace[0] == 0){
                levelTurns[0]++;
                levelTurnsPace[0] = prefs.getInt(paceKey, 0);
                coinPool[0]++;
                editor.putInt(coinsPoolKey, coinPool[0]);
                editor.apply();
            }
            nextLevel.setOnClickListener(view -> {
                startGameRun(levelTurns, correctSeq, currentLevel);
                nextLevel.setVisibility(View.INVISIBLE);
            });
            turns[0] = levelTurns[0];
            Handler handler = new Handler();
            Runnable afterCongrats = () -> {
                nextLevel.setVisibility(View.VISIBLE);
                changeSqAlpha(0.5f);
            };
            handler.postDelayed(afterCongrats, 500);
        }
    }

    public void makeSqUnclickable(){
        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);
        Button sq5 = findViewById(R.id.sq5);
        Button sq6 = findViewById(R.id.sq6);
        Button sq7 = findViewById(R.id.sq7);
        Button sq8 = findViewById(R.id.sq8);
        Button sq9 = findViewById(R.id.sq9);
        Button sq10 = findViewById(R.id.sq10);
        Button sq11 = findViewById(R.id.sq11);
        Button sq12 = findViewById(R.id.sq12);
        Button sq13 = findViewById(R.id.sq13);
        Button sq14 = findViewById(R.id.sq14);
        Button sq15 = findViewById(R.id.sq15);
        Button sq16 = findViewById(R.id.sq16);

        sq1.setClickable(false);
        sq2.setClickable(false);
        sq3.setClickable(false);
        sq4.setClickable(false);
        sq5.setClickable(false);
        sq6.setClickable(false);
        sq7.setClickable(false);
        sq8.setClickable(false);
        sq9.setClickable(false);
        sq10.setClickable(false);
        sq11.setClickable(false);
        sq12.setClickable(false);
        sq13.setClickable(false);
        sq14.setClickable(false);
        sq15.setClickable(false);
        sq16.setClickable(false);
    }

    public void makeSqClickable(){

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);
        Button sq5 = findViewById(R.id.sq5);
        Button sq6 = findViewById(R.id.sq6);
        Button sq7 = findViewById(R.id.sq7);
        Button sq8 = findViewById(R.id.sq8);
        Button sq9 = findViewById(R.id.sq9);
        Button sq10 = findViewById(R.id.sq10);
        Button sq11 = findViewById(R.id.sq11);
        Button sq12 = findViewById(R.id.sq12);
        Button sq13 = findViewById(R.id.sq13);
        Button sq14 = findViewById(R.id.sq14);
        Button sq15 = findViewById(R.id.sq15);
        Button sq16 = findViewById(R.id.sq16);

        sq1.setClickable(true);
        sq2.setClickable(true);
        sq3.setClickable(true);
        sq4.setClickable(true);
        sq5.setClickable(true);
        sq6.setClickable(true);
        sq7.setClickable(true);
        sq8.setClickable(true);
        sq9.setClickable(true);
        sq10.setClickable(true);
        sq11.setClickable(true);
        sq12.setClickable(true);
        sq13.setClickable(true);
        sq14.setClickable(true);
        sq15.setClickable(true);
        sq16.setClickable(true);
    }

    public void changeSqAlpha(float alphaValue){

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);
        Button sq5 = findViewById(R.id.sq5);
        Button sq6 = findViewById(R.id.sq6);
        Button sq7 = findViewById(R.id.sq7);
        Button sq8 = findViewById(R.id.sq8);
        Button sq9 = findViewById(R.id.sq9);
        Button sq10 = findViewById(R.id.sq10);
        Button sq11 = findViewById(R.id.sq11);
        Button sq12 = findViewById(R.id.sq12);
        Button sq13 = findViewById(R.id.sq13);
        Button sq14 = findViewById(R.id.sq14);
        Button sq15 = findViewById(R.id.sq15);
        Button sq16 = findViewById(R.id.sq16);

        Button[] squares = {sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9, sq10, sq11, sq12, sq13, sq14, sq15, sq16};
        for (Button square : squares) {
            square.setAlpha(alphaValue);
        }
    }

    private void showExitConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog_layout, null);
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

    private void showReviveConfirmation(int[] levelTurns, ArrayList<Button> correctSeq, int[] userIndex, ArrayList<Button> userSeq, int[] currentScore, int[] currentLevel) {

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        final int[] revivesOwned = {prefs.getInt(revivesKey, 0)};
        final int[] overallHighscore = {0};
        overallHighscore[0] = prefs.getInt(highscoreKey, 0);

        TextView newScore = findViewById(R.id.newHScore);
        TextView highscoreText = findViewById(R.id.highscore);
        ImageButton reset = findViewById(R.id.redoButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.revive_dialog_layout, null);
        builder.setView(dialogView);

        TextView revivesCount = dialogView.findViewById(R.id.revivesCount);
        revivesCount.setText("x" + revivesOwned[0]);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            revivesOwned[0]--;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(revivesKey, revivesOwned[0]);
            editor.apply();
            startGameRun(levelTurns, correctSeq, currentLevel);
            userIndex[0] = 0;
            userSeq.clear();

        });

        negativeButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (currentScore[0] > overallHighscore[0]){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(highscoreKey, currentScore[0]);
                editor.apply();
                overallHighscore[0] = prefs.getInt(highscoreKey, 0);
                newScore.setVisibility(View.VISIBLE);
            }
            highscoreText.setText("Highscore: " + overallHighscore[0]);
            makeSqUnclickable();
            userIndex[0] = 0;
            userSeq.clear();
            Handler handler = new Handler();
            Runnable afterGameOver = () -> {
                changeSqAlpha(0.5f);
                reset.setVisibility(View.VISIBLE);
            };handler.postDelayed(afterGameOver, 300);
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitConfirmation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        final int[] overallHighscore = {0};
        final int[] currentScore = {0};
        overallHighscore[0] = prefs.getInt(highscoreKey, 0);
        currentScore[0] = prefs.getInt(scoreKey, 0);

        if (currentScore[0] > overallHighscore[0]){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(highscoreKey, currentScore[0]);
            editor.apply();
            overallHighscore[0] = prefs.getInt(highscoreKey, 0);
        }

        if (sqSound != null){
            sqSound.release();
            sqSound = null;
        }
        if (startSound != null){
            startSound.release();
            startSound = null;
        }
        if (gameOnSound != null){
            gameOnSound.release();
            gameOnSound = null;
        }
        if(repeatSound != null){
            repeatSound.release();
            repeatSound = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameOnSound != null){
            gameOnSound.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameOnSound != null){
            gameOnSound.start();
        }
    }

}