package com.example.guessthepattern;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ColorPickerDialogFragment extends DialogFragment {

    private SeekBar hueSeekBar;
    private SeekBar saturationSeekBar;
    private SeekBar valueSeekBar;
    private View colorPreview;
    private Button okButton;
    private float hue;
    private float saturation;
    private float value;
    private int selectedColor;

    public interface ColorPickerListener {
        void onColorSelected(int color);
    }

    private ColorPickerListener listener;

    public void setColorPickerListener(ColorPickerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_dialog_layout, null);
        builder.setView(view);

        hueSeekBar = view.findViewById(R.id.hueSeekBar);
        saturationSeekBar = view.findViewById(R.id.saturationSeekBar);
        valueSeekBar = view.findViewById(R.id.valueSeekBar);
        colorPreview = view.findViewById(R.id.colorPreview);
        okButton = view.findViewById(R.id.okButton);

        hueSeekBar.setMax(360);
        hueSeekBar.setProgress(0);

        saturationSeekBar.setMax(100);
        saturationSeekBar.setProgress(100);

        valueSeekBar.setMax(100);
        valueSeekBar.setProgress(100);

        // Initialize hue, saturation, and value
        hue = 0.0f;
        saturation = 1.0f;
        value = 1.0f;

        // Initialize selected color based on initial HSV values
        selectedColor = getColorFromHSV(hue, saturation, value);
        updateColorPreview(selectedColor);

        hueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue = (float) progress;
                selectedColor = getColorFromHSV(hue, saturation, value);
                updateColorPreview(selectedColor);
                Drawable customThumb = createCustomThumbDrawable();

                // Set the custom thumb drawable to the SeekBar
                hueSeekBar.setThumb(customThumb);
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
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saturation = (float) progress / 100.0f;
                selectedColor = getColorFromHSV(hue, saturation, value);
                updateColorPreview(selectedColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        valueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = (float) progress / 100.0f;
                selectedColor = getColorFromHSV(hue, saturation, value);
                updateColorPreview(selectedColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onColorSelected(selectedColor);
                }
                dismiss();
            }
        });

        return builder.create();
    }

    private void updateColorPreview(int color) {
        colorPreview.setBackgroundColor(color);
    }

    private int getColorFromHSV(float hue, float saturation, float value) {
        return Color.HSVToColor(new float[]{hue, saturation, value});
    }

    private Drawable createCustomThumbDrawable() {
        int thumbSizeInPixels = 50; // Set the desired thumb size
        int thumbColor = getColorFromHSV(hue, 1.0f, 1.0f); // Calculate thumb color

        // Create a ShapeDrawable for the custom thumb (you can use other Drawable types as well)
        ShapeDrawable thumbDrawable = new ShapeDrawable(new OvalShape());
        thumbDrawable.getPaint().setColor(thumbColor);
        thumbDrawable.setIntrinsicWidth(thumbSizeInPixels);
        thumbDrawable.setIntrinsicHeight(thumbSizeInPixels);

        return thumbDrawable;
    }

}
