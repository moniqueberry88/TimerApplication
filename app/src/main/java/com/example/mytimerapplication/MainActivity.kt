package com.example.mytimerapplication


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mytimerapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timeCounterMessageTextView.text = getString(R.string.enter_your_start_timer)
        binding.timePicker.minValue = 0
        binding.timePicker.maxValue = 99

        binding.startTimerBtn.setOnClickListener {
            // if user doesn't enter a start time it will fire a toast. else perform countdown
            if (binding.timePicker.value == 0) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_time_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // set ui for when the countdown starts
                binding.selectedTimeView.text = ""
                binding.progressBar.max = binding.timePicker.value
                binding.timeCounterMessageTextView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.selectedTimeView.visibility = View.VISIBLE
                binding.timePicker.visibility = View.GONE
                binding.startTimerBtn.isEnabled = false
                binding.startTimerBtn.setTextColor(Color.parseColor("#000000"))
                binding.startTimerBtn.text = getString(R.string.timer_started)
                // launching coroutine for looping through the users input
                GlobalScope.launch(Dispatchers.IO) {
                    val time = binding.timePicker.value.toString().toInt()
                    for (i in time downTo 1) {
                        launch(Dispatchers.Main) {
                            binding.selectedTimeView.text = i.toString()
                            binding.progressBar.progress = binding.selectedTimeView.text.toString().toInt()
                        }
                        delay(1000)
                        // end of count show message to re-enter an input
                        if (i == 1) {
                            launch(Dispatchers.Main) {
                                binding.startTimerBtn.isEnabled = true
                                binding.startTimerBtn.setTextColor(Color.parseColor("#FFFFFF"))
                                binding.startTimerBtn.text = getString(R.string.start_timer)
                                binding.timeCounterMessageTextView.text =
                                    getString(R.string.enter_your_start_timer)
                                binding.selectedTimeView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                binding.timeCounterMessageTextView.visibility = View.VISIBLE
                                binding.timePicker.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
        // open quit app fragment
        binding.closeImage.setOnClickListener { finishAndRemoveTask() }
    }
}