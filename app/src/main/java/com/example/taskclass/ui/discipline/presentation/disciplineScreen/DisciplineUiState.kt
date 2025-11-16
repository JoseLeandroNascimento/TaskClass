package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.core.data.model.Discipline
import kotlin.reflect.KProperty1

data class DisciplineUiState(
    val disciplines: List<Discipline> = emptyList(),
    val isLoading: Boolean = false,
    val orderBy:KProperty1<Discipline, Comparable<*>> = Discipline::createdAt,
    val sortDirection: Boolean = false
)
