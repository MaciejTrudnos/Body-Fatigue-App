package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.Type
import com.mtdeveloper.bodyfatigue.database.dao.HeartRateDao
import com.mtdeveloper.bodyfatigue.database.dao.SleepTimeDao
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import com.mtdeveloper.bodyfatigue.database.model.SleepTime
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
        val sleepTimeDao = db.sleepTimeDao()

        Thread({
            mock(heartRateDao, sleepTimeDao)
//            try{
//                var asd = heartRateDao.getAll()
//
//                asd.forEach {
//                    Log.i("db", "${it.id}, ${it.bpm}, ${it.ibi}, ${it.type}, ${it.createDate}, ${it.sleepTimeId}")
//                }
//            }catch (ex: Exception){
//                Log.e("myerror", ex.message.toString())
//            }



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

    private fun mock(heartRateDao: HeartRateDao, sleepTimeDao: SleepTimeDao){
        Log.i("Sleep", "START")
        var startHour = 22
        var current = "2022-04-28 ${startHour}:15:17"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")
        val sleepStart = LocalDateTime.parse(current, formatter)

        var st = SleepTime(sleepStart, LocalDateTime.MIN)
        var stId = sleepTimeDao.insert(st)



        for (i in 1..10) {
            var bpmRnds = (40..140).random()
            var ibiRnds = (700..1400).random()

            var current = "2022-04-28 ${startHour}:15:17"
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")
            val dateTime = LocalDateTime.parse(current, formatter)

            Log.i("date", "${dateTime}")

            var hr = HeartRate(bpmRnds, ibiRnds, dateTime, stId.toInt())
            heartRateDao.insert(hr)

            Thread.sleep(100)

            var bpmRnds2 = (40..140).random()
            var ibiRnds2 = (700..1400).random()

            var current2 = "2022-04-28 ${startHour}:20:28"
            val dateTime2 = LocalDateTime.parse(current2, formatter)

            var hr2 = HeartRate(bpmRnds2, ibiRnds2, dateTime2, stId.toInt())
            heartRateDao.insert(hr2)

            Thread.sleep(100)

            var bpmRnds3 = (40..140).random()
            var ibiRnds3 = (700..1400).random()

            var current3 = "2022-04-28 ${startHour}:40:58"
            val dateTime3 = LocalDateTime.parse(current3, formatter)

            var hr3 = HeartRate(bpmRnds3, ibiRnds3, dateTime3, stId.toInt())
            heartRateDao.insert(hr3)

            startHour++

            if(startHour == 24){
                startHour = 0
            }

            Thread.sleep(100)
        }

        var current3 = "2022-04-29 8:40:58"
        val sleepStop = LocalDateTime.parse(current3, formatter)

        sleepTimeDao.updateSleepTime(stId.toInt(), sleepStop)

        Log.i("Sleep", "STOP")
    }


}