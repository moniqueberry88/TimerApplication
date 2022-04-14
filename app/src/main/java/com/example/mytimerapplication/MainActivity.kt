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

    private var closeAppFragment: CloseAppFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var closeImage: ImageView = findViewById(R.id.close_image)
        var progressBar: ProgressBar = findViewById(R.id.progress_bar)
        var button: Button = findViewById(R.id.timer_button);
        var counterMessageTextView: TextView = findViewById(R.id.time_counter_message_text_view)
        var timeView: TextView = findViewById(R.id.selected_time_view)
        var timerInput: EditText = findViewById(R.id.select_time_view)

        counterMessageTextView.text = getString(R.string.initial_time_message)

        button.setOnClickListener {
            // if user doenst enter a start time it will fire a toast. else perform countdown
            if (timerInput.text.toString().equals("")) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_time_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // setting ui
                timeView.text = ""
                progressBar.max = timerInput.text.toString().toInt()
                progressBar.visibility = View.VISIBLE
                counterMessageTextView.visibility = View.GONE
                timeView.visibility = View.VISIBLE
                timerInput.visibility = View.GONE
                button.isEnabled = false
                // launching coroutine for looping through the users input
                GlobalScope.launch(Dispatchers.IO) {
                    val time = timerInput.getText().toString().toInt()
                    for (i in time downTo 0) {
                        delay(1000)
                        launch(Dispatchers.Main) {
                            timeView.text = i.toString()
                            progressBar.progress = timeView.text.toString().toInt()
                        }
                        // end of count show message to re-enter an input
                        if (i == 0) {
                            launch(Dispatchers.Main) {
                                button.isEnabled = true
                                counterMessageTextView.text =
                                    getString(R.string.timer_done_time_message)
                                timeView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                counterMessageTextView.visibility = View.VISIBLE
                                timerInput.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        // open quit app fragment
        closeImage.setOnClickListener {
            closeAppFragment = CloseAppFragment()
            closeAppFragment?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .addToBackStack(fragment.tag)
                    .add(R.id.close_app_fragment, fragment)
                    .commit()
            }
        }
    }
}