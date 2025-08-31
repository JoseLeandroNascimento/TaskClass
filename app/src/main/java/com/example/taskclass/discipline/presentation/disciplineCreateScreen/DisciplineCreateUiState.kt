package com.example.taskclass.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import com.example.taskclass.common.data.FieldState

data class DisciplineCreateUiState(
    val id: Int? = null,
    val title: FieldState = FieldState(),
    val teacherName: FieldState = FieldState(),
    val colorSelect: Color = Color(0xFF9C27B0),
    val showPickerColor: Boolean = false
)
