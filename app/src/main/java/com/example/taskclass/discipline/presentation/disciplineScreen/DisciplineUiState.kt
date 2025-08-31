package com.example.taskclass.discipline.presentation.disciplineScreen

import com.example.taskclass.core.data.Discipline

data class DisciplineUiState(
    val disciplines: List<Discipline> = emptyList(),
    val loading: Boolean = false
)
