package com.example.taskclass.core.data.converters

import androidx.room.TypeConverter
import java.util.Locale

class TimeConverters {

    @TypeConverter
    fun fromTimeString(value: String?): Int? {
        return value?.let {
            val (hour, minute) = it.split(":").map { part -> part.toInt() }
            hour * 60 + minute
        }
    }

    @TypeConverter
    fun toTimeString(minutes: Int?): String? {
        return minutes?.let {
            val hour = it / 60
            val minute = it % 60
            String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
    }
}