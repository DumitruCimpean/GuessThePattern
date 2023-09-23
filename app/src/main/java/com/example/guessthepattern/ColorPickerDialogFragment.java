package com.example.guessthepattern;
import static com.example.guessthepattern.MainActivity.hexInputKey;
import static com.example.guessthepattern.MainActivity.hueKey;
import static com.example.guessthepattern.MainActivity.prefsName;
import static com.example.guessthepattern.MainActivity.saturationKey;
import static com.example.guessthepattern.MainActivity.valueKey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorPickerDialogFragment extends DialogFragment {

    private SeekBar hueSeekBar;
    private SeekBar saturationSeekBar;
    private SeekBar valueSeekBar;
    private View colorPreview;
    private Button okButton;
    private EditText hexInputBox;
    private TextView hexInputBoxTitle;
    private String hexColor;
    private float hue;
    private float saturation;
    private float value;
    private int selectedColor;
    private float hueSaved;
    private float saturationSaved;
    private float valueSaved;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Handler handler;

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

        prefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        editor = prefs.edit();
        handler = new Handler();

        hexInputBox = view.findViewById(R.id.hexEditText);
        hexInputBoxTitle = view.findViewById(R.id.hexTextTitle);
        hueSeekBar = view.findViewById(R.id.hueSeekBar);
        saturationSeekBar = view.findViewById(R.id.saturationSeekBar);
        valueSeekBar = view.findViewById(R.id.valueSeekBar);
        colorPreview = view.findViewById(R.id.colorPreview);
        okButton = view.findViewById(R.id.okButton);

        hueSaved = prefs.getFloat(hueKey, 0.0f);
        saturationSaved = prefs.getFloat(saturationKey, 1.0f);
        valueSaved = prefs.getFloat(valueKey, 1.0f);

        hueSeekBar.setMax(360);
        hueSeekBar.setProgress((int) hueSaved);

        saturationSeekBar.setMax(100);
        saturationSeekBar.setProgress((int) (saturationSaved * 100));

        valueSeekBar.setMax(100);
        valueSeekBar.setProgress((int) (valueSaved * 100));


        // Initialize hue, saturation, and value
        hue = prefs.getFloat(hueKey, 0.0f);
        saturation = prefs.getFloat(saturationKey, 1.0f);
        value = prefs.getFloat(valueKey, 1.0f);

        // Initialize selected color based on initial HSV values
        selectedColor = getColorFromHSV(hue, saturation, value);
        updateColorPreview(selectedColor);

        hexColor = Integer.toHexString(selectedColor).toUpperCase();
        hexInputBox.setHint("#" + hexColor.substring(2));

        hueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue = (float) progress;
                selectedColor = getColorFromHSV(hue, saturation, value);
                updateColorPreview(selectedColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
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
                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
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
                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
            }
        });

        okButton.setOnClickListener(v -> {
            if (isValidHexCode(hexInputBox.getText().toString()) && hexInputBox.getText() != null){
                editor.putString(hexInputKey, hexInputBox.getText().toString().toUpperCase());
                dismiss();
            }else if (hexInputBox.getText() == null){
                dismiss();
            }
            else{
                hexInputBoxTitle.setText("Not a valid HEX code!");
                hexInputBoxTitle.setTextColor(getResources().getColor(R.color.red));
                handler.postDelayed(() -> {
                    hexInputBoxTitle.setText("Or input your own HEX:");
                    hexInputBoxTitle.setTextColor(getResources().getColor(R.color.white));
                }, 1000);
            }
            editor.putFloat(hueKey, hue);
            editor.putFloat(saturationKey, saturation);
            editor.putFloat(valueKey, value);
            editor.apply();

            if (listener != null) {
                listener.onColorSelected(selectedColor);
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

    public static boolean isValidHexCode(String hexCode) {
        // Define a regular expression pattern for a valid hex color code
        String hexPattern = "^#([A-Fa-f0-9]{6})$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(hexPattern);

        // Match the input string against the pattern
        Matcher matcher = pattern.matcher(hexCode);

        // Return true if the input string matches the pattern, indicating a valid hex code
        return matcher.matches();
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
