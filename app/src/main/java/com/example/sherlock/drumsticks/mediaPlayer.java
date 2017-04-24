package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by sherlock on 24/4/17.
 */

public class mediaPlayer extends AppCompatActivity {

    Button playButton;
    Button exitButton;
    TextView myTextView;
    BluetoothAdapter mBluetoothAdapter;
    int loopController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.play_drums);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        super.onCreate(savedInstanceState);
        playButton = (Button)findViewById(R.id.play);
        exitButton = (Button)findViewById(R.id.exit);
        myTextView = (TextView)findViewById(R.id.message);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new bluetoothConnectionUtility(mBluetoothAdapter).start();
                MediaPlayer mp =new MediaPlayer();
                try{
                    loopController = 1;
                    while (loopController==1){
                        if(bluetoothConnectionUtility.inputValue == 'c'){
                            mp.create(getBaseContext(),R.raw.crash);
                            myTextView.setText("c");
                            mp.start();
                        }
                        else if(bluetoothConnectionUtility.inputValue == 'h'){
                            mp.create(getBaseContext(),R.raw.hithat);
                            myTextView.setText("h");
                            mp.start();
                        }
                        else {
                            mp.create(getBaseContext(),R.raw.snare);
                            myTextView.setText("s");
                            mp.start();
                        }
                    }
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

}

