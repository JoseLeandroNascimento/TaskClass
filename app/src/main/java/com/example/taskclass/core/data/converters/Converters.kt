package com.example.taskclass.core.data.converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalTime

class Converters {


    @TypeConverter
    fun fromColor(color: Color): Long = color.value.toLong()

    @TypeConverter
    fun toColor(value: Long): Color = Color(value.toULong())

    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): Int? = value?.toSecondOfDay()

    @TypeConverter
    fun toLocalTime(value: Int?): LocalTime? = value?.let { LocalTime.ofSecondOfDay(it.toLong()) }


}