package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.bcgKey;
import static com.example.guessthepattern.MainActivity.coinsKey;
import static com.example.guessthepattern.MainActivity.coinsPoolKey;
import static com.example.guessthepattern.MainActivity.delay1;
import static com.example.guessthepattern.MainActivity.delay2;
import static com.example.guessthepattern.MainActivity.delay3;
import static com.example.guessthepattern.MainActivity.musicVolKey;
import static com.example.guessthepattern.MainActivity.paceKey;
import static com.example.guessthepattern.MainActivity.prefsName;
import static com.example.guessthepattern.MainActivity.revealsKey;
import static com.example.guessthepattern.MainActivity.revivesKey;
import static com.example.guessthepattern.MainActivity.scoreKey;
import static com.example.guessthepattern.MainActivity.sfxVolKey;
import static com.example.guessthepattern.MainActivity.sqNum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

public class Easy extends AppCompatActivity {

    private MediaPlayer startSound;
    private MediaPlayer sqSound;
    private MediaPlayer gameOnSound;
    private MediaPlayer repeatSound;
    private MediaPlayer revealSound;
    private MediaPlayer reviveSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer correctSound;

    private static final String highscoreKey = "highscoreKeyEasy";


    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_easy);
        MyGlobals gob = new MyGlobals(this);

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);

        sqSound = MediaPlayer.create(this, R.raw.sq_clicked);
        startSound = MediaPlayer.create(this, R.raw.start_sound);
        gameOnSound = MediaPlayer.create(this, R.raw.gameon_sound);
        repeatSound = MediaPlayer.create(this, R.raw.repeat_sound);
        revealSound = MediaPlayer.create(this, R.raw.revealing_sound);
        reviveSound = MediaPlayer.create(this, R.raw.revived_sound);
        correctSound = MediaPlayer.create(this, R.raw.correct_sound);
        gameOverSound = MediaPlayer.create(this, R.raw.game_over);
        gameOnSound.setLooping(true);
        gameOnSound.start();

        float musicVol = prefs.getInt(musicVolKey, 100) * 0.01f;
        gameOnSound.setVolume(musicVol, musicVol);

        float sfxVol = prefs.getInt(sfxVolKey, 100) * 0.01f;
        sqSound.setVolume(sfxVol, sfxVol);
        startSound.setVolume(sfxVol, sfxVol);
        repeatSound.setVolume(sfxVol, sfxVol);
        revealSound.setVolume(sfxVol, sfxVol);
        reviveSound.setVolume(sfxVol, sfxVol);
        correctSound.setVolume(sfxVol, sfxVol);
        gameOverSound.setVolume(sfxVol, sfxVol);



        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            gob.clickEffectResize(back, this);
            showExitConfirmationDialog();
        });

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);

        int bcgID = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue);
        Resources res = getResources();
        Drawable background = ResourcesCompat.getDrawable(res, bcgID, getTheme());
        Button[] squares = {sq1, sq2, sq3, sq4};
        for (Button square : squares) {
            square.setBackground(background);
        }
        boolean sqNumbered = prefs.getBoolean(sqNum, false);
        if (sqNumbered){
            int sqIndex = 1;
            for (Button square : squares) {
                square.setText(String.valueOf(sqIndex));
                sqIndex++;
            }
        }

        Button start = findViewById(R.id.startBtn);
        TextView level = findViewById(R.id.level);
        TextView newScore = findViewById(R.id.newHScore);
        RelativeLayout itemBar = findViewById(R.id.itemBar);

        final int[] currentLevel = {1};
        final int[] currentScore = {0};
        final int[] overallHighscore = {prefs.getInt(highscoreKey, 0)};
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(scoreKey, currentScore[0]);
        editor.putInt(coinsPoolKey, 1);
        editor.apply();

        TextView highscoreText = findViewById(R.id.highscore);
        String combinedHighscore = "Highscore: " + overallHighscore[0];
        highscoreText.setText(combinedHighscore);
        TextView scoreText = findViewById(R.id.score);

        final int[] revealersCount = {prefs.getInt(revealsKey, 0)};
        TextView revealersCountText = findViewById(R.id.revelearsCount);
        revealersCountText.setText("x" + revealersCount[0]);

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

        makeSqUnclickable();
        ImageButton reset = findViewById(R.id.redoButton);
        reset.setOnClickListener(view -> {
            reset.setVisibility(View.INVISIBLE);
            startSound.start();
            gameOnSound.start();
            levelTurns[0] = 4;
            levelTurnsPace[0] = prefs.getInt(paceKey, 0);
            currentLevel[0] = 1;
            level.setText("Level: " + currentLevel[0]);
            currentScore[0] = 0;
            scoreText.setText("Score: " + currentScore[0]);
            turns[0] = levelTurns[0];
            newScore.setVisibility(View.INVISIBLE);
            startGameRun(levelTurns, correctSeq, currentLevel);
            editor.putInt(delay1, 1000);
            editor.putInt(delay2, 1800);
            editor.putInt(delay3, 1500);
            editor.putInt(coinsPoolKey, 1);
            editor.apply();
        });

        ImageButton revealBtn = findViewById(R.id.revelearBtn);
        ConstraintLayout revealBox = findViewById(R.id.revealerBox);
        TextView title = findViewById(R.id.title);
        if (revealersCount[0] == 0){
            revealBtn.setAlpha(0.5f);
        }
        revealBtn.setOnClickListener(view -> {
            if (revealersCount[0] > 0){
                gob.clickEffectResize(revealBox, this);
                revealSound.start();
                revealersCount[0]--;
                editor.putInt(revealsKey, revealersCount[0]);
                editor.apply();
                revealersCountText.setText("x" + revealersCount[0]);
                title.setText("Revealing!");
                makeSqUnclickable();
                revealBtn.setClickable(false);
                revealBtn.setAlpha(0.5f);
                Handler handler = new Handler();
                final int[] userIndexAux = {userIndex[0]};

                Runnable revealRun = new Runnable() {
                    @Override
                    public void run() {

                        if (userIndexAux[0] >= 0 && userIndexAux[0] < correctSeq.size()){
                            Button square = correctSeq.get(userIndexAux[0]);
                            int delay1ms = prefs.getInt(delay1, 0);
                            int delay2ms = prefs.getInt(delay2, 0);
                            int delayBetween = prefs.getInt(delay3, 0);
                            Handler handler = new Handler();

                            Runnable runnable = () -> square.setBackgroundResource(bcgID);
                            handler.postDelayed(runnable, delay2ms);

                            Runnable runnable2 = () -> {
                                square.setBackgroundResource(R.drawable.start_rectangle);
                                userIndexAux[0]++;
                            };
                            handler.postDelayed(runnable2, delay1ms);
                            if (userIndexAux[0] == correctSeq.size() - 1) {
                                Handler titleHandler = new Handler();
                                titleHandler.postDelayed(() -> {
                                    title.setText("Repeat the pattern");
                                    makeSqClickable();
                                    revealBtn.setClickable(true);
                                    if (revealersCount[0] > 0){
                                        revealBtn.setAlpha(1.0f);
                                    }
                                }, delay2ms);
                            }
                            handler.postDelayed(this, delayBetween);
                        }
                    }

                };
                handler.post(revealRun);
            }

        });

    }


    @SuppressLint("SetTextI18n")
    public void startGameRun(int[] levelTurns, ArrayList<Button> correctSeq, int[] currentLevel){

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        final int[] turns = {levelTurns[0]};
        final int[] revealersCount = {prefs.getInt(revealsKey, 0)};
        correctSeq.clear();
        TextView title = findViewById(R.id.title);
        TextView level = findViewById(R.id.level);
        ImageView coinPlus = findViewById(R.id.coinPlus);
        ImageButton revealBtn = findViewById(R.id.revelearBtn);
        coinPlus.setVisibility(View.INVISIBLE);

        title.setText("Watch the pattern");
        level.setText("Level " + currentLevel[0]);
        makeSqUnclickable();
        revealBtn.setClickable(false);
        revealBtn.setAlpha(0.5f);

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);

        changeSqAlpha(1.0f);

        Handler handler = new Handler();
        Runnable game = new Runnable() {
            @Override
            public void run() {
                if (turns[0] > 0){
                    int delay1ms = prefs.getInt(delay1, 0);
                    int delay2ms = prefs.getInt(delay2, 0);
                    int delayBetween = prefs.getInt(delay3, 0);

                    Button[] squares = {sq1, sq2, sq3, sq4};

                    Handler handler = new Handler();
                    Random random = new Random();
                    int randomIndex = random.nextInt(squares.length);
                    Button randomSq = squares[randomIndex];

                    Runnable runnable = () -> randomSq.setAlpha(1);
                    handler.postDelayed(runnable, delay2ms);

                    Runnable runnable2 = () -> {
                        randomSq.setAlpha(0.5F);
                        correctSeq.add(randomSq);
                    };
                    handler.postDelayed(runnable2, delay1ms);
                    turns[0]--;
                    if (turns[0] == 0) {
                        Handler titleHandler = new Handler();
                        titleHandler.postDelayed(() -> {
                            title.setText("Repeat the pattern");
                            level.setText(0 + "/" + levelTurns[0]);
                            if(repeatSound != null){
                                repeatSound.start();
                            }
                            makeSqClickable();
                            revealBtn.setClickable(true);
                            if (revealersCount[0] > 0){
                                revealBtn.setAlpha(1.0f);
                            }
                        }, delay2ms);
                    }
                    handler.postDelayed(this, delayBetween);
                }
            }

        };
        handler.post(game);
    }

    @SuppressLint("SetTextI18n")
    public void checkSequence(Button sqAdded , int[] userIndex, ArrayList<Button> userSeq, ArrayList<Button> correctSeq, int[] currentScore, int[] currentLevel, int[] levelTurns, int[] levelTurnsPace){

        SharedPreferences prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        final int[] turns = {levelTurns[0]};
        final int[] overallHighscore = {0};
        overallHighscore[0] = prefs.getInt(highscoreKey, 0);
        final int[] revivesOwned = {prefs.getInt(revivesKey, 0)};
        final int[] totalCoins = {prefs.getInt(coinsKey, 0)};
        final int[] coinPool = {prefs.getInt(coinsPoolKey, 0)};
        final int[] delay1change = {prefs.getInt(delay1, 0)};
        final int[] delay2change = {prefs.getInt(delay2, 0)};
        final int[] delayBetween = {prefs.getInt(delay3, 0)};

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
            correctSound.start();
            makeSqUnclickable();
            revealBtn.setClickable(false);
            currentScore[0]++;
            totalCoins[0]+= coinPool[0];
            level.setText("+" + coinPool[0] + " ");
            coinPlus.setVisibility(View.VISIBLE);
            delay1change[0] /= 1.01;    // ratios for increasing the speed at which the sequence is shown (decreases the delay)
            delay2change[0] /= 1.03;    //  --> these two should be close or same
            delayBetween[0] /= 1.03;    //  ----^
            editor.putInt(scoreKey, currentScore[0]);
            editor.putInt(coinsKey, totalCoins[0]);
            editor.putInt(delay1, delay1change[0]);
            editor.putInt(delay2, delay2change[0]);
            editor.putInt(delay3, delayBetween[0]);
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

        sq1.setClickable(false);
        sq2.setClickable(false);
        sq3.setClickable(false);
        sq4.setClickable(false);
    }

    public void makeSqClickable(){

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);


        sq1.setClickable(true);
        sq2.setClickable(true);
        sq3.setClickable(true);
        sq4.setClickable(true);
    }

    public void changeSqAlpha(float alphaValue){

        Button sq1 = findViewById(R.id.sq1);
        Button sq2 = findViewById(R.id.sq2);
        Button sq3 = findViewById(R.id.sq3);
        Button sq4 = findViewById(R.id.sq4);

        Button[] squares = {sq1, sq2, sq3, sq4};
        for (Button square : squares) {
            square.setAlpha(alphaValue);
        }
    }


    @SuppressLint("SetTextI18n")
    private void showExitConfirmationDialog() {
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
            dialog.dismiss();
            finish();
            overridePendingTransition(0, 0);
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
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
            reviveSound.start();
            gameOnSound.start();
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
        showExitConfirmationDialog();
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
        if (revealSound != null){
            revealSound.release();
            revealSound = null;
        }
        if (reviveSound != null){
            reviveSound.release();
            reviveSound = null;
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