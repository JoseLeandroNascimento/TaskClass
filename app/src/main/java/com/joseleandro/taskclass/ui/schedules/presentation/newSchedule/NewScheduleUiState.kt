package com.joseleandro.taskclass.ui.schedules.presentation.newSchedule

import com.joseleandro.taskclass.common.data.FieldState
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.common.validators.RequiredValidator
import com.joseleandro.taskclass.common.validators.TimeValidator
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.core.data.model.entity.ScheduleEntity

data class NewScheduleUiState(
    val disciplines: Resource<List<DisciplineEntity>> = Resource.Loading(),
    val scheduleResponse: Resource<ScheduleEntity>? = null,
    val dayWeek: FieldState<Int?> = FieldState(
        null,
        validators = listOf(
            RequiredValidator(messageError = "Informe o dia da semana")
        )
    ),
    val discipline: FieldState<DisciplineEntity?> = FieldState(
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
