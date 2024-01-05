package com.example.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;
import android.os.Message;
import android.bluetooth.BluetoothServerSocket;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Bluetooth_ extends AppCompatActivity {
    private BluetoothAdapter BA = null;

    private BluetoothSocket mmSocket;
    private  BluetoothServerSocket mmsocket2;
    private static String device_name;
    private static String device_address;
    BluetoothDevice DA = null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //We declare a default UUID to create the global variable
    private static String address = "48:23:35:F4:00:17";
    private static final String TAG = "bluetooth1";
    private byte[] mmBuffer;
    private OutputStream mmOutStream = null;
    private InputStream mmInStream = null;
    private static final int REQUEST_ENABLE_BT = 1;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private String valueRead;
    Button mOnBtn;
    Button mOffBtn;
    Button mPairedBtn;
    Button mConnect;
    Button mdis;
    Button m1;
    Button m2;
    Button send;
    EditText writemessage;
    TextView t1, t2, t3;
    TextView status, messagebox;
    ListView listView;
    BluetoothDevice[] btArray;
    SendReceive sendReceive;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        findViewByIdes();
        BA = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        implementListeners();
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BA.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= 31) {
                            ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                            return;
                        }
                    }
                    BA.enable();
                } else {
                    showToast("Bluetooth is ON!");
                }
            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BA.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= 31) {
                            ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                            return;
                        }
                    }
                    BA.disable();
                }
            }
        });
//        m1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                send("A");
//                t1.setText("You Sent A");
////                showToast("Turn LED ON!");
//            }
//        });

//        m2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////               write("B");
////                t1.setText("You Sent B");
////                showToast("Turn LED OFF!");
//            }
//        });
    }

    private void implementListeners() {

        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPaired_device();
            }
        });

        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass=new ClientClass(btArray[i]);
                clientClass.start();

                status.setText("Connecting");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string= String.valueOf(writemessage.getText());
                sendReceive.write(string.getBytes());
            }
        });
    }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    messagebox.setText(tempMsg);
                    break;
            }
            return true;
        }
    });
    private void findViewByIdes() {
        m1 = findViewById(R.id.btnOn);
        m2 = findViewById(R.id.btnOff);
        mOnBtn = findViewById(R.id.ON);
        mOffBtn = findViewById(R.id.OFF);
        mPairedBtn = findViewById(R.id.Pair);
        mConnect = findViewById(R.id.vis);
        mdis = findViewById(R.id.disc);
        send = findViewById(R.id.send);
        t1 = findViewById(R.id.textView17);
        t2 = findViewById(R.id.textView18);
        t3 = findViewById(R.id.textView19);
        status=findViewById(R.id.status);
        listView=findViewById(R.id.lv);
        messagebox=findViewById(R.id.messagebox);
        writemessage = findViewById(R.id.edit);
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (BA == null) {
            errorExit("Error", "Bluetooth not support");
        } else {
            if (BA.isEnabled()) {
                showToast("...Bluetooth is Already ON...");
                t1.setText("...Bluetooth is Already ON...");
            } else {
                showToast("...Please Turn Bluetooth ON...");
                t2.setText("...Please Turn Bluetooth ON...");
                //Prompt user to turn on Bluetooth

            }
        }
    }

    public void ShowPaired_device() {
        if (ContextCompat.checkSelfPermission(Bluetooth_.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 31) {
                ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                return;
            }
        }
        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();
        String[] strings=new String[pairedDevices.size()];
        btArray=new BluetoothDevice[pairedDevices.size()];
        int index=0;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                btArray[index]= device;
                strings[index]=device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
            listView.setAdapter(arrayAdapter);
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }


    //Transmit Data
//    public void send(String message) {
//        byte[] bytes = message.getBytes();
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) {
//            Log.e(TAG, "Error occurred when sending data", e);
//        }
//    }

//    public void receive() {                                 // Read from the InputStream.
//        //Receive Data
//        mmBuffer = new byte[1024];
//        int Bytes=0; // bytes returned from read()
//
//        // Keep listening to the InputStream until an exception occurs.
//        while (true) {
//                try {
//                    Bytes= mmInStream.read(mmBuffer);
////                    new Handler().post(new Runnable() {
////                        @Override
////                        public void run() {
////                            showToast("Listening for data"+mmInStream.toString());
////                        }
////                    });
//                    t1.setText("B");
//                }   catch (IOException e) {
//                    Log.d(TAG, "Input stream was disconnected", e);
//                    break;
//            }
//        }
//
//    }

//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) {
//            Log.e(TAG, "Could not close the client socket", e);
//        }
//    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

//    public void ConnectThread(BluetoothDevice device, UUID MY_UUID) {
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                if (Build.VERSION.SDK_INT >= 31) {
//                    ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
//                    return;
//                }
//            }
//            mmSocket=device.createRfcommSocketToServiceRecord(MY_UUID);
//            BA.cancelDiscovery();
//            mmSocket.connect();
//            showToast("Socket is created!");
//        } catch (IOException e) {
//            try {
//                mmSocket.close();
//            }
//            catch (IOException f){
//                Log.e(TAG, "Socket's create() method failed", e);
//            }
//        }
//        ConnectedThread(mmSocket);
//    }

//    public void ConnectedThread(BluetoothSocket socket) {
//
//        try {
//            mmInStream = socket.getInputStream();
//            mmOutStream = socket.getOutputStream();
//        }
//        catch (IOException e) {
//            Log.e(TAG, "Error occurred when creating input stream", e);
//        }
//    }


    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 31) {
                        ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                        return;
                    }
                }
                serverSocket=BA.listenUsingRfcommWithServiceRecord(TAG,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null)
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }
    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 31) {
                        ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                        return;
                    }
                }
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try {
                if (ActivityCompat.checkSelfPermission(Bluetooth_.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 31) {
                        ActivityCompat.requestPermissions(Bluetooth_.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                        return;
                    }
                }
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }
    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}





