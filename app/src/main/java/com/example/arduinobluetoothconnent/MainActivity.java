package com.example.arduinobluetoothconnent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btOn, btOff;
    BluetoothAdapter bluetoothAdapter;
    Intent btEnableIntent;
    int requestCodeForEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btOn = findViewById(R.id.btOn);
        btOff = findViewById(R.id.btOff);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothOnMethod();
        bluetoothOffMethod();
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