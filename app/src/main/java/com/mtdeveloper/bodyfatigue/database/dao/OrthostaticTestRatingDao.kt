package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.OrthostaticTestRating

@Dao
interface OrthostaticTestRatingDao {
    @Insert
    fun insert(orthostaticTestRating: OrthostaticTestRating)

    @Query("SELECT Id, AverageLyingBPM, MaxLyingBPM, MinLyingBPM, AverageLyingIBI, MaxLyingIBI, MinLyingIBI, AverageStandingBPM, MaxStandingBPM, MinStandingBPM, AverageStandingIBI, MaxStandingIBI, MinStandingIBI, DiffPositionBpm, DiffPositionIbi, CreateDate, Rating FROM OrthostaticTestRating")
    fun getAll(): List<OrthostaticTestRating>
}