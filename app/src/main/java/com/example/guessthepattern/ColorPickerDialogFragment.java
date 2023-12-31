package com.example.guessthepattern;
import static com.example.guessthepattern.MainActivity.sqBcgKey;
import static com.example.guessthepattern.MainActivity.hexInputKey;
import static com.example.guessthepattern.MainActivity.hueKey;
import static com.example.guessthepattern.MainActivity.prefsName;
import static com.example.guessthepattern.MainActivity.saturationKey;
import static com.example.guessthepattern.MainActivity.valueKey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorPickerDialogFragment extends DialogFragment {

    private BuyButton sq1bcg;
    private BuyButton sq2bcg;
    private BuyButton sq3bcg;
    private BuyButton sq4bcg;
    private BuyButton sq5bcg;
    private BuyButton sq6bcg;
    private BuyButton sq7bcg;
    private BuyButton[] squares;
    private RgbSeekbar hueSeekBar;
    private RgbSeekbar saturationSeekBar;
    private RgbSeekbar valueSeekBar;
    private ImageView saturationSeekBarBcg;
    private ImageView valueSeekBarBcg;
    private View colorPreview;
    private Button okButton;
    private ImageButton hexSaveBtn;
    private EditText hexInputBox;
    private TextView hexInputBoxTitle;
    private String hexColor;
    private float hue;
    private float saturation;
    private float value;
    private int selectedColor;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MyGlobals gob;
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
        View settingsView = inflater.inflate(R.layout.activity_settings, null);
        builder.setView(view);

        prefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        editor = prefs.edit();
        handler = new Handler();
        gob = new MyGlobals(getContext());

        hexInputBox = view.findViewById(R.id.hexEditText);
        hexInputBoxTitle = view.findViewById(R.id.hexTextTitle);
        hexSaveBtn = view.findViewById(R.id.hexSaveButton);

        hueSeekBar = view.findViewById(R.id.hueSeekBar);
        saturationSeekBar = view.findViewById(R.id.saturationSeekBar);
        valueSeekBar = view.findViewById(R.id.valueSeekBar);

        saturationSeekBarBcg = view.findViewById(R.id.saturationSeekBarBcg);
        valueSeekBarBcg = view.findViewById(R.id.valueSeekBarBcg);

        colorPreview = view.findViewById(R.id.colorPreview);
        okButton = view.findViewById(R.id.okButton);
        sq1bcg = settingsView.findViewById(R.id.sq1);
        sq2bcg = settingsView.findViewById(R.id.sq2);
        sq3bcg = settingsView.findViewById(R.id.sq3);
        sq4bcg = settingsView.findViewById(R.id.sq4);
        sq5bcg = settingsView.findViewById(R.id.sq5);
        sq6bcg = settingsView.findViewById(R.id.sq6);
        sq7bcg = settingsView.findViewById(R.id.sq7);
        squares = new BuyButton[]{sq1bcg, sq2bcg, sq3bcg, sq4bcg, sq5bcg, sq6bcg, sq7bcg};

        hueSeekBar.setSeekbarType("hue");
        saturationSeekBar.setSeekbarType("saturation");
        valueSeekBar.setSeekbarType("value");

        hexInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 6){
                    hexSaveBtn.setVisibility(View.VISIBLE);
                }else {
                    hexSaveBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Initialize hue, saturation, and value
        hue = prefs.getFloat(hueKey, 0.0f);
        saturation = prefs.getFloat(saturationKey, 1.0f);
        value = prefs.getFloat(valueKey, 1.0f);

        hueSeekBar.setMax(360);
        hueSeekBar.setProgress((int) hue);


        saturationSeekBar.setMax(100);
        saturationSeekBar.setProgress((int) (saturation * 100));

        valueSeekBar.setMax(100);
        valueSeekBar.setProgress((int) (value * 100));

        // Initialize selected color based on initial HSV values
        selectedColor = getColorFromHSV(hue, saturation, value);
        updateColorPreview(selectedColor);

        hexColor = Integer.toHexString(selectedColor).toUpperCase();
        hexInputBox.setHint("#" + hexColor.substring(2));

        saturationSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, 1.0f, value));
        valueSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, saturation, 1.0f));

        hueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue = (float) progress;
                selectedColor = getColorFromHSV(hue, saturation, value);
                updateColorPreview(selectedColor);

                saturationSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, 1.0f, value));
                valueSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, saturation, 1.0f));

                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
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

                saturationSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, 1.0f, value));
                valueSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, saturation, 1.0f));

                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
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

                saturationSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, 1.0f, value));
                valueSeekBarBcg.setBackgroundColor(getColorFromHSV(hue, saturation, 1.0f));

                hexColor = Integer.toHexString(selectedColor).toUpperCase();
                hexInputBox.setHint("#" + hexColor.substring(2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hexSaveBtn.setOnClickListener(v -> {
            gob.clickEffectResize(hexSaveBtn, getContext());
            String hexInputString = hexInputBox.getText().toString().toUpperCase();
            if (isValidHexCode(hexInputString) && !TextUtils.isEmpty(hexInputString)){
                editor.putString(hexInputKey, hexInputString);
                int colorToConvert = Color.parseColor(hexInputString);

                int red = Color.red(colorToConvert);
                int green = Color.green(colorToConvert);
                int blue = Color.blue(colorToConvert);

                float[] hsv = new float[3];
                Color.RGBToHSV(red, green, blue, hsv);

                hueSeekBar.setProgress((int) hsv[0]);
                saturationSeekBar.setProgress((int) (hsv[1] * 100));
                valueSeekBar.setProgress((int) (hsv[2] * 100));
                updateColorPreview(colorToConvert);
                selectedColor = colorToConvert;

                handler.postDelayed(()->hexSaveBtn.setVisibility(View.GONE), 200);
            }
            else{
                hexInputBoxTitle.setText("Not a valid HEX code!");
                hexInputBoxTitle.setTextColor(getResources().getColor(R.color.red));
                handler.postDelayed(() -> {
                    hexInputBoxTitle.setText("Or input your own HEX:");
                    hexInputBoxTitle.setTextColor(getResources().getColor(R.color.white));
                }, 2000);
            }
        });

        okButton.setOnClickListener(v -> {
            editor.putFloat(hueKey, hue);
            editor.putFloat(saturationKey, saturation);
            editor.putFloat(valueKey, value);
            editor.putInt(sqBcgKey, 0);
            editor.apply();

            if (listener != null) {
                listener.onColorSelected(selectedColor);
            }


            dismiss();
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
