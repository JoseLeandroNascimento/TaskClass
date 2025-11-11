package com.example.taskclass.ui.events.presentation.eventsScreen

import com.example.taskclass.core.data.model.dto.EventWithType
import java.time.LocalDate

data class EventsUiState(
    val events: List<EventWithType> = emptyList(),
    val loadingEvents: Boolean = false,
    val dateSelected: LocalDate = LocalDate.now()
)