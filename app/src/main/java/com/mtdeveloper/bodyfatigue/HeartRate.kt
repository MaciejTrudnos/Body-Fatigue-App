package com.mtdeveloper.bodyfatigue

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class HeartRate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "BPM") val bpm: Int,
    @ColumnInfo(name = "IBI") val ibi: Int,
    @ColumnInfo(name = "Type") val type: Type,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
){
    constructor(bpm: Int, ibi: Int, type: Type, createDate: LocalDateTime) : this(0, bpm, ibi, type, createDate)
}