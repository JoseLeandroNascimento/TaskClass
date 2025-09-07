package com.example.taskclass.agenda.presentation

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.dto.ScheduleDTO

data class AgendaUiState(
    val schedules: Resource<List<ScheduleDTO>> = Resource.Loading(),
    val selectItem: ScheduleDTO? = null,
    val currentDayOfWeek: Int? = null
)
