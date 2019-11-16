package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String timeArr[] = new String[1000];
    public static int i = 0;

    public static BluetoothSocket mmSocket;

    me.aflak.bluetooth.Bluetooth ble = new me.aflak.bluetooth.Bluetooth(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ble.onStart();
        if (!ble.isEnabled()) {
            Intent enableBTintnet = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTintnet, 1);
        }
    }

        ////DSD Tech HC-05
        //00:14:03:06:19:2D

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ble.onStop();
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
                    Intent intent = new Intent(MainActivity.this, Select.class);
                    startActivity(intent);
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

}
