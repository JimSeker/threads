package edu.cs4730.simplethreaddemo2_kt

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo and SimpleThreadDemo
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 *
 * Only this version uses the runOnUiThread instead of handlers.
 */
class MainActivity : AppCompatActivity() {
    lateinit var Progress: TextView
    var ProgressValue = 0
    lateinit var Button1: Button

    //for the thread
    var myThread: Thread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Progress = findViewById(R.id.textView1)
        Button1 = findViewById(R.id.button1)
        Button1.setOnClickListener(
            View.OnClickListener
            //starts the Thread.
            {
                myThread = Thread(CountingThread(0))
                myThread!!.start()
            })
    }

    private inner class CountingThread internal constructor(start: Int) : Runnable {
        var i = 0 //default value of zero.
        override fun run() {
            while (i < 100) {
                SystemClock.sleep(250)
                i++
                if (i % 5 == 0) {
                    //update UI
                    ProgressValue = i
                    //run on the UI thread to update the screen.
                    runOnUiThread { Progress.text = "Progress: $ProgressValue%" }
                }
            }
            ProgressValue = i
            runOnUiThread { Progress.text = "Completed: $ProgressValue%" }
        }

        init {
            i = start //starting value for the count.
        }
    }
}
