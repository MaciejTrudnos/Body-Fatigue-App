package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import kotlinx.android.synthetic.main.activity_orthostatic_test.*
import kotlinx.android.synthetic.main.activity_rest_heart.*
import java.time.LocalDateTime

class RestHeartActivity : AppCompatActivity() {

    private var save: Boolean = false
    private val localDateTime : LocalDateTime = LocalDateTime.now()

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

        buttonStartRH.setOnClickListener {
            Thread(
                {
                    runTest()
                }
            ).start()
        }

        buttonResultsRH.setOnClickListener {
            val restHeartRatingStatsIntent = Intent(this, RestHeartRatingStatsActivity::class.java)
            startActivity(restHeartRatingStatsIntent)
        }

        var bluetoothService = BluetoothService()
        var bluetoothSocket = bluetoothService.connect()

        Thread({
            while (true)
            {
                var data = bluetoothService
                    .readBluetoothData(bluetoothSocket)
                    .split(";").toList()

                var bpm = data[0]
                    .filterNot{ it.isWhitespace() }
                    .toInt()

                var ibi = data[1]
                    .filterNot{ it.isWhitespace() }
                    .toInt()

                if(save == true){
                    val btData = HeartRate(bpm, ibi, localDateTime)
                    heartRateDao.insert(btData)
                }

                runOnUiThread {
                    textViewBpmRH.setText(data[0])
                }

                runOnUiThread {
                    textViewIbiRH.setText(data[1])
                }
            }
        }).start()
    }

    private fun runTest() {
        save = true

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        runOnUiThread {
            buttonStartRH.setEnabled(false)
            buttonResultsRH.setEnabled(false)
        }

        startCountDown()

        runOnUiThread {
            textViewCountDownTimerRH.setText("")
            textViewInfoRH.setText("Test zakończony")
        }

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        runOnUiThread {
            buttonStartRH.setEnabled(true)
            buttonResultsRH.setEnabled(true)
        }

        save = false

        val restHeartRatingIntent = Intent(this, RestHeartRatingActivity::class.java)
        restHeartRatingIntent.putExtra("CreateDate", localDateTime.toString())
        startActivity(restHeartRatingIntent)
    }

    fun startCountDown() {
        var counter = 60

        runOnUiThread {
            textViewInfoRH.setText("Nie poruszaj się przez kolejne:")
        }

        while (counter >= 0) {

            runOnUiThread {
                textViewCountDownTimerRH.setText("$counter sekund")
            }

            Thread.sleep(1000)

            counter--
        }
    }
}