package com.example.taskclass.events.presentation.eventAllScreen

import com.example.taskclass.core.data.model.dto.EventWithType

data class EventAllUiState(
    val events: List<EventWithType> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
