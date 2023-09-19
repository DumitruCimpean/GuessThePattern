package com.example.guessthepattern;

import static com.example.guessthepattern.MainActivity.prefsName;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class BuyButton extends androidx.appcompat.widget.AppCompatImageButton {
    private boolean isBought = false;
    private boolean isBoughtLast = false;
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

    public void setBoughtLast(boolean bought) {
        prefs = getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        editor = prefs.edit();

        isBoughtLast = bought;
        editor.putBoolean("button_" + getId() + "_isBought", isBoughtLast);
        editor.apply();

        if (!isBoughtLast){
            setImageResource(R.drawable.lock);
        }else {
            setImageResource(R.drawable.plus);
        }
    }

    public boolean isBoughtLast(){
        return isBoughtLast;
    }

    public boolean isBought() {
        return isBought;
    }
}
