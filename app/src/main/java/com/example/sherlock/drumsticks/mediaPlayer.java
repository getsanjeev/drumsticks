package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sherlock on 24/4/17.
 */

public class mediaPlayer extends AppCompatActivity {

    Button playButton;
    TextView myTextView;
    BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.play_drums);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        super.onCreate(savedInstanceState);
        playButton = (Button)findViewById(R.id.play);
        myTextView = (TextView)findViewById(R.id.message);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new bluetoothConnectionUtility(mBluetoothAdapter).start();
            }
        });
        MediaPlayer mp=new MediaPlayer();

        try{
            mp.setDataSource("storage/sdcard0/SHAREit/audios/papercut.mp3");
            mp.prepare();
            mp.start();
        }
        catch(Exception e){e.printStackTrace();}


    }

}

