package com.example.taskclass.core.data.model

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@JvmInline
value class DateInt(val value: Int)

fun DateInt.formatted(): String {
    val year = value / 10000
    val month = (value % 10000) / 100
    val day = value % 100
    val monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.getDefault())
    return "$day de $monthName de $year"
}