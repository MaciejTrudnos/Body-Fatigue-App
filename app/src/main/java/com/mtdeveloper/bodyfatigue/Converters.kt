package com.mtdeveloper.bodyfatigue

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun toType(value: String) = enumValueOf<Type>(value)

    @TypeConverter
    fun fromType(value: Type) = value.name

    @TypeConverter
    fun toDate(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime): String {
        return date.toString()
    }
}