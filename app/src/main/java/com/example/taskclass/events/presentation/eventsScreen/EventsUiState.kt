package com.example.taskclass.events.presentation.eventsScreen

import com.example.taskclass.core.data.model.dto.EventWithType
import java.time.LocalDate

data class EventsUiState(
    val events: List<EventWithType> = emptyList(),
    val dateSelected: LocalDate = LocalDate.now()
)