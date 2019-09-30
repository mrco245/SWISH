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

    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = this.getWindow().getDecorView();

        ////DSD Tech HC-05
        //    //00:14:03:06:19:2D

        /*

        endBtn.setEnabled(false);
        //startBtn.setEnabled(false);

        if(nameText == "DSD HC-05")
        {
            startBtn.setEnabled(true);
            bluetoothBtn.setEnabled(false);
        }

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                startActivity(intent);

            }
        });

        TextView BLEGloveName = (TextView)findViewById(R.id.BLEGlove);
        BLEGloveName.setText(nameText);

        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.setBackgroundColor(getResources().getColor(R.color.green));
                startBtn.setEnabled(false);
                endBtn.setEnabled(true);

                //send signal through bluetooth to micro controller to start collecting
            }
        });


        endBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                view.setBackgroundColor(getResources().getColor(R.color.red));

                //send signal through bluetooth to microcontroller to start listening for data
                Intent intent = new Intent(MainActivity.this, VisualFeedback.class);
                startActivity(intent);

                startBtn.setEnabled(true);
                endBtn.setEnabled(false);

            }
        });

         */

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
