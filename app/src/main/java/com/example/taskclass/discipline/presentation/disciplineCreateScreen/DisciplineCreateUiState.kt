package com.example.taskclass.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.validators.MinLengthValidator
import com.example.taskclass.common.validators.NotBlankValidator
import com.example.taskclass.core.data.model.Discipline

data class DisciplineCreateUiState(
    val id: Int? = null,
    val title: FieldState<String> = FieldState(
        "",
        validators = listOf(
            NotBlankValidator(messageError = "Nome da disciplina não pode ser vazio"),
            MinLengthValidator(min = 3, messageError = "Nome da disciplina deve ter no mínimo %d caracteres")
        )
    ),
    val teacherName: FieldState<String> = FieldState(""),
    val colorSelect: Color = Color(0xFF9C27B0),
    val showPickerColor: Boolean = false,
    val disciplineResponse: Resource<Discipline>? = null
)
