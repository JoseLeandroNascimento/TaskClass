package com.joseleandro.taskclass.ui.discipline.presentation.disciplineScreen

import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity

data class DisciplineUiState(
    val disciplines: List<DisciplineEntity> = emptyList(),
    val isLoading: Boolean = false,
)
