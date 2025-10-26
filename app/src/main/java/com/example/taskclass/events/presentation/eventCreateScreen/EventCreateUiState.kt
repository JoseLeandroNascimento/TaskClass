package com.example.taskclass.events.presentation.eventCreateScreen

import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent

data class EventCreateUiState(
    val title: FieldState<String> = FieldState(""),
    val date: FieldState<String> = FieldState(""),
    val time: FieldState<String> = FieldState(""),
    val description: FieldState<String> = FieldState(""),
    val typeEventSelected: FieldState<TypeEvent?> = FieldState(null),
    val typeEvents: Resource<List<TypeEvent>>? = null,
    val eventResponse: Resource<Unit>? = null // ✅ adicione esta linha
)
