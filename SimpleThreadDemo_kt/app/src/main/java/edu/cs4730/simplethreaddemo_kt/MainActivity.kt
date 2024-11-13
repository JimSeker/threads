package edu.cs4730.simplethreaddemo_kt

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cs4730.simplethreaddemo_kt.databinding.ActivityMainBinding

/**
 * A very simple thread demo.  It matches what happens in aSyncTaskDemo.
 * basically starts a thread to display the progress from 0 to 100 (in increments of 5).
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var ProgressValue = 0

    //for threading and communication,
    protected var handler: Handler? = null

    //for the thread
    var myThread: Thread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.button1.setOnClickListener {
            // starts the Thread.
            myThread = Thread(CountingThread(0))
            myThread!!.start()
        }

        //message handler for the animation.
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) { // update progress.
                    binding.progress.text = "Progress: $ProgressValue%"
                } else if (msg.what == 1) { //finished.
                    binding.progress.text = "Completed: $ProgressValue%"
                }
            }
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
