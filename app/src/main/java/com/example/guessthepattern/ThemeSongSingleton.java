package com.example.guessthepattern;

import android.media.MediaPlayer;

public class ThemeSongSingleton {
    private static MediaPlayer themeSong;
    private ThemeSongSingleton(){
    }
    public static MediaPlayer getThemeSong(){
        if (themeSong == null){
            themeSong = new MediaPlayer();
        }
        return themeSong;
    }
}
