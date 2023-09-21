package edu.cs4730.simplethreaddemo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cs4730.simplethreaddemo.databinding.ActivityMainBinding;

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo.
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 */

public class MainActivity extends AppCompatActivity {

    int ProgressValue;
    ActivityMainBinding binding;

    //for threading and communication,
    protected Handler handler;
    //for the thread
    Thread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button1.setOnClickListener(new Button.OnClickListener() {
            // starts the Thread.
            @Override
            public void onClick(View view) {
                myThread = new Thread(new CountingThread(0));
                myThread.start();
            }
        });

        //message handler for the animation.
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) { // update progress.
                    binding.progress.setText("Progress: " + ProgressValue + "%");
                } else if (msg.what == 1) { //finished.
                    binding.progress.setText("Completed: " + ProgressValue + "%");
                }
            }
        };

    }

    class CountingThread implements Runnable {
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
                    //send message to update the screen.
                    handler.sendEmptyMessage(0);
                }
            }
            ProgressValue = i;
            handler.sendEmptyMessage(1);  //we are done!
        }
    }
}
