package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by sherlock on 24/4/17.
 */

public class blueToothConnection extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT = 3;
    Button gotIt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        gotIt = (Button)findViewById(R.id.gotit);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Your Device does not support Bluetooth Connectivity", Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOnBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnBluetooth, REQUEST_ENABLE_BT);
        }

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(blueToothConnection.this,availableDeviceList.class));
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth successfully turned ON", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Turn On Bluetooth to play drumsticks", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
