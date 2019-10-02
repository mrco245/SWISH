package com.example.swish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static com.example.swish.MainActivity.timeArr;

public class VisualFeedback extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_feedback);

        String feedback = "Training Sessions:\n";


        for (int x = 0; x < timeArr.length; x++) {
            if (timeArr[x] == null) {
                break;
            }
            else
            {
                feedback = feedback + "Training Session #" + (x+1) + ": " +timeArr[x] + "\n";
            }

        }

        text = (TextView)findViewById(R.id.textView);
        text.setText(feedback);
    }


    //sets up the menu system for the user interface
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feedback_menu, menu);
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
                Intent intent = new Intent(VisualFeedback.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.tab_TrainingSession:
                Intent intent1 = new Intent(VisualFeedback.this, TrainingSession.class);
                startActivity(intent1);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}
