package com.mtdeveloper.bodyfatigue.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mtdeveloper.bodyfatigue.database.Converters
import com.mtdeveloper.bodyfatigue.database.dao.*
import com.mtdeveloper.bodyfatigue.database.model.*

@Database(entities = [HeartRate::class, SleepTime::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heartRateDao(): HeartRateDao
    abstract fun sleepTimeDao(): SleepTimeDao
}