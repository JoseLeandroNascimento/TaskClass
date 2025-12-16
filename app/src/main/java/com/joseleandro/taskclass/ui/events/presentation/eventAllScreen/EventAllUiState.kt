package com.joseleandro.taskclass.ui.events.presentation.eventAllScreen

import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.joseleandro.taskclass.core.data.model.enums.EEventStatus


data class EventAllUiState(
    val eventsGroup: Map<EEventStatus, List<EventEndTypeEventDto>> = emptyMap(),
    val events:  List<EventEndTypeEventDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
