package com.example.swish;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.lang.reflect.Method;

import static com.example.swish.Bluetooth.mbluetoothAdapter;


public class MainActivity extends AppCompatActivity {

    public static String timeArr[] = new String[1000];
    public static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////DSD Tech HC-05
        //00:14:03:06:19:2D

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
                Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                startActivity(intent);
                break;

            case R.id.tab_TrainingSession:
                Intent intent1 = new Intent(MainActivity.this, TrainingSession.class);
                startActivity(intent1);
                break;

            case R.id.tab_Results:
                Intent intent2 = new Intent(MainActivity.this, VisualFeedback.class);
                startActivity(intent2);

                break;


        }
        return super.onOptionsItemSelected(item);
    }


}
