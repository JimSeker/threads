package edu.cs4730.asynctaskdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cs4730.asynctaskdemo.databinding.ActivityMainBinding;

/**
 * very simple demo of a AsyncTask.
 * Starts a AsyncTask to to display the progress from 0 to 100 (in increments of 5).
 * <p>
 * AsyncTask was deprecated in API level 30.
 */
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        binding.button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start the asynctask here.
                CountingTask task = new CountingTask();
                task.execute(0);
            }
        });
    }

    /**
     * This is a very simple AsyncTask that counts to 100 and sets to the text view in the
     * layout.
     */
    public class CountingTask extends AsyncTask<Integer, Integer, Integer> {
        CountingTask() {
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int i = params[0];
            while (i < 100) {
                SystemClock.sleep(250);
                i++;
                if (i % 5 == 0) {
                    //update UI
                    publishProgress(i);
                }
            }
            return i;
        }

        protected void onProgressUpdate(Integer... progress) {
            binding.progress.setText("Progress: " + progress[0] + "%");
        }

        protected void onPostExecute(Integer result) {
            binding.progress.setText("Completed: " + result + "%");
        }

        protected void onPreExecute() {
            //invoked on the UI thread before the task is executed.
            //his step is normally used to setup the task, for instance by showing a progress bar in the user interface.
            binding.progress.setText("About to start.");
        }
    }
}
