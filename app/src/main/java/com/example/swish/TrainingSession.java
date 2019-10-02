package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import static com.example.swish.MainActivity.i;
import static com.example.swish.MainActivity.timeArr;

public class TrainingSession extends AppCompatActivity {

    Chronometer cmTimer;
    Button btnStart, btnStop;
    long elapsedTime;
    String time = "";
    String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_session);

        cmTimer = (Chronometer)findViewById(R.id.cmTimer);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);

        cmTimer.setText("00:00");
        btnStop.setEnabled(false);

        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                elapsedTime = SystemClock.elapsedRealtime();
                time = "Minutes: " +minutes +" Seconds: " + seconds;
                Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
            }

        });
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            //send signal to glove via bluetooth to start the data collection
            case R.id.btnStart:
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                cmTimer.setBase(SystemClock.elapsedRealtime());
                cmTimer.start();
                break;

            //send signal to tell the glove to send that data over for the visual feedback.
            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                cmTimer.stop();
                Log.d(TAG, "Total time " + time);
                timeArr[i] = time;
                i++;
                Log.d(TAG, "i = " + i);

                break;


        }
    }

    //creates the menu system for the user interface
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.training_session_menu, menu);
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
            case R.id.home:
                Intent intent = new Intent(TrainingSession.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.tab_Results:
                Intent intent2 = new Intent(TrainingSession.this, VisualFeedback.class);
                startActivity(intent2);

                break;


        }
        return super.onOptionsItemSelected(item);
    }

}
