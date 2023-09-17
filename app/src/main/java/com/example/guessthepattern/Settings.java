package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.bcgImgPresetKey;
import static com.example.guessthepattern.MainActivity.bcgImgUriKey;
import static com.example.guessthepattern.MainActivity.coinsKey;
import static com.example.guessthepattern.MainActivity.isPresetKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;

public class Settings extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerListener {
    private static boolean shouldPlay;
    private static final String bcgKey = "bcgKey";
    private static final String prefsName = "MyPrefs";
    private static final String sqNum = "sqNum";
    private static final String musicVolKey = "musicVolKey";
    private static final String sfxVolKey = "sfxVolKey";

    private static final String IMAGE_URI_KEY = "imageUri";
    private static final String itemBought = "bought";
    private static final String itemNotBought = "notBought";


    private Drawable checkmark;
    private Drawable checkmarkDark;
    private boolean numberedBool;
    private int musicVolumeChosen;
    private int sfxVolumeChosen;
    private ColorPicker colorPicker;
    private ImageButton sqBlue;
    private ImageButton sqWhite;
    private ImageButton sqYellow;
    private ImageButton sqPurple;
    private ImageButton sqPeach;
    private ImageButton sqBrown;
    private ImageButton sqTurquoise;
    private ImageButton bcg1;
    private ImageButton bcg2;
    private ImageButton bcg3;
    private ImageButton bcg4;
    private ImageButton bcg5;
    private ImageButton bcg6;
    private ImageButton bcg7;

    private HorizontalScrollView sqScrollView;
    private HorizontalScrollView bcgSelectionScrollView;

    private Button openColorPickerButton;
    private TextView selectedColorTextView;
    private int sqBlueID;
    private int sqWhiteID;
    private int sqYellowID;
    private int sqPurpleID;
    private int sqPeachID;
    private int sqBrownID;
    private int sqTurquoiseID;

    private int bcg1id;
    private int bcg2id;
    private int bcg3id;
    private int bcg4id;
    private int bcg5id;
    private int bcg6id;
    private int bcg7id;

    private ImageButton selectBackgroundButton;
    private ImageButton[] squares;
    private ImageButton[] bcgs;
    private Resources resources;
    private MyGlobals gob;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Handler handler;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    private static final int REQUEST_CODE_IMAGE_PICK = 123;
    private static final int REQUEST_CODE_PERMISSION = 456;
    private int bcgPrice;
    private int sqColorPrice;
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

        bcgPrice = 10;
        sqColorPrice = 10;

        ImageButton back = findViewById(R.id.backButton);
        ImageButton numberedBtn = findViewById(R.id.numberedCheckbox);
        TextView musicText = findViewById(R.id.musicVolumeText);
        SeekBar musicSeek = findViewById(R.id.musicSeekBar);
        TextView sfxText = findViewById(R.id.sfxVolumeText);
        SeekBar sfxSeek = findViewById(R.id.sfxSeekBar);

        openColorPickerButton = findViewById(R.id.colorPickerButton);
        selectedColorTextView = findViewById(R.id.selectedColorText);

        openColorPickerButton.setOnClickListener(v -> showColorPickerDialog());

        selectBackgroundButton = findViewById(R.id.selectBackgroundButton);
        bcgSelectionScrollView = findViewById(R.id.bcgSelectionScrollView);
        sqScrollView = findViewById(R.id.sqColorScrollView);
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

        bcg1 = findViewById(R.id.bcg1);
        bcg2 = findViewById(R.id.bcg2);
        bcg3 = findViewById(R.id.bcg3);
        bcg4 = findViewById(R.id.bcg4);
        bcg5 = findViewById(R.id.bcg5);
        bcg6 = findViewById(R.id.bcg6);
        bcg7 = findViewById(R.id.bcg7);
        bcgs = new ImageButton[]{bcg1, bcg2, bcg3, bcg4, bcg5, bcg6, bcg7};

        bcg1id = R.drawable.bcg_grey_100;
        bcg2id = R.drawable.bcg_red_blue;
        bcg3id = R.drawable.bcg_turquoise_blue;
        bcg4id = R.drawable.bcg_yellow_orange;
        bcg5id = R.drawable.bcg_peach_blue;
        bcg6id = R.drawable.bcg_magenta_orange;
        bcg7id = R.drawable.bcg_turquoise_magenta;

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

