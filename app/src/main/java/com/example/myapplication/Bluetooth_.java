package com.example.myapplication;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Bluetooth_ extends AppCompatActivity {
    private BluetoothAdapter BA = null;
    private static String device_name;
    private static String device_address;
    private static final UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //We declare a default UUID to create the global variable
    private static String address = "48:23:35:F4:00:17";
    private static final String TAG = "bluetooth1";
    private static final int REQUEST_ENABLE_BT = 1;
    Button mOnBtn;
    Button mOffBtn;
    Button mPairedBtn;
    Button mVisBtn;
    Button m1;
    Button m2;
    TextView t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        m1=findViewById(R.id.btnOn);
        m2=findViewById(R.id.btnOff);
        mOnBtn = findViewById(R.id.ON);
        mOffBtn = findViewById(R.id.OFF);
        mPairedBtn = findViewById(R.id.Pair);
        BA = BluetoothAdapter.getDefaultAdapter();
        t1 = findViewById(R.id.textView17);
        t2 = findViewById(R.id.textView18);
        t3 = findViewById(R.id.textView19);
        checkBTState();
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (device_address == "48:23:35:F4:00:17") {
                    t1.setText("Thats Right!");
                } else {
                    t1.setText(device_name);
                }
            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPaired_device();
            }
        });


        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write("1");
                showToast("Turn LED ON!");
            }
        });


        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write("0");
                showToast("Turn LED OFF!");
            }
        });
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (BA == null) {
            errorExit("Error", "Bluetooth not support");
        } else {
            if (BA.isEnabled()) {
                showToast("...Bluetooth is Already ON...");
                t1.setText("Rnica");
            } else {
                showToast("...Please Turn Bluetooth ON...");
                t2.setText("Hi again!");
                //Prompt user to turn on Bluetooth

            }
        }
    }
    public void ShowPaired_device() {
//        BluetoothDevice device = BA.getRemoteDevice(address);
        BluetoothAdapter BA2;
        if (ContextCompat.checkSelfPermission(Bluetooth_.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT>=31)
            {
                ActivityCompat.requestPermissions(Bluetooth_.this ,new String[]{Manifest.permission.BLUETOOTH_CONNECT},100);
                return;
            }
        }
        BluetoothManager Bm = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if(Build.VERSION.SDK_INT>=31)
        {
            BA2 = Bm.getAdapter();
        }
        else
        {
            BA2 = BluetoothAdapter.getDefaultAdapter();
        }
        Set<BluetoothDevice> pairedDevices = BA2.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                 device_name= device.getName();
                 device_address = device.getAddress();
                Log.d(TAG, "deviceName:" + device_name);
//                Log.d(TAG, "deviceHardwareAddress:" + device_address);
                t2.setText(device_name);
                t3.setText(device_address);
                }
            }
        }


    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }



    //Transmit Data
    public void write(String message) {
        byte[] bytes=message.getBytes();
        try {
            outstream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }


    private void showToast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}



