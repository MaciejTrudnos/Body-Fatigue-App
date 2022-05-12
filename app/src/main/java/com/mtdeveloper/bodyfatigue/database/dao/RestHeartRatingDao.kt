package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.RestHeartRating
import com.mtdeveloper.bodyfatigue.database.model.SleepTime
import java.time.LocalDateTime

@Dao
interface RestHeartRatingDao {
    @Insert
    fun insert(restHeartRating: RestHeartRating)

    @Query("SELECT Id, CurrentBPM, CurrentIBI, AverageBPM, AverageIBI, SleepTime, SleepTimeId, CreateDate, Rating FROM RestHeartRating")
    fun getAll(): List<RestHeartRating>

    @Query("SELECT EXISTS(SELECT Id FROM RestHeartRating WHERE SleepTimeId = :sleepTimeId)")
    fun isRatingExists(sleepTimeId : Int) : Boolean
}