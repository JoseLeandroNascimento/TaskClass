package com.example.taskclass.common.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * Parse date and time to Instant
 * @param dateStr format dd/MM/yyyy
 * @param timeStr format HH:mm
 */
fun parseToInstant(dateStr: String, timeStr: String): Instant {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date = LocalDate.parse(dateStr, dateFormatter)
    val time = LocalTime.parse(timeStr, timeFormatter)

    return LocalDateTime.of(date, time)
        .atZone(ZoneId.systemDefault())
        .toInstant()
}


/**
 * Convert Instant to formatted date and time
 * @param pattern format dd/MM/yyyy 'de' HH:mm
 */
fun Instant.toFormattedDateTime(pattern: String = "dd/MM/yyyy 'de' HH:mm"): String {
    val zoned = this.atZone(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return zoned.format(formatter)
}


/**
 * Convert LocalTime to formatted time
 * @param pattern format HH:mm
 */
fun LocalTime.toFormattedTime(pattern: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}


/**
 * Convert String to LocalTime
 */
fun String.toLocalTime(): LocalTime {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this, timeFormatter)
}

/**
 * Convert LocalTime to total minutes
 */
fun LocalTime.toTotalMinutes(): Int {
    return this.hour * 60 + this.minute
}
