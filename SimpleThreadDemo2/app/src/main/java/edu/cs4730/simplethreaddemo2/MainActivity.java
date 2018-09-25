package edu.cs4730.simplethreaddemo2;

import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo and SimpleThreadDemo
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 * <p>
 * Only this version uses the runOnUiThread instead of handlers.
 */
public class MainActivity extends AppCompatActivity {

    TextView Progress;
    int ProgressValue;
    Button Button1;

    //for the thread
    Thread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        Progress = findViewById(R.id.textView1);

        Button1 = findViewById(R.id.button1);
        Button1.setOnClickListener(new Button.OnClickListener() {
            /*
             * starts the Thread.
             */
            @Override
            public void onClick(View view) {
                myThread = new Thread(new CountingThread(0));
                myThread.start();
            }
        });
    }

    private class CountingThread implements Runnable {
        int i = 0;  //default value of zero.

        CountingThread(int start) {
            i = start; //starting value for the count.
        }

        @Override
        public void run() {
            while (i < 100) {
                SystemClock.sleep(250);
                i++;
                if (i % 5 == 0) {
                    //update UI
                    ProgressValue = i;
                    //run on the UI thread to update the screen.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Progress.setText("Progress: " + ProgressValue + "%");
                        }
                    });
                }
            }
            ProgressValue = i;
            runOnUiThread(new Runnable() {  //we need to run this on the main ui thread to access the UI.
                @Override
                public void run() {
                    Progress.setText("Completed: " + ProgressValue + "%");
                }
            });
        }
    }
}