        int selectedSqBcg = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue_lc);
        if (selectedSqBcg == sqBlueID){
            checkAndScrollToSquare(sqBlue);
        }
        if (selectedSqBcg == sqWhiteID){
            checkAndScrollToSquare(sqWhite);
        }
        if (selectedSqBcg == sqYellowID){
            checkAndScrollToSquare(sqYellow);
        }
        if (selectedSqBcg == sqPurpleID){
            checkAndScrollToSquare(sqPurple);
        }
        if (selectedSqBcg == sqPeachID){
            checkAndScrollToSquare(sqPeach);
        }
        if (selectedSqBcg == sqBrownID){
            checkAndScrollToSquare(sqBrown);
        }
        if (selectedSqBcg == sqTurquoiseID){
            checkAndScrollToSquare(sqTurquoise);
        }


        int selectedBcg = prefs.getInt(bcgImgPresetKey, R.drawable.bcg_grey_100);

        if (selectedBcg == 0){
            for (ImageButton bcg : bcgs){
                bcg.setImageDrawable(null);
            }
        }

        if (selectedBcg == bcg1id){
            checkAndScrollToBackground(bcg1);
        }

        if (selectedBcg == bcg2id){
            checkAndScrollToBackground(bcg2);
        }

        if (selectedBcg == bcg3id){
            checkAndScrollToBackground(bcg3);
        }

        if (selectedBcg == bcg4id){
            checkAndScrollToBackground(bcg4);
        }

        if (selectedBcg == bcg5id){
            checkAndScrollToBackground(bcg5);
        }

        if (selectedBcg == bcg6id){
            checkAndScrollToBackground(bcg6);
        }

        if (selectedBcg == bcg7id){
            checkAndScrollToBackground(bcg7);
        }



        numberedBool = prefs.getBoolean(sqNum, false);
        if (numberedBool){
            numberedBtn.setImageDrawable(checkmark);
        }else{
            numberedBtn.setImageDrawable(null);
        }


        /*

        boolean isFirstTimeSetting = prefs.getBoolean("veryFirstInitialization", true);
        if (isFirstTimeSetting){
            bcg2.setImageResource(R.drawable.coin);
            bcg2.setTag(itemNotBought);
            bcg3.setImageResource(R.drawable.coin);
            bcg3.setTag(itemNotBought);
            bcg4.setImageResource(R.drawable.coin);
            bcg4.setTag(itemNotBought);
            bcg5.setImageResource(R.drawable.coin);
            bcg5.setTag(itemNotBought);
            bcg6.setImageResource(R.drawable.coin);
            bcg6.setTag(itemNotBought);
            bcg7.setImageResource(R.drawable.coin);
            bcg7.setTag(itemNotBought);
            editor.putBoolean("veryFirstInitialization", false);
            editor.apply();
        }

        */



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

        bcg1.setOnClickListener( v -> bcgCheckRoutine(bcg1, bcg1id));
        bcg2.setOnClickListener( v -> bcgCheckRoutine(bcg2, bcg2id));
        bcg3.setOnClickListener( v -> bcgCheckRoutine(bcg3, bcg3id));
        bcg4.setOnClickListener( v -> bcgCheckRoutine(bcg4, bcg4id));
        bcg5.setOnClickListener( v -> bcgCheckRoutine(bcg5, bcg5id));
        bcg6.setOnClickListener( v -> bcgCheckRoutine(bcg6, bcg6id));
        bcg7.setOnClickListener( v -> bcgCheckRoutine(bcg7, bcg7id));


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

    public void bcgCheckRoutine(ImageButton bcgColor, int bcgColorID){
        gob.clickEffectResize(bcgColor, this);
        if (bcgColor.getTag() == itemNotBought){
            showBuyConfirmationDialog(bcgColor, bcgPrice);
        }else {
            checkAndScrollToBackground(bcgColor);
            editor.putInt(bcgImgPresetKey, bcgColorID);
            editor.putBoolean(isPresetKey, true);
            editor.apply();
        }
    }

    public void checkAndScrollToSquare(ImageButton sqToCheck){
        for (ImageButton square : squares){
            if (square.getTag() != itemNotBought){
                square.setImageDrawable(null);
            }else{
                square.setImageResource(R.drawable.coin);
            }
        }
        sqToCheck.setImageDrawable(checkmarkDark);

        sqScrollView.post(() -> {
            int scrollX = sqToCheck.getLeft() - (sqScrollView.getWidth() - sqBlue.getWidth()) / 2;
            sqScrollView.smoothScrollTo(scrollX, 0);
        });
    }

    public void checkAndScrollToBackground(ImageButton bcgToCheck){
        for (ImageButton bcg : bcgs){
            if (bcg.getTag() != itemNotBought){
                bcg.setImageDrawable(null);
            }
        }
        bcgToCheck.setImageDrawable(checkmark);

        sqScrollView.post(() -> {
            int scrollX = bcgToCheck.getLeft() - (bcgSelectionScrollView.getWidth() - bcg1.getWidth()) / 2;
            bcgSelectionScrollView.smoothScrollTo(scrollX, 0);
        });
    }

    private void enableBackgroundImageSelection(View selectBackgroundButton) {
        selectBackgroundButton.setEnabled(true);
        selectBackgroundButton.setAlpha(1.0f);

    }

    private void disableBackgroundImageSelection(View selectBackgroundButton) {
        selectBackgroundButton.setEnabled(false);
        selectBackgroundButton.setAlpha(0.5f);
    }

    private void selectBackgroundImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }

    private void setAppBackground(Uri imageUri) {
        // Take persistable URI permission to ensure access to the image URI
        getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        editor.putString(bcgImgUriKey, imageUri.toString());
        editor.putInt(bcgImgPresetKey, 0);
        editor.putBoolean(isPresetKey, false);
        editor.apply();
        for (ImageButton bcg:bcgs){
            bcg.setImageDrawable(null);
        }
        gob.showToast("Background Image applied");
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
                disableBackgroundImageSelection(selectBackgroundButton);
                gob.showToast("Please allow access to media files to change background");
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

    private void showBuyConfirmationDialog(ImageButton itemBought, int price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_buy_confirmation, null);
        builder.setView(dialogView);

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        Button negativeButton = dialogView.findViewById(R.id.negative_button);
        TextView buyPrice = dialogView.findViewById(R.id.buyItemPriceText);
        buyPrice.setText(String.valueOf(price));
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            itemBought.setTag(itemBought);
            itemBought.setImageDrawable(null);
            int totalCoins = prefs.getInt(coinsKey, 0);
            totalCoins -= price;
            editor.putInt(coinsKey, totalCoins);
            editor.apply();
            dialog.dismiss();
        });
        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void showColorPickerDialog() {
        ColorPickerDialogFragment colorPickerDialog = new ColorPickerDialogFragment();
        colorPickerDialog.setColorPickerListener(this);
        colorPickerDialog.show(getSupportFragmentManager(), "color_picker_dialog");
    }


    public void onColorSelected(int color) {
        selectedColorTextView.setText("Selected Color: #" + Integer.toHexString(color).toUpperCase());
        // Handle the selected color here as needed
    }

}