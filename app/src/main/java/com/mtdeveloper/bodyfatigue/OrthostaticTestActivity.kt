package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.PositionTest
import com.mtdeveloper.bodyfatigue.database.dao.OrthostaticTestDao
import com.mtdeveloper.bodyfatigue.database.model.OrthostaticTest
import kotlinx.android.synthetic.main.activity_orthostatic_test.*
import java.time.LocalDateTime

class OrthostaticTestActivity : AppCompatActivity() {

    private var position : PositionTest = PositionTest.Lying
    private var bpm: Int = 0
    private var ibi: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test)

        val actionBar = supportActionBar
        actionBar!!.title = "Test ortostatyczny"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val orthostaticTestDao = db.orthostaticTestDao()

        Thread(
            {
                mock()
            }
        ).start()

        buttonStart.setOnClickListener {
            Thread(
                {
                    runTest(orthostaticTestDao)
                }
            ).start()
        }

        buttonTestResults.setOnClickListener {
            val orthostaticTestRatingStatsIntent = Intent(this, OrthostaticTestRatingStatsActivity::class.java)
            startActivity(orthostaticTestRatingStatsIntent)
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
                textViewBpm.setText("$bpmRnds")
            }

            runOnUiThread {
                textViewIbi.setText("$ibiRnds")
            }

            Thread.sleep(1000)
        }
    }

    private fun runTest(orthostaticTestDao: OrthostaticTestDao) {

        val localDateTime = LocalDateTime
            .now()

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        runOnUiThread {
            buttonStart.setEnabled(false)
            buttonTestResults.setEnabled(false)
        }

        position = PositionTest.Lying

        startCountDown(orthostaticTestDao, localDateTime)

        runOnUiThread {
            textViewCountDownTimer.setText("")
            textViewInfo.setText("Wstań")
        }

        position = PositionTest.Standing

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        Thread.sleep(5000)

        startCountDown(orthostaticTestDao, localDateTime)

        runOnUiThread {
            textViewCountDownTimer.setText("")
            textViewInfo.setText("Test zakończony")
        }

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        runOnUiThread {
            buttonStart.setEnabled(true)
            buttonTestResults.setEnabled(true)
        }

        val orthostaticTestRatingIntent = Intent(this, OrthostaticTestRatingActivity::class.java)
        orthostaticTestRatingIntent.putExtra("CreateDate", localDateTime.toString())
        startActivity(orthostaticTestRatingIntent)
    }

    fun startCountDown(orthostaticTestDao: OrthostaticTestDao, localDateTime: LocalDateTime) {
        var counter = 5

        runOnUiThread {
            textViewInfo.setText("Nie poruszaj się przez kolejne:")
        }

        while (counter >= 0) {

            runOnUiThread {
                textViewCountDownTimer.setText("$counter sekund")
            }

            val data = OrthostaticTest(bpm, ibi, position, localDateTime )

            orthostaticTestDao.insert(data)

            Thread.sleep(1000)

            counter--
        }
    }
}