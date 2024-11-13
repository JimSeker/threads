package edu.cs4730.threaddemo_kt


import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * This is a thread demo
 * When the user touches the image, a red bar will roll down.
 * If the user lifts before it is done, then it will pause the thread.
 */

class MainActivity : AppCompatActivity() {


    companion object {
        lateinit var log: TextView
        lateinit var theboardfield: ImageView
        lateinit var theboard: Bitmap
        lateinit var theboardc: Canvas
        val boardsize = 480
        var isAnimation = false
        var pause: kotlin.Boolean = false
        lateinit var handler: Handler

        //for drawing
        lateinit var myRec: Rect
        lateinit var myColor: Paint

        //for the thread
        var myThread: Thread? = null
        val mylock = ReentrantLock()
        val condition = mylock.newCondition()

        fun sendmessage(logthis: String) {
            val b = Bundle()
            b.putString("logthis", logthis)
            val msg = handler.obtainMessage()
            msg.data = b
            msg.arg1 = 1
            msg.what = 1 //so the empty message is not used!
            println("About to Send message$logthis")
            handler.sendMessage(msg)
            println("Sent message$logthis")
        }

        fun drawBmp() {
            theboardfield.setImageBitmap(theboard)
            theboardfield.invalidate()
        }


        /**
         * simple method to add the log TextView.
         */
        fun logthis(newinfo: String) {
            if (newinfo.compareTo("") != 0) {
                log.append("\n" + newinfo)

            }
        }

        /**
         * attempts to start or restart a the thread
         */
        fun go() {
            if (pause) {  //The thread is paused, so attempt to restart it.
                logthis("About to notify!")
                pause = false
                //kotlin threading is different from java.   ****
                mylock.withLock {
                    condition.signal() //in theory, this should wakeup the thread.  OR myThread.notifyAll()
                }
                logthis("waiting threads should be notified.")
            } else { //the thread is not running, so just start it.
                isAnimation = true
                if (myThread != null) myThread = null
                myThread = Thread(animator())
                myThread!!.start()

            }
        }


        /**
         * attempt to stop/suspend a thread
         */
        fun stop() {
            if (!pause) { //So it is running, now to stop it.
                if (myThread!!.isAlive) { //and still active

                    pause = true
                }
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        log = findViewById(R.id.log)
        //get the imageview and create a bitmap to put in the imageview.
        //also create the canvas to draw on.
        //get the imageview and create a bitmap to put in the imageview.
        //also create the canvas to draw on.
        theboardfield = findViewById(R.id.boardfield)
        theboard = Bitmap.createBitmap(boardsize, boardsize, Bitmap.Config.ARGB_8888)
        theboardc = Canvas(theboard)
        theboardc.drawColor(Color.WHITE) //background color for the board.

        theboardfield.setImageBitmap(theboard)
        theboardfield.setOnTouchListener(myTouchListener())
        //For drawing
        //For drawing
        myRec = Rect(0, 0, 10, 10)
        myColor = Paint() //default black

        myColor.style = Paint.Style.FILL

        //message handler for the animation.

        //message handler for the animation.
        handler = Handler { msg ->
            if (msg.what == 0) { //redraw image
                //if (theboard != null && theboardfield != null) {
                    drawBmp()
                //}
            } else {  //get the data package out of the msg
                val stuff = msg.data
                logthis("Thread: " + stuff.getString("logthis"))
            }
            true
        }


    }


    /**
     * touch listener
     */
    internal class myTouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            //This toggles back and forth the thread.  Starting or stopping it, based on up/down events.
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    logthis("onTouch called, DOWN")
                    v.performClick()
                    go()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    logthis("onTouch called, UP")
                    stop()
                    v.performClick()
                    return true
                }
            }
            return false
        }
    }


    internal class animator : Runnable {
        override fun run() {
            var done = false
            var x = 0
            //first draw a red down the board
            myColor.setColor(Color.RED)
            while (!done) {
                if (!isAnimation) {
                    return
                } //To stop the thread, if the system is paused or killed.
                if (pause) {  //We should wait now, until unpaused.
                    println("Attempting to send message")
                    sendmessage("Waiting!")
                    println("Sent message:  waiting")
                    //logthis("Thread: Waiting!");
                    try {
                        //kotlin is different then java ***
                        mylock.withLock {
                            condition.await()
                        }
                    } catch (e: InterruptedException) {
                        sendmessage("Can't Wait!!!")
                        //logthis("Thread: Can't WAIT!!!!");
                    }
                    sendmessage("Resumed!")
                    //logthis("Thread: Resumed!");
                }
                theboardc.drawLine(0f, x.toFloat(), boardsize.toFloat(), x.toFloat(), myColor)
                x++

                //send message to redraw
                handler.sendEmptyMessage(0)
                //now wait a little bit.
                try {
                    Thread.sleep(10) //change to 100 for a commented out block, instead of just a line.
                } catch (e: InterruptedException) {
                    //don't care
                }
                //determine if we are done or move the x?
                if (x >= boardsize) done = true
            }
            theboardc.drawColor(Color.WHITE)
            myColor.setColor(Color.BLACK)
            isAnimation = false
        }
    }


}