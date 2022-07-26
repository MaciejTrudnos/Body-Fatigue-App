package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class HeartRate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "BPM") val bpm: Int,
    @ColumnInfo(name = "IBI") val ibi: Int,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
){
    constructor(bpm: Int, ibi: Int, createDate: LocalDateTime)
            : this(0, bpm, ibi, createDate)
}