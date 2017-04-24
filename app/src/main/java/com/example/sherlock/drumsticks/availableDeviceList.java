package com.example.sherlock.drumsticks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Set;
import java.util.Vector;

public class availableDeviceList extends AppCompatActivity {

    Button searchAllDevices;
    Button createServer;
    ListView lv;
    BluetoothAdapter mBluetoothAdapter;
    Bitmap symbolBluetooth;
    Vector<String> searchListResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_available_device_list);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        searchListResults = new Vector<>();
        lv = (ListView) findViewById(R.id.listView_in_cominfo);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String lv.getAdapter().getItem(position);
            }
        });
        searchAllDevices = (Button) findViewById(R.id.search_devices);
        createServer = (Button) findViewById(R.id.create_server);
        createServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLicked create server","HEYYY");
                new bluetoothConnectionUtility(mBluetoothAdapter).start();
            }
        });
        symbolBluetooth = BitmapFactory.decodeResource(getResources(), R.drawable.bluetooth);
        showCurrentDevices();
        searchAllDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);
                showAllDevices();
            }
        });
    }


    private void showAllDevices() {
        String[] listResult = new String[searchListResults.size()];
        int i = 0;
        for (String item : searchListResults) {
            listResult[i] = item;
            i++;
        }
        customAdapter myAdapter = new customAdapter(this, listResult, symbolBluetooth);
        lv.setAdapter(myAdapter);
    }


    private void showCurrentDevices() {
        String[] savedDevices = querySavedDevice();
        customAdapter myAdapter = new customAdapter(this, savedDevices, symbolBluetooth);
        lv.setAdapter(myAdapter);
    }

    private String[] querySavedDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        String[] savedDevices = new String[pairedDevices.size()];
        int i = 0;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                savedDevices[i] = device.getName() + " " + device.getAddress();
                i++;
            }
        }
        return savedDevices;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName() + " " + device.getAddress();
                searchListResults.add(deviceName);
            }

        }
    };
}
