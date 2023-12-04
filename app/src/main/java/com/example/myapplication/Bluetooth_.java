package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth_ extends AppCompatActivity {
    private static final String TAG = "bluetooth1";
    Button btnOn, btnOff;
//    Button on, off, list, pair;
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private OutputStream outStream;
    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // MAC-address of Bluetooth module (WeShould edit that in our HJ Module!!)
    private static String address = "00:15:FF:F2:19:5F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        btnOn = (Button) findViewById(R.id.btnOn);
        btnOff = (Button) findViewById(R.id.btnOff);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        btnOn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                sendData("1");
                Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendData("0");
                Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
            }
        });
//        on = findViewById(R.id.ON);
//        off = findViewById(R.id.OFF);
//        list = findViewById(R.id.LIST);
//        pair = findViewById(R.id.Pair);
//        lv = (ListView)findViewById(R.id.listView);
//        BA = BluetoothAdapter.getDefaultAdapter();
    }
}
