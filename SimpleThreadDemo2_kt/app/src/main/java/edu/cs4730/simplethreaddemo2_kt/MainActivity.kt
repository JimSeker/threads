package edu.cs4730.simplethreaddemo2_kt

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import edu.cs4730.simplethreaddemo2_kt.databinding.ActivityMainBinding

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo and SimpleThreadDemo
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 *
 * Only this version uses the runOnUiThread instead of handlers.
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var ProgressValue = 0

    //for the thread
    var myThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);


        binding.button1.setOnClickListener {
            //starts the Thread.
            myThread = Thread(CountingThread(0))
            myThread!!.start()
        }
    }

    private inner class CountingThread(start: Int) : Runnable {
        var i = 0 //default value of zero.
        override fun run() {
            while (i < 100) {
                SystemClock.sleep(250)
                i++
                if (i % 5 == 0) {
                    //update UI
                    ProgressValue = i
                    //run on the UI thread to update the screen.
                    runOnUiThread { binding.progress.text = "Progress: $ProgressValue%" }
                }
            }
            ProgressValue = i
            runOnUiThread { binding.progress.text = "Completed: $ProgressValue%" }
        }

        init {
            i = start //starting value for the count.
        }
    }
}
