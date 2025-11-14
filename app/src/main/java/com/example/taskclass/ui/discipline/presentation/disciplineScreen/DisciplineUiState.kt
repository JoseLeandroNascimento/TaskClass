package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import kotlin.reflect.KProperty1

data class DisciplineUiState(
    val disciplines: Resource<List<Discipline>> = Resource.Loading(),
    val orderBy:KProperty1<Discipline, Comparable<*>> = Discipline::createdAt,
    val sortDirection: Boolean = false
)
