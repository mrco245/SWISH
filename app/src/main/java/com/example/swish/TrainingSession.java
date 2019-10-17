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
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

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
    private TextView mReadBuffer;
    private Handler mHandler;


    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status


    ConnectedThread thread = new ConnectedThread(mmSocket);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_session);

        cmTimer = (Chronometer)findViewById(R.id.cmTimer);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);

        mReadBuffer = (TextView) findViewById(R.id.readBuffer);

        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mReadBuffer.setText(readMessage);
                }

            }
        };

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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            boolean stop = false;
            while (!stop) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                        stop = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
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

                    //thread.run();
                    //thread.write("Hello");
                    //thread.run();

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
                    //thread.run();
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
