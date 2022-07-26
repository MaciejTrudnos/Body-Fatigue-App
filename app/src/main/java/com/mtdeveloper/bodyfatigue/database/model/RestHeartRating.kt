package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class RestHeartRating(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "AverageBPM") val averageBpm: Int,
    @ColumnInfo(name = "MaxBPM") val maxBpm: Int,
    @ColumnInfo(name = "MinBPM") val minBpm: Int,
    @ColumnInfo(name = "AverageIBI") val averageIbi: Int,
    @ColumnInfo(name = "MaxIBI") val maxIbi: Int,
    @ColumnInfo(name = "MinIBI") val minIbi: Int,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
    @ColumnInfo(name = "Rating") val rating: Int
){
    constructor(averageBpm: Int, maxBpm: Int, minBpm: Int, averageIbi: Int, maxIbi: Int, minIbi: Int, createDate: LocalDateTime, rating: Int)
            : this(0, averageBpm, maxBpm, minBpm, averageIbi, maxIbi, minIbi, createDate, rating)
}
