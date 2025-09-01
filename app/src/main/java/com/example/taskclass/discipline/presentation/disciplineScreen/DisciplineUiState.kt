package com.example.taskclass.discipline.presentation.disciplineScreen

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.Discipline

data class DisciplineUiState(
    val disciplines: Resource<List<Discipline>> = Resource.Loading(),
)
