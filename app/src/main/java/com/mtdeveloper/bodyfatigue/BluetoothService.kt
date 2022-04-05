package com.mtdeveloper.bodyfatigue

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothService() {

    private val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    companion object {
        var connectSuccess: Boolean = true
        var bluetoothSocket: BluetoothSocket? = null
        var isConnected: Boolean = false
    }

    public fun connect() :BluetoothSocket {
        try {
            if (bluetoothSocket == null || !isConnected) {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
                val bfDeviceAddress = pairedDevices.find{ it.name.contains("HC-06")}?.address
                if (bfDeviceAddress == null){
                    Log.e("BTDevice", "Couldn't find HC-06")
                    throw Exception("Couldn't find HC-06")
                }

                val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(bfDeviceAddress)
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                bluetoothAdapter.cancelDiscovery()
                bluetoothSocket!!.connect()
            }

            if (!connectSuccess) {
                Log.e("BTDevice", "Couldn't connect")
                throw Exception("Couldn't connect")
            } else {
                isConnected = true
                return bluetoothSocket!!
            }
        }
        catch (e: IOException) {
            connectSuccess = false
            e.printStackTrace()
            throw e
        }
    }

    public fun readBluetoothData(bluetoothSocket: BluetoothSocket) : String {
        val bluetoothSocketInputStream = bluetoothSocket.inputStream
        val buffer = ByteArray(1024)
        var bytes: Int

        try {
            bytes = bluetoothSocketInputStream.read(buffer)
            val readMessage = String(buffer, 0, bytes)
            return readMessage
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
}