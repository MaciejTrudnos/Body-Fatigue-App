package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class SleepTime(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "StartSleep") val startSleep: LocalDateTime,
    @ColumnInfo(name = "StopSleep") val stopSleep: LocalDateTime
){
    constructor(startSleep: LocalDateTime, stopSleep: LocalDateTime)
            : this(0, startSleep, stopSleep)
}