package com.example.myapplication;

import static androidx.activity.result.contract.ActivityResultContracts.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth_ extends AppCompatActivity {

    //    Button btnOn, btnOff;
    public static final int Bluetooth_req_code = 1;
    Button on, off, list, pair;
    ListView lv;
    BluetoothManager BM = getSystemService(BluetoothManager.class);
    BluetoothAdapter BA;
    ActivityResultLauncher ble = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Log.e("Activity result","OK");
            // There are no request codes
            Intent data = result.getData();
        }
    });     

//    private Set<BluetoothDevice> pairedDevices;
//    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        BA = BluetoothAdapter.getDefaultAdapter();
        on = findViewById(R.id.ON);
        off = findViewById(R.id.OFF);
        list = findViewById(R.id.LIST);
        pair = findViewById(R.id.Pair);
        lv = findViewById(R.id.listView);
        BA = BluetoothAdapter.getDefaultAdapter();

        if (BA == null)
            Toast.makeText(Bluetooth_.this, "This Device Does not Support", Toast.LENGTH_LONG).show();
        if (!BA.isEnabled()) {
            on.setText("Turn Blue ON");
        } else {
            on.setText("Turn Blue OFF");
        }
        // In this method we can make make bluetooth on by clicking On_Btn;
    }

    public void on(View v) {
        if (!BA.isEnabled()) {
            Intent BLE = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BLE, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }
}

