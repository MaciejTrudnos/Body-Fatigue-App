package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.SleepTime
import java.time.LocalDateTime

@Dao
interface SleepTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sleepTime: SleepTime) : Long

    @Query("UPDATE SleepTime SET stopSleep = :stopSleep WHERE Id = :id")
    fun updateSleepTime(id: Int, stopSleep: LocalDateTime)

    @Query("SELECT Id, StartSleep, StopSleep FROM SleepTime")
    fun getAll(): List<SleepTime>

    @Query("SELECT Id, StartSleep, StopSleep FROM SleepTime ORDER BY Id DESC LIMIT 1")
    fun getLastSleepTime(): SleepTime
}