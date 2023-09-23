package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.prefsName;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;

public class BuyButton extends androidx.appcompat.widget.AppCompatImageButton {
    private boolean isBought = false;
    private boolean isBoughtPremium = false;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MyGlobals gob = new MyGlobals(getContext());


    public BuyButton(Context context) {
        super(context);
    }

    public BuyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BuyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBought(boolean bought) {
        prefs = getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        editor = prefs.edit();

        isBought = bought;
        editor.putBoolean("button_" + getId() + "_isBought", isBought);
        editor.apply();

        if (!isBought){
            setImageResource(R.drawable.coin);
        }else {
            setImageDrawable(null);
        }
    }

    public void setBoughtPremium(boolean bought, int drawableToSet) {
        prefs = getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        editor = prefs.edit();

        isBoughtPremium = bought;
        editor.putBoolean("button_" + getId() + "_isBought", isBoughtPremium);
        editor.apply();

        if (!isBoughtPremium){
            setImageResource(R.drawable.crown);
        }else {
            setImageResource(drawableToSet);
        }
    }

    public boolean isBoughtPremium(){
        return isBoughtPremium;
    }

    public boolean isBought() {
        return isBought;
    }
}
