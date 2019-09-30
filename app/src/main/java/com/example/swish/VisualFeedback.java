package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class VisualFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feedback);
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
                //Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                //startActivity(intent);
                break;

            case R.id.tab_TrainingSession:
                //Intent intent1 = new Intent(MainActivity.this, TrainingSession.class);
                //startActivity(intent1);
                break;

            case R.id.tab_Results:
                //Intent intent2 = new Intent(MainActivity.this, VisualFeedback.class);
                //startActivity(intent2);

                break;


        }
        return super.onOptionsItemSelected(item);
    }

}
