package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.mtdeveloper.bodyfatigue.database.Type
import java.time.LocalDateTime

data class RestHeartRating(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "CurrentBPM") val currentBpm: Int,
    @ColumnInfo(name = "CurrentIBI") val currentIbi: Int,
    @ColumnInfo(name = "AverageBPM") val averageBpm: Int,
    @ColumnInfo(name = "AverageIBI") val averageIbi: Int,
    @ColumnInfo(name = "SleepTime") val sleepTime: Int,
    @ColumnInfo(name = "SleepTimeId") val sleepTimeId: Int,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
    @ColumnInfo(name = "Rating") val rating: Int
){
    constructor(currentBpm: Int, currentIbi: Int, averageBpm: Int, averageIbi: Int, sleepTime: Int, sleepTimeId: Int, createDate: LocalDateTime, rating: Int)
            : this(0, currentBpm, currentIbi, averageBpm, averageIbi, sleepTime, sleepTimeId, createDate, rating)
}
