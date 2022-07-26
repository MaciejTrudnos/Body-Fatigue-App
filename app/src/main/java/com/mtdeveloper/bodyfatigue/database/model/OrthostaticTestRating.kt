package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class OrthostaticTestRating(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "AverageLyingBPM") val averageLyingBPM: Int,
    @ColumnInfo(name = "MaxLyingBPM") val maxLyingBPM: Int,
    @ColumnInfo(name = "MinLyingBPM") val minLyingBPM: Int,
    @ColumnInfo(name = "AverageLyingIBI") val averageLyingIBI: Int,
    @ColumnInfo(name = "MaxLyingIBI") val maxLyingIBI: Int,
    @ColumnInfo(name = "MinLyingIBI") val minLyingIBI: Int,
    @ColumnInfo(name = "AverageStandingBPM") val averageStandingBPM: Int,
    @ColumnInfo(name = "MaxStandingBPM") val maxStandingBPM: Int,
    @ColumnInfo(name = "MinStandingBPM") val minStandingBPM: Int,
    @ColumnInfo(name = "AverageStandingIBI") val averageStandingIBI: Int,
    @ColumnInfo(name = "MaxStandingIBI") val maxStandingIBI: Int,
    @ColumnInfo(name = "MinStandingIBI") val minStandingIBI: Int,
    @ColumnInfo(name = "DiffPositionBpm") val diffPositionBpm: Int,
    @ColumnInfo(name = "DiffPositionIbi") val diffPositionIbi: Int,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
    @ColumnInfo(name = "Rating") val rating: Int
){
    constructor(averageLyingBPM: Int,
                maxLyingBPM: Int,
                minLyingBPM: Int,
                averageLyingIBI: Int,
                maxLyingIBI: Int,
                minLyingIBI: Int,
                averageStandingBPM: Int,
                maxStandingBPM: Int,
                minStandingBPM: Int,
                averageStandingIBI: Int,
                maxStandingIBI: Int,
                minStandingIBI: Int,
                diffPositionBpm: Int,
                diffPositionIbi: Int,
                createDate: LocalDateTime,
                rating: Int)
            : this(0,
                    averageLyingBPM,
                    maxLyingBPM,
                    minLyingBPM,
                    averageLyingIBI,
                    maxLyingIBI,
                    minLyingIBI,
                    averageStandingBPM,
                    maxStandingBPM,
                    minStandingBPM,
                    averageStandingIBI,
                    maxStandingIBI,
                    minStandingIBI,
                    diffPositionBpm,
                    diffPositionIbi,
                    createDate,
                    rating)
}