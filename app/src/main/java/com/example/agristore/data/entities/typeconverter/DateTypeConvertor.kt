package com.example.agristore.data.entities.typeconverter

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}