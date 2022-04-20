package com.mtdeveloper.bodyfatigue

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HeartRateDao {
    @Query("SELECT Id, BPM, IBI, Type, CreateDate FROM HeartRate")
    fun getAll(): List<HeartRate>

    @Insert
    fun insert(vararg heartRate: HeartRate)

    @Delete
    fun delete(heartRate: HeartRate)
}