package com.mtdeveloper.bodyfatigue

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        textViewBPM.setText("Pomiar...")
        textViewIBI.setText("Pomiar...")

        var bluetoothService = BluetoothService()
        var bluetoothSocket = bluetoothService.connect()

        Thread({
            while (true)
            {
                var data = bluetoothService
                    .readBluetoothData(bluetoothSocket)
                    .split(";").toList()

                Log.i("btdata1", data[0])
                Log.i("btdata2", data[1])

                runOnUiThread {
                    textViewBPM.setText(data[0])
                }

                runOnUiThread {
                    textViewIBI.setText(data[1])
                }
            }
        }).start()
    }
}