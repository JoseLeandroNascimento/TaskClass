package com.example.taskclass.events.presentation.eventCreateScreen

import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.validators.NotBlankValidator

data class EventCreateUiState(
    val title: FieldState<String> = FieldState(
        "",
        validators = listOf(
            NotBlankValidator()
        )
    ),
    val date: FieldState<String> = FieldState(""),
    val time: FieldState<String> = FieldState(""),
    val description: FieldState<String> = FieldState(""),
)