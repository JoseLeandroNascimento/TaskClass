package com.example.taskclass.ui.typeEvents.apresentation.typeEvent

import com.example.taskclass.core.data.model.TypeEvent

data class TypeEventsUiState(
    val id: Int = 0,
    val typeEvents: List<TypeEvent> = emptyList(),
    val isLoading: Boolean = false
)