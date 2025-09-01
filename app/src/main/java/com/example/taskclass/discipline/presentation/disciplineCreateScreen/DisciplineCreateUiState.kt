package com.example.taskclass.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import com.example.taskclass.common.data.FieldState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.Discipline

data class DisciplineCreateUiState(
    val id: Int? = null,
    val title: FieldState = FieldState(),
    val teacherName: FieldState = FieldState(),
    val colorSelect: Color = Color(0xFF9C27B0),
    val showPickerColor: Boolean = false,
    val disciplineResponse: Resource<Discipline>? = null
)
