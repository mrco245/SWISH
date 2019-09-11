package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.swish.Bluetooth.mbluetoothAdapter;


public class MainActivity extends AppCompatActivity {

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = this.getWindow().getDecorView();

        Button bluetoothBtn = (Button) findViewById(R.id.bluetoothBtn);
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                startActivity(intent);




            }
        });

        TextView BLEGloveName = (TextView)findViewById(R.id.BLEGlove);
        String nameText;
        Bluetooth ble = new Bluetooth();
        nameText = ble.getBluetoothName();
        BLEGloveName.setText(nameText);


        Button goodShotBtn = (Button) findViewById(R.id.goodShot);
        goodShotBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.setBackgroundColor(getResources().getColor(R.color.green));
            }
        });

        Button badShotBtn = (Button) findViewById(R.id.badShot);
        badShotBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                view.setBackgroundColor(getResources().getColor(R.color.red));

            }
        });

    }
}
