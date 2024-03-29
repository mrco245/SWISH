package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.reader.LineReader;


public class Chat extends AppCompatActivity {
    public static  String name;
    public static  me.aflak.bluetooth.Bluetooth bt;
    public static  EditText message;
    public   Button send;
    public static  TextView text;
    public static  ScrollView scrollView;
    public static  boolean registered=false;

    DeviceCallback deviceCallback;
    BluetoothCallback bluetoothCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        text = (TextView)findViewById(R.id.text);
        message = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.send);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        text.setMovementMethod(new ScrollingMovementMethod());
        send.setEnabled(false);

        bt = new Bluetooth(this);
        bt.onStart();

        deviceCallback = new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                name = "SWISH ";
               Display("Connected to "+ name+" - "+device.getAddress());

               Chat.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       send.setEnabled(true);
                   }
               });
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
               Display("Disconnected!");
               Display("Connecting again...");
               bt.connectToDevice(device);
            }

            @Override
            public void onMessage(byte[] message) {

                String finalMess = " ";
                finalMess = new String(message);

                Display(name +": " + finalMess);
           }

            @Override
            public void onError(int errorCode) {
               Display("Error: "+ message);
            }

            @Override
            public void onConnectError(final BluetoothDevice device, String message) {
               Display("Error: "+message);
               Display("Trying again in 3 sec.");
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Handler handler = new Handler();
                       handler.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               bt.connectToDevice(device);
                           }
                       }, 2000);
                   }
               });
            }
        };

        bt.setCallbackOnUI(this);
        bt.setDeviceCallback(deviceCallback);
        bt.setBluetoothCallback(bluetoothCallback);
        bt.setReader(LineReader.class);

        int pos = getIntent().getExtras().getInt("pos");
        name = bt.getPairedDevices().get(pos).getName();

        Display("Connecting...");

        bt.connectToDevice(bt.getPairedDevices().get(pos));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                message.setText("");
                bt.send(msg.getBytes());
                Display("You: "+ msg);
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
        bt.onStop();
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Back Button is disabled!",
                Toast.LENGTH_LONG).show();
    }

    public void Display(final String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(Chat.this, Select.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };}