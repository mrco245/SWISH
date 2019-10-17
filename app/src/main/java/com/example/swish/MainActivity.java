package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static com.example.swish.Bluetooth.mbluetoothAdapter;


public class MainActivity extends AppCompatActivity {

    public static String timeArr[] = new String[1000];
    public static int i = 0;

    public static BluetoothSocket mmSocket;

    BluetoothDevice mDevice;
    ConnectThread mConnectThread;
    static ConnectedThread mConnectedThread;
    Boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mbluetoothAdapter == null)
        {
            //device does not support
        }
        else
        {
            if(!mbluetoothAdapter.isEnabled())
            {
                Intent enableBTintnet = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTintnet, 1);
            }
            Set<BluetoothDevice> pairedDevices = mbluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() >0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    isConnected = true;
                     mDevice = device;
                }
                mConnectThread = new ConnectThread(mDevice);
                mConnectThread.start();
            }
        }

        ////DSD Tech HC-05
        //00:14:03:06:19:2D

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnectThread.cancel();
        mConnectedThread.cancel();
    }

    private class ConnectThread extends Thread {

        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
        public void run() {
            mbluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.tab_connect:
                if(!isConnected)
                {
                    Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                    startActivity(intent);
                }
                else
                {
                    Toast message = Toast.makeText(getApplicationContext(),"Already Connected to Glove:\n" + mDevice.getName(), Toast.LENGTH_LONG);
                    message.show();
                }
                break;

            case R.id.tab_TrainingSession:
                if(isConnected)
                {
                    Intent intent1 = new Intent(MainActivity.this, TrainingSession.class);
                    startActivity(intent1);
                }
                else
                {
                    Toast message = Toast.makeText(getApplicationContext(),"Please Connect to Glove", Toast.LENGTH_LONG);
                    message.show();
                }

                break;

            case R.id.tab_Results:
                if(isConnected)
                {
                    Intent intent2 = new Intent(MainActivity.this, VisualFeedback.class);
                    startActivity(intent2);
                }
                else
                {
                    Toast message = Toast.makeText(getApplicationContext(),"Please Connect to Glove", Toast.LENGTH_LONG);
                    message.show();
                }

                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;

            Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    byte[] writeBuf = (byte[]) msg.obj;
                    int begin = (int)msg.arg1;
                    int end = (int)msg.arg2;

                    switch(msg.what) {
                        case 1:
                            String writeMessage = new String(writeBuf);
                            writeMessage = writeMessage.substring(begin, end);
                            break;
                    }
                }
            };

            while (true) {
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for(int i = begin; i < bytes; i++) {
                        if(buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }

            ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


}
