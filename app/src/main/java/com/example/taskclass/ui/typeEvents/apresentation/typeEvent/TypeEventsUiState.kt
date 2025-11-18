package com.example.taskclass.ui.typeEvents.apresentation.typeEvent

import com.example.taskclass.core.data.model.entity.TypeEventEntity

data class TypeEventsUiState(
    val id: Int = 0,
    val typeEvents: List<TypeEventEntity> = emptyList(),
    val isLoading: Boolean = false
)