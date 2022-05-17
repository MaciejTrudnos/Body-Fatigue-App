package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_orthostatic_test.*
import kotlinx.android.synthetic.main.activity_rest_heart_rating.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class OrthostaticTestActivity : AppCompatActivity() {

    private val bpmStats : MutableList<Int> = ArrayList()
    private var bpm: Int = 0
    private var ibi: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test)

        val actionBar = supportActionBar
        actionBar!!.title = "Test ortostatyczny"

        Thread(
            {
                mock()
            }
        ).start()

        buttonStart.setOnClickListener {
            Thread(
                {
                    runTest()
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

    private fun runTest(){
        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        runOnUiThread {
            buttonStart.setEnabled(false)
            buttonTestResults.setEnabled(false)
        }

        startCountDown()

        runOnUiThread {
            textViewCountDownTimer.setText("")
            textViewInfo.setText("Wstań")
        }

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        Thread.sleep(5000)

        startCountDown()

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
        startActivity(orthostaticTestRatingIntent)
    }

    fun startCountDown() {
        var counter = 60

        runOnUiThread {
            textViewInfo.setText("Nie poruszaj się przez kolejne:")
        }

        while (counter >= 0) {

            bpmStats.add(bpm)

            runOnUiThread {
                textViewCountDownTimer.setText("$counter sekund")
            }

            Thread.sleep(1000)

            counter--
        }
    }
}