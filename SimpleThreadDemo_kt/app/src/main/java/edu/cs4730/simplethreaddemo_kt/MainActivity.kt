package edu.cs4730.simplethreaddemo_kt

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo.
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 */
class MainActivity : AppCompatActivity() {
    lateinit var Progress: TextView
    var ProgressValue = 0
    lateinit var Button1: Button

    //for threading and communication,
    protected var handler: Handler? = null

    //for the thread
    var myThread: Thread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Progress = findViewById(R.id.textView1)
        Button1 = findViewById(R.id.button1)
        Button1.setOnClickListener(
            View.OnClickListener
            // starts the Thread.
            {
                myThread = Thread(CountingThread(0))
                myThread!!.start()
            })

        //message handler for the animation.
        handler = Handler { msg ->
            if (msg.what == 0) { // update progress.
                Progress.setText("Progress: $ProgressValue%")
            } else if (msg.what == 1) { //finished.
                Progress.setText("Completed: $ProgressValue%")
            }
            true
        }
    }

    internal inner class CountingThread(start: Int) : Runnable {
        var i = 0 //default value of zero.
        override fun run() {
            while (i < 100) {
                SystemClock.sleep(250)
                i++
                if (i % 5 == 0) {
                    //update UI
                    ProgressValue = i
                    //send message to update the screen.
                    handler!!.sendEmptyMessage(0)
                }
            }
            ProgressValue = i
            handler!!.sendEmptyMessage(1) //we are done!
        }

        init {
            i = start //starting value for the count.
        }
    }
}
