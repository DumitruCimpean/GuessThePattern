package com.example.guessthepattern;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class RgbSeekbar extends androidx.appcompat.widget.AppCompatSeekBar {
    private int selectedTypeDrawable;

    public RgbSeekbar(@NonNull Context context) {
        super(context);
        init();
    }

    public RgbSeekbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RgbSeekbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable nullDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.null_shape, getContext().getTheme());
        Drawable thumb = ResourcesCompat.getDrawable(getResources(), R.drawable.hue_thumb, getContext().getTheme());
        setProgressDrawable(nullDrawable);
        setBackgroundResource(selectedTypeDrawable);
        setThumb(thumb);

    }

    public void setSeekbarType(String Hue_Saturation_Value){
        switch (Hue_Saturation_Value) {
            case "hue":
                selectedTypeDrawable = R.drawable.hue_seekbar_progress;
                setBackgroundResource(selectedTypeDrawable);
                break;
            case "saturation":
                selectedTypeDrawable = R.drawable.saturation_seekbar_progress;
                setBackgroundResource(selectedTypeDrawable);
                break;
            case "value":
                selectedTypeDrawable = R.drawable.value_seekbar_progress;
                setBackgroundResource(selectedTypeDrawable);
                break;
            default:
                selectedTypeDrawable = R.drawable.null_shape;
                setBackgroundResource(selectedTypeDrawable);
                break;
        }
    }



    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
