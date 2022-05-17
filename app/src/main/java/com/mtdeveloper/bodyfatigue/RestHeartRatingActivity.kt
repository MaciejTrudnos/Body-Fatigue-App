package com.mtdeveloper.bodyfatigue

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.model.RestHeartRating
import kotlinx.android.synthetic.main.activity_rest_heart_rating.*
import java.time.LocalDateTime


class RestHeartRatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart_rating)

        val actionBar = supportActionBar
        actionBar!!.title = "Ocena zmęczenia"
        actionBar!!.subtitle = "tętna spoczynkowego"

        val np = numberPicker
        np.minValue = 1
        np.maxValue = 4

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val restHeartRatingDao = db.restHeartRatingDao()

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

        val heartRateStats = HeartRateStats()
        val sleepTimeText = heartRateStats.changeMinutesToTextTime(sleepTime)

        textViewSleepTime.setText(sleepTimeText)

        buttonSaveRestHeartRating.setOnClickListener {
            val isRatingExists = restHeartRatingDao.isRatingExists(sleepTimeId)

            if (isRatingExists) {
                Toast.makeText(this, "Ocena została już dodana", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            val localDateTimeNow = LocalDateTime.now()
            val command = RestHeartRating(currentBpm, currentIBI, averageBPM, averageIBI, sleepTime.toInt(), sleepTimeId, localDateTimeNow, np.value )

            restHeartRatingDao.insert(command)

            Toast.makeText(this, "Dodano ocenę", Toast.LENGTH_SHORT)
                .show()

            buttonSaveRestHeartRating.setEnabled(false)
        }
    }
}