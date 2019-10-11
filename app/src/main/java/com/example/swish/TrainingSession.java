package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.swish.MainActivity.i;
import static com.example.swish.MainActivity.mConnectedThread;
import static com.example.swish.MainActivity.timeArr;
import static com.example.swish.MainActivity.mmSocket;

public class TrainingSession extends AppCompatActivity {

    Chronometer cmTimer;
    Button btnStart, btnStop;
    long elapsedTime;
    String time = "";
    String TAG = "TAG";

    //ConnectedThread thread;


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
            case R.id.btnStart:
                //enables/disables buttons
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                //sets the clock
                cmTimer.setBase(SystemClock.elapsedRealtime());
                cmTimer.start();

                //send signal to glove via bluetooth to start the data collection
                try{
                  //  thread = new ConnectedThread(mmSocket);

                    mConnectedThread.run();

                    byte[] message = new byte['*'];
                    Log.d(TAG, "Message Sent: " + message);
                    mConnectedThread.write(message);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                break;

            //send signal to tell the glove to send that data over for the visual feedback.
            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                cmTimer.stop();

                try {
                    mConnectedThread.run();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                Log.d(TAG, "Total time " + time);
                timeArr[i] = time;
                i++;
                Log.d(TAG, "i = " + i);

                break;


        }
    }

    private class ConnectedThread extends Thread {

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

        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
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
