package com.example.taskclass.core.data.converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.Time
import java.util.Locale

class Converters {

    @TypeConverter
    fun fromTimeString(value: String?): Time? {
        return value?.let {
            val (hour, minute) = it.split(":").map(String::toInt)
            Time(hour * 60 + minute)
        }
    }

    @TypeConverter
    fun toTimeString(time: Time?): String? {
        return time?.let {
            val hour = it.minutes / 60
            val minute = it.minutes % 60
            String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
    }

    @TypeConverter
    fun fromColor(color: Color): Long = color.value.toLong()

    @TypeConverter
    fun toColor(value: Long): Color = Color(value.toULong())

    @TypeConverter
    fun fromDate(date: DateInt?): String? = date?.value?.toString()

    fun toDate(dateString: String?): DateInt? {
        return dateString?.let { str ->
            val parts = str.split("/")
            if (parts.size == 3) {
                val day = parts[0].toIntOrNull() ?: return null
                val month = parts[1].toIntOrNull() ?: return null
                val year = parts[2].toIntOrNull() ?: return null
                DateInt(year * 10000 + month * 100 + day)
            } else null
        }
    }
}