package com.example.taskclass.events.presentation.eventsScreen

import com.example.taskclass.core.data.model.EventEntity
import java.util.Calendar

data class EventsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val events: List<EventEntity> = emptyList(),
    val monthDays: List<DayWithEvents> = emptyList(),
    val selectedDate: Calendar? = null
)