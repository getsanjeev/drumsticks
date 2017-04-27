package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;


 public class mediaPLayerArduino extends AppCompatActivity {
    Button playButton;
    Button exitButton;
    TextView myTextView;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket bSocket;
    private static UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:15:FF:F2:19:5F";
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
                Set bondedDevices =mBluetoothAdapter.getBondedDevices();
                if(bondedDevices.isEmpty()){
                    Toast.makeText(mediaPLayerArduino.this, "Please connect to device first", Toast.LENGTH_SHORT).show();
                }
                else {
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    try{
                        bSocket = createBluetoothSocket(device);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    mBluetoothAdapter.cancelDiscovery();
                    try{
                        Log.e("Strted","CONNECT SOME DEVICE");
                        bSocket.connect();
                        new ConnectedThread(bSocket).start();
                        Log.e("started","connected thread");
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        try{bSocket.close();}
                        catch (IOException e1){e1.printStackTrace();}
                    }
                }

            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clicked on","exitButton");
                mBluetoothAdapter.disable();
                startActivity(new Intent(mediaPLayerArduino.this,blueToothConnection.class));
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

     private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
         if(Build.VERSION.SDK_INT >= 10){
             try {
                 final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                 return (BluetoothSocket) m.invoke(device,DEFAULT_UUID);
             } catch (Exception e) {
                 Log.e("SORRY", "Could not create Insecure RFComm Connection",e);
             }
         }
         return  device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
     }


        private class ConnectedThread extends Thread {
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
                int numBytes;// bytes returned from read()
                // Keep listening to the InputStream until an exception occurs.
                while (true) {
                    try {
                        Log.e("Listening to ports","HELLO");
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        String myInput = new String(mmBuffer);
                        data.alpha = myInput.charAt(0);
                        mediaPLayerArduino.play(myInput.charAt(0),mySoundPool);
                        Log.e("inputValue", Character.toString(data.alpha));
                        Log.e("Received", Integer.toString(numBytes));

                    } catch (IOException e) {
                        Log.e("IN exception", "Input stream was disconnected", e);
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
                    Log.e("HELLO", "Could not close the connect socket", e);
                }
            }
        }


    }



