package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.bcgImgPresetKey;
import static com.example.guessthepattern.MainActivity.bcgImgUriKey;
import static com.example.guessthepattern.MainActivity.coinsKey;
import static com.example.guessthepattern.MainActivity.isPresetKey;
import static com.example.guessthepattern.MainActivity.isColorFromPicker;
import static com.example.guessthepattern.MainActivity.sqColorPickedKey;

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
import android.content.res.ColorStateList;
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


    private Drawable checkmark;
    private Drawable checkmarkDark;
    private boolean numberedBool;
    private int musicVolumeChosen;
    private int sfxVolumeChosen;
    private ColorPicker colorPicker;
    private BuyButton sq1bcg;
    private BuyButton sq2bcg;
    private BuyButton sq3bcg;
    private BuyButton sq4bcg;
    private BuyButton sq5bcg;
    private BuyButton sq6bcg;
    private BuyButton sq7bcg;
    private BuyButton bcg1;
    private BuyButton bcg2;
    private BuyButton bcg3;
    private BuyButton bcg4;
    private BuyButton bcg5;
    private BuyButton bcg6;
    private BuyButton bcg7;
    private TextView coinAmountText;
    private int totalCoins;

    private HorizontalScrollView sqScrollView;
    private HorizontalScrollView bcgSelectionScrollView;

    private BuyButton colorPickerButton;
    private TextView selectedColorTextView;
    private int sq1Id;
    private int sq2Id;
    private int sq3Id;
    private int sq4Id;
    private int sq5Id;
    private int sq6Id;
    private int sq7Id;

    private int bcg1id;
    private int bcg2id;
    private int bcg3id;
    private int bcg4id;
    private int bcg5id;
    private int bcg6id;
    private int bcg7id;

    private BuyButton selectBackgroundButton;
    private BuyButton[] squares;
    private BuyButton[] bcgs;
    private Resources resources;
    private MyGlobals gob;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Handler handler;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    private MediaPlayer levelEnter;
    private MediaPlayer buySound;
    private static final int REQUEST_CODE_IMAGE_PICK = 123;
    private static final int REQUEST_CODE_PERMISSION = 456;
    private int bcgPriceGradient;
    private int bcgPriceImage;
    private int sqColorPrice;
    private Uri selectedImageUri;
    private int premiumDrawable;

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

        bcgPriceGradient = 20;
        bcgPriceImage = 30;
        sqColorPrice = 10;

        ImageButton back = findViewById(R.id.backButton);
        ImageButton numberedBtn = findViewById(R.id.numberedCheckbox);
        TextView musicText = findViewById(R.id.musicVolumeText);
        SeekBar musicSeek = findViewById(R.id.musicSeekBar);
        TextView sfxText = findViewById(R.id.sfxVolumeText);
        SeekBar sfxSeek = findViewById(R.id.sfxSeekBar);

        colorPickerButton = findViewById(R.id.colorPickerButton);
        selectedColorTextView = findViewById(R.id.selectedColorText);


        selectBackgroundButton = findViewById(R.id.selectBackgroundButton);
        bcgSelectionScrollView = findViewById(R.id.bcgSelectionScrollView);
        sqScrollView = findViewById(R.id.sqColorScrollView);
        coinAmountText = findViewById(R.id.coinAmount);
        sq1bcg = findViewById(R.id.sq1);
        sq2bcg = findViewById(R.id.sq2);
        sq3bcg = findViewById(R.id.sq3);
        sq4bcg = findViewById(R.id.sq4);
        sq5bcg = findViewById(R.id.sq5);
        sq6bcg = findViewById(R.id.sq6);
        sq7bcg = findViewById(R.id.sq7);
        squares = new BuyButton[]{sq1bcg, sq2bcg, sq3bcg, sq4bcg, sq5bcg, sq6bcg, sq7bcg};

        sq1Id = R.drawable.sq_bcg_blue_lc;
        sq2Id = R.drawable.sq_bcg_white_dc;
        sq3Id = R.drawable.sq_bcg_yellow_dc;
        sq4Id = R.drawable.sq_bcg_purple_lc;
        sq5Id = R.drawable.sq_bcg_peach;
        sq6Id = R.drawable.sq_bcg_brown;
        sq7Id = R.drawable.sq_bcg_turquoise;

        bcg1 = findViewById(R.id.bcg1);
        bcg2 = findViewById(R.id.bcg2);
        bcg3 = findViewById(R.id.bcg3);
        bcg4 = findViewById(R.id.bcg4);
        bcg5 = findViewById(R.id.bcg5);
        bcg6 = findViewById(R.id.bcg6);
        bcg7 = findViewById(R.id.bcg7);
        bcgs = new BuyButton[]{bcg1, bcg2, bcg3, bcg4, bcg5, bcg6, bcg7};

        for (BuyButton square:squares){
            square.setBought(prefs.getBoolean("button_" + square.getId() + "_isBought", false));
        }
        sq1bcg.setBought(true);

        for (BuyButton bcg:bcgs){
            bcg.setBought(prefs.getBoolean("button_" + bcg.getId() + "_isBought", false));
        }
        bcg1.setBought(true);
        selectBackgroundButton.setBoughtPremium(prefs.getBoolean("button_" + selectBackgroundButton.getId() + "_isBought", false), R.drawable.image_gallery);

        int sqColorPicked2 = prefs.getInt(sqColorPickedKey, 0);
        colorPickerButton.setBoughtPremium(prefs.getBoolean("button_" + colorPickerButton.getId() + "_isBought", false), R.drawable.color_picker);
        if (colorPickerButton.isBoughtPremium()){
            colorPickerButton.setImageTintList(ColorStateList.valueOf(sqColorPicked2));
        }

        bcg1id = R.drawable.bcg_grey_100;
        bcg2id = R.drawable.bcg_red_blue;
        bcg3id = R.drawable.bcg_turquoise_blue;
        bcg4id = R.drawable.bcg_yellow_orange;
        bcg5id = R.drawable.bcg_peach_blue;
        bcg6id = R.drawable.bcg_magenta_orange;
        bcg7id = R.drawable.bcg_turquoise_magenta;

        levelEnter = MediaPlayer.create(this, R.raw.level_clicked);
        buySound = MediaPlayer.create(this, R.raw.spent_coins);

        checkmark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark, getTheme());
        checkmarkDark = ResourcesCompat.getDrawable(resources, R.drawable.checkmark_darkgrey, getTheme());

        totalCoins = prefs.getInt(coinsKey, 0);
        coinAmountText.setText(String.valueOf(totalCoins));

        // Numbered checkbox resize so it stays square
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int displayHeight = metrics.heightPixels;
        int numLayoutHeight = displayHeight / 10;

        numberedBtn.getLayoutParams().width = numLayoutHeight / 2;
        numberedBtn.getLayoutParams().height = numLayoutHeight / 2;

        // -------------------------------- Restoring settings ------------------------------------ //

        int selectedSqBcg = prefs.getInt(bcgKey, R.drawable.sq_bcg_blue_lc);

        if (selectedSqBcg == 0){
            for (BuyButton square : squares){
                if (square.isBought()){
                    square.setImageDrawable(null);
                }
            }
            sqScrollView.post(() -> {
                int scrollX = colorPickerButton.getLeft() - (sqScrollView.getWidth() - sq1bcg.getWidth()) / 2;
                sqScrollView.smoothScrollTo(scrollX, 0);
            });
        }
        if (selectedSqBcg == sq1Id){
            checkAndScrollToSquare(sq1bcg);
        }
        if (selectedSqBcg == sq2Id){
            checkAndScrollToSquare(sq2bcg);
        }
        if (selectedSqBcg == sq3Id){
            checkAndScrollToSquare(sq3bcg);
        }
        if (selectedSqBcg == sq4Id){
            checkAndScrollToSquare(sq4bcg);
        }
        if (selectedSqBcg == sq5Id){
            checkAndScrollToSquare(sq5bcg);
        }
        if (selectedSqBcg == sq6Id){
            checkAndScrollToSquare(sq6bcg);
        }
        if (selectedSqBcg == sq7Id){
            checkAndScrollToSquare(sq7bcg);
        }


        int selectedBcg = prefs.getInt(bcgImgPresetKey, R.drawable.bcg_grey_100);

        if (selectedBcg == 0){
            for (BuyButton bcg : bcgs){
                if (bcg.isBought()){
                    bcg.setImageDrawable(null);
                }
            }
            bcgSelectionScrollView.post(() -> {
                int scrollX = selectBackgroundButton.getLeft() - (bcgSelectionScrollView.getWidth() - bcg1.getWidth()) / 2;
                bcgSelectionScrollView.smoothScrollTo(scrollX, 0);
            });
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

        // ---------------------------------- Buttons --------------------------------------------- //

        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            handler.postDelayed(this::finish, 100);
        });

        selectBackgroundButton.setOnClickListener(v -> {
            gob.clickEffectResize(selectBackgroundButton, this);

            if (selectBackgroundButton.isBoughtPremium()){

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

            }else{
                if (totalCoins >= bcgPriceImage){
                    premiumDrawable = R.drawable.image_gallery;
                    showBuyConfirmationDialog(selectBackgroundButton, bcgPriceImage, true);
                }else{
                    showNotEnoughCoins(bcgPriceImage);
                }
            }

        });

        colorPickerButton.setOnClickListener(v -> {
            gob.clickEffectResize(colorPickerButton, this);
            if (colorPickerButton.isBoughtPremium()) {
                showColorPickerDialog();
            }else {
                if (totalCoins >= bcgPriceImage){
                    premiumDrawable = R.drawable.color_picker;
                    showBuyConfirmationDialog(colorPickerButton, bcgPriceImage, true);
                }else{
                    showNotEnoughCoins(bcgPriceImage);
                }
            }
        });


        // Squares color ---------------------------------------------------------------------------

        sq1bcg.setOnClickListener(v -> sqCheckRoutine(sq1bcg, sq1Id));
        sq2bcg.setOnClickListener(v -> sqCheckRoutine(sq2bcg, sq2Id));
        sq3bcg.setOnClickListener(v -> sqCheckRoutine(sq3bcg, sq3Id));
        sq4bcg.setOnClickListener(v -> sqCheckRoutine(sq4bcg, sq4Id));
        sq5bcg.setOnClickListener(v -> sqCheckRoutine(sq5bcg, sq5Id));
        sq6bcg.setOnClickListener(v -> sqCheckRoutine(sq6bcg, sq6Id));
        sq7bcg.setOnClickListener(v -> sqCheckRoutine(sq7bcg, sq7Id));

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

    public void sqCheckRoutine(BuyButton sqColor, int sqColorID){
        gob.clickEffectResize(sqColor, this);
        if (!sqColor.isBought()){
            if (totalCoins >= sqColorPrice){
                showBuyConfirmationDialog(sqColor, sqColorPrice, false);
            }else{
                showNotEnoughCoins(sqColorPrice);
            }
        }else {
            checkAndScrollToSquare(sqColor);
            editor.putInt(bcgKey, sqColorID);
            editor.putBoolean(isColorFromPicker, false);
            editor.apply();
        }
    }

    public void bcgCheckRoutine(BuyButton bcgColor, int bcgColorID){
        gob.clickEffectResize(bcgColor, this);
        if (!bcgColor.isBought()){
            if (totalCoins >= bcgPriceGradient){
                showBuyConfirmationDialog(bcgColor, bcgPriceGradient, false);
            }else{
                showNotEnoughCoins(bcgPriceGradient);
            }
        }else {
            checkAndScrollToBackground(bcgColor);
            editor.putInt(bcgImgPresetKey, bcgColorID);
            editor.putBoolean(isPresetKey, true);
            editor.apply();
        }
    }

    public void checkAndScrollToSquare(BuyButton sqToCheck){
        for (BuyButton square : squares){
            if (square.isBought()){
                square.setImageDrawable(null);
            }
        }
        sqToCheck.setImageDrawable(checkmarkDark);

        sqScrollView.post(() -> {
            int scrollX = sqToCheck.getLeft() - (sqScrollView.getWidth() - sq1bcg.getWidth()) / 2;
            sqScrollView.smoothScrollTo(scrollX, 0);
        });
    }

    public void checkAndScrollToBackground(BuyButton bcgToCheck){
        for (BuyButton bcg : bcgs){
            if (bcg.isBought()){
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
        for (BuyButton bcg:bcgs){
            if (bcg.isBought()){
                bcg.setImageDrawable(null);
            }
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
                // Permission was granted
                enableBackgroundImageSelection(selectBackgroundButton);
                gob.showToast("Click on the button again");
            } else {
                // Permission was denied
                disableBackgroundImageSelection(selectBackgroundButton);
                gob.showToast("Please allow access to media files to change background");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buySound != null){
            buySound.release();
            buySound = null;
        }
        if (levelEnter != null){
            levelEnter.release();
            levelEnter = null;
        }
    }

    private void showBuyConfirmationDialog(BuyButton itemBought, int price, boolean isPremium) {
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
            if (totalCoins >= price){
                gob.clickEffectResize(positiveButton, getApplicationContext());
                if (isPremium){
                    itemBought.setBoughtPremium(true, premiumDrawable);
                    if (buySound != null){
                        buySound.seekTo(0);
                        buySound.start();
                    }
                }else{
                    if (buySound != null){
                        buySound.seekTo(0);
                        buySound.start();
                    }
                    itemBought.setBought(true);
                }
                totalCoins -= price;
                coinAmountText.setText(String.valueOf(totalCoins));
                editor.putInt(coinsKey, totalCoins);
                editor.apply();
            }else{
                showNotEnoughCoins(bcgPriceGradient);
            }
            handler.postDelayed(dialog::dismiss, 200);
        });
        negativeButton.setOnClickListener(v -> {
            gob.clickEffectResize(negativeButton, getApplicationContext());
            handler.postDelayed(dialog::dismiss, 200);
        });

        dialog.show();

    }

    private void showNotEnoughCoins(int item_price){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.not_enough_money_dialog, null);
        builder.setView(dialogView);

        TextView buyPrice = dialogView.findViewById(R.id.buyItemPriceText);
        buyPrice.setText(String.valueOf(item_price));
        AlertDialog dialog = builder.create();

        Button positiveButton = dialogView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v->{
            gob.clickEffectResize(positiveButton, getApplicationContext());
            handler.postDelayed(dialog::dismiss, 200);
        });
        dialog.show();
    }

    private void showColorPickerDialog() {
        ColorPickerDialogFragment colorPickerDialog = new ColorPickerDialogFragment();
        colorPickerDialog.setColorPickerListener(this);
        colorPickerDialog.show(getSupportFragmentManager(), "color_picker_dialog");
    }


    public void onColorSelected(int color) {
        colorPickerButton.setImageTintList(ColorStateList.valueOf(color));
        editor.putBoolean(isColorFromPicker, true);
        editor.putInt(sqColorPickedKey, color);
        for (BuyButton square:squares){
            if (square.isBought()){
                square.setImageDrawable(null);
            }
        }
        editor.apply();
    }

}