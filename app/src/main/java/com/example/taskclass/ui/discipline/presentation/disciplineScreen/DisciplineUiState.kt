package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline

data class DisciplineUiState(
    val disciplines: Resource<List<Discipline>> = Resource.Loading(),
)
