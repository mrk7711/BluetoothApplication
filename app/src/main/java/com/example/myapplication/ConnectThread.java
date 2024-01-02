package com.example.myapplication;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;


//Class that will open the BT Socket to the Arduino BT Module
//Given a BT device, the UUID and a Handler to set the results
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private static final String TAG = "BT1";
    public static Handler handler;
    private final static int ERROR_READ = 0;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, UUID MY_UUID) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "connectException: " + connectException);

        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    public BluetoothSocket getMmSocket(){
        return mmSocket;
    }
}
