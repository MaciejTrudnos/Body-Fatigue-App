package com.mtdeveloper.bodyfatigue

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT))
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }

        buttonOrthostaticTest.setOnClickListener {
            val orthostaticTestIntent = Intent(this, OrthostaticTestActivity::class.java)
            startActivity(orthostaticTestIntent)
        }

        buttonRestHeart.setOnClickListener {
            val restHeartIntent = Intent(this, RestHeartActivity::class.java)
            startActivity(restHeartIntent)
        }
    }

    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d("bt-permissions", "granted")
        }else{
            Log.d("bt-permissions", "deny")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("bt-permissions", "${it.key} = ${it.value}")
            }
        }

}