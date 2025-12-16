package com.joseleandro.taskclass.ui.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import com.joseleandro.taskclass.common.data.FieldState
import com.joseleandro.taskclass.common.validators.MinLengthValidator
import com.joseleandro.taskclass.common.validators.NotBlankValidator

data class FormState(
    val idDiscipline: Int? = null,
    val title: FieldState<String> = FieldState(
        "",
        validators = listOf(
            NotBlankValidator(messageError = "Nome da disciplina não pode ser vazio"),
            MinLengthValidator(
                min = 3,
                messageError = "Nome da disciplina deve ter no mínimo %d caracteres"
            )
        )
    ),
    val teacherName: FieldState<String> = FieldState(""),
    val colorSelect: Color = Color(0xFF9C27B0),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)