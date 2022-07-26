package com.mtdeveloper.bodyfatigue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase

import com.mtdeveloper.bodyfatigue.database.dao.HeartRateDao
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).build()

//        val heartRateDao = db.heartRateDao()
//        val sleepTimeDao = db.sleepTimeDao()

//        Thread({
//            mock(heartRateDao, sleepTimeDao)
//            try{
//                var asd = heartRateDao.getAll()
//
//                asd.forEach {
//                    Log.i("db", "${it.id}, ${it.bpm}, ${it.ibi}, ${it.type}, ${it.createDate}, ${it.sleepTimeId}")
//                }
//            }catch (ex: Exception){
//                Log.e("myerror", ex.message.toString())
//            }



//        }).start()

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

        buttonOrthostaticTest.setOnClickListener {
            val orthostaticTestIntent = Intent(this, OrthostaticTestActivity::class.java)
            startActivity(orthostaticTestIntent)
        }

        buttonRestHeart.setOnClickListener {
            val restHeartIntent = Intent(this, RestHeartActivity::class.java)
            startActivity(restHeartIntent)
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




}