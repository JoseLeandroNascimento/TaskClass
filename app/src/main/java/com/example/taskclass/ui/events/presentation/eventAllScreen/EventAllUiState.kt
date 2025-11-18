package com.example.taskclass.ui.events.presentation.eventAllScreen

import com.example.taskclass.core.data.model.dto.EventEndTypeEventDto


data class EventAllUiState(
    val events: List<EventEndTypeEventDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
