package com.example.taskclass.ui.agenda.presentation

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.dto.ScheduleAndDisciplineDTO

data class AgendaUiState(
    val schedules: Resource<List<ScheduleAndDisciplineDTO>> = Resource.Loading(),
    val selectItem: ScheduleAndDisciplineDTO? = null,
    val currentDayOfWeek: Int? = null
)
