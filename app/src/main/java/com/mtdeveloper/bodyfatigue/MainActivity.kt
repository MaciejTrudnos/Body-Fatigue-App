package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    private var typeMode : Type = Type.Normal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).build()

        val heartRateDao = db.heartRateDao()

        Thread({
            mock(heartRateDao)
        }).start()

        textViewBPM.setText("Pomiar...")
        textViewIBI.setText("Pomiar...")

//        button.setOnClickListener{
//            Thread({
//                var result = heartRateDao.getAll().toList()
//                result.forEach {
//                    Log.i("db", "${it.id}, ${it.bpm}, ${it.ibi}, ${it.type}, ${it.createDate}")
//                }
//            }).start()
//
//
//        }

        buttonRestHeart.setOnClickListener {
            val restHeartIntent = Intent(this, RestHeartActivity::class.java)
            startActivity(restHeartIntent)
        }

        switchSleepMode.setOnClickListener {
            if (switchSleepMode.isChecked) {
                typeMode = Type.Sleep
                Toast.makeText(this, "Sleep mode", Toast.LENGTH_SHORT).show()
            } else {
                typeMode = Type.Normal
                Toast.makeText(this, "Normal mode", Toast.LENGTH_SHORT).show()
            }
        }

//        var bluetoothService = BluetoothService()
//        var bluetoothSocket = bluetoothService.connect()
//
//        Thread({
//            while (true)
//            {
//                var data = bluetoothService
//                    .readBluetoothData(bluetoothSocket)
//                    .split(";").toList()
//
//                Log.i("btdata1", data[0])
//                Log.i("btdata2", data[1])
//
//                runOnUiThread {
//                    textViewBPM.setText(data[0])
//                }
//
//                runOnUiThread {
//                    textViewIBI.setText(data[1])
//                }
//            }
//        }).start()
    }

    private fun mock(heartRateDao: HeartRateDao){

        while (true){
            var bpmRnds = (40..140).random()
            var ibiRnds = (700..1400).random()
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formattedDate = current.format(formatter)

            var hr = HeartRate(bpmRnds, ibiRnds, typeMode, current)

            heartRateDao.insert(hr)

            Log.i("mock", "${hr.bpm}, ${hr.ibi}, ${hr.type}, ${hr.createDate}")

            Thread.sleep(1_000)
        }
    }
}