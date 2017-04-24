package com.example.sherlock.drumsticks;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ByteChannel;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Handler;

/**
 * Created by sherlock on 24/4/17.
 */


public class bluetoothConnectionUtility extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    public static char inputValue;
    public static int connectionStatus;
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


    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            Log.e("CONNECTED SUccessfULLY",socket.toString());
            connectionStatus = 1;
            // Get the input and output streams; using temp objects because
            // member streams are final.
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
            connectionStatus = 5;
            int numBytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    Log.e("Listening to ports","HELLO");
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    String myInput = new String(mmBuffer);
                    inputValue = myInput.charAt(0);
                    Log.e("inputValue", Character.toString(inputValue));
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
                connectionStatus = 0;
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
