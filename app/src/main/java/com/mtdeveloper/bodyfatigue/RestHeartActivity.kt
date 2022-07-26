package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.dao.HeartRateDao
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import kotlinx.android.synthetic.main.activity_rest_heart.*
import java.time.LocalDateTime

class RestHeartActivity : AppCompatActivity() {

    private var bpm: Int = 0
    private var ibi: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart)

        val actionBar = supportActionBar
        actionBar!!.title = "Analiza tętna spoczynkowego"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val heartRateDao = db.heartRateDao()

        Thread(
            {
               mock()
            }
        ).start()

        buttonStartRH.setOnClickListener {
            Thread(
                {
                    runTest(heartRateDao)
                }
            ).start()
        }

        buttonResultsRH.setOnClickListener {
            val restHeartRatingStatsIntent = Intent(this, RestHeartRatingStatsActivity::class.java)
            startActivity(restHeartRatingStatsIntent)
        }
    }

    private fun mock() {
        while (true)
        {
            var bpmRnds = (40..140)
                .random()

            var ibiRnds = (700..1400)
                .random()

            bpm = bpmRnds
            ibi = ibiRnds

            runOnUiThread {
                textViewBpmRH.setText("$bpmRnds")
            }

            runOnUiThread {
                textViewIbiRH.setText("$ibiRnds")
            }

            Thread.sleep(1000)
        }
    }

    private fun runTest(heartRateDao: HeartRateDao) {

        val localDateTime = LocalDateTime
            .now()

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        runOnUiThread {
            buttonStartRH.setEnabled(false)
            buttonResultsRH.setEnabled(false)
        }

        startCountDown(heartRateDao, localDateTime)

        runOnUiThread {
            textViewCountDownTimerRH.setText("")
            textViewInfoRH.setText("Test zakończony")
        }

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        runOnUiThread {
            buttonStartRH.setEnabled(true)
            buttonResultsRH.setEnabled(true)
        }

        val restHeartRatingIntent = Intent(this, RestHeartRatingActivity::class.java)
        restHeartRatingIntent.putExtra("CreateDate", localDateTime.toString())
        startActivity(restHeartRatingIntent)
    }

    fun startCountDown(heartRateDao: HeartRateDao, localDateTime: LocalDateTime) {
        var counter = 5

        runOnUiThread {
            textViewInfoRH.setText("Nie poruszaj się przez kolejne:")
        }

        while (counter >= 0) {

            runOnUiThread {
                textViewCountDownTimerRH.setText("$counter sekund")
            }

            val data =HeartRate(bpm, ibi, localDateTime)

            heartRateDao.insert(data)

            Thread.sleep(1000)

            counter--
        }
    }
}