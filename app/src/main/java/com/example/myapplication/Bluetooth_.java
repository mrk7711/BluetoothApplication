package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth_ extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    Button mOnBtn;
    Button mOffBtn;
    Button mPairedBtn;
    BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
    private BluetoothAdapter mBlueAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        mOnBtn = findViewById(R.id.ON);
        mOffBtn = findViewById(R.id.OFF);
        mPairedBtn = findViewById(R.id.Pair);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.e("Activity result", "OK");
                        // There are no request codes
                        Intent data = result.getData();
                    }
                });
        // Adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        //Check if bluetooth is available or not
        if (mBlueAdapter == null) {
            showToast("Bluetooth is not Available");
        } else {
            showToast("Bluetooth is Available");
            mOnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Turn on Bluetooth btn click
                    if (!mBlueAdapter.isEnabled()) {
                        showToast("Turning On Bluetooth...");

                        // Intent to On Bluetooth
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        //  startActivityForResult(intent, REQUEST_ENABLE_BT);
                        activityResultLauncher.launch(intent);
                    } else {
                        showToast("Bluetooth is already on");
                    }
                }
            });
            // Turn on Bluetooth btn click
            //mDiscoverBtn.setOnClickListener(this);    // Discover bluetooth btn click
            //         // Turn off Bluetooth btn click
            //
            mOffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBlueAdapter.isEnabled()) {
                        if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mBlueAdapter.disable();
                        showToast("Turning Bluetooth Off");
                    } else {
                        showToast("Bluetooth is already off");
                    }
                }
            });

            // Get Paired devices button click
            mPairedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    pairedDevices = mBlueAdapter.getBondedDevices();
                    if(pairedDevices.size()>0) {
                        ArrayList list = new ArrayList();
                        for (BluetoothDevice a : pairedDevices)
                        {
                            String deviceName= a.getName();             // Name
                            String deviceAddress=a.getAddress();    //Mac Address
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showToast("Permission Granted");
            }
            else {
                showToast("Permission Denied");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    // Bluetooth is on
                    showToast("Bluetooth is on");
                }
                else {
                    showToast("Failed to connect to bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Toast message function
    private void showToast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}



