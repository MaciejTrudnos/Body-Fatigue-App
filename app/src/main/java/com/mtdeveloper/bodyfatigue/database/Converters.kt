package com.mtdeveloper.bodyfatigue.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun toType(value: String) = enumValueOf<PositionTest>(value)

    @TypeConverter
    fun fromType(value: PositionTest) = value.name

    @TypeConverter
    fun toDate(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime): String {
        return date.toString()
    }
}