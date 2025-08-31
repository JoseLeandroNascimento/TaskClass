package com.example.taskclass.core.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromColor(color: Color): Long {
        return color.value.toLong()
    }

    @TypeConverter
    fun toColor(value: Long): Color {
        return Color(value.toULong())
    }

}