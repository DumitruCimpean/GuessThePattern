package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.bcgImgPresetKey;
import static com.example.guessthepattern.MainActivity.bcgImgUriKey;
import static com.example.guessthepattern.MainActivity.bcgKey;
import static com.example.guessthepattern.MainActivity.coinsKey;
import static com.example.guessthepattern.MainActivity.isPresetKey;
import static com.example.guessthepattern.MainActivity.musicVolKey;
import static com.example.guessthepattern.MainActivity.prefsName;
import static com.example.guessthepattern.MainActivity.sfxVolKey;
import static com.example.guessthepattern.MainActivity.sqNum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HardReflex extends AppCompatActivity {
    private Button sq1;
    private Button sq2;
    private Button sq3;
    private Button sq4;
    private Button sq5;
    private Button sq6;
    private Button sq7;
    private Button sq8;
    private Button sq9;
    private Button sq10;
    private Button sq11;
    private Button sq12;
    private Button sq13;
    private Button sq14;
    private Button sq15;
    private Button sq16;
    private Button[] squares;
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
    private TextView highscoreText;
    private TextView newScore;
    private TextView timerText;
    private ImageView coinPlus;
    private ImageButton reset;
    private ArrayList<Button> correctSq;
    private int sqBcgID;
    private long overallHighscore;
    private Stopwatch stopwatch;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MyGlobals gob;
    private Handler handler;
    private static final String highscoreKey = "highscoreKeyHardReflex";


    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hard_reflex);

        // -------------------------- Initializations --------------------------------------------- //

        gob = new MyGlobals(this);
        stopwatch = new Stopwatch();
        handler = new Handler();

        prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        editor = prefs.edit();

        title = findViewById(R.id.title);
        level = findViewById(R.id.level);
        highscoreText = findViewById(R.id.highscore);
        newScore = findViewById(R.id.newHScore);
        coinPlus = findViewById(R.id.coinPlus);
        timerText = findViewById(R.id.timerText);
        reset = findViewById(R.id.redoButton);
        Button start = findViewById(R.id.startBtn);
        ImageButton back = findViewById(R.id.backButton);

        ImageView backgroundLayout = findViewById(R.id.backgroundLayout);
        String imageUriString = prefs.getString(bcgImgUriKey, null);
        boolean isGradient = prefs.getBoolean(isPresetKey, true);
        int bcgId = prefs.getInt(bcgImgPresetKey, R.drawable.bcg_grey_100);

        if (isGradient){
            highscoreText.setBackgroundResource(R.drawable.scores_bcg_empty);
            gob.setAppBackgroundPreset(bcgId, backgroundLayout);
        }else if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            gob.setAppBackground(imageUri, backgroundLayout);
            highscoreText.setBackgroundResource(R.drawable.scores_bcg_solid);
            backgroundLayout.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        sq1 = findViewById(R.id.sq1);
        sq2 = findViewById(R.id.sq2);
        sq3 = findViewById(R.id.sq3);
        sq4 = findViewById(R.id.sq4);
        sq5 = findViewById(R.id.sq5);
        sq6 = findViewById(R.id.sq6);
        sq7 = findViewById(R.id.sq7);
        sq8 = findViewById(R.id.sq8);
        sq9 = findViewById(R.id.sq9);
        sq10 = findViewById(R.id.sq10);
        sq11 = findViewById(R.id.sq11);
        sq12 = findViewById(R.id.sq12);
        sq13 = findViewById(R.id.sq13);
        sq14 = findViewById(R.id.sq14);
        sq15 = findViewById(R.id.sq15);
        sq16 = findViewById(R.id.sq16);
        squares = new Button[]{sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8, sq9, sq10, sq11, sq12, sq13, sq14, sq15, sq16};

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

        correctSq = new ArrayList<>();

        overallHighscore = prefs.getLong(highscoreKey, 999999);
        String combinedHighscore = "Highscore: " + overallHighscore;
        if (overallHighscore == 999999){
            highscoreText.setText("No highscore");
        }else{
            highscoreText.setText(combinedHighscore);
        }

        // -----------------------Applying selected settings -------------------------------------- //

        sqBcgID = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue_lc);
        Resources res = getResources();
        Drawable background = ResourcesCompat.getDrawable(res, sqBcgID, getTheme());
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

        // --------------------------------- Misc buttons ----------------------------------------- //

        start.setOnClickListener(view -> {
            start.setAlpha(0.5f);
            startSound.start();
            startGameRun();
            handler.postDelayed(() -> start.setVisibility(View.INVISIBLE), 100);
        });

        back.setOnClickListener(view -> {
            gob.clickEffectResize(back, this);
            showExitConfirmationDialog();
        });

        reset.setOnClickListener(view -> startGameRun());

        // --------------------------------- Squares ---------------------------------------------- //

        sq1.setOnClickListener(view -> {
            gob.clickEffectResize(sq1, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq1);
        });

        sq2.setOnClickListener(view -> {
            gob.clickEffectResize(sq2, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq2);
        });

        sq3.setOnClickListener(view -> {
            gob.clickEffectResize(sq3, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq3);
        });

        sq4.setOnClickListener(view -> {
            gob.clickEffectResize(sq4, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq4);
        });

        sq5.setOnClickListener(view -> {
            gob.clickEffectResize(sq5, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq5);
        });

        sq6.setOnClickListener(view -> {
            gob.clickEffectResize(sq6, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq6);
        });

        sq7.setOnClickListener(view -> {
            gob.clickEffectResize(sq7, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq7);
        });

        sq8.setOnClickListener(view -> {
            gob.clickEffectResize(sq8, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq8);
        });

        sq9.setOnClickListener(view -> {
            gob.clickEffectResize(sq9, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq9);
        });

        sq10.setOnClickListener(view -> {
            gob.clickEffectResize(sq10, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq10);
        });

        sq11.setOnClickListener(view -> {
            gob.clickEffectResize(sq11, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq11);
        });

        sq12.setOnClickListener(view -> {
            gob.clickEffectResize(sq12, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq12);
        });

        sq13.setOnClickListener(view -> {
            gob.clickEffectResize(sq13, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq13);
        });

        sq14.setOnClickListener(view -> {
            gob.clickEffectResize(sq14, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq14);
        });

        sq15.setOnClickListener(view -> {
            gob.clickEffectResize(sq15, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq15);
        });

        sq16.setOnClickListener(view -> {
            gob.clickEffectResize(sq16, this);
            sqSound.seekTo(0);
            sqSound.start();
            checkSequence(sq16);
        });

        gob.makeSqUnclickable(squares);

        // ---------------------------------------------------------------------------------------- //

    }


    public void startGameRun(){

        correctSq.clear();
        reset.setVisibility(View.INVISIBLE);
        gob.changeSqAlpha(squares, 1.0f);
        title.setText("Wait...");
        timerText.setVisibility(View.INVISIBLE);
        level.setVisibility(View.INVISIBLE);
        coinPlus.setVisibility(View.INVISIBLE);
        newScore.setVisibility(View.INVISIBLE);

        for (Button square:squares){
            square.setBackgroundResource(sqBcgID);
        }
        int randomDelay = ThreadLocalRandom.current().nextInt(1000, 3000);

        Random random = new Random();
        int randomIndex = random.nextInt(squares.length);
        correctSq.add(squares[randomIndex]);

        handler.postDelayed(() -> {

            gob.makeSqClickable(squares);
            title.setText("Go!");
            correctSq.get(0).setBackgroundResource(R.drawable.sq_bcg_green);
            stopwatch.start();

        }, randomDelay);



    }
    public void checkSequence(Button sqAdded){
        stopwatch.stop();
        if (sqAdded == correctSq.get(0)){
            correctSquareClick();
        }else{
            wrongSquareClick(sqAdded);
        }
    }

    public void wrongSquareClick(Button sqAdded){
        title.setText("Wrong square!");
        if (gameOverSound != null){
            gameOverSound.start();
        }
        sqAdded.setBackgroundResource(R.drawable.sq_bcg_red_lc);
        gob.makeSqUnclickable(squares);

        handler.postDelayed(() ->{
            gob.changeSqAlpha(squares, 0.5f);
            reset.setVisibility(View.VISIBLE);
        }, 1000);

    }
    public void correctSquareClick(){
        title.setText("Correct!");
        level.setText("+1 ");
        level.setVisibility(View.VISIBLE);
        coinPlus.setVisibility(View.VISIBLE);

        long reactionTime = stopwatch.getElapsedTime();
        timerText.setText(reactionTime + "ms");
        timerText.setVisibility(View.VISIBLE);
        overallHighscore = prefs.getLong(highscoreKey, 999999);

        if (reactionTime < overallHighscore){
            if (correctSound != null){
                correctSound.start();
            }
            newScore.setVisibility(View.VISIBLE);
            highscoreText.setText("Highscore: " + reactionTime);
            editor.putLong(highscoreKey,reactionTime);
            editor.apply();
        }

        int totalCoins = prefs.getInt(coinsKey, 0);
        totalCoins++;
        editor.putInt(coinsKey, totalCoins);
        editor.apply();

        correctSq.get(0).setBackgroundResource(sqBcgID);
        gob.makeSqUnclickable(squares);
        handler.postDelayed(()->{
            gob.changeSqAlpha(squares, 0.5f);
            reset.setVisibility(View.VISIBLE);
        },300);


    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog_layout, null);
        builder.setView(dialogView);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
            overridePendingTransition(0, 0);
        });
        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopwatch.stop();

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