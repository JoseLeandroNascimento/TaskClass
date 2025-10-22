package com.example.taskclass.events.presentation.eventsScreen

import android.icu.util.Calendar
import com.example.taskclass.core.data.model.EventEntity

data class EventsUiState(
    val events: List<EventEntity> = emptyList(),
    val dateSelected: Calendar = Calendar.getInstance()
)