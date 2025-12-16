package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEvent

import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity

data class TypeEventsUiState(
    val id: Int = 0,
    val typeEvents: List<TypeEventEntity> = emptyList(),
    val isLoading: Boolean = false,
    val showSearch: Boolean = false
)