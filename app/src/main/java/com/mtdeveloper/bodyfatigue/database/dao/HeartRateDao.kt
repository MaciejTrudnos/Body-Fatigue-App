package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import com.mtdeveloper.bodyfatigue.database.model.SleepTime
import java.time.LocalDateTime

@Dao
interface HeartRateDao {
    @Query("SELECT Id, BPM, IBI, Type, CreateDate, SleepTimeId FROM HeartRate")
    fun getAll(): List<HeartRate>

    @Insert
    fun insert(vararg heartRate: HeartRate)

    @Delete
    fun delete(heartRate: HeartRate)

    @Query("SELECT Id, BPM, IBI, Type, CreateDate, SleepTimeId FROM HeartRate hr WHERE SleepTimeId = :sleepTimeId")
    fun getLastSleepHeartRate(sleepTimeId : Int): List<HeartRate>
}