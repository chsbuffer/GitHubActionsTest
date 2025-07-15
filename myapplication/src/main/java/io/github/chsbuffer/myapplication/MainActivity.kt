package io.github.chsbuffer.myapplication

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import java.util.Timer
import java.util.TimerTask

class MainActivity : Activity() {
    var timer: Timer = Timer(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val delay = 1L
        timer.schedule(MyTask(this), delay, delay)
    }

    class MyTask(val activity: MainActivity) : TimerTask() {
        override fun run() {
            activity.findViewById<TextView>(R.id.license_activity_textview).apply {
                this.text = Companion.getText()
            }
        }
    }

    companion object {
        var num: Int = 0

        @JvmStatic
        fun getText(): String {
            val runtime = Runtime.getRuntime();
            return "Hello World!\nused: ${runtime.totalMemory() - runtime.freeMemory()}  free: ${runtime.freeMemory()} / ${runtime.totalMemory()}"
        }
    }
}