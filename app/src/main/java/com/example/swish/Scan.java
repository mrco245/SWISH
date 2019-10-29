package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;
import me.aflak.bluetooth.interfaces.DeviceCallback;
import me.aflak.bluetooth.interfaces.DiscoveryCallback;

/**
 * Created by Omar on 08/05/2016.
 */
public class Scan extends AppCompatActivity implements DiscoveryCallback, AdapterView.OnItemClickListener{
    public static Bluetooth bluetooth;
    public static ListView listView;
    public static ArrayAdapter<String> adapter;
    public static TextView state;
    public static ProgressBar progress;
    public static Button scan;
    public static List<BluetoothDevice> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        listView = (ListView)findViewById(R.id.scan_list);
        state = (TextView) findViewById(R.id.scan_state);
        progress = (ProgressBar) findViewById(R.id.scan_progress);
        scan = (Button) findViewById(R.id.scan_scan_again);

       // adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bluetooth = new Bluetooth(this);
        bluetooth.setDiscoveryCallback(this);

       // bluetooth.scanDevices();
        bluetooth.startScanning();
        progress.setVisibility(View.VISIBLE);
        state.setText("Scanning...");
        listView.setEnabled(false);

        scan.setEnabled(false);
        devices = new ArrayList<>();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        scan.setEnabled(false);
                    }
                });

                devices = new ArrayList<>();
                progress.setVisibility(View.VISIBLE);
                state.setText("Scanning...");
                //bluetooth.scanDevices();
                bluetooth.stopScanning();
            }
        });
    }

    private void setText(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                state.setText(txt);
            }
        });
    }

    private void setProgressVisibility(final int id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(id);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setProgressVisibility(View.VISIBLE);
        setText("Pairing...");
        bluetooth.pair(devices.get(position));
    }

    @Override
    public void onDiscoveryStarted() {

    }

    @Override
    public void onDiscoveryFinished() {
        setProgressVisibility(View.INVISIBLE);
        setText("Scan finished!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scan.setEnabled(true);
                listView.setEnabled(true);
            }
        });

    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        final BluetoothDevice tmp = device;
        devices.add(device);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(tmp.getAddress()+" - "+tmp.getName());
            }
        });
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        setProgressVisibility(View.INVISIBLE);
        setText("Paired!");
        Intent i = new Intent(Scan.this, Select.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onDeviceUnpaired(BluetoothDevice device) {
        setProgressVisibility(View.INVISIBLE);
        setText("Paired!");
    }

    @Override
    public void onError(int errorCode) {
        setProgressVisibility(View.INVISIBLE);
        //setText("Error: " +message);
    }
}