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
import static com.example.guessthepattern.MainActivity.timerMsKey;

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
import android.os.CountDownTimer;
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
import java.util.concurrent.TimeUnit;

public class MediumTimed extends AppCompatActivity {

    private MediaPlayer startSound;
    private MediaPlayer sqSound;
    private MediaPlayer gameOnSound;
    private MediaPlayer repeatSound;
    private MediaPlayer revealSound;
    private MediaPlayer reviveSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer correctSound;


    private TextView title;
    private TextView level;
    private TextView scoreText;
    private TextView highscoreText;
    private TextView newScore;
    private ImageButton reset;
    private Button nextLevel;
    private ImageView coinPlus;
    private ImageButton revealBtn;
    private TextView timerText;
    private CountDownTimer countDown;
    private long milliLeft;
    private long timerTotalTimeMs;
    private Button sq1;
    private Button sq2;
    private Button sq3;
    private Button sq4;
    private Button sq5;
    private Button sq6;
    private Button sq7;
    private Button sq8;
    private Button sq9;
    private int userIndex;
    private ArrayList<Button> userSeq;
    private ArrayList<Button> correctSeq;
    private int currentLevel;
    private int currentScore;
    private int overallHighscore;
    private int revealersCount;
    private int levelTurns;
    private int turns;
    private int levelTurnsPace;
    private int revivesOwned;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String highscoreKey = "highscoreKeyMediumTimed";



    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_medium_timed);
        MyGlobals gob = new MyGlobals(this);

        prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        editor = prefs.edit();

        title = findViewById(R.id.title);
        level = findViewById(R.id.level);
        scoreText = findViewById(R.id.score);
        highscoreText = findViewById(R.id.highscore);
        newScore = findViewById(R.id.newHScore);
        reset = findViewById(R.id.redoButton);
        nextLevel = findViewById(R.id.nextLevel);
        coinPlus = findViewById(R.id.coinPlus);
        revealBtn = findViewById(R.id.revelearBtn);
        timerText = findViewById(R.id.timerText);

        sq1 = findViewById(R.id.sq1);
        sq2 = findViewById(R.id.sq2);
        sq3 = findViewById(R.id.sq3);
        sq4 = findViewById(R.id.sq4);
        sq5 = findViewById(R.id.sq5);
        sq6 = findViewById(R.id.sq6);
        sq7 = findViewById(R.id.sq7);
        sq8 = findViewById(R.id.sq8);
        sq9 = findViewById(R.id.sq9);

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

        timerTotalTimeMs = prefs.getInt(timerMsKey, 0);
        milliLeft = timerTotalTimeMs;

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

        int bcgID = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue);
        Resources res = getResources();
        Drawable background = ResourcesCompat.getDrawable(res, bcgID, getTheme());
        Button[] squares = {sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9};
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
        RelativeLayout itemBar = findViewById(R.id.itemBar);

        currentLevel = 1;
        currentScore = 0;
        overallHighscore = prefs.getInt(highscoreKey, 0);
        editor.putInt(scoreKey, currentScore);
        editor.putInt(coinsPoolKey, 1);
        editor.apply();

        String combinedHighscore = "Highscore: " + overallHighscore;
        highscoreText.setText(combinedHighscore);

        revealersCount = prefs.getInt(revealsKey, 0);
        TextView revealersCountText = findViewById(R.id.revelearsCount);
        revealersCountText.setText("x" + revealersCount);
        revivesOwned = prefs.getInt(revivesKey, 0);


        levelTurns = 4;
        turns = levelTurns;
        levelTurnsPace = prefs.getInt(paceKey, 0);

        userIndex = 0;
        userSeq = new ArrayList<>();
        correctSeq = new ArrayList<>();

        start.setOnClickListener(view -> {

            start.setAlpha(0.5f);
            startSound.start();
            level.setVisibility(View.VISIBLE);
            scoreText.setText("Score: "+ (currentLevel - 1));
            scoreText.setVisibility(View.VISIBLE);
            itemBar.setVisibility(View.VISIBLE);

            startGameRun();

            Handler resetHandler = new Handler();
            resetHandler.postDelayed(() -> start.setVisibility(View.INVISIBLE), 100);
        });


        sq1.setOnClickListener(view -> {
            sq1.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq1);
            checkSequence(sq1);
        });

        sq2.setOnClickListener(view -> {
            sq2.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq2);
            checkSequence(sq2);
        });

        sq3.setOnClickListener(view -> {
            sq3.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq3);
            checkSequence(sq3);
        });

        sq4.setOnClickListener(view -> {
            sq4.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq4);
            checkSequence(sq4);
        });

        sq5.setOnClickListener(view -> {
            sq5.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq5);
            checkSequence(sq5);
        });

        sq6.setOnClickListener(view -> {
            sq6.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq6);
            checkSequence(sq6);
        });

        sq7.setOnClickListener(view -> {
            sq7.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq7);
            checkSequence(sq7);
        });

        sq8.setOnClickListener(view -> {
            sq8.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq8);
            checkSequence(sq8);
        });

        sq9.setOnClickListener(view -> {
            sq9.setAlpha(0.5F);
            sqSound.seekTo(0);
            sqSound.start();
            gob.clickEffectDarken(sq9);
            checkSequence(sq9);
        });

        makeSqUnclickable();
        reset.setOnClickListener(view -> {
            reset.setVisibility(View.INVISIBLE);
            startSound.start();
            gameOnSound.start();
            levelTurns = 4;
            levelTurnsPace = prefs.getInt(paceKey, 0);
            currentLevel = 1;
            level.setText("Level: " + currentLevel);
            currentScore = 0;
            scoreText.setText("Score: " + currentScore);
            turns = levelTurns;
            newScore.setVisibility(View.INVISIBLE);
            milliLeft = timerTotalTimeMs;
            timerText.setText(R.string.timerTextString);
            editor.putInt(delay1, 1000);
            editor.putInt(delay2, 1800);
            editor.putInt(delay3, 1500);
            editor.putInt(coinsPoolKey, 1);
            editor.apply();
            startGameRun();
        });

        ConstraintLayout revealBox = findViewById(R.id.revealerBox);
        if (revealersCount == 0){
            revealBtn.setAlpha(0.5f);
        }
        revealBtn.setOnClickListener(view -> {
            if (revealersCount > 0){
                gob.clickEffectResize(revealBox, this);
                revealSound.start();
                revealersCount--;
                editor.putInt(revealsKey, revealersCount);
                editor.apply();
                revealersCountText.setText("x" + revealersCount);
                title.setText("Revealing!");
                timerPause();
                makeSqUnclickable();
                revealBtn.setClickable(false);
                revealBtn.setAlpha(0.5f);
                Handler handler = new Handler();
                final int[] userIndexAux = {userIndex};

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
                                    timerResume();
                                    makeSqClickable();
                                    revealBtn.setClickable(true);
                                    if (revealersCount > 0){
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
    public void startGameRun(){

        correctSeq.clear();
        coinPlus.setVisibility(View.INVISIBLE);
        turns = levelTurns;

        title.setText("Watch the pattern");
        level.setText("Level " + currentLevel);
        timerStart(milliLeft);
        timerPause();
        makeSqUnclickable();
        revealBtn.setClickable(false);
        revealBtn.setAlpha(0.5f);

        changeSqAlpha(1.0f);

        Handler handler = new Handler();
        Runnable game = new Runnable() {
            @Override
            public void run() {
                if (turns > 0){
                    int delay1ms = prefs.getInt(delay1, 0);
                    int delay2ms = prefs.getInt(delay2, 0);
                    int delayBetween = prefs.getInt(delay3, 0);

                    Button[] squares = {sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9};

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
                    turns--;
                    if (turns == 0) {
                        Handler titleHandler = new Handler();
                        titleHandler.postDelayed(() -> {
                            title.setText("Repeat the pattern");
                            level.setText(0 + "/" + levelTurns);
                            timerResume();
                            if(repeatSound != null){
                                repeatSound.start();
                            }
                            makeSqClickable();
                            revealBtn.setClickable(true);
                            if (revealersCount > 0){
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
    public void checkSequence(Button sqAdded){

        userSeq.add(userIndex, sqAdded);

        if (userSeq.get(userIndex) == correctSeq.get(userIndex)){
            userIndex++;
            level.setText(userIndex + "/" + levelTurns);
        }else{
            gameOverCall();
        }
        if (userIndex == levelTurns){
            correctCall();
        }
    }
    @SuppressLint("SetTextI18n")
    public void gameOverCall(){

        title.setText("Game Over!");
        level.setText("Try again");
        timerPause();
        revealBtn.setClickable(false);
        if (gameOnSound != null){
            gameOnSound.stop();
            gameOnSound.prepareAsync();
        }
        if (gameOverSound != null){
            gameOverSound.start();
        }
        if (revivesOwned > 0){
            showReviveConfirmation();
        }else {
            if (currentScore > overallHighscore) {
                editor.putInt(highscoreKey, currentScore);
                editor.apply();
                overallHighscore = prefs.getInt(highscoreKey, 0);
                newScore.setVisibility(View.VISIBLE);
            }
            highscoreText.setText("Highscore: " + overallHighscore);
            makeSqUnclickable();
            userIndex = 0;
            userSeq.clear();
            Handler handler = new Handler();
            Runnable afterGameOver = () -> {
                changeSqAlpha(0.5f);
                reset.setVisibility(View.VISIBLE);
            };
            handler.postDelayed(afterGameOver, 300);
        }

    }

    @SuppressLint("SetTextI18n")
    public void correctCall(){

        final int[] totalCoins = {prefs.getInt(coinsKey, 0)};
        final int[] coinPool = {prefs.getInt(coinsPoolKey, 0)};
        final int[] delay1change = {prefs.getInt(delay1, 0)};
        final int[] delay2change = {prefs.getInt(delay2, 0)};
        final int[] delayBetween = {prefs.getInt(delay3, 0)};

        title.setText("Correct!");
        timerPause();
        if (correctSound != null){
            correctSound.start();
        }
        makeSqUnclickable();
        revealBtn.setClickable(false);
        currentScore++;
        totalCoins[0]+= coinPool[0];
        level.setText("+" + coinPool[0] + " ");
        coinPlus.setVisibility(View.VISIBLE);
        delay1change[0] /= 1.01;    // ratios for increasing the speed at which the sequence is shown (decreases the delay)
        delay2change[0] /= 1.03;    //  --> these two should be close or same
        delayBetween[0] /= 1.03;    //  ----^
        editor.putInt(scoreKey, currentScore);
        editor.putInt(coinsKey, totalCoins[0]);
        editor.putInt(delay1, delay1change[0]);
        editor.putInt(delay2, delay2change[0]);
        editor.putInt(delay3, delayBetween[0]);
        editor.apply();
        currentLevel++;
        scoreText.setText("Score: " + currentScore);
        userIndex = 0;
        userSeq.clear();
        correctSeq.clear();
        levelTurnsPace--;
        if(levelTurnsPace == 0){
            levelTurns++;
            levelTurnsPace = prefs.getInt(paceKey, 0);
            coinPool[0]++;
            editor.putInt(coinsPoolKey, coinPool[0]);
            editor.apply();
        }
        nextLevel.setOnClickListener(view -> {
            startGameRun();
            nextLevel.setVisibility(View.INVISIBLE);
        });
        turns = levelTurns;
        Handler handler = new Handler();
        Runnable afterCongrats = () -> {
            nextLevel.setVisibility(View.VISIBLE);
            changeSqAlpha(0.5f);
        };
        handler.postDelayed(afterCongrats, 500);
    }

    public void makeSqUnclickable(){
        sq1.setClickable(false);
        sq2.setClickable(false);
        sq3.setClickable(false);
        sq4.setClickable(false);
        sq5.setClickable(false);
        sq6.setClickable(false);
        sq7.setClickable(false);
        sq8.setClickable(false);
        sq9.setClickable(false);
    }

    public void makeSqClickable(){
        sq1.setClickable(true);
        sq2.setClickable(true);
        sq3.setClickable(true);
        sq4.setClickable(true);
        sq5.setClickable(true);
        sq6.setClickable(true);
        sq7.setClickable(true);
        sq8.setClickable(true);
        sq9.setClickable(true);
    }

    public void changeSqAlpha(float alphaValue){
        Button[] squares = {sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9};
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
    private void showReviveConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.revive_dialog_layout, null);
        builder.setView(dialogView);

        TextView revivesCount = dialogView.findViewById(R.id.revivesCount);
        revivesCount.setText("x" + revivesOwned);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            reviveSound.start();
            gameOnSound.start();
            revivesOwned--;
            editor.putInt(revivesKey, revivesOwned);
            editor.apply();
            userIndex = 0;
            userSeq.clear();
            milliLeft = timerTotalTimeMs;
            timerText.setText(R.string.timerTextString);
            startGameRun();

        });

        negativeButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (currentScore > overallHighscore){
                editor.putInt(highscoreKey, currentScore);
                editor.apply();
                overallHighscore = prefs.getInt(highscoreKey, 0);
                newScore.setVisibility(View.VISIBLE);
            }
            highscoreText.setText("Highscore: " + overallHighscore);
            makeSqUnclickable();
            userIndex = 0;
            userSeq.clear();
            Handler handler = new Handler();
            Runnable afterGameOver = () -> {
                changeSqAlpha(0.5f);
                reset.setVisibility(View.VISIBLE);
            };handler.postDelayed(afterGameOver, 300);
        });
        dialog.show();
    }

    private void timerStart(long timeLengthMs){
        countDown = new CountDownTimer(timeLengthMs, 1){
            public void onTick(long millisUntilFinished){
                milliLeft = millisUntilFinished;
                @SuppressLint("DefaultLocale")
                String text = String.format("%02d.%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),
                        (TimeUnit.MILLISECONDS.toMillis(millisUntilFinished) / 10) % 100);
                timerText.setText(text);
            }
            public  void onFinish(){
                timerText.setText("Out of time!");
                gameOverCall();
            }
        }.start();
    }

    private void timerPause() {
        countDown.cancel();
    }

    private void timerResume() {
        timerStart(milliLeft);
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overallHighscore = prefs.getInt(highscoreKey, 0);
        currentScore = prefs.getInt(scoreKey, 0);

        if (currentScore > overallHighscore){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(highscoreKey, currentScore);
            editor.apply();
            overallHighscore = prefs.getInt(highscoreKey, 0);
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