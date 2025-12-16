package com.joseleandro.taskclass.ui.agenda.presentation

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.dto.ScheduleAndDisciplineDTO

data class AgendaUiState(
    val schedules: Resource<List<ScheduleAndDisciplineDTO>> = Resource.Loading(),
    val selectItem: ScheduleAndDisciplineDTO? = null,
    val currentDayOfWeek: Int? = null
)
