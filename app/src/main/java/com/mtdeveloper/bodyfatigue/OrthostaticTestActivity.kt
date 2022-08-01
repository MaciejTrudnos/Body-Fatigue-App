package com.mtdeveloper.bodyfatigue

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.PositionTest
import com.mtdeveloper.bodyfatigue.database.model.OrthostaticTest
import kotlinx.android.synthetic.main.activity_orthostatic_test.*
import java.time.LocalDateTime


class OrthostaticTestActivity : AppCompatActivity() {

    private var position : PositionTest = PositionTest.Lying
    private var save: Boolean = false
    private val localDateTime : LocalDateTime = LocalDateTime.now()

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

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            textViewBpm.setText("")
            textViewIbi.setText("")
            textView26.setText("")
            textView25.setText("")

            Toast.makeText(this, "Bluetooth wyłączone!", Toast.LENGTH_SHORT)
                .show()

            return
        }

        val bluetoothService = BluetoothService()
        val bluetoothSocket = bluetoothService.connect()

        Thread({
            while (true)
            {
                val data = bluetoothService
                    .readBluetoothData(bluetoothSocket)
                    .split(";").toList()

                val bpm = data[0]
                    .filterNot{ it.isWhitespace() }
                    .toInt()

                val ibi = data[1]
                    .filterNot{ it.isWhitespace() }
                    .toInt()

                if (save == true){
                    val btData = OrthostaticTest(bpm, ibi, position, localDateTime )
                    orthostaticTestDao.insert(btData)
                }

                runOnUiThread {
                    textViewBpm.setText(data[0])
                }

                runOnUiThread {
                    textViewIbi.setText(data[1])
                }
            }
        }).start()
    }

    private fun runTest() {
        save = true

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        runOnUiThread {
            buttonStart.setEnabled(false)
            buttonTestResults.setEnabled(false)
        }

        position = PositionTest.Lying

        startCountDown("Nie poruszaj się przez kolejne:", 60)

        position = PositionTest.Standing

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        save = false
        startCountDown("Wstań", 30)
        save = true

        startCountDown("Nie poruszaj się przez kolejne:", 60)

        runOnUiThread {
            textViewCountDownTimer.setText("")
            textViewInfo.setText("Test zakończony")
        }

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)

        runOnUiThread {
            buttonStart.setEnabled(true)
            buttonTestResults.setEnabled(true)
        }

        save = false

        val orthostaticTestRatingIntent = Intent(this, OrthostaticTestRatingActivity::class.java)
        orthostaticTestRatingIntent.putExtra("CreateDate", localDateTime.toString())
        startActivity(orthostaticTestRatingIntent)
    }

    fun startCountDown(text: String, count: Int) {
        var counter = count

        runOnUiThread {
            textViewInfo.setText("$text")
        }

        while (counter >= 0) {

            runOnUiThread {
                textViewCountDownTimer.setText("$counter sekund")
            }

            Thread.sleep(1000)

            counter--
        }
    }
}