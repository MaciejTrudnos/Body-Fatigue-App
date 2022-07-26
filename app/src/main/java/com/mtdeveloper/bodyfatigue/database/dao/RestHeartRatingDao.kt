package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.RestHeartRating

@Dao
interface RestHeartRatingDao {
    @Insert
    fun insert(restHeartRating: RestHeartRating)

    @Query("SELECT Id, AverageBPM, MaxBPM, MinBPM, AverageIBI, MaxIBI, MinIBI, CreateDate, Rating FROM RestHeartRating")
    fun getAll(): List<RestHeartRating>

}