package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    MediaPlayer mp;
    TextView myTextView;
    BluetoothAdapter mBluetoothAdapter;
    bluetoothConnectionUtility mBluetoothConnectivityUtility;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.play_drums);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        super.onCreate(savedInstanceState);
        playButton = (Button)findViewById(R.id.play);
        exitButton = (Button)findViewById(R.id.exit);
        myTextView = (TextView)findViewById(R.id.message);
        final playMusic myMusic = new playMusic();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnectivityUtility = new bluetoothConnectionUtility(mBluetoothAdapter);
                mBluetoothConnectivityUtility.start();
                synchronized (mBluetoothConnectivityUtility){
                    try {
                        mBluetoothConnectivityUtility.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                myMusic.start();
            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked on","exitButton");
                mBluetoothConnectivityUtility.cancel();
                mBluetoothAdapter.disable();
                startActivity(new Intent(mediaPlayer.this,blueToothConnection.class));
                finish();
            }
        });

    }

    private class playMusic extends Thread{
        soundPool mySoundPoolPLayer = new soundPool(mediaPlayer.this);
        public void run(){
            while (true){
                Log.e("valueofmyinput",Integer.toString(bluetoothConnectionUtility.inputValue));

                if(bluetoothConnectionUtility.inputValue == 'c'){
                    mySoundPoolPLayer.playShortResource(R.raw.crash);
                }
                else if(bluetoothConnectionUtility.inputValue == 'h'){
                    mySoundPoolPLayer.playShortResource(R.raw.hithat);
                }
                else if(bluetoothConnectionUtility.inputValue =='s'){
                    mySoundPoolPLayer.playShortResource(R.raw.snare);
                }
            }
        }
    }

}

