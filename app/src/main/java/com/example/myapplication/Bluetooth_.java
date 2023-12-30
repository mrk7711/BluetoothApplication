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
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bluetooth_ extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final String TAG = "bluetooth1";
    private Handler handler;
    Button on;
    Button off;
    Button mOnBtn;
    Button mOffBtn;
    Button mPairedBtn;
    Button mVisBtn;
    ListView lv;
    Intent bluetoothEnablingIntent;
    BluetoothDevice[] bluetoothPairedDevArray;
    private BluetoothAdapter mBlueAdapter;
    private BluetoothSocket mBlueSocket;
    private OutputStream outstream;
    private InputStream instream;
    private byte[] mmBuffer;    // mmBuffer store for the stream
    private Set<BluetoothDevice> pairedDevices;
    private static final String APP_NAME = "DA14531";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    private static String name;
    ActivityResultLauncher activityResultLauncher;

    //آدرس مک مربوط به بلوتوث میکرو را در تابع در خط 167 از کاربر گرفته می شود.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        on = findViewById(R.id.btnOn);
        off = findViewById(R.id.btnOff);
        mOnBtn = findViewById(R.id.ON);
        mOffBtn = findViewById(R.id.OFF);
        mVisBtn = findViewById(R.id.vis);
        mPairedBtn = findViewById(R.id.Pair);
        lv = findViewById(R.id.lv);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.e("Activity result", "OK");
                        // There are no request codes
                        Intent data = result.getData();
                    }
                });

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        checkBTState(); // Check for Bluetooth support and then check to make sure it is turned on
        ShowPaired_device();
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write("1");
                showToast("Turn on LED");
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write("0");
                showToast("Turn off LED");
            }
        });
//        mOnBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Emulator doesn't support Bluetooth and will return null
//                if(mBlueAdapter==null) {
//                    errorExit("Fatal Error", "Bluetooth not support");
//                } else {
//                    if ( mBlueAdapter.isEnabled()) {
//                        showToast("Already On!");
//                    } else {
//                        showToast("Please turn it On!");
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_SEND);
////                        intent.setType("text/plain");
//                        startActivity(intent);
////                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////                startActivityForResult(enableBtIntent,0);
////                startActivity(enableBtIntent);
////                activityResultLauncher.launch(enableBtIntent);
//                    }
//                }
//            }
//        });
//
//        //
//        mOffBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mBlueAdapter.isEnabled())
//                {
//                    mBlueAdapter.disable();
//                    showToast("Turning Bluetooth Off");
//                } else {
//                    showToast("Bluetooth is already off");
//                }
//            }
//        });
//        // Get Paired devices button click
//
//        mVisBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                activityResultLauncher.launch(getVisible);
//            }
//        });
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pairedDevices = mBlueAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    ArrayList list = new ArrayList();
                    for (BluetoothDevice a : pairedDevices) {
                        address = a.getAddress();
//                            list.add(a.getName());
                        list.add(a.getAddress());
                    }
                    Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
                    final ArrayAdapter adapter = new ArrayAdapter(Bluetooth_.this, android.R.layout.simple_list_item_1, list);
                    lv.setAdapter(adapter);
                }
            }
        });
    }

    private void checkBTState() {
        // Emulator doesn't support Bluetooth and will return null
        if (mBlueAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (mBlueAdapter.isEnabled()) {
                showToast("Already On!");
            } else {
                showToast("Please turn it On!");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                startActivity(intent);
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activityResultLauncher.launch(enableBtIntent);
            }
        }
    }

    public void ShowPaired_device() {
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            ArrayList list = new ArrayList();
            for (BluetoothDevice a : pairedDevices) {
                name=a.getName();
                address = a.getAddress();
                list.add(name);
                list.add(address);
            }
            Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
            final ArrayAdapter adapter = new ArrayAdapter(Bluetooth_.this, android.R.layout.simple_list_item_1, list);
            adapter.add(name+ "\n"+ address);
            lv.setAdapter(adapter);
        }

    }
//    public void onResume() {
//        super.onResume();
//
//        Log.d(TAG, "...onResume - try connect...");
//
//        // Set up a pointer to the remote node using it's address.
//        BluetoothDevice device = mBlueAdapter.getRemoteDevice(address);
//
//        // Two things are needed to make a connection:
//        //   A MAC address, which we got above.
//        //   A Service ID or UUID.  In this case we are using the
//        //     UUID for SPP.
//
//        try {
//            mBlueSocket = createBluetoothSocket(device);
//        } catch (IOException e1) {
//            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
//        }
//
//        // Discovery is resource intensive.  Make sure it isn't going on
//        // when you attempt to connect and pass your message.
//        mBlueAdapter.cancelDiscovery();
//
//        // Establish the connection.  This will block until it connects.
//        Log.d(TAG, "...Connecting...");
//        try {
//            mBlueSocket.connect();
//            Log.d(TAG, "...Connection ok...");
//        } catch (IOException e) {
//            try {
//                mBlueSocket.close();
//            } catch (IOException e2) {
//                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
//            }
//        }
//
//        // Create a data stream so we can talk to server.
//        Log.d(TAG, "...Create Socket...");
//
//        try {
//            outstream = mBlueSocket.getOutputStream();
//        } catch (IOException e) {
//            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
//        }
//    }
//
//    public void onPause() {
//        super.onPause();
//
//        Log.d(TAG, "...In onPause()...");
//
//        if (outstream != null) {
//            try {
//                outstream.flush();
//            } catch (IOException e) {
//                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
//            }
//        }
//
//        try     {
//            mBlueSocket.close();
//        } catch (IOException e2) {
//            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
//        }
//    }
//
//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
//
//            try {
//                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
//                return (BluetoothSocket) m.invoke(device, MY_UUID);
//            } catch (Exception e) {
//                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
//            }
//    }


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

    public void write(String message) {                 //Transmit Data
        byte[] bytes=message.getBytes();
        try {
            outstream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }

    public void read() {                                //Receive Data
        mmBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                numBytes = instream.read(mmBuffer);
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    private void showToast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    public void cancel() {              //Socket Canceling Process.!
        try {
            mBlueSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}



