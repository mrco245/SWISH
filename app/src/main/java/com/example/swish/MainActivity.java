package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


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
