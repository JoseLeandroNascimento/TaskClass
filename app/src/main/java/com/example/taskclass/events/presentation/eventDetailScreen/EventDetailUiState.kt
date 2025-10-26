package com.example.taskclass.events.presentation.eventDetailScreen

import com.example.taskclass.core.data.model.dto.EventWithType

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val event: EventWithType? = null,
    val error: String? = null
)