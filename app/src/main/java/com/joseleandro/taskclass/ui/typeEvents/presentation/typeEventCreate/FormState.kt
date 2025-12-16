package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEventCreate

import androidx.compose.ui.graphics.Color
import com.joseleandro.taskclass.common.data.FieldState
import com.joseleandro.taskclass.common.validators.NotBlankValidator

data class FormState(
    val id: FieldState<Int?> = FieldState(null),
    val nameTypeEvent: FieldState<String> = FieldState(
        "", validators = listOf(
            NotBlankValidator(messageError = "Informe o nome do evento")
        )
    ),
    val createdAt: FieldState<Long?> = FieldState(null),
    val colorTypeEvent: FieldState<Color> = FieldState(Color.Blue)
)
