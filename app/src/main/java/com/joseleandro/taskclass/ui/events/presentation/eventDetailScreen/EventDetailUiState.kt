package com.joseleandro.taskclass.ui.events.presentation.eventDetailScreen

import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val event: EventEndTypeEventDto? = null,
    val error: String? = null,
    val isBackNavigation: Boolean = false
)