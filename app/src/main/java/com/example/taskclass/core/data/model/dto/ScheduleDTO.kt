package com.example.taskclass.core.data.model.dto

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo

data class ScheduleDTO(
    @ColumnInfo(name = "schedule_id")
    val scheduleId: Int,

    @ColumnInfo(name = "day_week")
    val dayWeek: Int,

    @ColumnInfo(name = "start_time")
    val startTime: Int,

    @ColumnInfo(name = "end_time")
    val endTime: Int,

    @ColumnInfo(name = "discipline_id")
    val disciplineId: Int,

    @ColumnInfo(name = "disciplineTitle")
    val disciplineTitle: String,

    @ColumnInfo(name = "teacher_name")
    val teacherName: String,

    @ColumnInfo(name = "color")
    val color: Color
)
