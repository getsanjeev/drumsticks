package com.example.sherlock.drumsticks;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sherlock on 24/4/17.
 */

public class mediaPlayer extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.play_drums);
        super.onCreate(savedInstanceState);
        MediaPlayer mp=new MediaPlayer();
        try{
            mp.setDataSource("storage/sdcard0/SHAREit/audios/papercut.mp3");
            mp.prepare();
            mp.start();
        }
        catch(Exception e){e.printStackTrace();}


    }

}

