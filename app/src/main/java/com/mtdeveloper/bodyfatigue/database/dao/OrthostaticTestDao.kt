package com.mtdeveloper.bodyfatigue.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mtdeveloper.bodyfatigue.database.model.OrthostaticTest
import com.mtdeveloper.bodyfatigue.database.model.RestHeartRating
import java.time.LocalDateTime

@Dao
interface OrthostaticTestDao {
    @Insert
    fun insert(orthostaticTest: OrthostaticTest)

    @Query("SELECT Id, BPM, IBI, Position, CreateDate FROM OrthostaticTest")
    fun getAll(): List<OrthostaticTest>

    @Query("SELECT Id, BPM, IBI, Position, CreateDate FROM OrthostaticTest WHERE CreateDate = :createDate")
    fun getByCreateDate(createDate: LocalDateTime): List<OrthostaticTest>
}