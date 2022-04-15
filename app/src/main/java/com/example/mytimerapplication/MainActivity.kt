package com.example.mytimerapplication


import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val closeImage: ImageView = findViewById(R.id.close_image)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val startTimerBtn: Button = findViewById(R.id.start_timer_btn)
        val counterMessageTextView: TextView = findViewById(R.id.time_counter_message_text_view)
        val timeView: TextView = findViewById(R.id.selected_time_view)
        val timePicker: NumberPicker = findViewById(R.id.time_picker)

        counterMessageTextView.text = getString(R.string.enter_your_start_timer)
        timePicker.minValue = 0
        timePicker.maxValue = 99

        startTimerBtn.setOnClickListener {
            // if user doesn't enter a start time it will fire a toast. else perform countdown
            if (timePicker.value == 0) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_time_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                timeView.text = ""
                progressBar.max = timePicker.value
                counterMessageTextView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                timeView.visibility = View.VISIBLE
                timePicker.visibility = View.GONE
                startTimerBtn.isEnabled = false
                startTimerBtn.text = getString(R.string.timer_started)
                // launching coroutine for looping through the users input
                GlobalScope.launch(Dispatchers.IO) {
                    val time = timePicker.value.toString().toInt()
                    for (i in time downTo 1) {
                        launch(Dispatchers.Main) {
                            timeView.text = i.toString()
                            progressBar.progress = timeView.text.toString().toInt()
                        }
                        delay(1000)
                        // end of count show message to re-enter an input
                        if (i == 1) {
                            launch(Dispatchers.Main) {
                                startTimerBtn.isEnabled = true
                                startTimerBtn.text = getString(R.string.start_timer)
                                counterMessageTextView.text =
                                    getString(R.string.timer_done_time_message)
                                timeView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                counterMessageTextView.visibility = View.VISIBLE
                                timePicker.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        // open quit app fragment
        closeImage.setOnClickListener { finishAndRemoveTask() }
    }
}