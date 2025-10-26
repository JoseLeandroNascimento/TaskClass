package com.example.taskclass.core.data.model

@JvmInline
value class Time(val minutes: Int)


fun Time.formatted(): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return "%02d:%02d".format(hours, mins)
}
