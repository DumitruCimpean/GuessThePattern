package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.bcgImgUriKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Settings extends AppCompatActivity {
    private static boolean shouldPlay;
    private static final String bcgKey = "bcgKey";
    private static final String prefsName = "MyPrefs";
    private static final String sqNum = "sqNum";
    private static final String musicVolKey = "musicVolKey";
    private static final String sfxVolKey = "sfxVolKey";

    private static final String IMAGE_URI_KEY = "imageUri";


    private Drawable checkmark;
    private Drawable checkmarkDark;
    private boolean numberedBool;
    private int musicVolumeChosen;
    private int sfxVolumeChosen;
    private ImageButton sqBlue;
    private ImageButton sqWhite;
    private ImageButton sqYellow;
    private ImageButton sqPurple;
    private ImageButton sqPeach;
    private ImageButton sqBrown;
    private ImageButton sqTurquoise;
    private HorizontalScrollView sqScrollView;
    private ConstraintLayout entireLayout;
    private int sqBlueID;
    private int sqWhiteID;
    private int sqYellowID;
    private int sqPurpleID;
    private int sqPeachID;
    private int sqBrownID;
    private int sqTurquoiseID;
    private Button selectBackgroundButton;
    private ImageButton[] squares;
    private Resources resources;
    private MyGlobals gob;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Handler handler;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    private static final int REQUEST_CODE_IMAGE_PICK = 123;
    private static final int REQUEST_CODE_PERMISSION = 456;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // ------------------------ Initializations ----------------------------------------------- //

        resources = getResources();
        gob = new MyGlobals(this);
        prefs = getSharedPreferences(prefsName, MODE_PRIVATE);
        editor = prefs.edit();
        handler = new Handler();

        ImageButton back = findViewById(R.id.backButton);
        ImageButton numberedBtn = findViewById(R.id.numberedCheckbox);
        TextView musicText = findViewById(R.id.musicVolumeText);
        SeekBar musicSeek = findViewById(R.id.musicSeekBar);
        TextView sfxText = findViewById(R.id.sfxVolumeText);
        SeekBar sfxSeek = findViewById(R.id.sfxSeekBar);

        selectBackgroundButton = findViewById(R.id.selectBackgroundButton);
        sqScrollView = findViewById(R.id.sqColorScrollView);
        entireLayout = findViewById(R.id.entireLayout);
        sqBlue = findViewById(R.id.sq1);
        sqWhite = findViewById(R.id.sq2);
        sqYellow = findViewById(R.id.sq3);
        sqPurple = findViewById(R.id.sq4);
        sqPeach = findViewById(R.id.sq5);
        sqBrown = findViewById(R.id.sq6);
        sqTurquoise = findViewById(R.id.sq7);
        squares = new ImageButton[]{sqBlue, sqWhite, sqYellow, sqPurple, sqPeach, sqBrown, sqTurquoise};

        sqBlueID = R.drawable.sq_bcg_blue_lc;
        sqWhiteID = R.drawable.sq_bcg_white_dc;
        sqYellowID = R.drawable.sq_bcg_yellow_dc;
        sqPurpleID = R.drawable.sq_bcg_purple_lc;
        sqPeachID = R.drawable.sq_bcg_peach;
        sqBrownID = R.drawable.sq_bcg_brown;
        sqTurquoiseID = R.drawable.sq_bcg_turquoise;

        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        checkmark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark, getTheme());
        checkmarkDark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark_darkgrey, getTheme());

        // Numbered checkbox resize so it stays square
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayHeight = metrics.heightPixels;
        int numLayoutHeight = displayHeight / 10;

        numberedBtn.getLayoutParams().width = numLayoutHeight / 2;
        numberedBtn.getLayoutParams().height = numLayoutHeight / 2;

        // -------------------------------- Restoring settings ------------------------------------ //

        int selectedBcg = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue_lc);
        if (selectedBcg == sqBlueID){
            sqBlue.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqBlue);
        }
        if (selectedBcg == sqWhiteID){
            sqWhite.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqWhite);
        }
        if (selectedBcg == sqYellowID){
            sqYellow.setImageDrawable(checkmarkDark);
            checkAndScrollToSquare(sqYellow);
        }
        if (selectedBcg == sqPurpleID){
            sqPurple.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqPurple);
        }
        if (selectedBcg == sqPeachID){
            sqPeach.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqPeach);
        }
        if (selectedBcg == sqBrownID){
            sqBrown.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqBrown);
        }
        if (selectedBcg == sqTurquoiseID){
            sqTurquoise.setImageDrawable(checkmark);
            checkAndScrollToSquare(sqTurquoise);
        }

        numberedBool = prefs.getBoolean(sqNum, false);
        if (numberedBool){
            numberedBtn.setImageDrawable(checkmark);
        }else{
            numberedBtn.setImageDrawable(null);
        }

        String imageUriString = prefs.getString(bcgImgUriKey, null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            setAppBackground(imageUri);
        }


        // ---------------------------------- Buttons --------------------------------------------- //

        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            handler.postDelayed(this::finish, 100);
        });

        selectBackgroundButton.setOnClickListener(v -> {
            gob.clickEffectResize(selectBackgroundButton, this);

            if (Build.VERSION.SDK_INT < 33){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectBackgroundImage();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                }
            }else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    selectBackgroundImage();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
                }
            }

        });


        // Squares color ---------------------------------------------------------------------------

        sqBlue.setOnClickListener(v -> {
            sqCheckRoutine(sqBlue, sqBlueID);
        });

        sqWhite.setOnClickListener(v -> {
            sqCheckRoutine(sqWhite, sqWhiteID);
        });

        sqYellow.setOnClickListener(v -> {
           sqCheckRoutine(sqYellow, sqYellowID);
        });

        sqPurple.setOnClickListener(v -> {
            sqCheckRoutine(sqPurple, sqPurpleID);
        });

        sqPeach.setOnClickListener(v -> {
            sqCheckRoutine(sqPeach, sqPeachID);
        });

        sqBrown.setOnClickListener(v -> {
            sqCheckRoutine(sqBrown, sqBrownID);
        });

        sqTurquoise.setOnClickListener(v -> {
            sqCheckRoutine(sqTurquoise, sqTurquoiseID);
        });

        // Numbered squares check ------------------------------------------------------------------

        numberedBtn.setOnClickListener(v ->{
            gob.clickEffectResize(numberedBtn, this);
            if (numberedBool){
                numberedBool = false;
                editor.putBoolean(sqNum, false);
                numberedBtn.setImageDrawable(null);
            }else{
                numberedBool = true;
                editor.putBoolean(sqNum, true);
                numberedBtn.setImageDrawable(checkmark);
            }
            editor.apply();
        });

        // Sound settings --------------------------------------------------------------------------

        musicVolumeChosen = prefs.getInt(musicVolKey, 100);
        musicSeek.setProgress(musicVolumeChosen);
        musicSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                themeSong.setVolume(i * 0.01f , i * 0.01f);
                musicVolumeChosen = i;
                editor.putInt(musicVolKey, musicVolumeChosen);
                editor.apply();
                musicText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicText.setText("Music");
            }
        });

        sfxVolumeChosen = prefs.getInt(sfxVolKey, 100);
        sfxSeek.setProgress(sfxVolumeChosen);

        sfxSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                levelEnter.setVolume(i * 0.01f, i * 0.01f);
                sfxVolumeChosen = i;
                editor.putInt(sfxVolKey, sfxVolumeChosen);
                editor.apply();
                sfxText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                levelEnter.seekTo(0);
                levelEnter.start();
                sfxText.setText("SFX");
            }
        });

        // ---------------------------------------------------------------------------------------- //

    }

    public void sqCheckRoutine(ImageButton sqColor, int sqColorID){
        gob.clickEffectResize(sqColor, this);
        checkAndScrollToSquare(sqColor);
        editor.putInt(bcgKey, sqColorID);
        editor.apply();
    }

    public void checkAndScrollToSquare(ImageButton sqToExclude){
        for (ImageButton square : squares){
            square.setImageDrawable(null);
        }
        sqToExclude.setImageDrawable(checkmarkDark);

        sqScrollView.post(() -> {
            int scrollX = sqToExclude.getLeft() - (sqScrollView.getWidth() - sqBlue.getWidth()) / 2;
            sqScrollView.smoothScrollTo(scrollX, 0);
        });

    }

    private void enableBackgroundImageSelection(Button selectBackgroundButton) {
        selectBackgroundButton.setEnabled(true);
        // Set button text or appearance as needed
    }

    private void disableBackgroundImageSelection(Button selectBackgroundButton) {
        selectBackgroundButton.setEnabled(false);
        // Set button text or appearance as needed
    }

    private void selectBackgroundImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }

    private void setAppBackground(Uri imageUri) {
        try {
            // Take persistable URI permission to ensure access to the image URI
            getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Drawable drawable = Drawable.createFromStream(inputStream, imageUri.toString());
            editor.putString(bcgImgUriKey, imageUri.toString());
            editor.apply();
            gob.showToast("Background Image applied");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("setAppBackground", "Exception occurred while setting the background");
        }
    }

    private void saveImageUriToInternalStorage(Uri imageUri) {
        try {
            // Convert the URI to a string and save it to internal storage
            String uriString = imageUri.toString();
            FileOutputStream fos = openFileOutput(IMAGE_URI_KEY, Context.MODE_PRIVATE);
            fos.write(uriString.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Uri loadImageUriFromInternalStorage() {
        try {
            // Read the saved URI string from internal storage
            FileInputStream fis = openFileInput(IMAGE_URI_KEY);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            // Convert the saved URI string back to a Uri
            return Uri.parse(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();

                // Save the selected image URI to internal storage
                saveImageUriToInternalStorage(selectedImageUri);

                // Set the app background
                setAppBackground(selectedImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Enable the image selection button.
                enableBackgroundImageSelection(selectBackgroundButton);
                gob.showToast("Click on the button again");
            } else {
                // Permission was denied. Handle this case (e.g., show a message to the user).
                // You can also disable the image selection button if needed.
            }
        }
    }

    @Override
    public void onBackPressed(){
        shouldPlay = true;
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!shouldPlay){
            if (themeSong != null){
                themeSong.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldPlay = false;
        if (themeSong != null){
            themeSong.start();
        }
    }

}