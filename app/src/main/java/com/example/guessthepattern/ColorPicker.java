package com.example.guessthepattern;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorPicker {
    private SeekBar hueSeekBar;
    private SeekBar saturationSeekBar;
    private View colorPreview;
    private TextView selectedColorTextView;
    private Activity activity;

    public ColorPicker(Activity activity, int hueSeekBarId, int saturationSeekBarId, int colorPreviewId, int selectedColorTextViewId) {
        this.activity = activity;
        this.hueSeekBar = activity.findViewById(hueSeekBarId);
        this.saturationSeekBar = activity.findViewById(saturationSeekBarId);
        this.colorPreview = activity.findViewById(colorPreviewId);
        this.selectedColorTextView = activity.findViewById(selectedColorTextViewId);

        setupListeners();
    }

    private void setupListeners() {
        hueSeekBar.setMax(360);
        saturationSeekBar.setMax(100);

        hueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int hue, boolean fromUser) {
                updateColor(hue, saturationSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        saturationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int saturation, boolean fromUser) {
                updateColor(hueSeekBar.getProgress(), saturation);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateColor(int hue, int saturation) {
        float[] hsv = {hue, saturation / 100.0f, 1.0f}; // Hue, Saturation, Value
        int color = Color.HSVToColor(hsv);
        colorPreview.setBackgroundColor(color);

        // You can also display the selected color's RGB values or other information
        selectedColorTextView.setText("Selected Color: #" + Integer.toHexString(color).toUpperCase());
    }
}
