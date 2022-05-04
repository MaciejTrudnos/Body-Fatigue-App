package com.mtdeveloper.bodyfatigue.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mtdeveloper.bodyfatigue.database.Type
import java.time.LocalDateTime

@Entity(tableName = "HeartRate", foreignKeys = [ForeignKey(
    entity = SleepTime::class,
    parentColumns = ["Id"],
    childColumns = ["SleepTimeId"],
    onDelete = ForeignKey.NO_ACTION
)])
data class HeartRate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
    @ColumnInfo(name = "BPM") val bpm: Int,
    @ColumnInfo(name = "IBI") val ibi: Int,
    @ColumnInfo(name = "Type") val type: Type,
    @ColumnInfo(name = "CreateDate") val createDate: LocalDateTime,
    @ColumnInfo(name = "SleepTimeId") val sleepTimeId: Int
){
    constructor(bpm: Int, ibi: Int, type: Type, createDate: LocalDateTime, sleepTimeId: Int)
            : this(0, bpm, ibi, type, createDate, sleepTimeId)
}