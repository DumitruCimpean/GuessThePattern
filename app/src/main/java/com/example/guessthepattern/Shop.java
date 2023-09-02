package com.example.guessthepattern;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
public class Shop extends AppCompatActivity {
    private static boolean shouldPlay;
    MediaPlayer themeSong = ThemeSongSingleton.getThemeSong();
    MediaPlayer coinSfx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        MyGlobals gob = new MyGlobals(this);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        final int[] coins = {prefs.getInt("coinsKey", 0)};
        final int[] revivesPrice = {30};
        final int[] revealsPrice = {20};
        TextView totalCoins = findViewById(R.id.coinAmount);
        totalCoins.setText(String.valueOf(coins[0]));

        Button buyReveal = findViewById(R.id.buyReveal);
        Button buyRevive = findViewById(R.id.buyRevive);
        coinSfx = MediaPlayer.create(this, R.raw.spent_coins);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            gob.clickEffectResize(back, this);
            shouldPlay = true;
            finish();
        });


        TextView revivePriceText = findViewById(R.id.priceAmountRevive);
        revivePriceText.setText(String.valueOf(revivesPrice[0]));
        TextView revivesCount = findViewById(R.id.ownedRevives);
        final int[] revives = {prefs.getInt("revivesKey", 0)};
        revivesCount.setText("Owned: " + revives[0]);
        buyRevive.setOnClickListener(view -> {
            if (coins[0] >= revivesPrice[0]) {
                gob.clickEffectResize(buyRevive, this);
                coinSfx.seekTo(0);
                coinSfx.start();
                revives[0]++;
                coins[0] -= revivesPrice[0];
                if(coins[0] < revivesPrice[0]){
                    buyRevive.setAlpha(0.5f);
                    buyRevive.setClickable(false);
                }
                if(coins[0] < revealsPrice[0]){
                    buyReveal.setAlpha(0.5f);
                    buyReveal.setClickable(false);
                }
                totalCoins.setText(String.valueOf(coins[0]));
                editor.putInt("revivesKey", revives[0]);
                editor.putInt("coinsKey", coins[0]);
                editor.apply();
                revivesCount.setText("Owned: " + revives[0]);
            }else{
                buyRevive.setAlpha(0.5f);
                buyRevive.setClickable(false);
            }
        });

        TextView revealPriceText = findViewById(R.id.priceAmountReveal);
        revealPriceText.setText(String.valueOf(revealsPrice[0]));
        TextView revealsCount = findViewById(R.id.ownedReveals);
        final int[] reveals = {prefs.getInt("revealsKey", 0)};
        revealsCount.setText("Owned: " + reveals[0]);
        buyReveal.setOnClickListener(view -> {
            if (coins[0] >= revealsPrice[0]) {
                gob.clickEffectResize(buyReveal, this);
                coinSfx.seekTo(0);
                coinSfx.start();
                reveals[0]++;
                coins[0] -= revealsPrice[0];
                if(coins[0] < revealsPrice[0]){
                    buyReveal.setAlpha(0.5f);
                    buyReveal.setClickable(false);
                }
                if(coins[0] < revivesPrice[0]){
                    buyRevive.setAlpha(0.5f);
                    buyRevive.setClickable(false);
                }
                totalCoins.setText(String.valueOf(coins[0]));
                editor.putInt("revealsKey", reveals[0]);
                editor.putInt("coinsKey", coins[0]);
                editor.apply();
                revealsCount.setText("Owned: " + reveals[0]);
            }else{
                buyReveal.setAlpha(0.5f);
                buyReveal.setClickable(false);
            }
        });


        if (coins[0] < revivesPrice[0]){
            buyRevive.setAlpha(0.5f);
            buyRevive.setClickable(false);
        }else{
            buyRevive.setAlpha(1);
            buyRevive.setClickable(true);
        }

        if (coins[0] < revealsPrice[0]){
            buyReveal.setAlpha(0.5f);
            buyReveal.setClickable(false);
        }else{
            buyReveal.setAlpha(1);
            buyReveal.setClickable(true);
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
        if (coinSfx != null){
            coinSfx.release();
            coinSfx = null;
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