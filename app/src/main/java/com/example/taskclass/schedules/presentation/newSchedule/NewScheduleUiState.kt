package com.example.taskclass.schedules.presentation.newSchedule

import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.validators.RequiredValidator
import com.example.taskclass.common.validators.TimeValidator
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Schedule

data class NewScheduleUiState(
    val disciplines: Resource<List<Discipline>> = Resource.Loading(),
    val scheduleResponse: Resource<Schedule>? = null,
    val dayWeek: FieldState<Int?> = FieldState(
        null,
        validators = listOf(
            RequiredValidator(messageError = "Informe o dia da semana")
        )
    ),
    val discipline: FieldState<Discipline?> = FieldState(
        null,
        validators = listOf(
            RequiredValidator(messageError = "Informe a disciplina")
        )
    ),
    val startTime: FieldState<String?> = FieldState(
        null,
        validators = listOf(
            TimeValidator()
        )
    ),
    val endTime: FieldState<String?> = FieldState(
        null, validators = listOf(
            TimeValidator()
        )
    ),
)
