package com.example.taskclass.core.data.converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class ColorConverters {

    @TypeConverter
    fun fromColor(color: Color): Long {
        return color.value.toLong()
    }

    @TypeConverter
    fun toColor(value: Long): Color {
        return Color(value.toULong())
    }

}