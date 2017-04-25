package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Handler;

/**
 * Created by sherlock on 24/4/17.
 */

public class mediaPlayer extends AppCompatActivity {

    Button playButton;
    Button exitButton;
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
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnectivityUtility = new bluetoothConnectionUtility(mBluetoothAdapter);
                mBluetoothConnectivityUtility.start();
                        Log.e("QWERTY","5555");
                        playMusic myMusic = new playMusic();
                        //myMusic.start();
                        Log.e("music.sta","me");
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

    public interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    static void play(char x,soundPool mySoundPoolPLayer){
        if(x == 'c'){
            mySoundPoolPLayer.playShortResource(R.raw.crash);
            mySoundPoolPLayer.playShortResource(R.raw.crash);
            mySoundPoolPLayer.playShortResource(R.raw.crash);
        }
        else if(x == 'h'){
            mySoundPoolPLayer.playShortResource(R.raw.hithat);
            mySoundPoolPLayer.playShortResource(R.raw.hithat);
            mySoundPoolPLayer.playShortResource(R.raw.hithat);
        }
        else if(x =='s'){
            mySoundPoolPLayer.playShortResource(R.raw.snare);
            mySoundPoolPLayer.playShortResource(R.raw.snare);
            mySoundPoolPLayer.playShortResource(R.raw.snare);
        }

    }

    public class playMusic extends Thread{
        soundPool mySoundPoolPLayer = new soundPool(mediaPlayer.this);
        public void run(){
            Log.e("IN AM IN","RUNN");
            while (true){
                Log.e("valueofdata",Character.toString(data.alpha));
                Character myData = data.alpha;
                if(myData == 'c'){
                    mySoundPoolPLayer.playShortResource(R.raw.crash);
                    mySoundPoolPLayer.playShortResource(R.raw.crash);
                    mySoundPoolPLayer.playShortResource(R.raw.crash);
                }
                else if(myData == 'h'){
                    mySoundPoolPLayer.playShortResource(R.raw.hithat);
                    mySoundPoolPLayer.playShortResource(R.raw.hithat);
                    mySoundPoolPLayer.playShortResource(R.raw.hithat);
                }
                else if(myData =='s'){
                    mySoundPoolPLayer.playShortResource(R.raw.snare);
                    mySoundPoolPLayer.playShortResource(R.raw.snare);
                    mySoundPoolPLayer.playShortResource(R.raw.snare);
                }
            }
        }
    }

    public class bluetoothConnectionUtility extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private static final String TAG = "MY_APP_DEBUG_TAG";
        public  char inputValue;
        public int connectionStatus;
        UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private Handler mHandler;

        public bluetoothConnectionUtility(BluetoothAdapter mBluetoothAdapter) {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            inputValue = 'x';
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Drumsticks", DEFAULT_UUID);
                Log.e("listening","rfcommwithservicerecord");
            } catch (IOException e) {
                e.getStackTrace();
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    Log.e("b4","socket accepting");
                    connectionStatus = 0;
                    socket = mmServerSocket.accept();
                    Log.e("waiting to accept",socket.toString());
                } catch (IOException e) {
                    Log.e("in IO",e.toString());
                    e.getStackTrace();
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    Log.e("socket not null","start");
                    connectionStatus = 5;
                    new ConnectedThread(socket).start();
                    try {
                        mmServerSocket.close();
                        Log.e("closed socket","hqwert");
                    }
                    catch (IOException e){
                        e.getStackTrace();
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }





        public class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private byte[] mmBuffer;
            private soundPool mySoundPool = new soundPool(getBaseContext());

            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                Log.e("CONNECTED SUccessfULLY",socket.toString());
                try {
                    tmpIn = socket.getInputStream();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                mmInStream = tmpIn;
            }

            public void run() {
                mmBuffer = new byte[1024];
                int numBytes; // bytes returned from read()
                // Keep listening to the InputStream until an exception occurs.
                while (true) {
                    try {
                        Log.e("Listening to ports","HELLO");
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        String myInput = new String(mmBuffer);
                        data.alpha = myInput.charAt(0);
                        mediaPlayer.play(myInput.charAt(0),mySoundPool);
                        Log.e("inputValue", Character.toString(data.alpha));
                        Log.e("RECIEVED", Integer.toString(numBytes));

                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    }
                }
            }

            // Call this method from the main activity to shut down the connection.
            public void cancel() {
                try {
                    mmSocket.close();
                    Log.e("HERE IT IS CALLE","remove");
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        }
    }

}

