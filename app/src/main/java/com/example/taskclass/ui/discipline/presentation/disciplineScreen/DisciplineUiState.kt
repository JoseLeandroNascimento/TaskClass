package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.core.data.model.entity.DisciplineEntity
import kotlin.reflect.KProperty1

data class DisciplineUiState(
    val disciplines: List<DisciplineEntity> = emptyList(),
    val isLoading: Boolean = false,
)
