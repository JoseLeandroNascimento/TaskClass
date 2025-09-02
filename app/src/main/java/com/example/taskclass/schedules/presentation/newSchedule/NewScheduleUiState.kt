package com.example.taskclass.schedules.presentation.newSchedule

import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Schedule

data class NewScheduleUiState(
    val disciplines: Resource<List<Discipline>> = Resource.Loading(),
    val scheduleResponse: Resource<Schedule>? = null,
    val dayWeek: FieldState<Int> = FieldState(0),
    val discipline: FieldState<Discipline?> = FieldState(null),
    val startTime: FieldState<String> = FieldState(""),
    val endTime: FieldState<String> = FieldState(""),
)
