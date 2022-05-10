package com.mtdeveloper.bodyfatigue

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_rest_heart_rating.*

class RestHeartRatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart_rating)

        val actionBar = supportActionBar
        actionBar!!.title = "Ocena zmęczenia"
        actionBar!!.subtitle = "tętna spoczynkowego"

        val np = numberPicker
        np.minValue = 1
        np.maxValue = 10

        val receivedRestHeartRatingIntent = getIntent()

        val currentBpm = receivedRestHeartRatingIntent
            .getIntExtra("CurrentBPM", 0)

        val currentIBI = receivedRestHeartRatingIntent
            .getIntExtra("CurrentIBI", 0)

        val averageBPM = receivedRestHeartRatingIntent
            .getIntExtra("AverageBPM", 0)

        val averageIBI = receivedRestHeartRatingIntent
            .getIntExtra("AverageIBI", 0)

        val sleepTime = receivedRestHeartRatingIntent
            .getLongExtra("SleepTime", 0)

        val sleepTimeId = receivedRestHeartRatingIntent
            .getIntExtra("SleepTimeId", 0)

        textViewCurrentBpm.setText("${currentBpm}")
        textViewAvgBpm.setText("${averageBPM}")
        textViewCurrentIbi.setText("${currentIBI}")
        textViewAvgIbi.setText("${averageIBI}")
        textViewSleepTime.setText("${sleepTime}")
    }
}