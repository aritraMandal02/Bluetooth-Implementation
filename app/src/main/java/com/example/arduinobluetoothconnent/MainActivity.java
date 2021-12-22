package com.example.arduinobluetoothconnent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btOn, btOff, btn;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    Intent btEnableIntent;
    int requestCodeForEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btOn = findViewById(R.id.btOn);
        btOff = findViewById(R.id.btOff);
        btn = findViewById(R.id.button3);
        listView = findViewById(R.id.listview);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothOnMethod();
        bluetoothOffMethod();
        exeButton();
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
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