package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import java.time.LocalDateTime

@Dao
interface HeartRateDao {
    @Query("SELECT Id, BPM, IBI, CreateDate FROM HeartRate")
    fun getAll(): List<HeartRate>

    @Insert
    fun insert(vararg heartRate: HeartRate)

    @Delete
    fun delete(heartRate: HeartRate)

    @Query("SELECT Id, BPM, IBI, CreateDate FROM HeartRate WHERE CreateDate = :createDate")
    fun getByCreateDate(createDate: LocalDateTime): List<HeartRate>
}