package com.example.arduinobluetoothconnent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    Button btOn, btOff, btn, btn2;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    Intent btEnableIntent;
    int requestCodeForEnable;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> stringArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btOn = findViewById(R.id.btOn);
        btOff = findViewById(R.id.btOff);
        btn = findViewById(R.id.button3);
        btn2 = findViewById(R.id.button4);
        listView = findViewById(R.id.listview);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothOnMethod();
        bluetoothOffMethod();
        checkCoarseLocationPermission();
        exeButton();
        getNearbyDevices();
    }

    private boolean checkCoarseLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        }else{
            return true;
        }
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                stringArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void getNearbyDevices() {
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.startDiscovery();
                IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(myReceiver, intentFilter);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
                listView.setAdapter(arrayAdapter);
            }
        });
    }

    private void exeButton() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if(bt.size() > 0){
                    for(BluetoothDevice device:bt){
                        strings[index] = device.getName();
                        index++;
                    }
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeForEnable) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is enabled.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabling canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void bluetoothOnMethod(){
        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(), "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
                }else{
                    if(!bluetoothAdapter.isEnabled()){
                        startActivityForResult(btEnableIntent, requestCodeForEnable);
                    }
                }
            }
        });
    }
    public void bluetoothOffMethod() {
        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.disable();
                }
            }
        });
    }
}