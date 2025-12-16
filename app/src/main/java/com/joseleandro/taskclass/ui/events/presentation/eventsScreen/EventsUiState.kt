package com.joseleandro.taskclass.ui.events.presentation.eventsScreen

import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import java.time.LocalDate

data class EventsUiState(
    val events: List<EventEndTypeEventDto> = emptyList(),
    val isLoading: Boolean = false,
    val dateSelected: LocalDate = LocalDate.now()
)